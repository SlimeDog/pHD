package me.ford.periodicholographicdisplays.holograms;

import com.gmail.filoghost.holographicdisplays.api.Hologram;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import me.ford.periodicholographicdisplays.PeriodicHolographicDisplays;

/**
 * FlashingHologram
 */
public abstract class FlashingHologram extends PeriodicHologramBase {
    public static final double NO_FLASH = -1.0D;
    public static final double MIN_FLASH = 1.0D;
    private final PeriodicHolographicDisplays phd;
    private boolean flashes = false;
    private double flashOn = NO_FLASH;
    private double flashOff = NO_FLASH;
    private BukkitTask on = null;
    private BukkitTask off = null;

    public FlashingHologram(Hologram hologram, String name, double activationDistance, long showTime, PeriodicType type,
            boolean isNew, String perms, double flashOn, double flashOff) {
        super(hologram, name, activationDistance, showTime, type, isNew, perms);
        phd = JavaPlugin.getPlugin(PeriodicHolographicDisplays.class);
        if (flashOn != NO_FLASH && flashOff != NO_FLASH) {
            setFlashOnOff(flashOn, flashOff, true);
        }
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
        setFlashOnOff(flashOn, flashOff, false);
    }

    private void setFlashOnOff(double flashOn, double flashOff, boolean onLoad) {
        flashes = true;
        this.flashOn = flashOn;
        this.flashOff = flashOff;
        if (!onLoad)
            markChanged();
    }

    public void changeFlashOn(double flashOn) {
        if (!flashes)
            throw new IllegalStateException(
                    "Cannot change flash on time when the hologram was previously not flashing");
        setFlashOnOff(flashOn, this.flashOff);
    }

    public void changeFlashOff(double flashOff) {
        if (!flashes)
            throw new IllegalStateException(
                    "Cannot change flash off time when the hologram was previously not flashing");
        setFlashOnOff(this.flashOn, flashOff);
    }

    public void setNoFlash() {
        flashes = false;
        flashOn = NO_FLASH;
        flashOff = NO_FLASH;
        markChanged();
    }

    @Override
    public void resetVisibility() {
        super.resetVisibility();
        if (!flashes())
            return;
        if (on == null || off == null)
            return;
        if (on.isCancelled() || off.isCancelled())
            return;
        on.cancel();
        off.cancel();
    }

    @Override
    public boolean show(Player player) {
        boolean showing = super.show(player);
        if (!flashes()) {
            return showing;
        }
        phd.debug(String.format("Starting to flash! %3.2f on and %3.2f off", flashOn, flashOff));
        if (!showing)
            return false;
        long cycleTicks = (int) ((flashOn + flashOff) * 20L);
        long offDelay = (int) (flashOn * 20L);
        on = new Flasher(player, true).runTaskTimer(phd, 0L, cycleTicks);
        off = new Flasher(player, false).runTaskTimer(phd, offDelay, cycleTicks);
        return true;
    }

    private class Flasher extends BukkitRunnable {
        private final Player player;
        private final boolean show;

        public Flasher(Player player, boolean show) {
            this.player = player;
            this.show = show;
        }

        @Override
        public void run() {
            if (!player.isValid() || !player.isOnline())
                return;
            if (!isBeingShownTo(player) || !flashes()) {
                cancel();
                return;
            }
            if (show) {
                showInternal(player);
            } else {
                hideFromInternal(player);
            }

        }

    }

}