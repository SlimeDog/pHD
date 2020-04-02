package me.ford.periodicholographicdisplays;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

/**
 * SchedulingPeriodicHolographicDisplays
 */
public abstract class SchedulingPeriodicHolographicDisplays extends JavaPlugin implements IPeriodicHolographicDisplays {

    public BukkitTask runTask(Runnable runnable) {
        return getServer().getScheduler().runTask(this, runnable);
    }

    public BukkitTask runTaskLater(Runnable runnable, long delay) {
        return getServer().getScheduler().runTaskLater(this, runnable, delay);
    }

    public BukkitTask runTaskTimer(Runnable runnable, long delay, long period) {
        return getServer().getScheduler().runTaskTimer(this, runnable, delay, period);
    }

    public BukkitTask runTaskAsynchronously(Runnable runnable) {
        return getServer().getScheduler().runTaskAsynchronously(this, runnable);
    }

    public BukkitTask runTaskLaterAsynchronously(Runnable runnable, long delay) {
        return getServer().getScheduler().runTaskLaterAsynchronously(this, runnable, delay);
    }

    public BukkitTask runTaskTimerAsynchronously(Runnable runnable, long delay, long period) {
        return getServer().getScheduler().runTaskTimerAsynchronously(this, runnable, delay, period);
    }

    
}