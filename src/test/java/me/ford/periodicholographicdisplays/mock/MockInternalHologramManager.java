package me.ford.periodicholographicdisplays.mock;

import java.util.HashMap;
import java.util.Map;

import me.filoghost.holographicdisplays.api.beta.HolographicDisplaysAPI;
import me.filoghost.holographicdisplays.plugin.internal.hologram.InternalHologram;
import me.filoghost.holographicdisplays.plugin.internal.hologram.InternalHologramManager;

public class MockInternalHologramManager extends InternalHologramManager {
    private final Map<String, InternalHologram> namedHDHolograms = new HashMap<>();

    public MockInternalHologramManager(HolographicDisplaysAPI api) {
        super(api);
    }

    public InternalHologram getHologramByName(String name) {
        return namedHDHolograms.get(name);
    }

    void putHDHologram(String name, InternalHologram hologram) {
        namedHDHolograms.put(name, hologram);
    }

}
