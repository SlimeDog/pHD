package me.ford.periodicholographicdisplays.holograms.storage;

import me.ford.periodicholographicdisplays.holograms.PeriodicType;

/**
 * HologramInfo
 */
public class HologramInfo {
    private final String name;
    private final PeriodicType type;
    private final double activationDistance;
    private final long showTime;
    private final String perms;
    private final TypeInfo typeInfo;

    public HologramInfo(String name, PeriodicType type, double activationDistance, long showTime, String perms, TypeInfo typeInfo) {
        this.name = name;
        this.type = type;
        this.activationDistance = activationDistance;
        this.showTime = showTime;
        this.perms = perms;
        this.typeInfo = typeInfo;
    }

    public String getName() {
        return name;
    }

    public PeriodicType getType() {
        return type;
    }

    public double getActivationDistance() {
        return activationDistance;
    }

    public long getShowTime() {
        return showTime;
    }

    public String getPermissions() {
        return perms;
    }

    public TypeInfo getTypeInfo() {
        return typeInfo;
    }

    @Override
    public String toString() {
        return String.format("{%f;%d;%s;%s}", activationDistance, showTime, perms, getTypeInfo().toString());
    }
    
}