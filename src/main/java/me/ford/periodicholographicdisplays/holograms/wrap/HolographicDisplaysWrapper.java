package me.ford.periodicholographicdisplays.holograms.wrap;

import org.bukkit.Location;
import org.bukkit.World;

import me.filoghost.holographicdisplays.api.hologram.Hologram;
import me.filoghost.holographicdisplays.plugin.internal.hologram.InternalHologram;
import me.ford.periodicholographicdisplays.holograms.wrap.visibility.HolographicDisplaysVisibilitySettings;
import me.ford.periodicholographicdisplays.holograms.wrap.visibility.VisibilitySettings;

public class HolographicDisplaysWrapper implements WrappedHologram {
    private final InternalHologram delegate;
    private final Hologram rendered;
    private final HolographicDisplaysVisibilitySettings visibilitySettings;

    public HolographicDisplaysWrapper(InternalHologram delegate) {
        this.delegate = delegate;
        this.rendered = delegate.getRenderedHologram();
        this.visibilitySettings = new HolographicDisplaysVisibilitySettings(rendered.getVisibilitySettings());
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
        return rendered.isDeleted();
    }

    @Override
    public World getWorldIfLoaded() {
        return delegate.getPosition().getWorldIfLoaded();
    }

    @Override
    public void setVisibilityDistance(double dist) {
        // ignore
    }

}
