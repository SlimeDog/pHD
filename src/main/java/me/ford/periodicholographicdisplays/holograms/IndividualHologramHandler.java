package me.ford.periodicholographicdisplays.holograms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.gmail.filoghost.holographicdisplays.api.Hologram;

import org.apache.commons.lang.Validate;

/**
 * IndividualHologramHandler
 */
public class IndividualHologramHandler {
    private final Hologram hologram;
    private final String name;
    private final Map<PeriodicType, PeriodicHologramBase> holograms = new HashMap<>();
    private boolean needsSaved = false; // in case something gets removed

    public IndividualHologramHandler(PeriodicType type, PeriodicHologramBase holo) {
        Validate.notNull(holo, "Periodic hologram cannot be null");
        hologram = holo.getHologram();
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
        Validate.isTrue(this.hologram == hologram.getHologram(), "Can only add pHD holograms that affect the same HD hologram");
        holograms.remove(hologram.getType());
        hologram.markRemoved();
        needsSaved = true;
    }

    Hologram getHologram() {
        return hologram;
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

    public boolean hasHolograms() {
        return !holograms.isEmpty();
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

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        List<String> types = new ArrayList<>();
        for (PeriodicType type : holograms.keySet()) types.add(type.name());
        return String.format("[IHH:%s;types:%s]", name, String.join(", ", types));
    }
    
}