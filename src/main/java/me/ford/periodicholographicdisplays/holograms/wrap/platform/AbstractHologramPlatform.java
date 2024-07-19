package me.ford.periodicholographicdisplays.holograms.wrap.platform;

import org.bukkit.plugin.java.JavaPlugin;

public abstract class AbstractHologramPlatform implements HologramPlatform {
    private final JavaPlugin plugin;
    private final String name;

    public AbstractHologramPlatform(JavaPlugin plugin, String name) {
        this.plugin = plugin;
        this.name = name;
    }

    @Override
    public JavaPlugin getProvidingPlugin() {
        return plugin;
    }

    @Override
    public String getName() {
        return name;
    }

}
