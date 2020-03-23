package me.ford.periodicholographicdisplays.commands.subcommands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

import me.ford.periodicholographicdisplays.Messages;
import me.ford.periodicholographicdisplays.commands.SubCommand;
import me.ford.periodicholographicdisplays.holograms.FlashingHologram;
import me.ford.periodicholographicdisplays.holograms.HologramStorage;
import me.ford.periodicholographicdisplays.holograms.PeriodicType;

/**
 * UnmanageSub
 */
public class UnmanageSub extends SubCommand {
    private static final String PERMS = "phd.commands.phd.unmanage";
    private static final String USAGE = "/phd unmanage <hologram> <type>";
    private final HologramStorage storage;
    private final Messages messages;

    public UnmanageSub(HologramStorage storage, Messages messages) {
        this.storage = storage;
        this.messages = messages;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        List<String> list = new ArrayList<>();
        switch(args.length) {
            case 1:
            return StringUtil.copyPartialMatches(args[0], storage.getNames(), list);
            case 2:
            List<String> typeNames = new ArrayList<>();
            for (PeriodicType type : storage.getAvailableTypes(args[0])) {
                typeNames.add(type.name());
            }
            return StringUtil.copyPartialMatches(args[1], typeNames, list);
        }
        return list;
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
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
        FlashingHologram holo = storage.getHologram(args[0], type);
        if (holo == null) {
            sender.sendMessage(messages.getHologramNotFoundMessage(args[0], type));
            return true;
        }
        storage.removeHologram(holo);
        sender.sendMessage(messages.getUnmanagedHologramMessage(holo.getName(), type));
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