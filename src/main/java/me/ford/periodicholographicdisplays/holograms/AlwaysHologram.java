package me.ford.periodicholographicdisplays.holograms;

import com.gmail.filoghost.holographicdisplays.api.Hologram;

import org.bukkit.entity.Player;

import me.ford.periodicholographicdisplays.hooks.NPCHook;

/**
 * AlwaysHologram
 */
public class AlwaysHologram extends NTimesHologram {

    public AlwaysHologram(Hologram hologram, String name, double activationDistance, long showTime, boolean isNew,
            String perms, double flashOn, double flashOff) {
        super(hologram, name, activationDistance, showTime, PeriodicType.ALWAYS, -1, isNew, perms, flashOn, flashOff);
        checkWorldPlayers();
    }

    public boolean isShownWhileInArea() {
        return isForever() && hasActivationDistance();
    }

    public boolean isShownOnWorldJoin() {
        return !hasActivationDistance() && isForever();
    }

    public boolean isForever() {
        return NO_SECONDS == getShowTime();
    }

    public void leftArea(Player player) {
        if (this.isBeingShownTo(player))
            hideFrom(player);
    }

    private void checkWorldPlayers() {
        NPCHook hook = getPlugin().getNPCHook();
        for (Player player : getHologram().getWorld().getEntitiesByClass(Player.class)) {
            if (hook != null && hook.isNPC(player))
                continue;
            if (!canSee(player)) {
                hideFrom(player);
            } else {
                attemptToShow(player);
            }
        }
    }

    public boolean hasActivationDistance() {
        return getActivationDistance() != NO_DISTANCE;
    }

    @Override
    protected boolean specialDisable() {
        return isShownOnWorldJoin() || isShownWhileInArea();
    }

    @Override
    public void resetVisibility() {
        super.resetVisibility();
        if (isShownOnWorldJoin()) {
            checkWorldPlayers();
        }
    }

}