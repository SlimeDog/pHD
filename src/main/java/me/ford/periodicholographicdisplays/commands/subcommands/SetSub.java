package me.ford.periodicholographicdisplays.commands.subcommands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

import dev.ratas.slimedogcore.api.commands.SDCCommandOptionSet;
import dev.ratas.slimedogcore.api.messaging.recipient.SDCRecipient;
import me.ford.periodicholographicdisplays.Messages;
import me.ford.periodicholographicdisplays.Settings;
import me.ford.periodicholographicdisplays.holograms.FlashingHologram;
import me.ford.periodicholographicdisplays.holograms.HologramStorage;
import me.ford.periodicholographicdisplays.holograms.PeriodicType;
import me.ford.periodicholographicdisplays.holograms.WorldHologramStorageBase.HologramSaveReason;
import me.ford.periodicholographicdisplays.holograms.wrap.provider.HologramProvider;
import me.ford.periodicholographicdisplays.hooks.LuckPermsHook;

/**
 * SetSub
 */
public class SetSub extends OptionPairSetSub {
    private static final String PERMS = "phd.set";
    private static final String USAGE = "/phd set <hologram> <type> <options...>";
    private final HologramStorage storage;
    private final LuckPermsHook hook;
    private final Messages messages;
    private final List<String> settables = Arrays.asList("times", "time", "seconds", "distance", "permission", "flash",
            "flashOn", "flashOff");

    public SetSub(HologramProvider provider, HologramStorage storage, LuckPermsHook hook, Settings settings,
            Messages messages) {
        super(provider, "set", PERMS, USAGE);
        this.storage = storage;
        this.hook = hook;
        this.messages = messages;
    }

    @Override
    public List<String> onTabComplete(SDCRecipient sender, String[] args) {
        List<String> list = new ArrayList<>();
        switch (args.length) {
            case 1:
                List<String> names = storage.getNames();
                names.sort(String.CASE_INSENSITIVE_ORDER);
                return StringUtil.copyPartialMatches(args[0], names, list);
            case 2:
                List<String> typeNames = new ArrayList<>();
                for (PeriodicType type : storage.getAvailableTypes(args[0])) {
                    typeNames.add(type.name());
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
        String holoName = args[0];
        if (storage.getAvailableTypes(holoName).size() == 0) {
            sender.sendMessage(messages.getHologramNotManagedMessage().createWith(holoName));
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
        if (args.length < 4) {
            return false;
        }
        Map<String, String> optionPairs;
        try {
            optionPairs = getOptionPairs(Arrays.copyOfRange(args, 2, args.length));
        } catch (IllegalArgumentException e) {
            sender.sendMessage(messages.getNeedPairedOptionsMessage().getMessage());
            return true;
        }
        FlashingHologram existing = storage.getHologram(holoName, type);
        if (existing == null) {
            sender.sendMessage(messages.getHologramNotTrackedMessage().createWith(holoName, type));
            return true;
        }
        try {
            setAll(sender, existing, optionPairs, true);
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
            return true;
        }
        existing.resetVisibility();
        storage.save(HologramSaveReason.CHANGE, false);
        sender.sendMessage(messages.getSetNewOptionsMessage().createWith(holoName, type, optionPairs));
        return true;
    }

}