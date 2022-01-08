package me.ford.periodicholographicdisplays.holograms.visbility;

import java.util.HashMap;
import java.util.Map;

import me.filoghost.holographicdisplays.plugin.hologram.tracking.LineTracker;

public class VisibilityManagerRegistry {
    private final Map<LineTracker<?>, LineVisibilityManager> wrappers = new HashMap<>();

    public void register(LineTracker<?> tracker, LineVisibilityManager wrapper) {
        wrappers.put(tracker, wrapper);
    }

    public LineVisibilityManager getWrapper(LineTracker<?> tracker) {
        return wrappers.get(tracker);
    }

}
