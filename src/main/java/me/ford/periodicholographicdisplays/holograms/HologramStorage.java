package me.ford.periodicholographicdisplays.holograms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.bukkit.World;
import org.bukkit.entity.Player;

import me.ford.periodicholographicdisplays.PeriodicHolographicDisplays;
import me.ford.periodicholographicdisplays.holograms.WorldHologramStorageBase.HologramSaveReason;
import me.ford.periodicholographicdisplays.holograms.storage.SQLStorage;
import me.ford.periodicholographicdisplays.holograms.storage.Storage;
import me.ford.periodicholographicdisplays.holograms.storage.YAMLStorage;
import me.ford.periodicholographicdisplays.holograms.storage.Storage.HDHologramInfo;

/**
 * HologramStorage
 */
public class HologramStorage {
    private final Storage storage;
    private final PeriodicHolographicDisplays plugin;
    private final Map<World, WorldHologramStorage> holograms = new HashMap<>();

    public HologramStorage(PeriodicHolographicDisplays plugin) {
        if (plugin.getSettings().useDatabase()) {
            this.storage = new SQLStorage(plugin);
        } else {
            this.storage = new YAMLStorage(); // TODO - check config and use database if needed
        }
        this.plugin = plugin;
        initWorldStorage();
    }

    private void initWorldStorage() {
        for (World world : plugin.getServer().getWorlds()) {
            holograms.put(world, new WorldHologramStorage(plugin, world, storage));
        }
    }

    public WorldHologramStorage getHolograms(World world) {
        Validate.notNull(world, "Cannot get holograms of a null world!");
        WorldHologramStorage storage = holograms.get(world);
        if (storage == null) {
            storage = new WorldHologramStorage(plugin, world, this.storage);
            holograms.put(world, storage);
        }
        return storage;
    }

    public void reload() {
        for (WorldHologramStorage storage : holograms.values()) {
            for (PeriodicHologramBase hologram : storage.getHolograms()) {
                storage.removeHologram(hologram, false);
            }
        }
        holograms.clear();
        initWorldStorage();
    }

    public void imported(HDHologramInfo info) {
        for (WorldHologramStorage storage : holograms.values()) {
            storage.imported(info); // try for all -> the method will figure out if it's the correct world
        }
    }

    // adding

    public void addHologram(PeriodicHologramBase hologram) {
        WorldHologramStorage storage = getHolograms(hologram.getLocation().getWorld());
        storage.addHologram(hologram);
        storage.saveHolograms(false, HologramSaveReason.ADD);
    }

    public void removeHologram(PeriodicHologramBase hologram) {
        Validate.notNull(hologram, "Cannot remove null hologram");
        WorldHologramStorage storage = holograms.get(hologram.getLocation().getWorld());
        storage.removeHologram(hologram);
    }

    // saving

    public void save() {
        save(false);
    }

    public void save(boolean inSync) {
        save(HologramSaveReason.MANUAL, inSync);
    }

    public void save(HologramSaveReason reason, boolean inSync) {
        for (WorldHologramStorage storage : holograms.values()) {
            storage.saveHolograms(inSync, reason);
        }
    }

    public List<PeriodicType> getAvailableTypes(String name) {
        for (WorldHologramStorage storage : holograms.values()) {
            IndividualHologramHandler handler = storage.getHandler(name);
            if (handler != null) {
                List<PeriodicType> types = new ArrayList<>();
                for (PeriodicHologramBase holo : handler.getHolograms()) {
                    types.add(holo.getType());
                }
                return types;
            }
        }
        return new ArrayList<>();
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
        joinedWorld(player, player.getWorld());
    }

    public void joinedWorld(Player player, World world) {
        WorldHologramStorage worldStorage = getHolograms(world);
        for (PeriodicHologramBase holo : worldStorage.getHolograms()) {
            if (holo.getType() == PeriodicType.ALWAYS) {
                AlwaysHologram always = (AlwaysHologram) holo;
                if (!always.hasActivationDistance()) {
                    always.attemptToShow(player);
                }
            }
        }
    }

    public void left(Player player) { // left server
        leftWorld(player, player.getWorld());
    }

    public void leftWorld(Player player, World world) {
        WorldHologramStorage worldStorage = getHolograms(world);
        for (PeriodicHologramBase holo : worldStorage.getHolograms()) {
            if (holo.getType() == PeriodicType.ALWAYS) {
                AlwaysHologram always = (AlwaysHologram) holo;
                if (!always.hasActivationDistance()) {
                    always.hideFrom(player);
                }
            }
        }
    }

	public List<String> getNames() {
        List<String> names = new ArrayList<>();
        for (WorldHologramStorage storage: holograms.values()) {
            names.addAll(storage.getHologramNames());
        }
		return names;
	}
    
}