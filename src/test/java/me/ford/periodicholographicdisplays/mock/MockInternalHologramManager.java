package me.ford.periodicholographicdisplays.mock;

import java.util.HashMap;
import java.util.Map;

import me.filoghost.holographicdisplays.plugin.hologram.tracking.LineTrackerManager;
import me.filoghost.holographicdisplays.plugin.internal.hologram.InternalHologram;
import me.filoghost.holographicdisplays.plugin.internal.hologram.InternalHologramManager;

public class MockInternalHologramManager extends InternalHologramManager {
    private final Map<String, InternalHologram> namedHDHolograms = new HashMap<>();

    public MockInternalHologramManager(LineTrackerManager lineTrackerManager) {
        super(lineTrackerManager);
    }

    public InternalHologram getHologramByName(String name) {
        return namedHDHolograms.get(name);
    }

    void putHDHologram(String name, InternalHologram hologram) {
        namedHDHolograms.put(name, hologram);
    }

}
