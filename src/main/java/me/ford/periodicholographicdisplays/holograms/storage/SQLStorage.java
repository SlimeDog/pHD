package me.ford.periodicholographicdisplays.holograms.storage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.logging.Level;

import me.ford.periodicholographicdisplays.PeriodicHolographicDisplays;
import me.ford.periodicholographicdisplays.holograms.FlashingHologram;
import me.ford.periodicholographicdisplays.holograms.PeriodicType;
import me.ford.periodicholographicdisplays.holograms.events.HologramsLoadedEvent;
import me.ford.periodicholographicdisplays.holograms.storage.TypeInfo.IRLTimeTypeInfo;
import me.ford.periodicholographicdisplays.holograms.storage.TypeInfo.MCTimeTypeInfo;
import me.ford.periodicholographicdisplays.holograms.storage.TypeInfo.NTimesTypeInfo;
import me.ford.periodicholographicdisplays.holograms.storage.TypeInfo.NullTypeInfo;
import me.ford.periodicholographicdisplays.storage.sqlite.SQLStorageBase;
import me.ford.periodicholographicdisplays.util.TimeUtils;

/**
 * SQLStorage
 */
public class SQLStorage extends SQLStorageBase implements Storage {
    private final PeriodicHolographicDisplays phd;
    private final String hologramTableName = "phd_hologram";
    private final String playerTableName = "phd_player";

    public SQLStorage(PeriodicHolographicDisplays phd) {
        super(phd);
        this.phd = phd;
    }

    @Override
    public void saveHolograms(Set<HDHologramInfo> holograms, boolean inSync) {
        if (inSync) {
            saveHologramsAsync(holograms);
        } else {
            phd.getServer().getScheduler().runTaskAsynchronously(phd, () -> saveHologramsAsync(holograms));
        }
    }

    // hologram_name, hologram_type, activation_distance, display_seconds, clock_time, max_views, permission, flash_on, flash_off
    private void saveHologramsAsync(Set<HDHologramInfo> holograms) {
        createHologramTableIfNotExists();
        createPlayerTableIfNotExists();
        String deleteQuery = "DELETE FROM " + hologramTableName + " WHERE hologram_name=? AND hologram_type=?";
        String query = "INSERT INTO " + hologramTableName + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)" 
                        + " ON CONFLICT(hologram_name, hologram_type) DO UPDATE SET activation_distance=? , display_seconds=? , "
                        + "clock_time=?, max_views=?, permission=?, flash_on=?, flash_off=?;";
        for (HDHologramInfo hologram : holograms) {
            for (HologramInfo info : hologram.getInfos()) {
                TypeInfo typeInfo = info.getTypeInfo();
                if (typeInfo instanceof NullTypeInfo) {
                    executeUpdate(deleteQuery, info.getName(), typeInfo.getType().name());
                    saveTypeInfoAsync(info.getName(), typeInfo);
                    continue; // next
                }
                String time = "";
                switch(info.getType()) {
                    case IRLTIME:
                    IRLTimeTypeInfo irlTypeInfo = (IRLTimeTypeInfo) typeInfo;
                    time = TimeUtils.toIRLTime(irlTypeInfo.getAtTime());
                    break;
                    case MCTIME:
                    MCTimeTypeInfo mcTypeInfo = (MCTimeTypeInfo) typeInfo;
                    time = TimeUtils.toMCTime(mcTypeInfo.getAtTime());
                    break;
                    case ALWAYS:
                    case NTIMES:
                    time = "";
                    break;
                    default:
                    throw new IllegalArgumentException("Incorrect type of info:" + info.getType());
                }
                int activationTimes = 0;
                if (info.getType() == PeriodicType.NTIMES) activationTimes = ((NTimesTypeInfo) info.getTypeInfo()).getShowTimes();
                PreparedStatement statement;
                try {
                    statement = conn.prepareStatement(query);
                    statement.setString(1, info.getName());
                    statement.setString(2, info.getType().name());
                    statement.setDouble(3, info.getActivationDistance());
                    statement.setLong(4, info.getShowTime());
                    statement.setString(5, time);
                    statement.setInt(6, activationTimes);
                    statement.setString(7, info.getPermissions());
                    statement.setDouble(8, info.getFlashOn());
                    statement.setDouble(9, info.getFlashOff());
                    statement.setDouble(10, info.getActivationDistance());
                    statement.setLong(11, info.getShowTime());
                    statement.setString(12, time);
                    statement.setInt(13, activationTimes);
                    statement.setString(14, info.getPermissions());
                    statement.setDouble(15, info.getFlashOn());
                    statement.setDouble(16, info.getFlashOff());
                } catch (SQLException e) {
                    phd.getLogger().log(Level.WARNING, "Problem while setting up prepared statement", e);
                    continue;
                }
                try {
                    statement.executeUpdate();
                } catch (SQLException e) {
                    phd.getLogger().log(Level.WARNING, "Problem while executing update", e);
                }
                saveTypeInfoAsync(info.getName(), typeInfo);
            }
        }
    }

    // player_UUID, hologram_name, hologram_type, views
    private void saveTypeInfoAsync(String holoName, TypeInfo info) {
        String insertQuery = "INSERT INTO " + playerTableName + " VALUES (?, ?, ?, ?)" 
                            + " ON CONFLICT(player_UUID, hologram_name, hologram_type) DO UPDATE SET views=?;";
        String query;
        if (info instanceof NullTypeInfo) {
            String deleteQuery = "DELETE FROM " + playerTableName + " WHERE hologram_name=? AND hologram_type=?;";
            executeUpdate(deleteQuery, holoName, info.getType().name());
            return;
        } else {
            query = insertQuery;
        }
        switch(info.getType()) {
            case NTIMES:
            NTimesTypeInfo ninfo = (NTimesTypeInfo) info;
            for (Entry<UUID, Integer> entry : ninfo.getShownToTimes().entrySet()) {
                PreparedStatement statement;
                try {
                    statement = conn.prepareStatement(query);
                    statement.setString(1, entry.getKey().toString());
                    statement.setString(2, holoName);
                    statement.setString(3, info.getType().name());
                    statement.setInt(4, entry.getValue());
                    statement.setInt(5, entry.getValue());
                } catch (SQLException e) {
                    phd.getLogger().log(Level.WARNING, "Problem while setting up prepared statement", e);
                    continue;
                }
                try {
                    statement.executeUpdate();
                } catch (SQLException e) {
                    phd.getLogger().log(Level.WARNING, "Problem while executing update", e);
                }
            }
            break;
            case IRLTIME:
            case MCTIME:
            case ALWAYS:
            default:
            // don't do anything
        }
    }

    @Override
    public void loadHolograms(Consumer<HDHologramInfo> consumer) {
        phd.getServer().getScheduler().runTaskAsynchronously(phd, () -> loadHologramsAsync(consumer));
    }
    
    
    // hologram_name, hologram_type, activation_distance, display_seconds, clock_time, max_views, permission, flash_on, flash_off
	private void loadHologramsAsync(Consumer<HDHologramInfo> consumer) {
        createHologramTableIfNotExists();
        createPlayerTableIfNotExists();
        Map<String, HDHologramInfo> infos = new HashMap<>();
        String query = "SELECT * FROM " + hologramTableName + ";";
        ResultSet rs = executeQuery(query);
        if (rs == null) return;

        try {
            while(rs.next()) {
                String holoName = rs.getString("hologram_name");
                String typeStr = rs.getString("hologram_type");
                PeriodicType type;
                try {
                    type = PeriodicType.valueOf(typeStr);
                } catch (IllegalArgumentException e) {
                    phd.getLogger().log(Level.WARNING, "Unable to parse periodic type from '" + typeStr + "'", e);
                    continue;
                }
                double distance = rs.getDouble("activation_distance");
                int seconds = rs.getInt("display_seconds");
                String time = rs.getString("clock_time");
                String perms = rs.getString("permission");
                double flashOn = rs.getDouble("flash_on");
                double flashOff = rs.getDouble("flash_off");
                if (flashOn == 0.0D || flashOff == 0.0D) { // to get started
                    flashOn = FlashingHologram.NO_FLASH;
                    flashOff = FlashingHologram.NO_FLASH;
                }
                HDHologramInfo info = infos.get(holoName);
                if (info == null) {
                    info = new HDHologramInfo(holoName);
                    infos.put(holoName, info);
                }
                String activationTimes = rs.getString("max_views");
                TypeInfo typeInfo;
                try {
                    typeInfo = getTypeInfo(type, time, activationTimes);
                } catch (IllegalArgumentException e) {
                    phd.getLogger().log(Level.WARNING, "Unable to get typeinfo of " + holoName + " of type " + type.name());
                    continue;
                }
                HologramInfo holo = new HologramInfo(holoName, type, distance, seconds, perms, typeInfo, flashOn, flashOff);
                info.addInfo(holo);
            }
            rs.close();
        } catch (SQLException e) {
			phd.getLogger().log(Level.WARNING, "Issue while loading holograms from database!", e);
        }
        
        for (HDHologramInfo info : infos.values()) {
            for (HologramInfo hinfo : info.getInfos()) {
                if (hinfo.getType() == PeriodicType.NTIMES) {
                    addShownTo(info.getHoloName(), (NTimesTypeInfo) hinfo.getTypeInfo());
                }
            }
        }

        phd.getServer().getScheduler().runTask(phd, () -> {
            for (HDHologramInfo info : infos.values()) {
                consumer.accept(info);
            }
            phd.getServer().getPluginManager().callEvent(new HologramsLoadedEvent());
        });
    }

    // player_UUID, hologram_name, hologram_type, views
    private void addShownTo(String name, NTimesTypeInfo info) {
        String query = "SELECT * FROM " + playerTableName + " WHERE hologram_name=?;";
        ResultSet rs = executeQuery(query, name);
        if (rs == null) return;

        try {
            while (rs.next()) {
                String id = rs.getString("player_UUID");
                UUID uuid;
                try {
                    uuid = UUID.fromString(id);
                } catch (IllegalArgumentException e) {
                    phd.getLogger().log(Level.WARNING, "Cannot parse UUID " + id);
                    continue;
                }
                int ntimes = rs.getInt("views");
                info.addShownTo(uuid, ntimes);
            }
            rs.close();
        } catch (SQLException e) {
			phd.getLogger().log(Level.WARNING, "Issue while loading user shown times from database!", e);
        }
    }
    
    private TypeInfo getTypeInfo(PeriodicType type, String time, String activationTimes) {
        TypeInfo typeInfo;
        int times = -1;
        switch(type) {
            case IRLTIME:
            typeInfo = new IRLTimeTypeInfo(TimeUtils.parseHoursAndMinutesToSeconds(time));
            break;
            case MCTIME:
            typeInfo = new MCTimeTypeInfo(TimeUtils.parseMCTime(time));
            break;
            case ALWAYS:
            case NTIMES:
            if (type == PeriodicType.NTIMES) {
                try {
                    times = Integer.parseInt(activationTimes);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Incorrect activation times specified:" + activationTimes);
                }
            }
            typeInfo = new NTimesTypeInfo(times, new HashMap<>());
            break;
            default:
            throw new IllegalArgumentException("Incorrect type specified:" + type);
        }
        return typeInfo;
    }

	public void createHologramTableIfNotExists() {
        String query = "CREATE TABLE IF NOT EXISTS " + hologramTableName + " (" +
                    "hologram_name VARCHAR(255) NOT NULL," +
                    "hologram_type VARCHAR(16) NOT NULL," +
                    "activation_distance REAL DEFAULT -1.0," +
                    "display_seconds INTEGER DEFAULT -1," +
                    "clock_time VARCHAR(8)," +
                    "max_views INTEGER," +
                    "permission VARCHAR(255)," +
                    "flash_on REAL DEFAULT -1.0," +
                    "flash_off REAL DEFAULT -1.0," +
                    "PRIMARY KEY (hologram_name, hologram_type)" +
                    ");";
		if (!executeUpdate(query)) {
			phd.getLogger().severe("Unable to create table: " + hologramTableName);
        }
        String indexQuery = "CREATE INDEX IF NOT EXISTS phd_hologram_hindex ON " + hologramTableName + " ( hologram_name, hologram_type );";
        if (!executeUpdate(indexQuery)) {
            phd.getLogger().severe("Unable to create index for " + hologramTableName);
        }
    }
    
    public void createPlayerTableIfNotExists() {
        String query = "CREATE TABLE IF NOT EXISTS " + playerTableName + "(" + 
                    "player_UUID VARCHAR(36) NOT NULL, " +
                    "hologram_name VARCHAR(255), " +
                    "hologram_type VARCHAR(16) NOT NULL, " +
                    "views INTEGER, " +
                    "PRIMARY KEY (player_UUID, hologram_name, hologram_type)" +
                    ");";
		if (!executeUpdate(query)) {
			phd.getLogger().severe("Unable to create table: " + playerTableName);
        }
        String indexQuery1 = "CREATE INDEX IF NOT EXISTS phd_player_pindex ON " + playerTableName + " ( player_UUID );";
        if (!executeUpdate(indexQuery1)) {
            phd.getLogger().severe("Unable to create index (1) for " + playerTableName);
        }
        String indexQuery2 = "CREATE INDEX IF NOT EXISTS phd_player_hindex ON " + playerTableName + " ( hologram_name, hologram_type );";
        if (!executeUpdate(indexQuery2)) {
            phd.getLogger().severe("Unable to create index (2) for " + playerTableName);
        }
    }

    @Override
    public boolean hasData() { // TODO - this is run IN SYNC
        String holgorams = "SELECT name FROM sqlite_master WHERE type='table' AND name='" + hologramTableName + "';";
        String players = "SELECT name FROM sqlite_master WHERE type='table' AND name='" + playerTableName + "';";
        ResultSet rs = executeQuery(holgorams);
        if (rs == null) return false;
        try {
            if (!rs.next()) return false;
        } catch (SQLException e) {
            phd.getLogger().log(Level.WARNING, "Problem checking for existance of table (hologram table)", e);
        }
        try {
            rs.close();
        } catch (SQLException e) {
            phd.getLogger().log(Level.WARNING, "Problem releasing resultset (hologram table)", e);
        }
        rs = executeQuery(players);
        if (rs == null) return false;
        try {
            if (!rs.next()) return false;
        } catch (SQLException e) {
            phd.getLogger().log(Level.WARNING, "Problem checking for existance of table (player table)", e);
        }
        try {
            rs.close();
        } catch (SQLException e) {
            phd.getLogger().log(Level.WARNING, "Problem releasing resultset (player table)", e);
        }
        return true;
    }

    @Override
    public void clear() {
        phd.getServer().getScheduler().runTaskAsynchronously(phd, () -> {
            String delHologramTable = "DELETE FROM " + hologramTableName + ";";
            executeUpdate(delHologramTable);
            String delPlayerTable = "DELETE FROM " + playerTableName + ";";;
            executeUpdate(delPlayerTable);
        });
    }    

	public void close() {
		try {
			if (conn.isClosed()) {
                conn.close();
				return;
			}
		} catch (SQLException e) {
			phd.getLogger().log(Level.WARNING, "Issue while closing connection (while checking if connection is closed)", e);
		}
		try {
			conn.close();
		} catch (SQLException e) {
			phd.getLogger().log(Level.WARNING, "Issue while closing connection:", e);
		}
	}
    
}