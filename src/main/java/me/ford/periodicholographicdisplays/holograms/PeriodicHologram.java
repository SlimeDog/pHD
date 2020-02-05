package me.ford.periodicholographicdisplays.holograms;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.gmail.filoghost.holographicdisplays.api.Hologram;

import org.bukkit.entity.Player;

/**
 * PeriodicHologram
 */
public class PeriodicHologram extends PeriodicHologramBase {
    private final Map<UUID, Long> lastShown = new HashMap<>();
    private long showDelay;

    public PeriodicHologram(Hologram hologram, String name, double activationDistance, long showTime, long showDelay, boolean isNew) { 
        super(hologram, name, activationDistance, showTime, PeriodicType.PERIODIC, isNew);
        this.showDelay = showDelay * 1000L; // seconds-> milliseconds
    }

    public void setShowDelay(long delay) {
        showDelay = delay * 1000L; // second -> milliseconds
        markChanged();
    }

    public long getShowDelay() {
        return showDelay;
    }

    public void addShownTo(UUID id, long time) {
        lastShown.put(id, time);
        markChanged();
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