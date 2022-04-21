package me.ford.periodicholographicdisplays.holograms.wrap;

import org.bukkit.Location;
import org.bukkit.World;

import eu.decentsoftware.holograms.api.holograms.Hologram;
import me.ford.periodicholographicdisplays.holograms.wrap.visibility.DecentHologramVisibilitySettings;
import me.ford.periodicholographicdisplays.holograms.wrap.visibility.VisibilitySettings;

public class DecentHologramWrapper implements WrappedHologram {
    private final Hologram delegate;
    private final DecentHologramVisibilitySettings visibilitySettings;

    public DecentHologramWrapper(Hologram delegate) {
        this.delegate = delegate;
        this.visibilitySettings = new DecentHologramVisibilitySettings(delegate);
    }

    @Override
    public Location getBukkitLocation() {
        return delegate.getLocation();
    }

    @Override
    public String getName() {
        return delegate.getName();
    }

    @Override
    public VisibilitySettings getVisibilitySettings() {
        return visibilitySettings;
    }

    @Override
    public boolean isDeleted() {
        return !delegate.isEnabled();
    }

    @Override
    public World getWorldIfLoaded() {
        return getBukkitLocation().getWorld();
    }

    @Override
    public void setVisibilityDistance(double dist) {
        // Don't do anything
    }

}
