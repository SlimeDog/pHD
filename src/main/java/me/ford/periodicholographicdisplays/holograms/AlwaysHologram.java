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

    public AlwaysHologram(Hologram hologram, String name, double activationDistance, long showTime,
            boolean isNew, String perms) {
        super(hologram, name, activationDistance, showTime, -1, isNew, perms);
        settings = JavaPlugin.getPlugin(PeriodicHolographicDisplays.class).getSettings();
        checkWorldPlayers();
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
    
}