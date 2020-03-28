package me.ford.periodicholographicdisplays.holograms.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import me.ford.periodicholographicdisplays.holograms.PeriodicHologramBase;

/**
 * StartedManagingHologramEvent
 */
public class StartedManagingHologramEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final PeriodicHologramBase hologram;

    public StartedManagingHologramEvent(PeriodicHologramBase hologram) {
        this.hologram = hologram;
    }

    public PeriodicHologramBase getHologram() {
        return hologram;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}