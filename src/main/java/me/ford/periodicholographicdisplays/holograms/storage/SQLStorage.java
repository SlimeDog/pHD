package me.ford.periodicholographicdisplays.holograms.storage;

import java.sql.Connection;
import java.sql.DriverManager;
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
import me.ford.periodicholographicdisplays.util.TimeUtils;

/**
 * SQLStorage
 */
public class SQLStorage implements Storage {
    private static Connection conn; // shared connection
    private final PeriodicHolographicDisplays phd;
    // private final Settings settings;
    private final String hologramTableName = "phd_hologram";
    private final String playerTableName = "phd_player";

    public SQLStorage(PeriodicHolographicDisplays phd) {
        this.phd = phd;
        // this.settings = phd.getSettings();
        conn = connect();
    } 

    private Connection connect() {
        Connection conn = null;
        try {
            // db parameters
            String url = "jdbc:sqlite:" + phd.getDataFolder().getAbsolutePath() + "/database.db";
            // create a connection to the database
            conn = DriverManager.getConnection(url);
            phd.getLogger().info(phd.getMessages().getSqlConnectionMessage());
        } catch (SQLException e) {
            phd.getLogger().log(Level.SEVERE, "Problem connecting to database", e);
        }
        return conn;
    }

    @Override
    public void saveHolograms(Set<HDHologramInfo> holograms, boolean inSync) {
        if (inSync) {
            saveHologramsAsync(holograms);
        } else {
            phd.getServer().getScheduler().runTaskAsynchronously(phd, () -> saveHologramsAsync(holograms));
        }
    }

    // hologram, type, activation_distance, display_seconds, periodic_time, activation_times, permission, flashon, flashoff
    private void saveHologramsAsync(Set<HDHologramInfo> holograms) {
        createHologramTableIfNotExists();
        createPlayerTableIfNotExists();
        String deleteQuery = "DELETE FROM " + hologramTableName + " WHERE hologram=? AND type=?";
        String query = "INSERT INTO " + hologramTableName + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)" 
                        + " ON CONFLICT(hologram, type) DO UPDATE SET activation_distance=? , display_seconds=? , "
                        + "periodic_time=?, activation_times=?, permission=?, flashon=?, flashoff=?;";
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

    // playerUUID, hologram_id, type, ntimes
    private void saveTypeInfoAsync(String holoName, TypeInfo info) {
        String insertQuery = "INSERT INTO " + playerTableName + " VALUES (?, ?, ?, ?)" 
                            + " ON CONFLICT(playerUUID, hologram_id, type) DO UPDATE SET ntimes=?;";
        String query;
        if (info instanceof NullTypeInfo) {
            String deleteQuery = "DELETE FROM " + playerTableName + " WHERE hologram_id=? AND type=?;";
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
    
    
    // hologram, type, activation_distance, display_seconds, periodic_time, activation_times, permission, flashon, flashoff
	private void loadHologramsAsync(Consumer<HDHologramInfo> consumer) {
        createHologramTableIfNotExists();
        createPlayerTableIfNotExists();
        Map<String, HDHologramInfo> infos = new HashMap<>();
        String query = "SELECT * FROM " + hologramTableName + ";";
        ResultSet rs = executeQuery(query);
        if (rs == null) return;

        try {
            while(rs.next()) {
                String holoName = rs.getString("hologram");
                String typeStr = rs.getString("type");
                PeriodicType type;
                try {
                    type = PeriodicType.valueOf(typeStr);
                } catch (IllegalArgumentException e) {
                    phd.getLogger().log(Level.WARNING, "Unable to parse periodic type from '" + typeStr + "'", e);
                    continue;
                }
                double distance = rs.getDouble("activation_distance");
                int seconds = rs.getInt("display_seconds");
                String time = rs.getString("periodic_time");
                String perms = rs.getString("permission");
                double flashOn = rs.getDouble("flashon");
                double flashOff = rs.getDouble("flashoff");
                if (flashOn == 0.0D || flashOff == 0.0D) { // to get started
                    flashOn = FlashingHologram.NO_FLASH;
                    flashOff = FlashingHologram.NO_FLASH;
                }
                HDHologramInfo info = infos.get(holoName);
                if (info == null) {
                    info = new HDHologramInfo(holoName);
                    infos.put(holoName, info);
                }
                String activationTimes = rs.getString("activation_times");
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
			phd.getLogger().log(Level.WARNING, "Issue while loading holohrams from database!", e);
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

    // playerUUID, hologram_id, type, ntimes
    private void addShownTo(String name, NTimesTypeInfo info) {
        String query = "SELECT * FROM " + playerTableName + " WHERE hologram_id=?;";
        ResultSet rs = executeQuery(query, name);
        if (rs == null) return;

        try {
            while (rs.next()) {
                String id = rs.getString("playerUUID");
                UUID uuid;
                try {
                    uuid = UUID.fromString(id);
                } catch (IllegalArgumentException e) {
                    phd.getLogger().log(Level.WARNING, "Cannot parse UUID " + id);
                    continue;
                }
                int ntimes = rs.getInt("ntimes");
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

    public ResultSet executeQuery(String query, String... args) {
		checkConnection();
		try {
			PreparedStatement statement = conn.prepareStatement(query);
			int i = 1;
			for (String arg : args) {
				statement.setString(i, arg);
				i++;
			}
			return statement.executeQuery();
		} catch (SQLException e) {
			phd.getLogger().warning("Unable to execute QUERY:" + query);
			e.printStackTrace();
			return null;
		}
	}
	
	public boolean tableExists(String table) {
		checkConnection();
		ResultSet rs;
		try {
			PreparedStatement statement = conn.prepareStatement("SHOW TABLES LIKE '" + table + "';");
			rs = statement.executeQuery();
		} catch (SQLException e) {
			phd.getLogger().log(Level.WARNING, "Unable to check if table exists: " + table, e);
			return false;
		}
		try {
            boolean can = rs.next();
            rs.close();
            return can;
		} catch (SQLException e) {
			phd.getLogger().log(Level.WARNING, "Unable to check if table exists(next): " + table, e);
			return false;
		}
	}
	
	public void checkConnection() {
		try {
			if (conn.isClosed() || !conn.isValid(10)) {
				conn.close();
				conn = connect();
				phd.getLogger().info("Creating a new connection for DB");
			}
		} catch (SQLException e) {
			phd.getLogger().log(Level.WARNING, "Unable to check if connection is closed!", e);
		}
	}
	
	public boolean executeUpdate(String query, String... args) {
		checkConnection();
		try {
			PreparedStatement statement = conn.prepareStatement(query);
			int i = 1;
			for (String arg : args) {
				statement.setString(i, arg);
				i++;
			}
			statement.executeUpdate();
			return true;
		} catch (SQLException e) {
			phd.getLogger().log(Level.WARNING, "Unable to update QUERY: " + query, e);
			return false;
		}
	}

	public void createHologramTableIfNotExists() {
        String query = "CREATE TABLE IF NOT EXISTS " + hologramTableName + "(" + 
                    "hologram STRING(255) NOT NULL, " +
                    "type STRING(255) NOT NULL, " +
                    "activation_distance REAL, " +
                    "display_seconds INT, " +
                    "periodic_time STRING, " + 
                    "activation_times INT, " + 
                    "permission STRING(255), " +
                    "flashon REAL, " + 
                    "flashoff REAL, " + 
                    "PRIMARY KEY (hologram, type)" +
                    ");";
		if (!executeUpdate(query)) {
			phd.getLogger().severe("Unable to create table: " + hologramTableName);
		}
    }
    
    public void createPlayerTableIfNotExists() {
        String query = "CREATE TABLE IF NOT EXISTS " + playerTableName + "(" + 
                    "playerUUID STRING(36) NOT NULL, " +
                    "hologram_id STRING(255), " +
                    "type STRING(255) NOT NULL, " +
                    "ntimes INT, " +
                    "PRIMARY KEY (playerUUID, hologram_id, type)" +
                    ");";
		if (!executeUpdate(query)) {
			phd.getLogger().severe("Unable to create table: " + playerTableName);
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