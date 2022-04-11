package me.ford.periodicholographicdisplays.holograms.wrap;

import org.bukkit.Location;
import org.bukkit.World;

import me.filoghost.holographicdisplays.plugin.internal.hologram.InternalHologram;
import me.ford.periodicholographicdisplays.holograms.wrap.visibility.HolographicDisplaysVisibilitySettings;
import me.ford.periodicholographicdisplays.holograms.wrap.visibility.VisibilitySettings;

public class HolographicDisplaysWrapper implements WrappedHologram {
    private final InternalHologram delegate;
    private final HolographicDisplaysVisibilitySettings visibilitySettings;

    public HolographicDisplaysWrapper(InternalHologram delegate) {
        this.delegate = delegate;
        this.visibilitySettings = new HolographicDisplaysVisibilitySettings(delegate.getVisibilitySettings());
    }

    @Override
    public Location getBukkitLocation() {
        return delegate.getPosition().toLocation();
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
        return delegate.isDeleted();
    }

    @Override
    public World getWorldIfLoaded() {
        return delegate.getWorldIfLoaded();
    }

}
