package me.ford.periodicholographicdisplays.holograms;

import com.gmail.filoghost.holographicdisplays.api.Hologram;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import me.ford.periodicholographicdisplays.PeriodicHolographicDisplays;

/**
 * FlashingHologram
 */
public abstract class FlashingHologram extends PeriodicHologramBase {
    private final PeriodicHolographicDisplays phd;
    private boolean flashes = false;
    private double flashOn = 0.0D;
    private double flashOff = 0.0D;

    public FlashingHologram(Hologram hologram, String name, double activationDistance, long showTime, PeriodicType type,
            boolean isNew) {
        super(hologram, name, activationDistance, showTime, type, isNew);
        phd = JavaPlugin.getPlugin(PeriodicHolographicDisplays.class);
    }

    public boolean flashes() {
        return flashes;
    }

    public double getFlashOn() {
        return flashOn;
    }

    public double getFlashOff() {
        return flashOff;
    }

    public void setFlashOnOff(double flash) {
        setFlashOnOff(flash, flash);
    }

    public void setFlashOnOff(double flashOn, double flashOff) {
        flashes = true;
        this.flashOn = flashOn;
        this.flashOff = flashOff;
    }

    public void changeFlashOn(double flashOn) {
        if (!flashes) throw new IllegalStateException("Cannot change flash on time when the hologram was previously not flashing");
        this.flashOn = flashOn;
    }

    public void changeFlashOff(double flashOff) {
        if (!flashes) throw new IllegalStateException("Cannot change flash off time when the hologram was previously not flashing");
        this.flashOff = flashOff;
    }

    public void setNoFlash() {
        flashes = false;
    }

    @Override
    public boolean show(Player player) {
        boolean showing = super.show(player);
        if (!flashes()) {
            return showing;
        }
        if (!showing) return false;
        long cycleTicks = (int) ((flashOn + flashOff) * 20L);
        long offDelay = (int) (flashOn * 20L);
        BukkitTask on = phd.getServer().getScheduler().runTaskTimer(phd, new Flasher(player, true), 0L, cycleTicks);
        BukkitTask off = phd.getServer().getScheduler().runTaskTimer(phd, new Flasher(player, false), offDelay, cycleTicks);
        phd.getServer().getScheduler().runTaskLater(phd, () -> {
            on.cancel();
            off.cancel();
            hideFrom(player);
        }, getShowTimeTicks());
        return true;
    }

    private class Flasher implements Runnable {
        private final Player player;
        private final boolean show;

        public Flasher(Player player, boolean show) {
            this.player = player;
            this.show = show;
        }

        @Override
        public void run() {
            if (!player.isValid() || !player.isOnline()) return;
            if (show) {
                show(player);
            } else {
                hideFrom(player);
            }

        }

    }
    
}