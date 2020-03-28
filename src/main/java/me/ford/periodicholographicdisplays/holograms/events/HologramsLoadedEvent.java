package me.ford.periodicholographicdisplays.holograms.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * HologramsLoadedEvent
 */
public class HologramsLoadedEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}