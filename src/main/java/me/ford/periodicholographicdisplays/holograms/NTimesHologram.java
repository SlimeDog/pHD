package me.ford.periodicholographicdisplays.holograms;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.gmail.filoghost.holographicdisplays.api.Hologram;

import org.bukkit.entity.Player;

/**
 * NTimesHologram
 */
public class NTimesHologram extends PeriodicHologramBase {
    private int timesToShow;
    private final Map<UUID, Integer> shownTo = new HashMap<>();

    public NTimesHologram(Hologram hologram, String name, double activationDistance, long showTime,
            int timesToShow, boolean isNew) {
        super(hologram, name, activationDistance, showTime, PeriodicType.NTIMES, isNew);
        this.timesToShow = timesToShow;
    }

    @Override
    public void attemptToShow(Player player) {
        if (isBeingShownTo(player)) return;
        UUID id = player.getUniqueId();
        Integer shown = shownTo.get(id);
        // -1 -> infinitely
        if (timesToShow == -1 || shown == null || shown < timesToShow) {
            if (shown == null) shown = 0;
            show(player);
            if (timesToShow > 0) addShownTo(id, shown + 1);
        }   
    }

    public void setTimesToShow(int times) {
        timesToShow = times;
        markChanged();
    }

    public int getTimesToShow() {
        return timesToShow;
    }

    public Map<UUID, Integer> getShownTo() {
        return new HashMap<>(shownTo);
    }

    public void addShownTo(UUID id, int timesShown) {
        shownTo.put(id, timesShown);
        markChanged();
    }
    
}