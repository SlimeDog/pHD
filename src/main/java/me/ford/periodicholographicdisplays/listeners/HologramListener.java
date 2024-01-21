package me.ford.periodicholographicdisplays.listeners;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import dev.ratas.slimedogcore.api.messaging.recipient.SDCPlayerRecipient;
import dev.ratas.slimedogcore.api.scheduler.SDCScheduler;
import dev.ratas.slimedogcore.api.wrappers.SDCOnlinePlayerProvider;
import me.ford.periodicholographicdisplays.holograms.AlwaysHologram;
import me.ford.periodicholographicdisplays.holograms.HologramStorage;
import me.ford.periodicholographicdisplays.holograms.PeriodicHologramBase;
import me.ford.periodicholographicdisplays.holograms.PeriodicType;
import me.ford.periodicholographicdisplays.holograms.WorldHologramStorage;
import me.ford.periodicholographicdisplays.hooks.NPCHook;

/**
 * HologramListener
 */
public class HologramListener implements Listener {
    private final long FORCE_MOVE_AFTER_MS = 1L * 1000L; // 1s
    private final long POS_UPDATE_TICKS = 20L; // 1s
    private final HologramStorage holograms;
    private final SDCOnlinePlayerProvider playerProvider;
    private final Function<UUID, Player> bukkitPlayerGetter;
    private final NPCHook hook;
    private final Map<UUID, Long> lastMoved = new HashMap<>();

    public HologramListener(HologramStorage holograms, SDCScheduler scheduler, SDCOnlinePlayerProvider playerProvider,
            Function<UUID, Player> bukkitPlayerGetter, NPCHook hook) {
        this.holograms = holograms;
        this.hook = hook;
        this.playerProvider = playerProvider;
        this.bukkitPlayerGetter = bukkitPlayerGetter;
        scheduler.runTaskTimer(this::positionUpdate, POS_UPDATE_TICKS, POS_UPDATE_TICKS);
    }

    private final void positionUpdate() {
        long lastMoveTarget = System.currentTimeMillis() - FORCE_MOVE_AFTER_MS;
        for (SDCPlayerRecipient player : playerProvider.getAllPlayers()) {
            if (lastMoved.getOrDefault(player.getId(), 0L) < lastMoveTarget) {
                movedTo(bukkitPlayerGetter.apply(player.getId()), player.getLocation(), false);
            }
        }
    }

    private void movedTo(Player player, Location location, boolean forceUpdate) {
        if (hook != null && hook.isNPC(player))
            return; // ignore
        lastMoved.put(player.getUniqueId(), System.currentTimeMillis());
        WorldHologramStorage wh = holograms.getHolograms(location.getWorld());
        for (PeriodicHologramBase base : wh.getHolograms(true)) {
            double dist2 = base.getLocation().distanceSquared(location);
            boolean showing = false;
            if (dist2 < base.getSquareDistance()) {
                if (base.getType() == PeriodicType.ALWAYS && ((AlwaysHologram) base).isShownOnWorldJoin()) {
                    continue; // ignore
                }
                base.attemptToShow(player);
                showing = base.isBeingShownTo(player);
            }
            // handle leaving ALWAYS holograms with activation distance and FOREVER settings
            if (base.getType() == PeriodicType.ALWAYS) {
                AlwaysHologram always = (AlwaysHologram) base;
                if (!always.isShownWhileInArea())
                    continue;
                if (base.getLocation().distanceSquared(location) > base.getSquareDistance()) {
                    always.leftArea(player);
                }
            }
            if (forceUpdate) {
                if (!showing) {
                    base.hideFrom(player);
                }
            }
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (event.getFrom().distanceSquared(event.getTo()) == 0)
            return;
        movedTo(event.getPlayer(), event.getTo(), false);
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        movedTo(event.getPlayer(), event.getTo(), event.getPlayer().getWorld() != event.getTo().getWorld());
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event) {
        movedTo(event.getPlayer(), event.getPlayer().getLocation(), true);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        movedTo(event.getPlayer(), event.getPlayer().getLocation(), true);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        lastMoved.remove(event.getPlayer().getUniqueId());
    }

}