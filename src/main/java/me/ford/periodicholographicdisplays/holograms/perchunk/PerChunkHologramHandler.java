package me.ford.periodicholographicdisplays.holograms.perchunk;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import me.ford.periodicholographicdisplays.holograms.IndividualHologramHandler;

/**
 * PerChunkHologramHandler
 */
public class PerChunkHologramHandler {
    private final Map<HChunk, Set<IndividualHologramHandler>> perChunkHandlers = new HashMap<>();
    private final Map<IndividualHologramHandler, HChunk> chunksOfHandlers = new HashMap<>();

    public void setChunkOf(IndividualHologramHandler handler, int x, int z) {
        removeFromChunk(handler);
        HChunk chunk = HChunk.of(x, z);
        addToChunk(chunk, handler);
    }

    public HChunk getChunkOf(IndividualHologramHandler handler) {
        return chunksOfHandlers.get(handler);
    }

    public Set<IndividualHologramHandler> getHandlersInChunk(int x, int z) {
        HChunk chunk = HChunk.of(x, z);
        Set<IndividualHologramHandler> set = perChunkHandlers.get(chunk);
        if (set == null)
            return new HashSet<>();
        else
            return new HashSet<>(set);
    }

    private void removeFromChunk(IndividualHologramHandler handler) {
        HChunk prevChunk = chunksOfHandlers.remove(handler);
        if (prevChunk != null) {
            Set<IndividualHologramHandler> set = perChunkHandlers.get(prevChunk);
            if (set != null) {
                set.remove(handler);
            }
        }
    }

    private void addToChunk(HChunk chunk, IndividualHologramHandler handler) {
        Set<IndividualHologramHandler> set = perChunkHandlers.get(chunk);
        if (set == null) {
            set = new HashSet<>();
            perChunkHandlers.put(chunk, set);
        }
        set.add(handler);
        chunksOfHandlers.put(handler, chunk);
    }

}