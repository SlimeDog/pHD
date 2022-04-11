package me.ford.periodicholographicdisplays.commands;

import java.util.ArrayList;
import java.util.List;

import me.filoghost.holographicdisplays.plugin.internal.hologram.InternalHologram;
import me.filoghost.holographicdisplays.plugin.internal.hologram.InternalHologramManager;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.ratas.slimedogcore.impl.commands.AbstractSubCommand;

public abstract class PHDSubCommand extends AbstractSubCommand {
    protected final InternalHologramManager man;

    protected PHDSubCommand(InternalHologramManager man, String name, String permission, String usage) {
        super(name, permission, usage);
        this.man = man;
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

}