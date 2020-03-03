package me.ford.periodicholographicdisplays.listeners.legacy;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.java.JavaPlugin;

import me.ford.periodicholographicdisplays.PeriodicHolographicDisplays;
import me.ford.periodicholographicdisplays.holograms.HologramStorage;
import me.ford.periodicholographicdisplays.holograms.WorldHologramStorage;
import me.ford.periodicholographicdisplays.holograms.events.StartedManagingHologramEvent;
import me.ford.periodicholographicdisplays.holograms.events.StoppedManagingHologramEvent;
import me.ford.periodicholographicdisplays.listeners.WorldTimeListener;

/**
 * LegacyWorldTimeListener
 */
public class LegacyWorldTimeListener extends WorldTimeListener {
    private final PeriodicHolographicDisplays phd;
    private final Map<World, Long> worldTimes = new HashMap<>();

    public LegacyWorldTimeListener(HologramStorage storage) {
        super(storage);
        phd = JavaPlugin.getPlugin(PeriodicHolographicDisplays.class);
        phd.getServer().getScheduler().runTaskTimer(phd, new TickRunnable(), 1L, 1L);
    }

    @EventHandler
    public void onStartManage(StartedManagingHologramEvent event) {
        World world = event.getHologram().getLocation().getWorld();
        if (!worldTimes.containsKey(world)) addWorld(world);
    }

    @EventHandler
    public void onStopManage(StoppedManagingHologramEvent event) {
        phd.getServer().getScheduler().runTask(phd, new WorldChecker(event.getHologram().getLocation().getWorld()));
    }

    private void addWorld(World world) { // TODO - how do I know if I will or will not still have the TickRunnable run this tick ?
        if (phd.getSettings().onDebug()) {
            phd.getLogger().info("Adding world to LegacyWorldTimeListener: " + world.getName()); 
        }
        worldTimes.put(world, world.getFullTime()); // or does it even matter?
    }

    private void removeWorld(World world) {
        if (phd.getSettings().onDebug()) {
            phd.getLogger().info("Removing world from LegacyWorldTimeListener: " + world.getName()); 
        }
        worldTimes.remove(world);
    }

    private void timeChanged(World world, long addTime) {
        if (phd.getSettings().onDebug()) {
            phd.getLogger().info("Time changed in " + world.getName() + " by " + addTime); 
        }
        getStorage().mcTimeChanged(world, addTime);
    }

    private final class TickRunnable implements Runnable {

        @Override
        public void run() {
            for (Entry<World, Long> entry : worldTimes.entrySet()) {
                World world = entry.getKey();
                long prevTime = entry.getValue();
                long newTime = world.getFullTime();
                if (newTime != prevTime + 1) {
                    // time changed
                    if (world.getGameRuleValue(GameRule.DO_DAYLIGHT_CYCLE)) { // ignore if time doesn't change anyway ?
                        timeChanged(world, newTime - (prevTime + 1));
                    }
                }
                worldTimes.put(world, newTime);
            }
        }

    }

    private final class WorldChecker implements Runnable {
        private final World world;

        private WorldChecker(World world) {
            this.world = world;
        }

        @Override
        public void run() {
            WorldHologramStorage holos = getStorage().getHolograms(world);
            if (holos == null || holos.getHolograms().isEmpty()) {
                removeWorld(world);
            }
        }

    }
    
}