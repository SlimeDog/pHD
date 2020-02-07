package me.ford.periodicholographicdisplays.holograms;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.Validate;

/**
 * IndividualHologramHandler
 */
public class IndividualHologramHandler {
    private final String name;
    private final Map<PeriodicType, PeriodicHologramBase> holograms = new HashMap<>();
    private boolean needsSaved = false; // in case something gets removed

    public IndividualHologramHandler(PeriodicType type, PeriodicHologramBase holo) {
        Validate.notNull(holo, "Periodic hologram cannot be null");
        name = holo.getName();
        addHologram(type, holo);
    }

    void addHologram(PeriodicType type, PeriodicHologramBase holo) {
        Validate.notNull(type, "PeriodicType cannot be null");
        Validate.notNull(holo, "Periodic hologram cannot be null");
        Validate.isTrue(holo.getType() == type, "Wrong type of hologram. Expected " + type.name() + " got " + holo.getType().name());
        Validate.isTrue(holo.getName().equals(name), "Can only handle pHD holograms of the same HD hologram");
        holograms.put(type, holo);
    }

    void removeHologram(PeriodicHologramBase hologram) {
        Validate.notNull(hologram, "Cannot remove null hologram");
        holograms.remove(hologram.getType());
        hologram.markRemoved();
        needsSaved = true;
    }

    public String getName() {
        return name;
    }

    public PeriodicHologramBase getHologram(PeriodicType type) {
        return holograms.get(type);
    }

    public Set<PeriodicHologramBase> getHolograms() {
        return new HashSet<>(holograms.values());
    }

    public Set<PeriodicType> getTypes() {
        return new HashSet<>(holograms.keySet());
    }

    public boolean needsSaved() {
        if (needsSaved) return true;
        for (PeriodicHologramBase holo : holograms.values()) {
            if (holo.needsSaved()) return true;
        }
        return false;
    }

    public void markSaved() {
        for (PeriodicHologramBase holo : holograms.values()) {
            holo.markSaved();
        }
        needsSaved = false;
    }
    
}