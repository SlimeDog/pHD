package me.ford.periodicholographicdisplays.holograms;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

import org.apache.commons.lang.Validate;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.ford.periodicholographicdisplays.PeriodicHolographicDisplays;

/**
 * Storage
 */
public class WorldHologramStorage {
    private final World world;
    private final String fileBase = "periodicholograms_";
    private final String fileName;
    private final PeriodicHolographicDisplays plugin;
    private final File storageFile;
    private FileConfiguration storage;
    private final Map<String, PeriodicHologramBase> holograms = new HashMap<>();

    public WorldHologramStorage(PeriodicHolographicDisplays plugin, World world) {
        this.world = world;
        this.plugin = plugin;
        this.fileName = fileBase + world.getName() + ".yml";
        storageFile = new File(this.plugin.getDataFolder(), fileName);
        for (String name : getConfig().getKeys(false)) {
            PeriodicHologramBase holo;
            try {
                holo = loadHologram(name);
            } catch (HologramLoadException e) {
                plugin.getLogger().log(Level.WARNING, "Problem loading hologram from file", e);
                continue;
            }
            if (holo == null) continue;
            holograms.put(name, holo);
        }
    }

    private PeriodicHologramBase loadHologram(String name) throws HologramLoadException {
        ConfigurationSection section = getConfig().getConfigurationSection(name);
        Location loc = section.getLocation("location");
        if (loc == null) {
            throw new HologramLoadException("Unable to parse Location for hologram:" + name);
        }
        String strType = section.getString("type", "EVERYTIME");
        PeriodicType type;
        try {
            type = PeriodicType.valueOf(strType);
        } catch (IllegalArgumentException e) {
            throw new HologramLoadException("Unable to parse type of hologram: " + strType);
        }
        double distance = section.getDouble("activation-distance", 10);
        long showTimeTicks = section.getLong("show-time-ticks", 60);
        List<String> lines = section.getStringList("lines");
        final PeriodicHologramBase holo;
        switch (type) {
        case PERIODIC:
            long delay = section.getLong("delay", 86400); // in seconds
            PeriodicHologram periodic = new PeriodicHologram(name, distance, showTimeTicks, loc, delay);
            addShownTo(periodic, section.getConfigurationSection("last-shown"));
            holo = periodic;
            break;
        case ONCE:
            OnceHologram once = new OnceHologram(name, distance, showTimeTicks, loc);
            addShownTo(once, section.getStringList("shown-to"));
            holo = once;
            break;
        case NTIMES:
            int timesToShow = section.getInt("times-to-show", 1);
            NTimesHologram ntimes = new NTimesHologram(name, distance, showTimeTicks, loc, timesToShow);
            addShownToTimes(ntimes, section.getConfigurationSection("shown-to"));
            holo = ntimes;
            break;
        default: // ALWAYS
            holo = new EverytimeHologram(name, distance, showTimeTicks, loc);
            break;
        }
        holo.addLines(lines);
        return holo;
    }

    private void addShownTo(PeriodicHologram holo, ConfigurationSection section) {
        for (String uuid : section.getKeys(false)) {
            UUID id;
            try {
                id = UUID.fromString(uuid);
            } catch (IllegalArgumentException e) {
                plugin.getLogger().warning("Unable to parse UUID of Periodic hologram " + holo.getName() + " : " + uuid);
                continue;
            }
            holo.addShownTo(id, section.getLong(uuid));
        }
    }

    private void addShownToTimes(NTimesHologram holo, ConfigurationSection section) {
        for (String uuid : section.getKeys(false)) {
            UUID id;
            try {
                id = UUID.fromString(uuid);
            } catch (IllegalArgumentException e) {
                plugin.getLogger().warning("Unable to parse UUID of Periodic hologram " + holo.getName() + " : " + uuid);
                continue;
            }
            holo.addShownTo(id, section.getInt(uuid));
        }
    }

    private void addShownTo(OnceHologram holo, List<String> uuids) {
        for (String uuid : uuids) {
            UUID id;
            try {
                id = UUID.fromString(uuid);
            } catch (IllegalArgumentException e) {
                plugin.getLogger().warning("Unable to parse UUID of Periodic hologram " + holo.getName() + " : " + uuid);
                continue;
            }
            holo.addShownTo(id);
        }
    }

    public PeriodicHologramBase getHologram(String name) {
        return holograms.get(name);
    }

    public List<PeriodicHologramBase> getHolograms() {
        return new ArrayList<>(holograms.values());
    }

    public List<String> getHologramNames() {
        List<String> names = new ArrayList<>();
        for (PeriodicHologramBase holo : holograms.values()) {
            names.add(holo.getName());
        }
        return names;
    }

    public World getWorld() {
        return world;
    }

    private void saveHolograms() {
        for (PeriodicHologramBase hologram : holograms.values()) {
            saveHologram(hologram);
        }
    }

    private void saveHologram(PeriodicHologramBase hologram) {
        if (hologram == null) {
            plugin.getLogger().warning("Trying to save a null-hologram!");
            return;
        }
        ConfigurationSection section = getConfig().createSection(hologram.getName());
        section.set("location", hologram.getLocation());
        section.set("type", hologram.getType().toString());
        section.set("activation-distance", hologram.getActivationDistance());
        section.set("show-time-ticks", hologram.getShowTimeTicks());
        section.set("lines", hologram.getLines());
        if (hologram instanceof PeriodicHologram) {
            PeriodicHologram periodic = (PeriodicHologram) hologram;
            section.set("delay", periodic.getShowDelay()/1000L); // seconds->ms
            ConfigurationSection lastShownSection = section.createSection("last-shown"); 
            for (Map.Entry<UUID, Long> entry : periodic.getLastShown().entrySet()) {
                lastShownSection.set(entry.getKey().toString(), entry.getValue());
            }
        } else if (hologram instanceof OnceHologram) {
            OnceHologram once = (OnceHologram) hologram;
            List<String> shownTo = new ArrayList<>();
            for (UUID id : once.getShownTo()) {
                shownTo.add(id.toString());
            }
            section.set("shown-to", shownTo);
        } else if (hologram instanceof NTimesHologram) {
            NTimesHologram ntimes = (NTimesHologram) hologram;
            section.set("times-to-show", ntimes.getTimesToShow()); // seconds->ms
            ConfigurationSection shownToSection = section.createSection("shown-to"); 
            for (Map.Entry<UUID, Integer> entry : ntimes.getShownTo().entrySet()) {
                shownToSection.set(entry.getKey().toString(), entry.getValue());
            }

        }
    }

    void addHologram(PeriodicHologramBase hologram) {
        Validate.notNull(hologram, "Cannot add null hologram!");
        Validate.isTrue(hologram.getLocation().getWorld() == world, "Cannot add holograms in a different world!");
        holograms.put(hologram.getName(), hologram);
    }

    // config

    public void reload() {
        storage = YamlConfiguration.loadConfiguration(storageFile);

        // Look for defaults in the jar
        Reader defConfigStream = null;
        if (plugin.getResource(fileName) != null) {
            try {
                defConfigStream = new InputStreamReader(plugin.getResource(fileName), "UTF8");
            } catch (UnsupportedEncodingException e) {
                plugin.getLogger().warning("Unsupported encoding in the holograms storage file!");
            }
        }
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            storage.setDefaults(defConfig);
        }
    }

    private FileConfiguration getConfig() {
        if (storage == null) {
            reload();
        }
        return storage;
    }

    public void save() {
        if (storage == null || storageFile == null) {
            return;
        }
        saveHolograms();
        try {
            getConfig().save(storageFile);
        } catch (IOException ex) {
            plugin.getLogger().log(Level.SEVERE, "Could not save config to " + storageFile, ex);
        }
    }

    public class HologramLoadException extends IllegalArgumentException {

        /**
         *
         */
        private static final long serialVersionUID = -2998543533635166107L;
        
        public HologramLoadException(String msg) {
            super(msg);
        }

    }
    
}