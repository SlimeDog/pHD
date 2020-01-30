package me.ford.periodicholographicdisplays.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import me.ford.periodicholographicdisplays.holograms.HologramStorage;

/**
 * JoinLeaveListener
 */
public class JoinLeaveListener implements Listener {
    private final HologramStorage storage;

    public JoinLeaveListener(HologramStorage storage) {
        this.storage = storage;
    }
    
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        storage.joined(event.getPlayer());
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        storage.left(event.getPlayer());
    }
    
}