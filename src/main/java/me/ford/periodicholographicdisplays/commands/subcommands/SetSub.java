package me.ford.periodicholographicdisplays.commands.subcommands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.gmail.filoghost.holographicdisplays.commands.CommandValidator;
import com.gmail.filoghost.holographicdisplays.exception.CommandException;
import com.gmail.filoghost.holographicdisplays.object.NamedHologram;

import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

import me.ford.periodicholographicdisplays.Messages;
import me.ford.periodicholographicdisplays.Settings;
import me.ford.periodicholographicdisplays.commands.SubCommand;
import me.ford.periodicholographicdisplays.holograms.HologramStorage;
import me.ford.periodicholographicdisplays.holograms.NTimesHologram;
import me.ford.periodicholographicdisplays.holograms.PeriodicHologram;
import me.ford.periodicholographicdisplays.holograms.PeriodicHologramBase;
import me.ford.periodicholographicdisplays.holograms.PeriodicType;
import me.ford.periodicholographicdisplays.holograms.WorldHologramStorage;

/**
 * SetSub
 */
public class SetSub extends SubCommand {
    private static final String PERMS = "phd.set";
    private static final String USAGE = "/phd set {hologram} {type} [times {integer}] [time {hh:mm}] [seconds {integer}] [distance {integer|decimal}] [permission {string}]";
    private final HologramStorage storage;
    private final Settings settings;
    private final Messages messages;
    private final List<String> settables = Arrays.asList("times", "time", "seconds", "distance", "permission");

    public SetSub(HologramStorage storage, Settings settings, Messages messages) {
        this.storage = storage;
        this.settings = settings;
        this.messages = messages;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        List<String> list = new ArrayList<>();
        switch (args.length) {
        case 1:
            return StringUtil.copyPartialMatches(args[0], getNamedHolograms(), list);
        case 2:
            return StringUtil.copyPartialMatches(args[1], PeriodicType.names(), list);
        case 3:
        case 5:
        case 7:
        case 9:
        case 11:
            return StringUtil.copyPartialMatches(args[args.length - 1], settables, list);
        }
        return list;
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        if (args.length < 4) {
            return false;
        }
        NamedHologram holo;
        try {
            holo = CommandValidator.getNamedHologram(args[0]);
        } catch (CommandException e) {
            sender.sendMessage(messages.getHologramNotFoundMessage(args[0]));
            return true;
        }
        PeriodicType type;
        try {
            type = PeriodicType.valueOf(args[1]);
        } catch (IllegalArgumentException e) {
            sender.sendMessage(messages.getTypeNotRecognizedMessage(args[1]));
            return true;
        }
        Map<String, String> optionPairs;
        try {
            optionPairs = getOptionPairs(Arrays.copyOfRange(args, 2, args.length));
        } catch (IllegalArgumentException e) {
            sender.sendMessage("Need an even number of arguments (pairs)! - TODO - messaging");
            return true;
        }
        WorldHologramStorage worldStorage = storage.getHolograms(holo.getWorld());
        PeriodicHologramBase existing = worldStorage.getHologram(holo.getName(), type);
        boolean existed = existing != null;
        if (!existed) { // keep track of new one
            existing = adoptHologram(sender, holo, type, optionPairs);
            if (existing == null) return true;
            storage.addHologram(existing);
        }
        setAll(sender, existing, optionPairs, existed);
        sender.sendMessage("Set these: " + optionPairs + " for " + holo.getName() + "; (this might contain extra stuff, not a proper message) - TODO - messaging");
        return true;
    }

    // TODO - SRP - this should throw exceptions that are caught and the appropriate message sent in onCommand
    private PeriodicHologramBase adoptHologram(CommandSender sender, NamedHologram holo, PeriodicType type, Map<String, String> optionPairs) {
        PeriodicHologramBase existing;
        double defaultDistance = settings.getDefaultActivationDistance();
        int showTime = settings.getDefaultShowTime();
        switch (type) {
            case PERIODIC:
            String delayResult = optionPairs.get("delay");
            if (delayResult == null) {
                sender.sendMessage("a PERIODIC hologram needs a delay to be set - TODO - messaging");
                return null;
            }
            int showDelay;
            try {
                showDelay = Integer.parseInt(delayResult);
            } catch (NumberFormatException e) {
                messages.getNeedANumberMessage(delayResult);
                return null;
            }
            existing = new PeriodicHologram(holo, holo.getName(), defaultDistance, showTime, showDelay, true);
            break;
            case ALWAYS:
            case NTIMES:
            default:
            int timesToShow = 1;
            String timesResult = optionPairs.get("times");
            if (timesResult != null) { // otherwise stay at 1
                try {
                    timesToShow = Integer.parseInt(timesResult);
                } catch (NumberFormatException e) {
                    messages.getNeedANumberMessage(timesResult);
                    return null;
                }
            }
            if (type == PeriodicType.ALWAYS) timesToShow = -1;
            existing = new NTimesHologram(holo, holo.getName(), defaultDistance, showTime, timesToShow, true);
            break;
        }
        return existing;
    }

    private void setAll(CommandSender sender, PeriodicHologramBase holo, Map<String, String> options, boolean doSpecial) {
        
        for (Entry<String, String> entry : options.entrySet()) {
            String result = entry.getValue();
            if (holo.getType() == PeriodicType.PERIODIC && entry.getKey().equalsIgnoreCase("delay")) {
                long delay;
                try {
                    delay = Long.parseLong(result);
                } catch (NumberFormatException e) {
                    sender.sendMessage(messages.getNeedANumberMessage(result));
                    return;
                }
                ((PeriodicHologram) holo).setShowDelay(delay);
                continue;
            }
            if (holo.getType() == PeriodicType.NTIMES && entry.getKey().equalsIgnoreCase("times")) {
                int times;
                try {
                    times = Integer.parseInt(result);
                } catch (NumberFormatException e) {
                    sender.sendMessage(messages.getNeedANumberMessage(result));
                    return;
                }
                ((NTimesHologram) holo).setTimesToShow(times);
                continue;
            }
            switch (entry.getKey()) {
                case "distance":
                double distance;
                try {
                    distance = Double.parseDouble(result);
                } catch (NumberFormatException e) {
                    sender.sendMessage(messages.getNeedANumberMessage(result));
                    return;
                }
                holo.setActivationDistance(distance);
                break;
                case "time":
                int time;
                try {
                    time = Integer.parseInt(result);
                } catch (NumberFormatException e) {
                    sender.sendMessage(messages.getNeedANumberMessage(result));
                    return;
                }
                holo.setShowTime(time);
                break;
                case "permission":
                holo.setPermissions(result);
                break;
                default:
                sender.sendMessage("Unable to set option " + entry.getKey() + " TODO - messaging");
                break; 
            }
        }
    }

    private Map<String, String> getOptionPairs(String[] args) {
        if (args.length%2 != 0) throw new IllegalArgumentException("Expected an even number of arguments!");
        Map<String, String> map = new HashMap<>();
        boolean isKey = true;
        String curKey = "";
        String curValue = "";
        for (String arg : args) {
            if (isKey) {
                curKey = arg.toLowerCase();
            } else {
                curValue = arg;
            }
            if (!isKey) {
                map.put(curKey, curValue);
            }
            isKey = !isKey;
        }
        return map;
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