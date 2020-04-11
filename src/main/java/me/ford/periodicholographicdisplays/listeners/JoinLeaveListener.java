package me.ford.periodicholographicdisplays.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import me.ford.periodicholographicdisplays.PeriodicHolographicDisplays;
import me.ford.periodicholographicdisplays.holograms.HologramStorage;
import me.ford.periodicholographicdisplays.users.UserCache;

/**
 * JoinLeaveListener
 */
public class JoinLeaveListener implements Listener {
    private final PeriodicHolographicDisplays phd;
    private final HologramStorage storage;

    public JoinLeaveListener(PeriodicHolographicDisplays phd, HologramStorage storage) {
        this.phd = phd;
        this.storage = storage;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        UserCache userCache = phd.getUserCache();
        Player player = event.getPlayer();
        storage.joined(player);
        String prevName = userCache.getName(player.getUniqueId());
        if (prevName == null || !prevName.equalsIgnoreCase(player.getName())) {
            String reason = prevName == null ? "they didn't have a name cached": "they have a new name";
            phd.debug("Adding name of joining player to cache: " + player.getName() + " because " + reason);
            userCache.set(player.getUniqueId(), player.getName());
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        storage.left(event.getPlayer());
    }

}