package me.ford.periodicholographicdisplays.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.filoghost.holographicdisplays.plugin.internal.hologram.InternalHologram;
import me.filoghost.holographicdisplays.plugin.internal.hologram.InternalHologramManager;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

public abstract class SubCommand implements TabExecutor {
    protected final InternalHologramManager man;

    protected SubCommand(InternalHologramManager man) {
        this.man = man;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return onTabComplete(sender, Arrays.copyOfRange(args, 1, args.length));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return onCommand(sender, Arrays.copyOfRange(args, 1, args.length));
    }

    protected Player getPlayerOrNull(CommandSender sender) {
        if (sender instanceof Player) {
            return (Player) sender;
        } else {
            return null;
        }
    }

    public List<String> getNamedHolograms() {
        List<String> holograms = new ArrayList<>();
        for (InternalHologram holo : man.getHolograms()) {
            holograms.add(holo.getName());
        }
        return holograms;
    }

    public abstract List<String> onTabComplete(CommandSender sender, String[] args);

    public abstract boolean onCommand(CommandSender sender, String[] args);

    public abstract boolean hasPermission(CommandSender sender);

    public abstract String getUsage(CommandSender sender, String[] args);

}