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
    private final double flashOn, flashOff;

    public HologramInfo(String name, PeriodicType type, double activationDistance, long showTime, String perms,
            TypeInfo typeInfo, double flashOn, double flashOff) {
        this.name = name;
        this.type = type;
        this.activationDistance = activationDistance;
        this.showTime = showTime;
        this.perms = perms;
        this.typeInfo = typeInfo;
        this.flashOn = flashOn;
        this.flashOff = flashOff;
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

    public double getFlashOn() {
        return flashOn;
    }

    public double getFlashOff() {
        return flashOff;
    }

    @Override
    public String toString() {
        return String.format("{%f;%d;%s;%s}", activationDistance, showTime, perms, getTypeInfo().toString());
    }

    @Override
    public boolean equals(Object other) {
        if (other == this)
            return true;
        if (!(other instanceof HologramInfo))
            return false;
        HologramInfo info = (HologramInfo) other;
        return name.equals(info.name) && type == info.type && activationDistance == info.activationDistance
                && showTime == info.showTime && (perms == null ? info.perms == null : perms.equals(info.perms))
                && typeInfo.equals(info.typeInfo) && flashOn == info.flashOn && flashOff == info.flashOff;
    }

}