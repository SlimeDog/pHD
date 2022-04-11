package me.ford.periodicholographicdisplays.holograms;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;

import org.bukkit.entity.Player;

import me.ford.periodicholographicdisplays.IPeriodicHolographicDisplays;
import me.ford.periodicholographicdisplays.holograms.wrap.WrappedHologram;

/**
 * NTimesHologram
 */
public class NTimesHologram extends FlashingHologram {
    private int timesToShow;
    private final Map<UUID, Integer> shownTo = new HashMap<>();
    private final Map<UUID, Integer> toSave = new HashMap<>();

    public NTimesHologram(IPeriodicHolographicDisplays phd, WrappedHologram hologram, String name,
            double activationDistance, long showTime, int timesToShow, boolean isNew, String perms, double flashOn,
            double flashOff) {
        this(phd, hologram, name, activationDistance, showTime, PeriodicType.NTIMES, timesToShow, isNew, perms,
                flashOn, flashOff);
    }

    NTimesHologram(IPeriodicHolographicDisplays phd, WrappedHologram hologram, String name,
            double activationDistance, long showTime, PeriodicType type, int timesToShow, boolean isNew, String perms,
            double flashOn, double flashOff) {
        super(phd, hologram, name, activationDistance, showTime, type, isNew, perms, flashOn, flashOff);
        this.timesToShow = timesToShow;
    }

    @Override
    public void attemptToShow(Player player) {
        if (!canSee(player))
            return;
        if (isBeingShownTo(player))
            return;
        UUID id = player.getUniqueId();
        Integer shown = shownTo.get(id);
        // -1 -> infinitely
        if (timesToShow == -1 || shown == null || shown < timesToShow) {
            if (shown == null)
                shown = 0;
            show(player);
            if (timesToShow > 0)
                addShownTo(id, shown + 1);
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

    public Map<UUID, Integer> getToSave() {
        return new HashMap<>(toSave);
    }

    void addAllShownTo(Map<UUID, Integer> shownTo) {
        for (Entry<UUID, Integer> entry : shownTo.entrySet()) {
            addShownTo(entry.getKey(), entry.getValue(), false);
        }
    }

    public void addShownTo(UUID id, int timesShown) {
        addShownTo(id, timesShown, true);
    }

    private void addShownTo(UUID id, int timesShown, boolean markChanged) {
        shownTo.put(id, timesShown);
        toSave.put(id, timesShown);
        if (markChanged)
            markChanged();
    }

    public void resetShownTo(UUID id) {
        shownTo.remove(id);
        toSave.put(id, 0);
        markChanged();
    }

    @Override
    public void markSaved() {
        super.markSaved();
        toSave.clear();
    }

    @Override
    protected boolean specialDisable() {
        return false;
    }

}