package me.ford.periodicholographicdisplays.storage.yaml;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.ford.periodicholographicdisplays.PeriodicHolographicDisplays;

/**
 * CustomConfig
 */
public class CustomConfigHandler {
    private final PeriodicHolographicDisplays phd;
    private final String fileName;
    private FileConfiguration customConfig = null;
    private File customConfigFile = null;

    public CustomConfigHandler(PeriodicHolographicDisplays phd, String name) {
        this.phd = phd;
        this.fileName = name;
    }

    public void reloadConfig() {
        if (customConfigFile == null) {
            customConfigFile = new File(phd.getDataFolder(), fileName);
        }
        customConfig = YamlConfiguration.loadConfiguration(customConfigFile);

        // Look for defaults in the jar
        Reader defConfigStream;
        try {
            defConfigStream = new InputStreamReader(phd.getResource(fileName), "UTF8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            customConfig.setDefaults(defConfig);
        }
    }

    public FileConfiguration getConfig() {
        if (customConfig == null) {
            reloadConfig();
        }
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
    
}