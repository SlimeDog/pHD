package me.ford.periodicholographicdisplays;

import java.util.List;
import java.util.UUID;

import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import dev.ratas.slimedogcore.impl.SlimeDogCore;

public abstract class AbstractPeriodicHolographicDisplays extends SlimeDogCore implements IPeriodicHolographicDisplays {

    @Override
    public List<World> getWorlds() {
        return getServer().getWorlds();
    }

    @Override
    public Player getPlayer(UUID id) {
        return getServer().getPlayer(id);
    }

    @Override
    public JavaPlugin asPlugin() {
        return this;
    }

    @Override
    public OfflinePlayer getOfflinePlayer(UUID id) {
        return getServer().getOfflinePlayer(id);
    }

}