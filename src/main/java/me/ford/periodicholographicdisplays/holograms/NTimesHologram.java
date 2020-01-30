package me.ford.periodicholographicdisplays.holograms;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.gmail.filoghost.holographicdisplays.api.Hologram;

import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * NTimesHologram
 */
public class NTimesHologram extends PeriodicHologramBase {
    private final int timesToShow;
    private final Map<UUID, Integer> shownTo = new HashMap<>();

    public NTimesHologram(Hologram hologram, String name, double activationDistance, long showTimeTicks,
            Location location, int timesToShow) {
        super(hologram, name, activationDistance, showTimeTicks, PeriodicType.NTIMES, location);
        this.timesToShow = timesToShow;
    }

    public NTimesHologram(String name, double activationDistance, long showTimeTicks,
            Location location, int timesToShow) {
        super(name, activationDistance, showTimeTicks, PeriodicType.NTIMES, location);
        this.timesToShow = timesToShow;
    }

    @Override
    public void attemptToShow(Player player) {
        UUID id = player.getUniqueId();
        Integer shown = shownTo.get(id);
        if (shown == null || shown < timesToShow) {
            if (shown == null) shown = 0;
            show(player);
            shownTo.put(id, shown + 1);
        }   
    }

    @Override
    public PeriodicType getType() {
        return PeriodicType.NTIMES;
    }

    public int getTimesToShow() {
        return timesToShow;
    }

    public Map<UUID, Integer> getShownTo() {
        return new HashMap<>(shownTo);
    }

    public void addShownTo(UUID id, int timesShown) {
        shownTo.put(id, timesShown);
    }
    
}