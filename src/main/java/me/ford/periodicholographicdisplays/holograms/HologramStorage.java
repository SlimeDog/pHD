package me.ford.periodicholographicdisplays.holograms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.bukkit.World;
import org.bukkit.entity.Player;

import me.ford.periodicholographicdisplays.PeriodicHolographicDisplays;

/**
 * HologramStorage
 */
public class HologramStorage {
    private final PeriodicHolographicDisplays plugin;
    private final Map<World, WorldHologramStorage> holograms = new HashMap<>();

    public HologramStorage(PeriodicHolographicDisplays plugin) {
        this.plugin = plugin;
        for (World world : plugin.getServer().getWorlds()) {
            holograms.put(world, new WorldHologramStorage(plugin, world));
        }
    }

    public WorldHologramStorage getHolograms(World world) {
        Validate.notNull(world, "Cannot get holograms of a null world!");
        WorldHologramStorage storage = holograms.get(world);
        if (storage == null) {
            storage = new WorldHologramStorage(plugin, world);
            holograms.put(world, storage);
            plugin.getLogger().info("Initializing per world holograms at an odd time!");
        }
        return storage;
    }

    // adding

    public void addHologram(PeriodicHologramBase hologram) {
        WorldHologramStorage storage = getHolograms(hologram.getLocation().getWorld());
        storage.addHologram(hologram);
        storage.save();
    }

    public void removeHologram(PeriodicHologramBase hologram) {
        Validate.notNull(hologram, "Cannot remove null hologram");
        WorldHologramStorage storage = holograms.get(hologram.getLocation().getWorld());
        storage.removeHologram(hologram);
    }

    // saving

    public void save() {
        for (WorldHologramStorage storage : holograms.values()) {
            storage.save();
        }
    }

    public PeriodicHologramBase getHologram(String name, PeriodicType type) {
        for (WorldHologramStorage storage : holograms.values()) {
            PeriodicHologramBase holo = storage.getHologram(name, type);
            if (holo != null) return holo;
        }
        return null;
    }

    public void mcTimeChanged(World world, long amount) {
        WorldHologramStorage storage = holograms.get(world);
        if (storage == null) return; // nothing being tracked
        for (PeriodicHologramBase holo : storage.getHolograms()) {
            if (holo.getType() == PeriodicType.MCTIME) {
                ((MCTimeHologram) holo).timeChanged(amount);
            }
        }
    }

    // onJoin holgorams

    public void joined(Player player) {
        // nothing really...
    }

    public void left(Player player) { // left server
        // in case I need something
    }

    public void leftWorld(Player player, World world) {
        // in case I need something
    }

	public List<String> getNames() {
        List<String> names = new ArrayList<>();
        for (WorldHologramStorage storage: holograms.values()) {
            names.addAll(storage.getHologramNames());
        }
		return names;
	}
    
}