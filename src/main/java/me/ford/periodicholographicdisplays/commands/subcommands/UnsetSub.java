package me.ford.periodicholographicdisplays.commands.subcommands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

import me.ford.periodicholographicdisplays.Messages;
import me.ford.periodicholographicdisplays.Settings;
import me.ford.periodicholographicdisplays.commands.SubCommand;
import me.ford.periodicholographicdisplays.holograms.HologramStorage;
import me.ford.periodicholographicdisplays.holograms.NTimesHologram;
import me.ford.periodicholographicdisplays.holograms.PeriodicHologramBase;
import me.ford.periodicholographicdisplays.holograms.PeriodicType;

/**
 * UnsetSub
 */
public class UnsetSub extends SubCommand {
    private static final String PERMS = "phd.set";
    private static final String USAGE = "phd unset {hologram} {type} [times] [seconds] [distance] [permission] [playercount {player}]";
    private final HologramStorage storage;
    private final Settings settings;
    private final Messages messages;
    private final List<String> optionList = Arrays.asList("times", "seconds", "distance", "permission", "playercount");

    public UnsetSub(HologramStorage storage, Settings settings, Messages messages) {
        this.storage = storage;
        this.settings = settings;
        this.messages = messages;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        List<String> list = new ArrayList<>();
        switch(args.length) {
        case 1:
            return StringUtil.copyPartialMatches(args[0], storage.getNames(), list);
        case 2:
            return StringUtil.copyPartialMatches(args[1], PeriodicType.names(), list);
        case 3:
        case 4:
        case 5:
        case 6:
        case 7:
            return StringUtil.copyPartialMatches(args[args.length - 1], optionList, list);
        }
        return list;
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        if (args.length < 3) return false;
        PeriodicType type;
        try {
            type = PeriodicType.valueOf(args[1]);
        } catch (IllegalArgumentException e) {
            sender.sendMessage(messages.getTypeNotRecognizedMessage(args[1]));
            return true;
        }
        PeriodicHologramBase hologram = storage.getHologram(args[0], type);
        if (hologram == null) {
            sender.sendMessage(messages.getHologramNotFoundMessage(args[0], type));
            return true;
        }
        String[] opts = Arrays.copyOfRange(args, 1, args.length);
        for (String opt : opts) {
            switch(opt) {
                case "distance":
                hologram.setActivationDistance(settings.getDefaultActivationDistance());
                break;
                case "seconds":
                hologram.setShowTime(settings.getDefaultShowTime());
                break;
                case "permission":
                hologram.setPermissions(null);
                break;
                case "times":
                if (hologram.getType() == PeriodicType.NTIMES) {
                    sender.sendMessage("pHD of type " + type.name() + " does not have 'times' - need NTIMES for that!");
                    return true;
                }
                ((NTimesHologram) hologram).setTimesToShow(-1); // ALWAYS
                break;
                default:
                sender.sendMessage("Unable to understand option " + opt + " - TODO - messaging");
                break;
            }
        }
        sender.sendMessage("Unset the options " + String.join(", ", opts) + " - TODO - messaging");
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