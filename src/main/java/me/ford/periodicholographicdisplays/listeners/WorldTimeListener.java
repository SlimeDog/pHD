package me.ford.periodicholographicdisplays.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.TimeSkipEvent;

import me.ford.periodicholographicdisplays.holograms.HologramStorage;

/**
 * WorldTimeListener
 */
public class WorldTimeListener implements Listener {
    private final HologramStorage storage;

    public WorldTimeListener(HologramStorage storage) {
        this.storage = storage;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onTimeChange(TimeSkipEvent event) {
        storage.mcTimeChanged(event.getWorld(), event.getSkipAmount());
    }

    
}