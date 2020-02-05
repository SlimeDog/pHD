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
import me.ford.periodicholographicdisplays.holograms.PeriodicHologramBase;

/**
 * UnsetSub
 */
public class UnsetSub extends SubCommand {
    private static final String PERMS = "phd.set";
    private static final String USAGE = "phd unset {hologram} [distance] [time] [permission]";
    private final HologramStorage storage;
    private final Settings settings;
    private final Messages messages;
    private final List<String> optionList = Arrays.asList("distance", "time", "permission");

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
        case 3:
        case 4:
            return StringUtil.copyPartialMatches(args[args.length - 1], optionList, list);
        }
        return list;
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        if (args.length < 2) return false;
        PeriodicHologramBase hologram = storage.getHologram(args[0]);
        if (hologram == null) {
            sender.sendMessage(messages.getHologramNotFoundMessage(args[0]));
            return true;
        }
        String[] opts = Arrays.copyOfRange(args, 1, args.length);
        for (String opt : opts) {
            switch(opt) {
                case "distance":
                hologram.setActivationDistance(settings.getDefaultActivationDistance());
                break;
                case "time":
                hologram.setShowTime(settings.getDefaultShowTime());
                break;
                case "permission":
                hologram.setPermissions(null);
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