package me.ford.periodicholographicdisplays.commands.subcommands;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.bukkit.command.CommandSender;

import me.ford.periodicholographicdisplays.Messages;
import me.ford.periodicholographicdisplays.commands.SubCommand;
import me.ford.periodicholographicdisplays.holograms.HologramStorage;
import me.ford.periodicholographicdisplays.holograms.PeriodicType;

/**
 * ListSub
 */
public class ListSub extends SubCommand {
    private static final String PERMS = "phd.commands.phd.list";
    private static final String USAGE = "/phd list";
    private final HologramStorage storage;
    private final Messages messages;

    public ListSub(HologramStorage storage, Messages messages) {
        this.storage = storage;
        this.messages = messages;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return new ArrayList<>();
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        List<String> names = storage.getNames();
        names.sort(String.CASE_INSENSITIVE_ORDER);
        Map<String, String> hologramTypes = new TreeMap<>();
        for (String name : names) {
            List<PeriodicType> types = storage.getAvailableTypes(name);
            List<String> typesStr = new ArrayList<>();
            for (PeriodicType type : types) {
                typesStr.add(type.name());
            }
            hologramTypes.put(name, String.join(", ", typesStr));
        }
        sender.sendMessage(messages.getHologramListMessage(hologramTypes));
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