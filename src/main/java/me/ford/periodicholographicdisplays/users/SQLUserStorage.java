package me.ford.periodicholographicdisplays.users;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.logging.Level;

import me.ford.periodicholographicdisplays.PeriodicHolographicDisplays;
import me.ford.periodicholographicdisplays.storage.sqlite.SQLStorageBase;

/**
 * SQLUserManager
 */
public class SQLUserStorage extends SQLStorageBase implements UserStorage {
    private static final String TABLE_NAME = "phd_playerUUID";
    private final PeriodicHolographicDisplays phd;
    private final SimpleUserCache cache;

    public SQLUserStorage(PeriodicHolographicDisplays phd) {
        super(phd);
        this.phd = phd;
        this.cache = new SimpleUserCache();
        load();
    }

    @Override
    public UserCache getCache() {
        return cache;
    }

    // DATABASE handling down here

    private void load() {
        phd.getServer().getScheduler().runTaskAsynchronously(phd, () -> loadInAsync());
    }

    private void loadInAsync() {
        createTableIfNotExists();
        String query = "SELECT * FROM " + TABLE_NAME + ";";
        ResultSet rs = executeQuery(query);
        if (rs == null) return;
        Map<UUID, String> map = new HashMap<>();
        try {
            while (rs.next()) {
                String id = rs.getString("uuid");
                UUID uuid;
                try {
                    uuid = UUID.fromString(id);
                } catch (IllegalArgumentException e) {
                    phd.getLogger().log(Level.WARNING, "Unable to parse UUID from database: " + id);
                    continue;
                }
                String name = rs.getString("name");
                map.put(uuid, name);
            }
        } catch (SQLException e) {
            phd.getLogger().log(Level.SEVERE, "Issue while loading UUIDs and player names!", e);
        }
        phd.getServer().getScheduler().runTask(phd, () -> {
            for (Entry<UUID, String> entry : map.entrySet()) {
                cache.addOnStartup(entry.getKey(), entry.getValue());
            }
        });
    }

    @Override
    public void save(boolean inSync) {
        Map<UUID, String> toSave = cache.getToSave();
        if (toSave.isEmpty()) return;
        if (inSync) {
            saveInAsync(toSave);
        } else {
            phd.getServer().getScheduler().runTaskAsynchronously(phd, () -> saveInAsync(toSave));
        }        
    }

    private void saveInAsync(Map<UUID, String> map) {
        createTableIfNotExists();
        String query = "INSERT INTO " + TABLE_NAME + " VALUES (?, ?) " + 
                        "ON CONFLICT(hologram, type) DO UPDATE SET name=?";
        for (Entry<UUID, String> entry : map.entrySet()) {
            executeUpdate(query, entry.getKey().toString(), entry.getValue());
        }
    }

	private void createTableIfNotExists() {
        String query = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" + 
                    "uuid STRING(36) UNIQUE NOT NULL, " +
                    "name STRING(255) NOT NULL, " +
                    ");";
		if (!executeUpdate(query)) {
			phd.getLogger().log(Level.SEVERE, "Unable to create table: " + TABLE_NAME);
		}
    }
    
}