package me.ford.periodicholographicdisplays.listeners;

import java.util.function.Consumer;

import org.apache.commons.lang.Validate;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.plugin.PluginManager;

import me.ford.periodicholographicdisplays.IPeriodicHolographicDisplays;

/**
 * ChunkListener
 */
public class PerChunkListener implements Listener {
    private final World world;
    private final Consumer<Chunk> chunkLoad;
    private final Consumer<Chunk> chunkUnload;

    public PerChunkListener(IPeriodicHolographicDisplays phd, PluginManager pm, World world, Consumer<Chunk> chunkLoad, Consumer<Chunk> chunkUnload) {
        Validate.notNull(world, "Cannot listen for null world");
        Validate.notNull(chunkLoad, "The loading consumer cannot be null");
        Validate.notNull(chunkUnload, "The unloading consumer cannot be null");
        this.world = world;
        this.chunkLoad = chunkLoad;
        this.chunkUnload = chunkUnload;
        pm.registerEvents(this, phd.asPlugin());
    }

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {
        if (event.getWorld() != world)
            return;
        chunkLoad.accept(event.getChunk());
    }

    @EventHandler
    public void onChunkLoad(ChunkUnloadEvent event) {
        if (event.getWorld() != world)
            return;
        chunkUnload.accept(event.getChunk());
    }

}