package me.ford.periodicholographicdisplays.holograms;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.gmail.filoghost.holographicdisplays.api.Hologram;

import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * OnWorldJoinHologram
 */
public class OnWorldJoinHologram extends PeriodicHologramBase {
    private final Set<UUID> shownTo = new HashSet<>();

    public OnWorldJoinHologram(String name, double activationDistance, long showTimeTicks, Location location) {
        super(name, activationDistance, showTimeTicks, PeriodicType.WORLDJOIN, location);
    }

    public OnWorldJoinHologram(Hologram hologram, String name, double activationDistance, long showTimeTicks,
            Location location) {
        super(hologram, name, activationDistance, showTimeTicks, PeriodicType.WORLDJOIN, location);
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