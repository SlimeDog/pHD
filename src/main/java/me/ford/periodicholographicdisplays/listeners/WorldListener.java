package me.ford.periodicholographicdisplays.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.world.WorldLoadEvent;

import me.ford.periodicholographicdisplays.holograms.HologramStorage;

/**
 * WorldListener
 */
public class WorldListener implements Listener {
    private final HologramStorage storage;

    public WorldListener(HologramStorage storage) {
        this.storage = storage;
    }

    @EventHandler
    public void onWorldLeave(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        storage.leftWorld(player, event.getFrom());
        storage.joinedWorld(player, player.getWorld());
    }

    @EventHandler
    public void onWorldLoad(WorldLoadEvent event) {
        storage.newWorld(event.getWorld());
    }

}