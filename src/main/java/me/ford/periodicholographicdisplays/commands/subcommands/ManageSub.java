package me.ford.periodicholographicdisplays.commands.subcommands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

import dev.ratas.slimedogcore.api.commands.SDCCommandOptionSet;
import dev.ratas.slimedogcore.api.messaging.recipient.SDCRecipient;
import me.ford.periodicholographicdisplays.IPeriodicHolographicDisplays;
import me.ford.periodicholographicdisplays.Messages;
import me.ford.periodicholographicdisplays.holograms.AlwaysHologram;
import me.ford.periodicholographicdisplays.holograms.FlashingHologram;
import me.ford.periodicholographicdisplays.holograms.HologramStorage;
import me.ford.periodicholographicdisplays.holograms.IRLTimeHologram;
import me.ford.periodicholographicdisplays.holograms.MCTimeHologram;
import me.ford.periodicholographicdisplays.holograms.NTimesHologram;
import me.ford.periodicholographicdisplays.holograms.PeriodicHologramBase;
import me.ford.periodicholographicdisplays.holograms.PeriodicType;
import me.ford.periodicholographicdisplays.holograms.WorldHologramStorage;
import me.ford.periodicholographicdisplays.holograms.wrap.WrappedHologram;
import me.ford.periodicholographicdisplays.hooks.LuckPermsHook;
import me.ford.periodicholographicdisplays.util.TimeUtils;

/**
 * ManageSub
 */
public class ManageSub extends OptionPairSetSub {
    private static final String PERMS = "phd.manage";
    private static final String USAGE_1 = "/phd manage <hologram> <type> times <integer> time <hh:mm> "
            + "[<options...>]";
    private static final String USAGE;
    static {
        List<String> lines = new ArrayList<>();
        for (PeriodicType type : PeriodicType.values()) {
            lines.add(getUsage(type));
        }
        USAGE = String.join("\n", lines);
    }
    private final IPeriodicHolographicDisplays phd;

    public static String getUsage(PeriodicType type) {
        String msg = USAGE_1.replace("<type>", type.name());
        switch (type) {
            case ALWAYS:
                msg = msg.replace("time <hh:mm> ", "");
            case IRLTIME:
            case MCTIME:
                msg = msg.replace("times <integer> ", "");
                break;
            case NTIMES:
                msg = msg.replace("time <hh:mm> ", "");
                break;
            default:
                break; // do nothing
        }
        return msg;
    }

    private final HologramStorage storage;
    private final LuckPermsHook hook;
    private final Messages messages;
    private final List<String> settables = Arrays.asList("times", "time", "seconds", "distance", "permission", "flash",
            "flashOn", "flashOff");

    public ManageSub(IPeriodicHolographicDisplays phd) {
        super(phd.getHologramProvider(), "manage", PERMS, USAGE);
        this.phd = phd;
        this.storage = phd.getHolograms();
        this.hook = phd.getLuckPermsHook();
        this.messages = phd.getMessages();
    }

    @Override
    public List<String> onTabComplete(SDCRecipient sender, String[] args) {
        List<String> list = new ArrayList<>();
        switch (args.length) {
            case 1:
                return StringUtil.copyPartialMatches(args[0], getNamedHolograms(), list);
            case 2:
                List<String> typeNames = PeriodicType.names();
                for (PeriodicType type : storage.getAvailableTypes(args[0])) {
                    typeNames.remove(type.name());
                }
                return StringUtil.copyPartialMatches(args[1], typeNames, list);
            case 3:
            case 5:
            case 7:
            case 9:
            case 11:
                List<String> options = new ArrayList<>(settables);
                PeriodicType type;
                try {
                    type = PeriodicType.valueOf(args[1].toUpperCase());
                } catch (IllegalArgumentException e) {
                    return list;
                }
                if (type != PeriodicType.MCTIME && type != PeriodicType.IRLTIME) {
                    options.remove("time");
                }
                if (type != PeriodicType.NTIMES) {
                    options.remove("times");
                }
                for (int i = 2; i < args.length - 2; i += 2) {
                    options.remove(args[i]);
                }
                for (String arg : args) {
                    if (arg.equalsIgnoreCase("flash")) {
                        options.remove("flashOn");
                        options.remove("flashOff");
                    } else if (arg.equalsIgnoreCase("flashOn") || arg.equalsIgnoreCase("flashOff")) {
                        options.remove("flash");
                    }
                }
                return StringUtil.copyPartialMatches(args[args.length - 1], options, list);
            case 4:
            case 6:
            case 8:
            case 10:
            case 12:
                if (args[args.length - 2].equalsIgnoreCase("permission")) {
                    if (hook == null)
                        return list;
                    // TODO - fix
                    return hook.tabCompletePermissions((CommandSender) sender, args[args.length - 1]);
                }
        }
        return list;
    }

    @Override
    public boolean onOptionedCommand(SDCRecipient sender, String[] args, SDCCommandOptionSet options) {
        if (args.length < 1) {
            return false;
        }
        WrappedHologram holo = provider.getByName(args[0]);
        if (holo == null) {
            sender.sendMessage(messages.getHDHologramNotFoundMessage().createWith(args[0]));
            return true;
        }
        if (args.length < 2) {
            return false;
        }
        PeriodicType type;
        try {
            type = PeriodicType.valueOf(args[1].toUpperCase());
        } catch (IllegalArgumentException e) {
            sender.sendMessage(messages.getTypeNotRecognizedMessage().createWith(args[1]));
            return true;
        }
        boolean defaultedAlways = args.length == 2 && type == PeriodicType.ALWAYS;
        if (args.length < 4 && !defaultedAlways) { // in case of ALWAYS, allow all defaults
            return false;
        }
        Map<String, String> optionPairs;
        if (defaultedAlways) {
            optionPairs = new HashMap<>();
        } else {
            try {
                optionPairs = getOptionPairs(Arrays.copyOfRange(args, 2, args.length));
            } catch (IllegalArgumentException e) {
                sender.sendMessage(messages.getNeedPairedOptionsMessage().getMessage());
                return true;
            }
        }
        WorldHologramStorage worldStorage = storage.getHolograms(holo.getBukkitLocation().getWorld());
        FlashingHologram existing = worldStorage.getHologram(holo.getName(), type);
        if (existing != null) { // already managed
            sender.sendMessage(messages.getHologramAlreadyManagedMessage().createWith(holo.getName(), type));
            return true;
        }
        existing = adoptHologram(sender, holo, type, optionPairs);
        if (existing == null) {
            return true;
        }
        if (!defaultedAlways) {
            try {
                setAll(sender, existing, optionPairs, false);
            } catch (OptionPairException e) {
                switch (e.getType()) {
                    case NEED_A_NUMBER:
                        sender.sendMessage(messages.getNeedANumberMessage().createWith(e.getExtra()));
                        break;
                    case NEED_AN_INTEGER:
                        sender.sendMessage(messages.getNeedAnIntegerMessage().createWith(e.getExtra()));
                        break;
                    case INCORRECT_TIME:
                        sender.sendMessage(messages.getIncorrectTimeMessage().createWith(e.getExtra()));
                        break;
                    case NO_SUCH_OPTION:
                        sender.sendMessage(messages.getNoSuchOptionMessage().createWith(type, e.getExtra()));
                        break;
                    case DISTANCE_NEGATIVE:
                        sender.sendMessage(messages.getDistanceTooSmallMessage().createWith(e.getExtra()));
                        break;
                    case SECONDS_NEGATIVE:
                        sender.sendMessage(messages.getSecondsTooSmallMessage().createWith(e.getExtra()));
                        break;
                    case FLASH_ONLY_ONE:
                        sender.sendMessage(messages.getFlashMustHaveBothMessage().createWith(e.getExtra()));
                        break;
                    case FLASH_TOO_SMALL:
                        sender.sendMessage(messages.getFlashTimeTooSmallMessage().createWith(e.getExtra()));
                        break;
                    case TIMES_TOO_SMALL:
                        sender.sendMessage(messages.getNegativeTimesMessage().createWith(e.getExtra()));
                        break;
                    default:
                        sender.sendRawMessage("Unusual problem: " + e);
                }
                if (worldStorage.getHologram(holo.getName(), type) == null)
                    existing.markRemoved(); // nothing managed before -> not adding
                return true;
            }
        }
        storage.addHologram(existing);
        sender.sendMessage(messages.getStartedManagingMessage().createWith(holo.getName(), type, optionPairs));
        return true;
    }

    // TODO - SRP - this should throw exceptions that are caught and the appropriate
    // message sent in onCommand
    private FlashingHologram adoptHologram(SDCRecipient sender, WrappedHologram holo, PeriodicType type,
            Map<String, String> optionPairs) {
        FlashingHologram existing;
        double defaultDistance = PeriodicHologramBase.NO_DISTANCE;
        int showTime = PeriodicHologramBase.NO_SECONDS;
        String perms = null; // default to nothing
        double flashOn = FlashingHologram.NO_FLASH;
        double flashOff = FlashingHologram.NO_FLASH;
        if (optionPairs.containsKey("flash")) {
            double flash;
            try {
                flash = Double.parseDouble(optionPairs.get("flash"));
                flashOn = flash;
                flashOff = flash;
            } catch (NumberFormatException e) {
            } // remain default
        }
        if (optionPairs.containsKey("flashon")) {
            try {
                flashOn = Double.parseDouble(optionPairs.get("flashon"));
            } catch (NumberFormatException e) {
            } // remains default
        }
        if (optionPairs.containsKey("flashoff")) {
            try {
                flashOff = Double.parseDouble(optionPairs.get("flashoff"));
            } catch (NumberFormatException e) {
            } // remains default
        }
        switch (type) {
            case IRLTIME:
                String tResult = optionPairs.get("time");
                if (tResult == null) {
                    sender.sendMessage(messages.getOptionMissingMessage().createWith(type, "time"));
                    return null;
                }
                long time;
                try {
                    time = TimeUtils.parseHoursAndMinutesToSeconds(tResult);
                } catch (IllegalArgumentException e) {
                    sender.sendMessage(messages.getIncorrectTimeMessage().createWith(tResult));
                    return null;
                }
                existing = new IRLTimeHologram(phd, holo, holo.getName(), defaultDistance, showTime, time, true, perms,
                        flashOn, flashOff);
                break;
            case MCTIME:
                String timeResult = optionPairs.get("time");
                if (timeResult == null) {
                    sender.sendMessage(messages.getOptionMissingMessage().createWith(type, "time"));
                    return null;
                }
                long timeAt;
                try {
                    timeAt = TimeUtils.parseMCTime(timeResult);
                } catch (IllegalArgumentException e) {
                    sender.sendMessage(messages.getIncorrectTimeMessage().createWith(timeResult));
                    return null;
                }
                existing = new MCTimeHologram(phd, holo, holo.getName(), defaultDistance, showTime, timeAt, true, perms,
                        flashOn, flashOff);
                break;
            case ALWAYS:
                existing = new AlwaysHologram(phd, holo, holo.getName(), defaultDistance, showTime, true, perms,
                        flashOn,
                        flashOff);
                break;
            case NTIMES:
            default:
                int timesToShow = 1;
                String timesResult = optionPairs.get("times");
                if (timesResult != null) { // otherwise stay at 1
                    try {
                        timesToShow = Integer.parseInt(timesResult);
                    } catch (NumberFormatException e) {
                        sender.sendMessage(messages.getNeedAnIntegerMessage().createWith(timesResult));
                        return null;
                    }
                } else {
                    sender.sendMessage(messages.getOptionMissingMessage().createWith(type, "times"));
                    return null;
                }
                existing = new NTimesHologram(phd, holo, holo.getName(), defaultDistance, showTime, timesToShow, true,
                        perms,
                        flashOn, flashOff);
                break;
        }
        return existing;
    }

}