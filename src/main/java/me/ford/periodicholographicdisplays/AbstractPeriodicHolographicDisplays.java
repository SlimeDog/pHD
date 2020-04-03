package me.ford.periodicholographicdisplays;

import java.util.List;

import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class AbstractPeriodicHolographicDisplays extends SchedulingPeriodicHolographicDisplays {

    @Override
    public List<World> getWorlds() {
        return getServer().getWorlds();
    }

    @Override
    public JavaPlugin asPlugin() {
        return this;
    }

}