package me.ford.periodicholographicdisplays.holograms.perchunk;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * HChunk
 */
public class HChunk {
    private static final Map<Integer, Map<Integer, HChunk>> CHUNKS = new HashMap<>();
    private final int x, z;

    private HChunk(int x, int z) {
        this.x = x;
        this.z = z;
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, z);
    }

    public static HChunk of(int x, int z) {
        Map<Integer, HChunk> xChunks = CHUNKS.get(x);
        if (xChunks == null) {
            xChunks = new HashMap<>();
            CHUNKS.put(x, xChunks);
        }
        HChunk chunk = xChunks.get(z);
        if (chunk == null) {
            chunk = new HChunk(x, z);
            xChunks.put(z, chunk);
        }
        return chunk;
    }

    @Override
    public String toString() {
        return String.format("(%d,%d)", x, z);
    }

}