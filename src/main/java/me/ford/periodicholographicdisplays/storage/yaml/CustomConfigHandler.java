package me.ford.periodicholographicdisplays.storage.yaml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.ford.periodicholographicdisplays.PeriodicHolographicDisplays;

/**
 * CustomConfig
 */
public class CustomConfigHandler {
    private final PeriodicHolographicDisplays phd;
    private final String fileName;
    private final boolean noSave;
    private FileConfiguration customConfig = null;
    private File customConfigFile = null;

    public CustomConfigHandler(PeriodicHolographicDisplays phd, String name, boolean noSave) {
        this.phd = phd;
        this.fileName = name;
        this.noSave = noSave;
    }

    public CustomConfigHandler(PeriodicHolographicDisplays phd, String name) throws InvalidConfigurationException {
        this.phd = phd;
        this.fileName = name;
        this.noSave = false;
        reloadConfig();
    }

    public boolean reloadConfig() throws InvalidConfigurationException {
        if (customConfigFile == null) {
            customConfigFile = new File(phd.getDataFolder(), fileName);
        }
        customConfig = loadConfiguration(customConfigFile);

        // Look for defaults in the jar
        Reader defConfigStream = null;
        InputStream resource = phd.getResource(fileName);
        if (resource != null) {
            try {
                defConfigStream = new InputStreamReader(resource, "UTF8");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            customConfig.setDefaults(defConfig);
        }
        if (customConfig.getKeys(true).isEmpty()) {
            if (!noSave) saveConfig();
            return false;
        } else {
            return true;
        }
    }

    public File getFile() {
        return customConfigFile;
    }

    public FileConfiguration getConfig() {
        return customConfig;
    }

    public void saveConfig() {
        if (customConfig == null || customConfigFile == null) {
            return;
        }
        try {
            getConfig().save(customConfigFile);
        } catch (IOException ex) {
            phd.getLogger().log(Level.SEVERE, "Could not save config to " + customConfigFile, ex);
        }
    }

    public void saveDefaultConfig() {
        if (customConfigFile == null) {
            customConfigFile = new File(phd.getDataFolder(), fileName);
        }
        if (!customConfigFile.exists()) {
            phd.saveResource(fileName, false);
        }
    }

    public static YamlConfiguration loadConfiguration(File file) throws InvalidConfigurationException {
        Validate.notNull(file, "File cannot be null");

        YamlConfiguration config = new YamlConfiguration();

        try {
            config.load(file);
        } catch (FileNotFoundException ex) {
            // empty
        } catch (IOException ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Cannot load " + file, ex);
        }
        return config;
    }

}