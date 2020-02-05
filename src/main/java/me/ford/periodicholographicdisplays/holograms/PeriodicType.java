package me.ford.periodicholographicdisplays.holograms;

import java.util.ArrayList;
import java.util.List;

/**
 * Periodictype
 */
public enum PeriodicType {

    PERIODIC, ALWAYS, NTIMES;

    private static final List<String> NAMES = new ArrayList<>();
    static {
        for (PeriodicType type : values()) {
            NAMES.add(type.name());
        }
    }

    public static List<String> names() {
        return NAMES;
    }
    
}