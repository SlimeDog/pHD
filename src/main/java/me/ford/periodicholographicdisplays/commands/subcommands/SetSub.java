package me.ford.periodicholographicdisplays.commands.subcommands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.gmail.filoghost.holographicdisplays.commands.CommandValidator;
import com.gmail.filoghost.holographicdisplays.exception.CommandException;
import com.gmail.filoghost.holographicdisplays.object.NamedHologram;

import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

import me.ford.periodicholographicdisplays.Messages;
import me.ford.periodicholographicdisplays.Settings;
import me.ford.periodicholographicdisplays.holograms.HologramStorage;
import me.ford.periodicholographicdisplays.holograms.PeriodicHologramBase;
import me.ford.periodicholographicdisplays.holograms.PeriodicType;
import me.ford.periodicholographicdisplays.holograms.WorldHologramStorage;
import me.ford.periodicholographicdisplays.holograms.WorldHologramStorageBase.HologramSaveReason;

/**
 * SetSub
 */
public class SetSub extends OptionPairSetSub {
    private static final String PERMS = "phd.set";
    private static final String USAGE = "/phd set {hologram} {type} [times {integer}] [time {hh:mm}] [seconds {integer}] [distance {integer|decimal}] [permission {string}]";
    private final HologramStorage storage;
    private final Messages messages;
    private final List<String> settables = Arrays.asList("times", "time", "seconds", "distance", "permission");

    public SetSub(HologramStorage storage, Settings settings, Messages messages) {
        this.storage = storage;
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
            sender.sendMessage(messages.getHDHologramNotFoundMessage(args[0]));
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
            sender.sendMessage(messages.getNeedPairedOptionsMessage());
            return true;
        }
        WorldHologramStorage worldStorage = storage.getHolograms(holo.getWorld());
        PeriodicHologramBase existing = worldStorage.getHologram(holo.getName(), type);
        if (existing == null) {
            sender.sendMessage(messages.getHologramNotTrackedMessage(holo.getName(), type));
            return true;
        }
        try {
            setAll(sender, existing, optionPairs, true);
        } catch (OptionPairException e) {
            switch(e.getType()) {
                case NEED_A_NUMBER:
                sender.sendMessage(messages.getNeedANumberMessage(e.getExtra()));
                break;
                case INCORRECT_TIME:
                sender.sendMessage(messages.getIncorrectTimeMessage(e.getExtra()));
                break;
                case NO_SUCH_OPTION:
                sender.sendMessage(messages.getNoSuchOptionMessage(type, e.getExtra()));
                break;
                default:
                sender.sendMessage("Unusual problem: " + e);
            }
            return true;
        }
        storage.save(HologramSaveReason.CHANGE, false);
        sender.sendMessage(messages.getSetNewOptionsMessage(holo.getName(), type, optionPairs));
        return true;
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