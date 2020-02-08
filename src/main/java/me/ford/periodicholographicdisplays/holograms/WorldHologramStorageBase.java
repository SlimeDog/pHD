package me.ford.periodicholographicdisplays.holograms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.World;

import me.ford.periodicholographicdisplays.PeriodicHolographicDisplays;

/**
 * WorldHologramStorageBase
 */
public abstract class WorldHologramStorageBase {
    private final World world;
    private final PeriodicHolographicDisplays phd;
    private final Map<String, IndividualHologramHandler> holograms = new HashMap<>();

    WorldHologramStorageBase(PeriodicHolographicDisplays phd, World world) {
        this.phd = phd;
        this.world = world;
    }

    public PeriodicHolographicDisplays getPlugin() {
        return phd;
    }

    public World getWorld() {
        return world;
    }

    protected void addHandler(String name, IndividualHologramHandler handler) {
        holograms.put(name, handler);
        
    }

    protected IndividualHologramHandler getHandler(String name) {
        return holograms.get(name);
    }

    protected void removeHandler(String name) {
        holograms.remove(name);
    }

    protected Collection<IndividualHologramHandler> getHandlers() {
        return new ArrayList<>(holograms.values());
    }

    protected Set<Entry<String, IndividualHologramHandler>> getEntries() {
        return new HashSet<>(holograms.entrySet());
    }

    protected abstract boolean saveHolograms();
    
}