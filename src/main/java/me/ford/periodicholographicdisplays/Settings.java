package me.ford.periodicholographicdisplays;

import java.util.HashMap;
import java.util.Map;

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

    public Map<SettingIssue, String> reload() {
        Map<SettingIssue, String> issues = new HashMap<>();
        for (SettingIssue issue : SettingIssue.values()) {
            String val = phd.getConfig().getString(issue.getPath());
            if (!issue.fits(val)) {
                issues.put(issue, val);
            }
        }
        defaultActivationDistance = getDefaultActivationDistance_();
        defaultShowTime = getDefaultShowTime_();
        saveDelay = getSaveDelay_();
        useDatabase = useDatabase_();
        enableMetrics = enableMetrics_();
        checkForUpdates = checkForUpdates_();
        onDebug = onDebug_();
        return issues;
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

    public static enum SettingIssue {
        ACTIVATION_DISTANCE("defaults.activation-distance", SettingType.DOUBLE_PLUS),
        SHOW_TIME("defaults.show-time", SettingType.INTEGER_PLUS),
        SAVE_DELAY("save-frequency", SettingType.LONG_PLUS),
        ENABLE_METRICS("enable-metrics", SettingType.BOOLEAN),
        CHECK_FOR_UPDATES("check-for-updates", SettingType.BOOLEAN);

        private final String path;
        private final SettingType type;
        
        SettingIssue(String path, SettingType type) {
            this.path = path;
            this.type = type;
        }

        public String getPath() {
            return path;
        }

        public SettingType getType() {
            return type;
        }

        public boolean fits(String val) {
            if (val == null) return false; // fallback to default is elsewhere
            switch(type) {
                case DOUBLE:
                case DOUBLE_PLUS:
                try {
                    double d = Double.parseDouble(val);
                    return type == SettingType.DOUBLE_PLUS ?  d > 0 : true;
                } catch (NumberFormatException e) {
                    return false;
                }
                case INTEGER:
                case INTEGER_PLUS:
                try {
                    int i = Integer.parseInt(val);
                    return type == SettingType.INTEGER_PLUS ? i > 0 : true;
                } catch (NumberFormatException e) {
                    return false;
                }
                case LONG:
                case LONG_PLUS:
                try {
                    long l = Long.parseLong(val);
                    return type == SettingType.LONG_PLUS ? l > 0 : true;
                } catch (NumberFormatException e) {
                    return false;
                }
                case BOOLEAN:
                try {
                    Boolean.parseBoolean(val);
                    return true;
                } catch (NumberFormatException e) {
                    return false;
                }
                default:
                throw new IllegalArgumentException("Unrecognized type!" + type);
            }
        }

    }

    public static enum SettingType {
        DOUBLE("DOUBLE"), DOUBLE_PLUS("DOUBLE > 0.0"),
        INTEGER("INT"), INTEGER_PLUS("INT > 0"),
        LONG("LONG"), LONG_PLUS("LONG > 0"),
        BOOLEAN("BOOL");

        private final String name;
        SettingType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

}