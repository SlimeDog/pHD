package me.ford.periodicholographicdisplays.commands.subcommands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gmail.filoghost.holographicdisplays.commands.CommandValidator;
import com.gmail.filoghost.holographicdisplays.exception.CommandException;
import com.gmail.filoghost.holographicdisplays.object.NamedHologram;

import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

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
            lines.add(msg);
        }
        USAGE = String.join("\n", lines);
    }
    private final HologramStorage storage;
    private final LuckPermsHook hook;
    private final Messages messages;
    private final List<String> settables = Arrays.asList("times", "time", "seconds", "distance", "permission", "flash",
            "flashOn", "flashOff");

    public ManageSub(HologramStorage storage, LuckPermsHook hook, Messages messages) {
        this.storage = storage;
        this.hook = hook;
        this.messages = messages;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
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
                    type = PeriodicType.valueOf(args[1]);
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
                    return hook.tabCompletePermissions(sender, args[args.length - 1]);
                }
        }
        return list;
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        if (args.length < 1) {
            return false;
        }
        NamedHologram holo;
        try {
            holo = CommandValidator.getNamedHologram(args[0]);
        } catch (CommandException e) {
            sender.sendMessage(messages.getHDHologramNotFoundMessage(args[0]));
            return true;
        }
        if (args.length < 2) {
            return false;
        }
        PeriodicType type;
        try {
            type = PeriodicType.valueOf(args[1]);
        } catch (IllegalArgumentException e) {
            sender.sendMessage(messages.getTypeNotRecognizedMessage(args[1]));
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
                sender.sendMessage(messages.getNeedPairedOptionsMessage());
                return true;
            }
        }
        WorldHologramStorage worldStorage = storage.getHolograms(holo.getWorld());
        FlashingHologram existing = worldStorage.getHologram(holo.getName(), type);
        if (existing != null) { // already managed
            sender.sendMessage(messages.getHologramAlreadyManagedMessage(holo.getName(), type));
            return true;
        }
        existing = adoptHologram(sender, holo, type, optionPairs);
        if (!defaultedAlways) {
            try {
                setAll(sender, existing, optionPairs, false);
            } catch (OptionPairException e) {
                switch (e.getType()) {
                    case NEED_A_NUMBER:
                        sender.sendMessage(messages.getNeedANumberMessage(e.getExtra()));
                        break;
                    case NEED_AN_INTEGER:
                        sender.sendMessage(messages.getNeedAnIntegerMessage(e.getExtra()));
                        break;
                    case INCORRECT_TIME:
                        sender.sendMessage(messages.getIncorrectTimeMessage(e.getExtra()));
                        break;
                    case NO_SUCH_OPTION:
                        sender.sendMessage(messages.getNoSuchOptionMessage(type, e.getExtra()));
                        break;
                    case DISTANCE_NEGATIVE:
                        sender.sendMessage(messages.getNegativeDistanceMessage(e.getExtra()));
                        break;
                    case SECONDS_NEGATIVE:
                        sender.sendMessage(messages.getNegativeSecondsMessage(e.getExtra()));
                        break;
                    case FLASH_ONLY_ONE:
                        sender.sendMessage(messages.getFlashMustHaveBothMessage(e.getExtra()));
                        break;
                    case FLASH_TOO_SMALL:
                        sender.sendMessage(messages.getFlashTimeTooSmallMessage(e.getExtra()));
                        break;
                    case TIMES_TOO_SMALL:
                        sender.sendMessage(messages.getNegativeTimesMessage(e.getExtra()));
                        break;
                    default:
                        sender.sendMessage("Unusual problem: " + e);
                }
                if (worldStorage.getHologram(holo.getName(), type) == null)
                    existing.markRemoved(); // nothing managed before -> not adding
                return true;
            }
        }
        storage.addHologram(existing);
        sender.sendMessage(messages.getStartedManagingMessage(holo.getName(), type, optionPairs));
        return true;
    }

    // TODO - SRP - this should throw exceptions that are caught and the appropriate
    // message sent in onCommand
    private FlashingHologram adoptHologram(CommandSender sender, NamedHologram holo, PeriodicType type,
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
                    sender.sendMessage(messages.getOptionMissingMessage(type, "time"));
                    return null;
                }
                long time;
                try {
                    time = TimeUtils.parseHoursAndMinutesToSeconds(tResult);
                } catch (IllegalArgumentException e) {
                    sender.sendMessage(messages.getIncorrectTimeMessage(tResult));
                    return null;
                }
                existing = new IRLTimeHologram(holo, holo.getName(), defaultDistance, showTime, time, true, perms,
                        flashOn, flashOff);
                break;
            case MCTIME:
                String timeResult = optionPairs.get("time");
                if (timeResult == null) {
                    sender.sendMessage(messages.getOptionMissingMessage(type, "time"));
                    return null;
                }
                long timeAt;
                try {
                    timeAt = TimeUtils.parseMCTime(timeResult);
                } catch (IllegalArgumentException e) {
                    sender.sendMessage(messages.getIncorrectTimeMessage(timeResult));
                    return null;
                }
                existing = new MCTimeHologram(holo, holo.getName(), defaultDistance, showTime, timeAt, true, perms,
                        flashOn, flashOff);
                break;
            case ALWAYS:
                existing = new AlwaysHologram(holo, holo.getName(), defaultDistance, showTime, true, perms, flashOn,
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
                        messages.getNeedAnIntegerMessage(timesResult);
                        return null;
                    }
                }
                existing = new NTimesHologram(holo, holo.getName(), defaultDistance, showTime, timesToShow, true, perms,
                        flashOn, flashOff);
                break;
        }
        return existing;
    }

    @Override
    public boolean hasPermission(CommandSender sender) {
        return sender.hasPermission(PERMS);
    }

    @Override
    public String getUsage(CommandSender sender) {
        return USAGE;
    }

}