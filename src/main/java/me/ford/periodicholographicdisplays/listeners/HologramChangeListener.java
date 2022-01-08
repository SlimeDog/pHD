package me.ford.periodicholographicdisplays.listeners;

import java.util.function.Consumer;

import me.filoghost.holographicdisplays.plugin.event.InternalHologramChangeEvent;
import me.filoghost.holographicdisplays.plugin.internal.hologram.InternalHologram;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import me.ford.periodicholographicdisplays.PeriodicHolographicDisplays;

/**
 * HologramChangeListener
 */
public class HologramChangeListener implements Listener {
    private final Consumer<InternalHologram> hConsumer;

    public HologramChangeListener(Consumer<InternalHologram> hConsumer) {
        this.hConsumer = hConsumer;
        PeriodicHolographicDisplays phd = JavaPlugin.getPlugin(PeriodicHolographicDisplays.class);
        phd.getServer().getPluginManager().registerEvents(this, phd);
    }

    @EventHandler
    public void onHologramNameChange(InternalHologramChangeEvent event) {
        hConsumer.accept(event.getHologram());
    }

}