package me.ford.periodicholographicdisplays.holograms;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.gmail.filoghost.holographicdisplays.api.Hologram;

import org.apache.commons.lang.Validate;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import me.ford.periodicholographicdisplays.IPeriodicHolographicDisplays;
import me.ford.periodicholographicdisplays.Settings;

/**
 * PeriodicHologram
 */
public abstract class PeriodicHologramBase {
    public static final int NO_SECONDS = -1;
    public static final double NO_DISTANCE = -1.0D;
    private final IPeriodicHolographicDisplays plugin;
    private final Set<UUID> beingShownTo = new HashSet<>();
    private final String name;
    private double activationDistance; // internal
    private double squareDistance;
    private long showTime; // internal
    private long showTimeTicks;
    private boolean hasChanged = false;
    private final PeriodicType type; // in order to change this, I'll create a new instance
    private final Hologram hologram;
    private String perms;

    public PeriodicHologramBase(IPeriodicHolographicDisplays phd, Hologram hologram, String name, double activationDistance, long showTime,
            PeriodicType type, boolean isNew) {
        this(phd, hologram, name, activationDistance, showTime, type, isNew, null);
    }

    public PeriodicHologramBase(IPeriodicHolographicDisplays phd, Hologram hologram, String name, double activationDistance, long showTime,
            PeriodicType type, boolean isNew, String perms) {
        Validate.notNull(hologram, "Hologram cannot be null!");
        this.hologram = hologram;
        this.name = name;
        this.plugin = phd;
        if (activationDistance == NO_DISTANCE) {
            defaultDistance(plugin.getSettings());
        } else {
            setActivationDistance(activationDistance);
        }
        if (showTime == NO_SECONDS) {
            defaultShowtime(plugin.getSettings());
        } else {
            setShowTime((int) showTime);
        }
        this.type = type;
        this.hasChanged = isNew;
        this.perms = perms;
        hologram.getVisibilityManager().setVisibleByDefault(false);
    }

    public String getName() {
        return name;
    }

    Hologram getHologram() {
        return hologram;
    }

    public void setActivationDistance(double distance) {
        setActivationDistance(distance, distance * distance);
    }

    private void setActivationDistance(double distance, double distanceSquared) {
        this.activationDistance = distance;
        this.squareDistance = distanceSquared;
        markChanged();
    }

    public double getActivationDistance() {
        return activationDistance;
    }

    public double getSquareDistance() {
        return squareDistance;
    }

    public void setShowTime(int time) {
        setShowTime(time, time * 20L);
    }

    private void setShowTime(int time, long ticks) {
        showTime = time;
        showTimeTicks = ticks;
        markChanged();
    }

    public long getShowTime() {
        return showTime;
    }

    public long getShowTimeTicks() {
        return showTimeTicks;
    }

    public PeriodicType getType() {
        return type;
    }

    public boolean hasPermissions() {
        return perms != null;
    }

    public void setPermissions(String perms) {
        this.perms = perms;
        markChanged();
    }

    public String getPermissions() {
        return perms;
    }

    public void markSaved() {
        hasChanged = false;
    }

    public void markRemoved() {
        this.hologram.getVisibilityManager().setVisibleByDefault(true);
        this.hologram.getVisibilityManager().resetVisibilityAll();
        this.beingShownTo.clear();
    }

    protected void markChanged() {
        hasChanged = true;
    }

    public boolean needsSaved() {
        return hasChanged;
    }

    public Location getLocation() {
        return hologram.getLocation();
    }

    public abstract void attemptToShow(Player player);

    protected abstract boolean specialDisable();

    public boolean isBeingShownTo(Player player) {
        return beingShownTo.contains(player.getUniqueId());
    }

    public boolean canSee(Player player) {
        return (!hasPermissions() || player.hasPermission(getPermissions()));
    }

    public void hideFrom(Player player) {
        if (plugin.getSettings().onDebug() && this instanceof FlashingHologram) {
            String info = plugin.getMessages().getHologramInfoMessage((FlashingHologram) this, 1, true).replace("\n", ";");
            plugin.debug(String.format("Hiding from %s: %s", player.getName(), info));
        }
        hideFromInternal(player);
        beingShownTo.remove(player.getUniqueId());
    }

    protected void hideFromInternal(Player player) {
        hologram.getVisibilityManager().hideTo(player);
    }

    public boolean show(Player player) {
        if (player == null)
            return false;
        UUID id = player.getUniqueId();
        if (beingShownTo.contains(id))
            return false;
        if (plugin.getSettings().onDebug() && this instanceof FlashingHologram) {
            String info = plugin.getMessages().getHologramInfoMessage((FlashingHologram) this, 1, true).replace("\n", ";");
            plugin.debug(String.format("Showing to %s: %s", player.getName(), info));
        }
        showInternal(player);
        beingShownTo.add(id);
        boolean scheduleHide = !specialDisable();
        if (scheduleHide) {
            plugin.runTaskLater(() -> hideFrom(player), showTimeTicks);
        }
        return true;
    }

    protected void showInternal(Player player) {
        hologram.getVisibilityManager().showTo(player);
    }

    public void defaultDistance(Settings settings) {
        double defDist = settings.getDefaultActivationDistance();
        setActivationDistance(NO_DISTANCE, defDist * defDist);
    }

    public void defaultShowtime(Settings settings) {
        int time = settings.getDefaultShowTime();
        setShowTime(NO_SECONDS, time * 20L);
    }

    protected IPeriodicHolographicDisplays getPlugin() {
        return plugin;
    }

    public void resetVisibility() {
        for (UUID id : beingShownTo) {
            Player player = plugin.getPlayer(id);
            if (player != null)
                hideFrom(player);
        }
        beingShownTo.clear();
    }

}