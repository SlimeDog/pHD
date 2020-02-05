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
        return phd.getConfig().getInt("defaults.showtime", 10);
    }

    public long getSaveDelay() {
        return phd.getConfig().getLong("save-delay", 60L);
    }
    
}