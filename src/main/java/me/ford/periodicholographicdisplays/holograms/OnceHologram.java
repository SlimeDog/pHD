package me.ford.periodicholographicdisplays.holograms;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.gmail.filoghost.holographicdisplays.api.Hologram;

import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * OnceHologram
 */
public class OnceHologram extends PeriodicHologramBase {
    private final Set<UUID> shownTo = new HashSet<>();

    public OnceHologram(Hologram hologram, String name, double activationDistance, long showTimeTicks, Location location) {
        super(hologram, name, activationDistance, showTimeTicks, PeriodicType.ONCE, location);
    }

    public OnceHologram(String name, double activationDistance, long showTimeTicks, Location location) {
        super(name, activationDistance, showTimeTicks, PeriodicType.ONCE, location);
    }

    public boolean hasBeenShown(UUID id) {
        return shownTo.contains(id);
    }

    public void addShownTo(UUID id) {
        shownTo.add(id);
    }

    public Set<UUID> getShownTo() {
        return new HashSet<>(shownTo);
    }

    @Override
    public void attemptToShow(Player player) {
        UUID id = player.getUniqueId();
        if (!shownTo.contains(id)) {
            show(player);
            addShownTo(id);
        }
    }
    
}