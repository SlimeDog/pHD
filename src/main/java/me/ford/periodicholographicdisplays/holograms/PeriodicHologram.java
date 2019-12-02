package me.ford.periodicholographicdisplays.holograms;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.gmail.filoghost.holographicdisplays.api.Hologram;

import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * PeriodicHologram
 */
public class PeriodicHologram extends PeriodicHologramBase {
    private final Map<UUID, Long> lastShown = new HashMap<>();
    private final long showDelay;

    public PeriodicHologram(Hologram hologram, String name, double activationDistance, long showTimeTicks, Location location, long showDelay) { 
        super(hologram, name, activationDistance, showTimeTicks, PeriodicType.PERIODIC, location);
        this.showDelay = showDelay * 1000L; // seconds-> milliseconds
    }

    public PeriodicHologram(String name, double activationDistance, long showTimeTicks, Location location, long showDelay) {
        super(name, activationDistance, showTimeTicks, PeriodicType.PERIODIC, location);
        this.showDelay = showDelay * 1000L; // seconds-> milliseconds
    }

    public long getShowDelay() {
        return showDelay;
    }

    public void addShownTo(UUID id, long time) {
        lastShown.put(id, time);
    }

    public Map<UUID, Long> getLastShown() {
        return new HashMap<>(lastShown);
    }

    @Override
    public void attemptToShow(Player player) {
        UUID id = player.getUniqueId();
        Long shown = lastShown.get(id);
        if (shown == null || System.currentTimeMillis() > shown + showDelay) {
            show(player);
            addShownTo(id, System.currentTimeMillis());
        }   
    }
    
}