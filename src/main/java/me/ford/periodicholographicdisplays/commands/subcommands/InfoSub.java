package me.ford.periodicholographicdisplays.commands.subcommands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

import me.ford.periodicholographicdisplays.Messages;
import me.ford.periodicholographicdisplays.commands.SubCommand;
import me.ford.periodicholographicdisplays.holograms.HologramStorage;
import me.ford.periodicholographicdisplays.holograms.PeriodicHologramBase;
import me.ford.periodicholographicdisplays.holograms.PeriodicType;

/**
 * InfoSub
 */
public class InfoSub extends SubCommand {
    private static final String PERMS = "phd.commands.phd.info";
    private static final String USAGE = "/phd info <name> <type>";
    private final HologramStorage storage;
    private final Messages messages;

    public InfoSub(HologramStorage storage, Messages messages) {
        this.storage = storage;
        this.messages = messages;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        List<String> list = new ArrayList<>();
        switch(args.length) {
        case 1:
            List<String> names = storage.getNames();
            names.sort(String.CASE_INSENSITIVE_ORDER);
            return StringUtil.copyPartialMatches(args[0], names, list);
        case 2:
            List<PeriodicType> types = storage.getAvailableTypes(args[0]);
            List<String> availableTypes = new ArrayList<>();
            for (PeriodicType type : types) {
                availableTypes.add(type.name());
            }
            return StringUtil.copyPartialMatches(args[1], availableTypes, list);
        } 
        return list;
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        if (args.length < 1) {
            return false;
        }
        if (args.length == 1) {
            List<PeriodicType> availableTypes = storage.getAvailableTypes(args[0]);
            sender.sendMessage(messages.getAvailableTypesMessage(args[0], availableTypes));
            return true;
        }
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