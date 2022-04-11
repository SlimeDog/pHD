package me.ford.periodicholographicdisplays.holograms;

import java.util.Calendar;

import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import me.ford.periodicholographicdisplays.IPeriodicHolographicDisplays;
import me.ford.periodicholographicdisplays.holograms.wrap.WrappedHologram;

/**
 * IRLTimeHologram
 */
public class IRLTimeHologram extends FlashingHologram {
    private static final long DELAY = 24 * 60 * 60 * 20; // 24 hours, 60 minutes, 60 seconds, 20 ticks
    private final IPeriodicHolographicDisplays plugin;
    private final IRLTimeHologramDisplayer displayer;
    private BukkitTask task;
    private long atTime; // in day in seconds

    public IRLTimeHologram(IPeriodicHolographicDisplays phd, WrappedHologram hologram, String name,
            double activationDistance, long showTime, long atTime, boolean isNew, String perms, double flashOn,
            double flashOff) {
        super(phd, hologram, name, activationDistance, showTime, PeriodicType.IRLTIME, isNew, perms, flashOn,
                flashOff);
        this.atTime = atTime;
        plugin = phd;
        displayer = new IRLTimeHologramDisplayer();
        initTask();
    }

    private void initTask() {
        Calendar calendar = Calendar.getInstance();
        long curTimeSeconds = calendar.get(Calendar.HOUR_OF_DAY) * 3600 + calendar.get(Calendar.MINUTE) * 60
                + calendar.get(Calendar.SECOND);
        long curDelay = ((atTime - curTimeSeconds) * 20) % DELAY; // in ticks
        task = plugin.runTaskTimer(displayer, curDelay, DELAY);
    }

    public long getTime() {
        return atTime;
    }

    public void setTime(long time) {
        if (atTime == time)
            return;
        this.atTime = time;
        task.cancel();
        initTask();
        markChanged();
    }

    @Override
    public void attemptToShow(Player player) {
        // nothing - only showing when the time is right
    }

    private void showInRange() {
        World world = getLocation().getWorld();
        double dist = getActivationDistance();
        if (dist == NO_DISTANCE)
            dist = plugin.getSettings().getDefaultActivationDistance();
        for (Entity entity : world.getNearbyEntities(getLocation(), dist, dist, dist,
                (e) -> e.getType() == EntityType.PLAYER)) {
            Player player = (Player) entity;
            show(player);
        }
    }

    @Override
    protected boolean specialDisable() {
        return false;
    }

    private class IRLTimeHologramDisplayer implements Runnable {

        @Override
        public void run() {
            showInRange();
        }

    }

}