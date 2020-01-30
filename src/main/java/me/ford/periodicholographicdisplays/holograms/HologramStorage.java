package me.ford.periodicholographicdisplays.holograms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.line.HologramLine;
import com.gmail.filoghost.holographicdisplays.api.line.ItemLine;
import com.gmail.filoghost.holographicdisplays.api.line.TextLine;

import org.apache.commons.lang.Validate;
import org.bukkit.World;
import org.bukkit.entity.Player;

import me.ford.periodicholographicdisplays.PeriodicHolographicDisplays;

/**
 * HologramStorage
 */
public class HologramStorage {
    private final PeriodicHolographicDisplays plugin;
    private final Map<World, WorldHologramStorage> holograms = new HashMap<>();

    public HologramStorage(PeriodicHolographicDisplays plugin) {
        this.plugin = plugin;
        for (World world : plugin.getServer().getWorlds()) {
            holograms.put(world, new WorldHologramStorage(plugin, world));
        }
    }

    public WorldHologramStorage getHolograms(World world) {
        Validate.notNull(world, "Cannot get holograms of a null world!");
        WorldHologramStorage storage = holograms.get(world);
        if (storage == null) {
            storage = new WorldHologramStorage(plugin, world);
            holograms.put(world, storage);
            plugin.getLogger().info("Initializing per world holograms at an odd time!");
        }
        return storage;
    }

    // adoptions

    public PeriodicHologramBase adoptEveryTime(Hologram oldHologram, String name, double activationDistance, long showTimeTicks) {
        Hologram newHolo = cloneHologram(oldHologram);
        PeriodicHologramBase holo = new EverytimeHologram(newHolo, name, activationDistance, showTimeTicks, oldHologram.getLocation());
        adoptBase(oldHologram, newHolo, holo);
        return holo;
    }

    public OnceHologram adoptOnce(Hologram oldHologram, String name, double activationDistance, long showTimeTicks) {
        Hologram newHolo = cloneHologram(oldHologram);
        OnceHologram holo = new OnceHologram(newHolo, name, activationDistance, showTimeTicks, oldHologram.getLocation());
        adoptBase(oldHologram, newHolo, holo);
        return holo;
    }

    public PeriodicHologramBase adoptPeriodic(Hologram oldHologram, String name, double activationDistance, long showTimeTicks, long showDelaySeconds) {
        Hologram newHolo = cloneHologram(oldHologram);
        PeriodicHologramBase holo = new PeriodicHologram(newHolo, name, activationDistance, showTimeTicks, newHolo.getLocation(), showDelaySeconds);
        adoptBase(oldHologram, newHolo, holo);
        return holo;
    }

    public PeriodicHologramBase adoptNTimes(Hologram oldHologram, String name, double activationDistance, long showTimeTicks, int showTimes) {
        Hologram newHolo = cloneHologram(oldHologram);
        PeriodicHologramBase holo = new NTimesHologram(newHolo, name, activationDistance, showTimeTicks, newHolo.getLocation(), showTimes);
        adoptBase(oldHologram, newHolo, holo);
        return holo;
    }

    public OnJoinHologram adoptOnJoin(Hologram oldHologram, String name, double activationDistance, long showTimeTicks) {
        Hologram newHolo = cloneHologram(oldHologram);
        OnJoinHologram holo = new OnJoinHologram(newHolo, name, activationDistance, showTimeTicks, newHolo.getLocation());
        adoptBase(oldHologram, newHolo, holo);
        return holo;
    }

    public OnWorldJoinHologram adoptOnWorldJoin(Hologram oldHologram, String name, double activationDistance, long showTimeTicks) {
        Hologram newHolo = cloneHologram(oldHologram);
        OnWorldJoinHologram holo = new OnWorldJoinHologram(newHolo, name, activationDistance, showTimeTicks, newHolo.getLocation());
        adoptBase(oldHologram, newHolo, holo);
        return holo;
    }

    private void adoptBase(Hologram oldHologram, Hologram newHolo, PeriodicHologramBase holo) {
        oldHologram.delete();
        addHologram(holo);
    }

    private Hologram cloneHologram(Hologram oldHologram) {
        List<HologramLine> lines = new ArrayList<>();
        for (int i = 0; i < oldHologram.size(); i++) {
            try {
                lines.add(oldHologram.getLine(i));
            } catch (IndexOutOfBoundsException e) {
                break;
            }
        }
        Hologram newHolo = HologramsAPI.createHologram(plugin, oldHologram.getLocation());
        for (HologramLine line : lines) {
            if (line instanceof ItemLine) {
                ItemLine itemLine = (ItemLine) line;
                newHolo.appendItemLine(itemLine.getItemStack());
            } else if (line instanceof TextLine) {
                TextLine text = (TextLine) line;
                newHolo.appendTextLine(text.getText());
            } else {
                throw new IllegalArgumentException("Unable to adopt a hologram with a line of class " + line.getClass());
            }
        }
        return newHolo;
    }

    // adding

    public void addHologram(PeriodicHologramBase hologram) {
        WorldHologramStorage storage = getHolograms(hologram.getLocation().getWorld());
        storage.addHologram(hologram);
        storage.save();
    }

    // saving

    public void save() {
        for (WorldHologramStorage storage : holograms.values()) {
            storage.save();
        }
    }

    // onJoin holgorams

    public void joined(Player player) {
        // nothing really...
    }

    public void left(Player player) {
        for (WorldHologramStorage ws : holograms.values()) {
            ws.left(player);
        }
    }

    public void leftWorld(Player player, World world) {
        WorldHologramStorage wh = getHolograms(world);
        wh.leftWorld(player);
    }
    
}