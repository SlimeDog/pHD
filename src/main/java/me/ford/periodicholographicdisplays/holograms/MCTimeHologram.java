package me.ford.periodicholographicdisplays.holograms;

import com.gmail.filoghost.holographicdisplays.api.Hologram;

import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import me.ford.periodicholographicdisplays.IPeriodicHolographicDisplays;

/**
 * MCTimeHologram
 */
public class MCTimeHologram extends FlashingHologram {
    private static final long DELAY = 20 * 60 * 20; // 20 minutes, 60 seconds, 20 ticks
    private final IPeriodicHolographicDisplays plugin;
    private final MCTimeHologramDisplayer displayer;
    private BukkitTask task;
    private long atTime; // between 0 and 23999

    public MCTimeHologram(IPeriodicHolographicDisplays phd, Hologram hologram, String name,
            double activationDistance, long showTime, long atTime, boolean isNew, String perms, double flashOn,
            double flashOff) {
        super(phd, hologram, name, activationDistance, showTime, PeriodicType.MCTIME, isNew, perms, flashOn,
                flashOff);
        this.atTime = atTime;
        plugin = phd;
        displayer = new MCTimeHologramDisplayer();
        schedule();
    }

    public long getTime() {
        return atTime;
    }

    public void timeChanged(long amount) {
        if (amount == 0)
            return;
        schedule(amount);
    }

    private void schedule() {
        schedule(0);
    }

    private final static long TIME_FIX = 6000L;

    private void schedule(long amount) {
        if (task != null)
            task.cancel();
        long newTime = (getLocation().getWorld().getTime() + amount + TIME_FIX) % 24000;
        long curDelay = (atTime - newTime) % 24000;// in MC time = ticks
        task = plugin.runTaskTimer(displayer, curDelay, DELAY);
    }

    public void setTime(long time) {
        this.atTime = time;
        schedule();
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

    private class MCTimeHologramDisplayer implements Runnable {

        @Override
        public void run() {
            showInRange();
        }

    }

}