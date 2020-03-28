package me.ford.periodicholographicdisplays.users;

import java.util.UUID;
import java.util.Map.Entry;
import java.util.logging.Level;

import org.bukkit.configuration.ConfigurationSection;

import me.ford.periodicholographicdisplays.PeriodicHolographicDisplays;
import me.ford.periodicholographicdisplays.storage.yaml.CustomConfigHandler;

/**
 * YamlUserManager
 */
public class YamlUserStorage implements UserStorage {
    private static final String NAME = "playerUUID.yml";
    private final PeriodicHolographicDisplays phd;
    private final SimpleUserCache cache;
    private final CustomConfigHandler configHandler;

    public YamlUserStorage(PeriodicHolographicDisplays phd) {
        this.phd = phd;
        this.cache = new SimpleUserCache();
        this.configHandler = new CustomConfigHandler(this.phd, NAME);
        load();
    }

    private void load() {
        ConfigurationSection section = configHandler.getConfig();
        for (String id : section.getKeys(false)) {
            UUID uuid;
            try {
                uuid = UUID.fromString(id);
            } catch (IllegalArgumentException e) {
                phd.getLogger().log(Level.WARNING, String.format("Could not parse UUID in %s: %s", NAME, id));
                continue;
            }
            cache.addOnStartup(uuid, section.getString(id));
        }
    }

    @Override
    public void save(boolean inSync) {
        ConfigurationSection section = configHandler.getConfig();
        for (Entry<UUID, String> entry : cache.getToSave().entrySet()) {
            section.set(entry.getKey().toString(), entry.getValue());
        }
        cache.markSaved();
        configHandler.saveConfig();
    }

    @Override
    public UserCache getCache() {
        return cache;
    }

}