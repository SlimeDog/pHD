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
        SQLResponse sr = executeQuery(query);
        if (sr == null)
            return;
        ResultSet rs = sr.getResultSet();
        Map<UUID, String> map = new HashMap<>();
        try {
            while (rs.next()) {
                String id = rs.getString("player_UUID");
                UUID uuid;
                try {
                    uuid = UUID.fromString(id);
                } catch (IllegalArgumentException e) {
                    phd.getLogger().log(Level.WARNING, "Unable to parse UUID from database: " + id);
                    continue;
                }
                String name = rs.getString("player_name");
                map.put(uuid, name);
            }
        } catch (SQLException e) {
            phd.getLogger().log(Level.SEVERE, "Issue while loading UUIDs and player names!", e);
        }
        sr.close();
        phd.getServer().getScheduler().runTask(phd, () -> {
            for (Entry<UUID, String> entry : map.entrySet()) {
                cache.addOnStartup(entry.getKey(), entry.getValue());
            }
        });
    }

    @Override
    public void save(boolean inSync) {
        Map<UUID, String> toSave = cache.getToSave();
        if (toSave.isEmpty())
            return;
        phd.debug("Saving to UUID cache:" + toSave);
        if (inSync) {
            saveInAsync(toSave);
        } else {
            phd.getServer().getScheduler().runTaskAsynchronously(phd, () -> saveInAsync(toSave));
        }
        cache.markSaved();
    }

    private void saveInAsync(Map<UUID, String> map) {
        createTableIfNotExists();
        String query = "INSERT INTO " + TABLE_NAME + " VALUES (?, ?) "
                + "ON CONFLICT(player_UUID) DO UPDATE SET player_name=?";
        for (Entry<UUID, String> entry : map.entrySet()) {
            executeUpdate(query, entry.getKey().toString(), entry.getValue(), entry.getValue());
        }
    }

    private void createTableIfNotExists() {
        String query = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" + "player_UUID VARCHAR(36) UNIQUE NOT NULL, "
                + "player_name VARCHAR(16) NOT NULL, " + "PRIMARY KEY (player_name, player_UUID)" + ");";
        if (!executeUpdate(query)) {
            phd.getLogger().log(Level.SEVERE, "Unable to create table: " + TABLE_NAME);
        }
        String indexQuery1 = "CREATE INDEX IF NOT EXISTS phd_playerUUIDmap_UUID ON " + TABLE_NAME + " ( player_UUID );";
        String indexQuery2 = "CREATE INDEX IF NOT EXISTS phd_playerUUIDmap_name ON " + TABLE_NAME + " ( player_name );";
        if (!executeUpdate(indexQuery1)) {
            phd.getLogger().log(Level.SEVERE, "Unable to create index (1) for " + TABLE_NAME);
        }
        if (!executeUpdate(indexQuery2)) {
            phd.getLogger().log(Level.SEVERE, "Unable to create index (2) for " + TABLE_NAME);
        }
    }

}