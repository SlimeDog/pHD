package me.ford.periodicholographicdisplays.holograms.wrap;

import java.util.Collection;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.google.common.base.Supplier;

import de.oliver.fancyholograms.api.hologram.Hologram;
import me.ford.periodicholographicdisplays.holograms.wrap.visibility.FancyHologramsVisibilitySettings;
import me.ford.periodicholographicdisplays.holograms.wrap.visibility.VisibilitySettings;

public class FancyHologramWrapper implements WrappedHologram {
    private final Hologram delegate;
    private final FancyHologramsVisibilitySettings visibilitySettings;

    public FancyHologramWrapper(Function<UUID, Player> playerGetter,
            Supplier<Collection<Player>> allPlayersGetter, Hologram delegate,
            Consumer<Runnable> asyncScheduler) {
        this.delegate = delegate;
        this.visibilitySettings = new FancyHologramsVisibilitySettings(playerGetter, allPlayersGetter, delegate,
                asyncScheduler);
    }

    @Override
    public Location getBukkitLocation() {
        return delegate.getData().getLocation();
    }

    @Override
    public String getName() {
        return delegate.getData().getName();
    }

    @Override
    public VisibilitySettings getVisibilitySettings() {
        return visibilitySettings;
    }

    @Override
    public boolean isDeleted() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isDeleted'");
    }

    @Override
    public World getWorldIfLoaded() {
        return getBukkitLocation().getWorld();
    }

    @Override
    public void setVisibilityDistance(double dist) {
        delegate.getData().setVisibilityDistance((int) dist);
    }

}
