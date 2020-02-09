package me.ford.periodicholographicdisplays.holograms;

import com.gmail.filoghost.holographicdisplays.api.Hologram;

import org.bukkit.entity.Player;

/**
 * AlwaysHologram
 */
public class AlwaysHologram extends NTimesHologram {

    public AlwaysHologram(Hologram hologram, String name, double activationDistance, long showTime,
            boolean isNew, String perms) {
        super(hologram, name, activationDistance, showTime, -1, isNew, perms);
    }

    public void leftArea(Player player) {
        hideFrom(player);
    }
    
}