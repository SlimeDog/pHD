package me.ford.periodicholographicdisplays.listeners;

import java.util.function.Consumer;

import org.apache.commons.lang.Validate;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.plugin.java.JavaPlugin;

import me.ford.periodicholographicdisplays.PeriodicHolographicDisplays;

/**
 * ChunkListener
 */
public class PerChunkListener implements Listener {
    private final World world;
    private final Consumer<Chunk> chunkLoad;
    private final Consumer<Chunk> chunkUnload;

    public PerChunkListener(World world, Consumer<Chunk> chunkLoad, Consumer<Chunk> chunkUnload) {
        Validate.notNull(world, "Cannot listen for null world");
        Validate.notNull(chunkLoad, "The loading consumer cannot be null");
        Validate.notNull(chunkUnload, "The unloading consumer cannot be null");
        this.world = world;
        this.chunkLoad = chunkLoad;
        this.chunkUnload = chunkUnload;
        PeriodicHolographicDisplays phd = JavaPlugin.getPlugin(PeriodicHolographicDisplays.class);
        phd.getServer().getPluginManager().registerEvents(this, phd);
    }

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {
        if (event.getWorld() != world) return;
        chunkLoad.accept(event.getChunk());
        // DEBUG
        IllegalStateException e = new IllegalStateException();
        int count = 0;
        for(StackTraceElement p : e.getStackTrace()) {
            if (p.toString().contains("me.ford.periodicholographicdisplays")) count++;
        }
        if (count >= 2) e.printStackTrace();
        // DEBUG
    }

    @EventHandler
    public void onChunkLoad(ChunkUnloadEvent event) {
        if (event.getWorld() != world) return;
        chunkUnload.accept(event.getChunk());   
    }

    
}