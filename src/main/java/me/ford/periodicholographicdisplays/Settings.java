package me.ford.periodicholographicdisplays;

/**
 * Settings
 */
public class Settings {
    private final PeriodicHolographicDisplays phd;

    public Settings(PeriodicHolographicDisplays plugin) {
        this.phd = plugin;
    }

    public double getDefaultActivationDistance() {
        return phd.getConfig().getDouble("defaults.activation-distance", 20.0D);
    }

    public int getDefaultShowTime() {
        return phd.getConfig().getInt("defaults.show-time", 10);
    }

    public long getSaveDelay() {
        return phd.getConfig().getLong("save-frequency", 60L);
    }
    
    public boolean useDatabase() {
        String type = phd.getConfig().getString("storage-type", "SQLITE");
        switch(type) {
            case "YAML":
            return false;
            case "SQLITE":
            return true;
            default:
            throw new StorageTypeException(type);
        }
    }

    public void setDefaultDatabaseInternal() {
        phd.getConfig().set("storage-type", "SQLITE"); // isn't saved anywhere
    }

    public boolean enableMetrics() {
        return phd.getConfig().getBoolean("enable-metrics", true);
    }

    public boolean checkForUpdates() {
        return phd.getConfig().getBoolean("check-for-updates", true);
    }

    public boolean onDebug() {
        return phd.getConfig().getBoolean("debug", false);
    }

    public static class StorageTypeException extends IllegalStateException {
        private static final long serialVersionUID = -1888165138957133828L;

        private final String type;

        public StorageTypeException(String type) {
            super("Only 'YAML' or 'SQLITE' allowed as database type,  got " + type);
            this.type = type;
        }

        public String getType() {
            return type;
        }
        
    }

}