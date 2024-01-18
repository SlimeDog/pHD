package me.ford.periodicholographicdisplays.holograms.wrap.platform;

import org.bukkit.plugin.java.JavaPlugin;

import me.ford.periodicholographicdisplays.holograms.wrap.command.CommandWrapper;
import me.ford.periodicholographicdisplays.holograms.wrap.provider.HologramProvider;

public interface HologramPlatform {

    HologramProvider getHologramProvider();

    CommandWrapper getHologramCommand();

    String getName();

    JavaPlugin getProvidingPlugin();

    default boolean requiresLongerLoadDelay() {
        return false;
    }

}
