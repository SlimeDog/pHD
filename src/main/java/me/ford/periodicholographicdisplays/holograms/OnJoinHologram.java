package me.ford.periodicholographicdisplays.holograms;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.gmail.filoghost.holographicdisplays.api.Hologram;

import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * OnJoinHologram
 */
public class OnJoinHologram extends PeriodicHologramBase {
    private final Set<UUID> shownTo = new HashSet<>();

    public OnJoinHologram(String name, double activationDistance, long showTimeTicks, Location location) {
        super(name, activationDistance, showTimeTicks, PeriodicType.ONJOIN, location);
    }

    public OnJoinHologram(Hologram hologram, String name, double activationDistance, long showTimeTicks,
            Location location) {
        super(hologram, name, activationDistance, showTimeTicks, PeriodicType.ONJOIN, location);
    }

    @Override
    public void attemptToShow(Player player) {
        UUID id = player.getUniqueId();
        if (!shownTo.contains(id)) {
            shownTo.add(id);
            show(player);
        }
    }

    public void left(Player player) {
        shownTo.remove(player.getUniqueId());
    }

    public Set<UUID> getShownTo() {
        return new HashSet<>(shownTo);
    }
    
}