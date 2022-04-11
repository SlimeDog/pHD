package me.ford.periodicholographicdisplays.commands;

import java.util.ArrayList;
import java.util.List;

import me.ford.periodicholographicdisplays.holograms.wrap.WrappedHologram;
import me.ford.periodicholographicdisplays.holograms.wrap.provider.HologramProvider;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.ratas.slimedogcore.impl.commands.AbstractSubCommand;

public abstract class PHDSubCommand extends AbstractSubCommand {
    protected final HologramProvider provider;

    protected PHDSubCommand(HologramProvider provider, String name, String permission, String usage) {
        super(name, permission, usage);
        this.provider = provider;
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
        for (WrappedHologram holo : provider.getAllHolograms()) {
            holograms.add(holo.getName());
        }
        return holograms;
    }

}