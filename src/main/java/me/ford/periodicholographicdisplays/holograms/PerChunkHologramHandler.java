package me.ford.periodicholographicdisplays.holograms;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Map.Entry;

/**
 * PerChunkHologramHandler
 */
public class PerChunkHologramHandler {
    private final Map<InnerChunk, Set<IndividualHologramHandler>> perChunkHandler = new HashMap<>();

    public void addToChunk(int x, int z, IndividualHologramHandler handler) {
        getHandlersInChunkInternal(new InnerChunk(x, z)).add(handler);
    }

    public void removeFromChunk(int x, int z, IndividualHologramHandler handler) {
        InnerChunk chunk = new InnerChunk(x, z);
        Set<IndividualHologramHandler> set = getHandlersInChunkInternal(chunk);
        set.remove(handler);
        if (set.isEmpty()) {
            perChunkHandler.remove(chunk);
        }
    }

    public Set<IndividualHologramHandler> getHandlersInChunk(int x, int z) {
        InnerChunk chunk = new InnerChunk(x, z);
        if (perChunkHandler.containsKey(chunk)) { // don't keep an empty set in there if we don't change it
            return new HashSet<>(getHandlersInChunkInternal(chunk));
        } else {
            return new HashSet<>();
        }        
    }

    private Set<IndividualHologramHandler> getHandlersInChunkInternal(InnerChunk chunk) {
        Set<IndividualHologramHandler> set = perChunkHandler.get(chunk);
        if (set == null) {
            set = new HashSet<>();
            perChunkHandler.put(chunk, set);
        }
        return set;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("[PerChunkHologramHandler:");
        for (Entry<InnerChunk, Set<IndividualHologramHandler>> entry : perChunkHandler.entrySet()) {
            if (entry.getValue().size() > 0)
            builder.append(String.format("(%d,%d):%d,", entry.getKey().x, entry.getKey().z, entry.getValue().size()));
        }
        builder.append("]");
        return builder.toString();
    }
    

    private final class InnerChunk {
        private final int x;
        private final int z;

        private InnerChunk(int x, int z) {
            this.x = x;
            this.z = z;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, z);
        }

    }
    
}