package me.ford.periodicholographicdisplays.holograms;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;

import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.ford.periodicholographicdisplays.PeriodicHolographicDisplays;

/**
 * WorldHologramStorageBase
 */
public abstract class WorldHologramStorageBase {
    private final World world;
    private final PeriodicHolographicDisplays phd;
    private final Map<String, IndividualHologramHandler> holograms = new HashMap<>();
    private final String fileBase = "periodicholograms_";
    private final String fileName;
    private final File storageFile;
    private FileConfiguration storage;

    WorldHologramStorageBase(PeriodicHolographicDisplays phd, World world) {
        this.phd = phd;
        this.world = world;
        this.fileName = fileBase + world.getName() + ".yml";
        storageFile = new File(this.phd.getDataFolder(), fileName);
    }

    public PeriodicHolographicDisplays getPlugin() {
        return phd;
    }

    public World getWorld() {
        return world;
    }

    protected void addHandler(String name, IndividualHologramHandler handler) {
        holograms.put(name, handler);
        
    }

    protected IndividualHologramHandler getHandler(String name) {
        return holograms.get(name);
    }

    protected void removeHandler(String name) {
        holograms.remove(name);
    }

    protected Collection<IndividualHologramHandler> getHandlers() {
        return new ArrayList<>(holograms.values());
    }

    protected Set<Entry<String, IndividualHologramHandler>> getEntries() {
        return new HashSet<>(holograms.entrySet());
    }

    protected abstract boolean saveHolograms();

    // config

    public void reload() {
        storage = YamlConfiguration.loadConfiguration(storageFile);

        // Look for defaults in the jar
        Reader defConfigStream = null;
        if (phd.getResource(fileName) != null) {
            try {
                defConfigStream = new InputStreamReader(phd.getResource(fileName), "UTF8");
            } catch (UnsupportedEncodingException e) {
                phd.getLogger().warning("Unsupported encoding in the holograms storage file!");
            }
        }
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            storage.setDefaults(defConfig);
        }
    }

    protected FileConfiguration getConfig() {
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
                phd.getLogger().log(Level.SEVERE, "Could not save config to " + storageFile, ex);
            }
        }
    }
    
}