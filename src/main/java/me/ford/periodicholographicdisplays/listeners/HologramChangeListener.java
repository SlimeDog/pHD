package me.ford.periodicholographicdisplays.listeners;

import java.util.function.Consumer;

import com.gmail.filoghost.holographicdisplays.event.NamedHologramEditedEvent;
import com.gmail.filoghost.holographicdisplays.object.NamedHologram;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import me.ford.periodicholographicdisplays.PeriodicHolographicDisplays;

/**
 * HologramChangeListener
 */
public class HologramChangeListener implements Listener {
    private final Consumer<NamedHologram> hConsumer;

    public HologramChangeListener(Consumer<NamedHologram> hConsumer) {
        this.hConsumer = hConsumer;
        PeriodicHolographicDisplays phd = JavaPlugin.getPlugin(PeriodicHolographicDisplays.class);
        phd.getServer().getPluginManager().registerEvents(this, phd);
    }

    @EventHandler
    public void onHologramNameChange(NamedHologramEditedEvent event) {
        hConsumer.accept(event.getNamedHologram());
    }

}