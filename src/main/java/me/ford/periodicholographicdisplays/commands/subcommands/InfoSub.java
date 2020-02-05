package me.ford.periodicholographicdisplays.commands.subcommands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

import me.ford.periodicholographicdisplays.Messages;
import me.ford.periodicholographicdisplays.commands.SubCommand;
import me.ford.periodicholographicdisplays.holograms.HologramStorage;
import me.ford.periodicholographicdisplays.holograms.PeriodicHologramBase;

/**
 * InfoSub
 */
public class InfoSub extends SubCommand {
    private static final String PERMS = "phd.commands.phd.info";
    private static final String USAGE = "/phd info <world> <name>";
    private final HologramStorage storage;
    private final Messages messages;

    public InfoSub(HologramStorage storage, Messages messages) {
        this.storage = storage;
        this.messages = messages;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        List<String> list = new ArrayList<>();
        if (args.length == 1) {
            return StringUtil.copyPartialMatches(args[0], storage.getNames(), list);
        }
        return list;
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        if (args.length < 1) {
            return false;
        }
        PeriodicHologramBase hologram = storage.getHologram(args[0]);
        if (hologram == null) {
            sender.sendMessage(messages.getHologramNotFoundMessage(args[0]));
            return true;
        }
        sender.sendMessage(messages.getHologramInfoMessage(hologram));
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