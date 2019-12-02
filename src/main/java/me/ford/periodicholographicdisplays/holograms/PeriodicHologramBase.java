package me.ford.periodicholographicdisplays.holograms;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.line.HologramLine;
import com.gmail.filoghost.holographicdisplays.api.line.TextLine;

import org.apache.commons.lang.Validate;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import me.ford.periodicholographicdisplays.PeriodicHolographicDisplays;

/**
 * PeriodicHologram
 */
public abstract class PeriodicHologramBase {
    private final PeriodicHolographicDisplays plugin = JavaPlugin.getPlugin(PeriodicHolographicDisplays.class);
    private final Set<UUID> beingShownTo = new HashSet<>();
    private final String name;
    private final double activationDistance;
    private final double squareDistance;
    private final long showTimeTicks;
    private final PeriodicType type;
    private final Location location;
    private final Hologram hologram;

    public PeriodicHologramBase(String name, double activationDistance, long showTimeTicks, PeriodicType type, Location location) {
        this(HologramsAPI.createHologram(JavaPlugin.getPlugin(PeriodicHolographicDisplays.class), location), 
                                        name, activationDistance, showTimeTicks, type, location);
    }

    public PeriodicHologramBase(Hologram hologram, String name, double activationDistance, long showTimeTicks, PeriodicType type, Location location) {
        Validate.notNull(hologram, "Hologram cannot be null!");
        this.hologram = hologram;
        this.name = name;
        this.activationDistance = activationDistance;
        this.squareDistance = activationDistance * activationDistance;
        this.showTimeTicks = showTimeTicks;
        this.type = type;
        this.location = location;
        hologram.getVisibilityManager().setVisibleByDefault(false);
    }
    
    public String getName() {
        return name;
    }

    public double getActivationDistance() {
        return activationDistance;
    }

    public double getSquareDistance() {
        return squareDistance;
    }

    public long getShowTimeTicks() {
        return showTimeTicks;
    }

    public PeriodicType getType() {
        return type;
    }

    public Location getLocation() {
        return location;
    }

    public void addLines(List<String> lines) {
        for (String line : lines) {
            hologram.appendTextLine(plugin.getSettings().color(line));
        }
    }

    public List<String> getLines() {
        List<String> lines = new ArrayList<>();
        for (int i = 0; i < hologram.size(); i++) {
            HologramLine line = hologram.getLine(i);
            if (!(line instanceof TextLine)) {
                throw new IllegalStateException("Can only use text lines for periodic holograms (for now at least!)");
            }
            lines.add(((TextLine) line).getText());
        }
        return lines;
    }

    public abstract void attemptToShow(Player player);

    public void show(Player player) {
        if (player == null) return;
        UUID id = player.getUniqueId();
        if (beingShownTo.contains(id)) return;
        hologram.getVisibilityManager().showTo(player);
        beingShownTo.add(id);
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            hologram.getVisibilityManager().hideTo(player);
            beingShownTo.remove(id);
        }, showTimeTicks);
    }
    
}