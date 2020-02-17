package me.ford.periodicholographicdisplays.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import me.ford.periodicholographicdisplays.holograms.AlwaysHologram;
import me.ford.periodicholographicdisplays.holograms.HologramStorage;
import me.ford.periodicholographicdisplays.holograms.PeriodicHologramBase;
import me.ford.periodicholographicdisplays.holograms.PeriodicType;
import me.ford.periodicholographicdisplays.holograms.WorldHologramStorage;

/**
 * AlwaysHologramListener
 */
public class AlwaysHologramListener implements Listener {
    private final HologramStorage storage;

    public AlwaysHologramListener(HologramStorage storage) {
        this.storage = storage;
    }

    @EventHandler 
    public void onJoin(PlayerJoinEvent event) {
        WorldHologramStorage worldStorage = storage.getHolograms(event.getPlayer().getWorld());
        for (PeriodicHologramBase holo : worldStorage.getHolograms()) {
            if (holo.getType() == PeriodicType.ALWAYS) {
                AlwaysHologram always = (AlwaysHologram) holo;
                if (!always.hasActivationDistance()) {
                    always.attemptToShow(event.getPlayer());
                }
            }
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (event.getFrom().distanceSquared(event.getTo()) == 0) return;
        WorldHologramStorage worldStorage = storage.getHolograms(event.getPlayer().getWorld());
        for (PeriodicHologramBase holo : worldStorage.getHolograms()) {
            if (holo.getType() == PeriodicType.ALWAYS) {
                AlwaysHologram always = (AlwaysHologram) holo;
                if (!always.hasActivationDistance()) continue;
                if (holo.getLocation().distanceSquared(event.getTo()) > holo.getActivationDistance()) {
                    always.leftArea(event.getPlayer());
                }
            }
        }
    }
    
}