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

import com.gmail.filoghost.holographicdisplays.commands.CommandValidator;
import com.gmail.filoghost.holographicdisplays.exception.CommandException;
import com.gmail.filoghost.holographicdisplays.object.NamedHologram;

import org.apache.commons.lang.Validate;
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
    private final Map<String, IndividualHologramHandler> holograms = new HashMap<>();

    public WorldHologramStorage(PeriodicHolographicDisplays plugin, World world) {
        this.world = world;
        this.plugin = plugin;
        this.fileName = fileBase + world.getName() + ".yml";
        storageFile = new File(this.plugin.getDataFolder(), fileName);
        scheduleLoad();
        scheduleSave();
    }

    private void scheduleLoad() { // TODO - maybe there's an event?
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            for (String name : getConfig().getKeys(false)) {
                IndividualHologramHandler holo = loadHologram(name);
                if (holo == null)
                    continue;
                holograms.put(name, holo);
            }
        }, 40L); // need to do this later so the holograms are loaded
    }

    private void scheduleSave() {
        long delay = plugin.getSettings().getSaveDelay() * 20L;
        plugin.getServer().getScheduler().runTaskTimer(plugin, () -> save(), delay, delay);
    }

    private PeriodicHologramBase loadType(String name, ConfigurationSection section) throws HologramLoadException {
        if (section == null) {
            throw new HologramLoadException("Unable to parse hologram because of incorrect config (using the old system?): " + name);
        }
        PeriodicType type;
        try {
            type = PeriodicType.valueOf(section.getName());
        } catch (IllegalArgumentException e) {
            throw new HologramLoadException("Unable to parse type of hologram: " + section.getName());
        }
        NamedHologram hologram;
        try {
            hologram = CommandValidator.getNamedHologram(name);
        } catch (CommandException e) {
            throw new HologramLoadException("Hologram by the name of '" + name + "' does not exist'");
        }
        double distance = section.getDouble("activation-distance", 10);
        long showTime = section.getLong("show-time", 60);
        String perms = section.getString("permission"); // defaults to null
        final PeriodicHologramBase holo;
        switch (type) {
        case PERIODIC:
            long delay = section.getLong("delay", 86400); // in seconds
            PeriodicHologram periodic = new PeriodicHologram(hologram, name, distance, showTime, delay, false, perms);
            addShownTo(periodic, section.getConfigurationSection("last-shown"));
            holo = periodic;
            break;
        case ALWAYS:
        case NTIMES:
        default:
            int timesToShow;
            if (type == PeriodicType.ALWAYS) {
                timesToShow = -1;
            } else {
                timesToShow = section.getInt("times-to-show", 1);
            }
            NTimesHologram ntimes = new NTimesHologram(hologram, name, distance, showTime, timesToShow, false, perms);
            addShownToTimes(ntimes, section.getConfigurationSection("shown-to"));
            holo = ntimes;
            break;
        }
        return holo;
    }

    private IndividualHologramHandler loadHologram(String name) {
        ConfigurationSection section = getConfig().getConfigurationSection(name);
        IndividualHologramHandler handler = null;
        for (String typeStr : section.getKeys(false)) {
            PeriodicHologramBase holo;
            try {
                holo = loadType(name, section.getConfigurationSection(typeStr));
            } catch (HologramLoadException e) {
                plugin.getLogger().log(Level.WARNING, "Problem loading hologram of type " + typeStr + " from file for hologram " + name, e);
                continue;
            }
            if (handler == null) {
                handler = new IndividualHologramHandler(holo.getType(), holo);
            } else {
                handler.addHologram(holo.getType(), holo);
            }
        }
        return handler;
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

    public PeriodicHologramBase getHologram(String name, PeriodicType type) {
        IndividualHologramHandler handler = holograms.get(name);
        if (handler == null) return null;
        return handler.getHologram(type);
    }

    public List<PeriodicHologramBase> getHolograms() { // TODO - potentially only show those in loaded chunks
        List<PeriodicHologramBase> holos = new ArrayList<>();
        for (IndividualHologramHandler handler : holograms.values()) {
            holos.addAll(handler.getHolograms());
        }
        return holos;
    }

    public List<String> getHologramNames() { // TODO - potentially only show those in loaded chunks
        List<String> names = new ArrayList<>();
        for (PeriodicHologramBase holo : getHolograms()) {
            names.add(holo.getName());
        }
        return names;
    }

    public World getWorld() {
        return world;
    }

    private boolean saveHolograms() {
        boolean madeChanges = false;
        for (IndividualHologramHandler handler : holograms.values()) {
            if (handler.needsSaved()){
                saveHologram(handler);
                handler.markSaved();
                madeChanges = true;
            }
        }
        return madeChanges;
    }

    private void saveType(ConfigurationSection section, PeriodicHologramBase holo) {
        section.set("type", holo.getType().toString());
        section.set("activation-distance", holo.getActivationDistance());
        section.set("show-time", holo.getShowTimeTicks()/20L);
        section.set("permission", holo.getPermissions());
        if (holo instanceof PeriodicHologram) {
            PeriodicHologram periodic = (PeriodicHologram) holo;
            section.set("delay", periodic.getShowDelay()/1000L); // seconds->ms
            ConfigurationSection lastShownSection = section.createSection("last-shown"); 
            for (Map.Entry<UUID, Long> entry : periodic.getLastShown().entrySet()) {
                lastShownSection.set(entry.getKey().toString(), entry.getValue());
            }
        } else if (holo instanceof NTimesHologram) {
            NTimesHologram ntimes = (NTimesHologram) holo;
            section.set("times-to-show", ntimes.getTimesToShow()); // seconds->ms
            ConfigurationSection shownToSection = section.createSection("shown-to"); 
            for (Map.Entry<UUID, Integer> entry : ntimes.getShownTo().entrySet()) {
                shownToSection.set(entry.getKey().toString(), entry.getValue());
            }
        }
    }

    private void saveHologram(IndividualHologramHandler handler) {
        ConfigurationSection section = getConfig().createSection(handler.getName());
        for (PeriodicHologramBase holo : handler.getHolograms()) {
            saveType(section.createSection(holo.getType().name()), holo);
        }
    }

    void addHologram(PeriodicHologramBase hologram) {
        Validate.notNull(hologram, "Cannot add null hologram!");
        Validate.isTrue(hologram.getLocation().getWorld() == world, "Cannot add holograms in a different world!");
        IndividualHologramHandler handler = holograms.get(hologram.getName());
        if (handler == null) {
            handler = new IndividualHologramHandler(hologram.getType(), hologram);
            holograms.put(hologram.getName(), handler);
        } else {
            handler.addHologram(hologram.getType(), hologram);
        }
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
        if (saveHolograms()) { // something was saved/changed   
            try {
                getConfig().save(storageFile);
            } catch (IOException ex) {
                plugin.getLogger().log(Level.SEVERE, "Could not save config to " + storageFile, ex);
            }
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