package me.ford.periodicholographicdisplays.holograms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.object.NamedHologram;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;

import me.ford.periodicholographicdisplays.holograms.events.StartedManagingHologramEvent;
import me.ford.periodicholographicdisplays.holograms.events.StoppedManagingHologramEvent;

/**
 * IndividualHologramHandler
 */
public class IndividualHologramHandler {
    private final Hologram hologram;
    private final String name;
    private final Map<PeriodicType, PeriodicHologramBase> holograms = new HashMap<>();
    private final Map<PeriodicType, PeriodicHologramBase> toSave = new HashMap<>();

    public IndividualHologramHandler(NamedHologram hologram) {
        Validate.notNull(hologram, "Periodic hologram cannot be null");
        this.hologram = hologram;
        this.name = hologram.getName();
    }

    void addHologram(PeriodicType type, PeriodicHologramBase holo) {
        addHologram(type, holo, false);
    }

    void addHologram(PeriodicType type, PeriodicHologramBase holo, boolean wasLoaded) {
        Validate.notNull(type, "PeriodicType cannot be null");
        Validate.notNull(holo, "Periodic hologram cannot be null");
        Validate.isTrue(holo.getType() == type, "Wrong type of hologram. Expected " + type.name() + " got " + holo.getType().name());
        Validate.isTrue(holo.getName().equals(name), "Can only handle pHD holograms of the same HD hologram");
        Bukkit.getPluginManager().callEvent(new StartedManagingHologramEvent(holo));
        holograms.put(type, holo);
        if (!wasLoaded) toSave.put(type, holo);
    }

    void removeHologram(PeriodicHologramBase hologram, boolean markForRemoval) {
        Validate.notNull(hologram, "Cannot remove null hologram");
        Validate.isTrue(this.hologram == hologram.getHologram(), "Can only add pHD holograms that affect the same HD hologram");
        Bukkit.getPluginManager().callEvent(new StoppedManagingHologramEvent(hologram));
        holograms.remove(hologram.getType());
        if (holograms.isEmpty()) hologram.markRemoved();
        toSave.put(hologram.getType(), null); // i.e to remove
    }

    Hologram getHologram() {
        return hologram;
    }

    public String getName() {
        return name;
    }

    public boolean isEmpty() {
        return holograms.size() == 0;
    }

    public PeriodicHologramBase getHologram(PeriodicType type) {
        return holograms.get(type);
    }

    Map<PeriodicType, PeriodicHologramBase> getToSave() {
        return new HashMap<>(toSave);
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
        for (PeriodicHologramBase holo : holograms.values()) {
            if (holo.needsSaved()) {
                toSave.put(holo.getType(), holo);
            }
        }
        return !toSave.isEmpty();
    }

    public void markSaved() {
        for (PeriodicHologramBase holo : holograms.values()) {
            holo.markSaved();
        }
        toSave.clear();
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