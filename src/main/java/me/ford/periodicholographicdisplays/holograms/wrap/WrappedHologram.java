package me.ford.periodicholographicdisplays.holograms.wrap;

import org.bukkit.Location;
import org.bukkit.World;

import me.ford.periodicholographicdisplays.holograms.wrap.visibility.VisibilitySettings;

public interface WrappedHologram {

    Location getBukkitLocation();

    String getName();

    VisibilitySettings getVisibilitySettings();

    boolean isDeleted();

    World getWorldIfLoaded();

    void setVisibilityDistance(double dist);

}
