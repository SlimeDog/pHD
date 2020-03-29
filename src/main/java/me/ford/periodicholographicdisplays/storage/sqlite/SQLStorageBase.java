package me.ford.periodicholographicdisplays.storage.sqlite;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import me.ford.periodicholographicdisplays.PeriodicHolographicDisplays;

/**
 * SQLStorageBase
 */
public abstract class SQLStorageBase {
    private static final String DATABSE_NAME = "database.db";
    protected static Connection conn; // shared connection
    private final PeriodicHolographicDisplays phd;

    public SQLStorageBase(PeriodicHolographicDisplays phd) {
        this.phd = phd;
        try {
            if (conn == null || conn.isClosed())
                conn = connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Connection connect() {
        Connection conn = null;
        try {
            // db parameters
            String url = "jdbc:sqlite:" + phd.getDataFolder().getAbsolutePath() + "/" + DATABSE_NAME;
            // create a connection to the database
            conn = DriverManager.getConnection(url);
            phd.getLogger().info(phd.getMessages().getSqlConnectionMessage());
        } catch (SQLException e) {
            phd.getLogger().log(Level.SEVERE, "Problem connecting to database", e);
        }
        return conn;
    }

    protected ResultSet executeQuery(String query, String... args) {
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

    protected boolean tableExists(String table) {
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

    protected void checkConnection() {
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

    protected boolean executeUpdate(String query, String... args) {
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

}