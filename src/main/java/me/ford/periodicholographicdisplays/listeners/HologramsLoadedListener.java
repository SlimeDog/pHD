package me.ford.periodicholographicdisplays.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import me.ford.periodicholographicdisplays.holograms.events.HologramsLoadedEvent;

/**
 * HologramsLoadedListener
 */
public class HologramsLoadedListener implements Listener {
    private Runnable whenLoaded;

    public HologramsLoadedListener(Runnable whenDone) {
        this.whenLoaded = whenDone;
    }

    @EventHandler
    public void onHologramsLoaded(HologramsLoadedEvent event) {
        whenLoaded.run();
        HandlerList.unregisterAll(this); // only once
    }

}