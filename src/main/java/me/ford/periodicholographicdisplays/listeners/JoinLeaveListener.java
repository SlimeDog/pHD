package me.ford.periodicholographicdisplays.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import me.ford.periodicholographicdisplays.holograms.HologramStorage;
import me.ford.periodicholographicdisplays.users.UserStorage;

/**
 * JoinLeaveListener
 */
public class JoinLeaveListener implements Listener {
    private final HologramStorage storage;
    private final UserStorage userStorage;

    public JoinLeaveListener(HologramStorage storage, UserStorage userStorage) {
        this.storage = storage;
        this.userStorage = userStorage;
    }
    
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        storage.joined(player);
        if (!player.hasPlayedBefore()) {
            userStorage.getCache().set(player.getUniqueId(), player.getName());
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        storage.left(event.getPlayer());
    }
    
}