package me.ford.periodicholographicdisplays.holograms;

import com.gmail.filoghost.holographicdisplays.api.Hologram;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import me.ford.periodicholographicdisplays.PeriodicHolographicDisplays;
import me.ford.periodicholographicdisplays.Settings;

/**
 * AlwaysHologram
 */
public class AlwaysHologram extends NTimesHologram {
    private final Settings settings;

    public AlwaysHologram(Hologram hologram, String name, double activationDistance,
            boolean isNew, String perms) {
        super(hologram, name, activationDistance, -1, -1, isNew, perms);
        settings = JavaPlugin.getPlugin(PeriodicHolographicDisplays.class).getSettings();
        checkWorldPlayers();
    }

     @Override
     public void setShowTime(int time) {
         if (time == settings.getDefaultShowTime()) {
             if (getShowTimeTicks() == settings.getDefaultShowTime() * 20L) return;
             super.setShowTime(time);
         }
         throw new IllegalStateException("Cannot set showtime of ALWAYS hologram - it's meaningless");
     }

    public AlwaysType getAlwaysType() {
        if (isPermsOnly()) {
            return AlwaysType.PERMS_ONLY;
        }
        if (isDefaulted()) {
            return AlwaysType.DEFAULTED;
        }
        return AlwaysType.CUSTOM;
    }

    public boolean isDefaulted() {
        if (this.hasPermissions()) return false;
        if (settings.getDefaultActivationDistance() != getActivationDistance()) return false;
        return settings.getDefaultShowTime()*20L == getShowTimeTicks();
    }

    public boolean isPermsOnly() {
        if (!this.hasPermissions()) return false;
        if (settings.getDefaultActivationDistance() != getActivationDistance()) return false;
        return settings.getDefaultShowTime()*20L == getShowTimeTicks();
    }

    public void leftArea(Player player) {
        if (this.isBeingShownTo(player)) hideFrom(player);
    }

    @Override
    public void setPermissions(String perms) {
        super.setPermissions(perms);
        if (!hasActivationDistance()) {
            checkWorldPlayers();
        }
    }

    private void checkWorldPlayers() {
        for (Player player : getHologram().getWorld().getEntitiesByClass(Player.class)) {
            if (!canSee(player)) {
                hideFrom(player);
            } else {
                attemptToShow(player);
            }
        }
    }

    public boolean hasActivationDistance() {
        return getActivationDistance() != settings.getDefaultActivationDistance();
    }

    public static enum AlwaysType {
        DEFAULTED, PERMS_ONLY, CUSTOM
    }
    
}