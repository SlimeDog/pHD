package me.ford.periodicholographicdisplays.listeners;

import org.bukkit.event.Listener;

import me.ford.periodicholographicdisplays.holograms.HologramStorage;

/**
 * WorldTimeListener
 */
public abstract class WorldTimeListener implements Listener {
    private final HologramStorage storage;

    public WorldTimeListener(HologramStorage storage) {
        this.storage = storage;
    }

    public HologramStorage getStorage() {
        return storage;
    }
    
}