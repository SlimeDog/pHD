package me.ford.periodicholographicdisplays.holograms;

import com.gmail.filoghost.holographicdisplays.api.Hologram;

import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import me.ford.periodicholographicdisplays.PeriodicHolographicDisplays;

/**
 * MCTimeHologram
 */
public class MCTimeHologram extends PeriodicHologramBase {
    private static final long DELAY = 20 * 60 * 20; // 20 minutes, 60 seconds, 20 ticks 
    private final PeriodicHolographicDisplays plugin;
    private final MCTimeHologramDisplayer displayer;
    private BukkitTask task;
    private long atTime; // between 0 and 23999

    public MCTimeHologram(Hologram hologram, String name, double activationDistance, long showTime, long atTime, boolean isNew) {
        this(hologram, name, activationDistance, showTime, atTime, isNew, null);
    }

    public MCTimeHologram(Hologram hologram, String name, double activationDistance, long showTime, long atTime, boolean isNew, String perms) {
        super(hologram, name, activationDistance, showTime, PeriodicType.MCTIME, isNew, perms);
        this.atTime = atTime;
        plugin = JavaPlugin.getPlugin(PeriodicHolographicDisplays.class);
        displayer = new MCTimeHologramDisplayer();
        task = plugin.getServer().getScheduler().runTaskTimer(plugin, displayer, DELAY, DELAY);
    }

    public long getTime() {
        return atTime;
    }

    public void timeChanged(long amount) {
        if (amount == 0) return;
        task.cancel();
        long newTime = (getLocation().getWorld().getTime() + amount)%24000;
        long curDelay = (atTime - newTime)%24000;// in MC time = ticks
        task = plugin.getServer().getScheduler().runTaskTimer(plugin, displayer, curDelay, DELAY);
    }

    public void setTime(long time) {
        this.atTime = time;
    }

    @Override
    public void attemptToShow(Player player) {
        // nothing - only showing when the time is right
    }

    private void showInRange() {
        World world = getLocation().getWorld();
        double dist = getActivationDistance();
        for (Entity entity : world.getNearbyEntities(getLocation(), dist, dist, dist, (e) -> e.getType() == EntityType.PLAYER)) {
            Player player = (Player) entity;
            show(player);
        }
    }

    private class MCTimeHologramDisplayer implements Runnable {

        @Override
        public void run() {
            showInRange();
        }
        
    }
    
}