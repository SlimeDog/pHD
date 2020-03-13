package me.ford.periodicholographicdisplays.listeners;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import me.ford.periodicholographicdisplays.holograms.AlwaysHologram;
import me.ford.periodicholographicdisplays.holograms.HologramStorage;
import me.ford.periodicholographicdisplays.holograms.PeriodicHologramBase;
import me.ford.periodicholographicdisplays.holograms.PeriodicType;
import me.ford.periodicholographicdisplays.holograms.WorldHologramStorage;
import me.ford.periodicholographicdisplays.hooks.CitizensHook;

/**
 * HologramListener
 */
public class HologramListener implements Listener {
    private final HologramStorage holograms;
    private final CitizensHook hook;

    public HologramListener(HologramStorage holograms, CitizensHook hook) {
        this.holograms = holograms;
        this.hook = hook;
    }

    private void movedTo(Player player, Location location) {
        if (hook != null && hook.isNPC(player)) return; // ignore
        WorldHologramStorage wh = holograms.getHolograms(location.getWorld());
        for (PeriodicHologramBase base : wh.getHolograms(true)) {
            double dist2 = base.getLocation().distanceSquared(location);
            if (dist2 < base.getSquareDistance()) {
                if (base.getType() == PeriodicType.ALWAYS &&
                      ((AlwaysHologram) base).isShownOnWorldJoin()) {
                    continue; // ignore
                }
                base.attemptToShow(player);
            }
            // handle leaving ALWAYS holograms with activation distance and FOREVER settings
            if (base.getType() == PeriodicType.ALWAYS) {
                AlwaysHologram always = (AlwaysHologram) base;
                if (!always.isShownWhileInArea()) continue;
                if (base.getLocation().distanceSquared(location) > base.getSquareDistance()) {
                    always.leftArea(player);
                }
            }
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (event.getFrom().distanceSquared(event.getTo()) == 0) return;
        movedTo(event.getPlayer(), event.getTo());
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        movedTo(event.getPlayer(), event.getTo());
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event) {
        movedTo(event.getPlayer(), event.getPlayer().getLocation());
    }

}