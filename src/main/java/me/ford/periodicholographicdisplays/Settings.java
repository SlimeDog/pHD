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
        return phd.getConfig().getLong("save-delay", 60L);
    }
    
    public boolean useDatabase() {
        String type = phd.getConfig().getString("storage-type", "YAML");
        switch(type) {
            case "YAML":
            return false;
            case "SQLITE":
            return true;
            default:
            throw new IllegalStateException("Only 'YAML' or 'SQLITE' allowed as database type!");
        }
    }

    public boolean enableMetrics() {
        return phd.getConfig().getBoolean("enable-metrics", true);
    }

    public boolean checkForUpdates() {
        return phd.getConfig().getBoolean("check-for-updates", true);
    }

}