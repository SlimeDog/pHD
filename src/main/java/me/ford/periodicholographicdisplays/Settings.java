package me.ford.periodicholographicdisplays;

/**
 * Settings
 */
public class Settings {
    private final PeriodicHolographicDisplays phd;
    private boolean useDatabase;
    private double defaultActivationDistance;
    private int defaultShowTime;
    private long saveDelay;
    private boolean enableMetrics;
    private boolean checkForUpdates;
    private boolean onDebug;

    public Settings(PeriodicHolographicDisplays plugin) {
        this.phd = plugin;
        reload();
    }

    public void reload() {
        defaultActivationDistance = getDefaultActivationDistance_();
        defaultShowTime = getDefaultShowTime_();
        saveDelay = getSaveDelay_();
        useDatabase = useDatabase_();
        enableMetrics = enableMetrics_();
        checkForUpdates = checkForUpdates_();
        onDebug = onDebug_();
    }

    public double getDefaultActivationDistance() {
        return defaultActivationDistance;
    }
    
    public int getDefaultShowTime() {
        return defaultShowTime;
    }

    public long getSaveDelay() {
        return saveDelay;
    }

    public boolean useDatabase() {
        return useDatabase;
    }

    public boolean enableMetrics() {
        return enableMetrics;
    }

    public boolean checkForUpdates() {
        return checkForUpdates;
    }

    public boolean onDebug() {
        return onDebug;
    }

    private double getDefaultActivationDistance_() {
        return phd.getConfig().getDouble("defaults.activation-distance", 20.0D);
    }

    private int getDefaultShowTime_() {
        return phd.getConfig().getInt("defaults.show-time", 10);
    }

    private long getSaveDelay_() {
        return phd.getConfig().getLong("save-frequency", 60L);
    }
    
    private boolean useDatabase_() {
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

    void setDefaultDatabaseInternal() {
        useDatabase = true;
    }

    private boolean enableMetrics_() {
        return phd.getConfig().getBoolean("enable-metrics", true);
    }

    private boolean checkForUpdates_() {
        return phd.getConfig().getBoolean("check-for-updates", true);
    }

    private boolean onDebug_() {
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