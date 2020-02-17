package me.ford.periodicholographicdisplays.holograms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;

import me.ford.periodicholographicdisplays.PeriodicHolographicDisplays;
import me.ford.periodicholographicdisplays.holograms.perchunk.PerChunkHologramHandler;
import me.ford.periodicholographicdisplays.listeners.PerChunkListener;

/**
 * WorldHologramStorageBase
 */
public abstract class WorldHologramStorageBase {
    private final World world;
    private final PeriodicHolographicDisplays phd;
    private final Map<String, IndividualHologramHandler> holograms = new HashMap<>();
    private final Set<IndividualHologramHandler> loadedHandlers = new HashSet<>();
    private final PerChunkHologramHandler perChunkHandler;

    WorldHologramStorageBase(PeriodicHolographicDisplays phd, World world) {
        this.phd = phd;
        this.world = world;
        perChunkHandler = new PerChunkHologramHandler();
        new PerChunkListener(world, (chunk) -> chunkLoaded(chunk), (chunk) -> chunkUnloaded(chunk));
    }

    private void chunkLoaded(Chunk chunk) {
        loadedHandlers.addAll(perChunkHandler.getHandlersInChunk(chunk.getX(), chunk.getZ()));
    }

    private void chunkUnloaded(Chunk chunk) {
        loadedHandlers.removeAll(perChunkHandler.getHandlersInChunk(chunk.getX(), chunk.getZ()));
    }

    public PeriodicHolographicDisplays getPlugin() {
        return phd;
    }

    public World getWorld() {
        return world;
    }

    protected void addHandler(String name, IndividualHologramHandler handler) {
        holograms.put(name, handler);
        Location loc = handler.getHologram().getLocation();
        int x = loc.getBlockX() >> 4;
        int z = loc.getBlockZ() >> 4;
        perChunkHandler.setChunkOf(handler, x, z);
        if (world.isChunkLoaded(x, z)) {
            loadedHandlers.add(handler);
        }
    }

    protected IndividualHologramHandler getHandler(String name) {
        return holograms.get(name);
    }

    protected void removeHandler(String name) {
        IndividualHologramHandler handler = holograms.remove(name);
        if (handler != null) loadedHandlers.remove(handler);
    }

    protected Collection<IndividualHologramHandler> getHandlers(boolean onlyLoaded) {
        if (onlyLoaded) return new ArrayList<>(loadedHandlers);
        return new ArrayList<>(holograms.values());
    }

    protected Set<Entry<String, IndividualHologramHandler>> getEntries() {
        return new HashSet<>(holograms.entrySet());
    }

    protected abstract boolean saveHolograms(boolean inSync, HologramSaveReason reason);
    
    public static enum HologramSaveReason {
        ADD, PERIODIC, MANUAL
    }
    
}