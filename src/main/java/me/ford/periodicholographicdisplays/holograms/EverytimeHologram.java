package me.ford.periodicholographicdisplays.holograms;

import com.gmail.filoghost.holographicdisplays.api.Hologram;

import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * EverytimeHologram
 */
public class EverytimeHologram extends PeriodicHologramBase {

    public EverytimeHologram(Hologram hologram, String name, double activationDistance, long showTimeTicks, Location location) {
        super(hologram, name, activationDistance, showTimeTicks, PeriodicType.EVERYTIME, location);
    }

    public EverytimeHologram(String name, double activationDistance, long showTimeTicks, Location location) {
        super(name, activationDistance, showTimeTicks, PeriodicType.EVERYTIME, location);
    }

    @Override
    public void attemptToShow(Player player) {
        show(player);
    }
    
}