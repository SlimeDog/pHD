package me.ford.periodicholographicdisplays.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.world.TimeSkipEvent;

import me.ford.periodicholographicdisplays.holograms.HologramStorage;

/**
 * WorldTimeListener
 */
public class SimpleWorldTimeListener extends WorldTimeListener {

    public SimpleWorldTimeListener(HologramStorage storage) {
        super(storage);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onTimeChange(TimeSkipEvent event) {
        getStorage().mcTimeChanged(event.getWorld(), event.getSkipAmount());
    }

}