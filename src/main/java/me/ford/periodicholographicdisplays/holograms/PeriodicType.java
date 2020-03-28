package me.ford.periodicholographicdisplays.holograms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Periodictype
 */
public enum PeriodicType {

    ALWAYS, NTIMES, MCTIME, IRLTIME;

    private static final List<String> NAMES;
    static {
        List<String> names = new ArrayList<>();
        for (PeriodicType type : values()) {
            names.add(type.name());
        }
        NAMES = Collections.unmodifiableList(names);
    }

    public static List<String> names() {
        return new ArrayList<>(NAMES);
    }

}