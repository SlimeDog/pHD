package me.ford.periodicholographicdisplays.holograms.visbility;

import java.util.HashMap;
import java.util.Map;

import me.filoghost.holographicdisplays.plugin.hologram.tracking.LineTracker;

public class VisibilityManagerRegistry {
    private final Map<LineTracker<?>, LineTrackerWrapper> wrappers = new HashMap<>();

    public void register(LineTracker<?> tracker, LineTrackerWrapper wrapper) {
        wrappers.put(tracker, wrapper);
    }

    public LineTrackerWrapper getWrapper(LineTracker<?> tracker) {
        return wrappers.get(tracker);
    }

}
