package me.ford.periodicholographicdisplays.holograms;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.gmail.filoghost.holographicdisplays.api.Hologram;

import org.apache.commons.lang.Validate;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import me.ford.periodicholographicdisplays.PeriodicHolographicDisplays;
import me.ford.periodicholographicdisplays.Settings;

/**
 * PeriodicHologram
 */
public abstract class PeriodicHologramBase {
    public static final int NO_SECONDS = -1;
    public static final double NO_DISTANCE = -1.0D;
    private final PeriodicHolographicDisplays plugin = JavaPlugin.getPlugin(PeriodicHolographicDisplays.class);
    private final Set<UUID> beingShownTo = new HashSet<>();
    private final String name;
    private double activationDistance;
    private double squareDistance;
    private long showTimeTicks;
    private boolean hasChanged = false;
    private final PeriodicType type; // in order to change this, I'll create a new instance
    private final Hologram hologram;
    private String perms;

    public PeriodicHologramBase(Hologram hologram, String name, double activationDistance, long showTime, PeriodicType type, boolean isNew) {
        this(hologram, name, activationDistance, showTime, type, isNew, null);
    }

    public PeriodicHologramBase(Hologram hologram, String name, double activationDistance, long showTime, PeriodicType type, boolean isNew, String perms) {
        Validate.notNull(hologram, "Hologram cannot be null!");
        this.hologram = hologram;
        this.name = name;
        this.activationDistance = activationDistance;
        this.squareDistance = activationDistance * activationDistance;
        this.showTimeTicks = showTime * 20L;// seconds -> ticks
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
        this.activationDistance = distance;
        this.squareDistance = distance * distance;
        markChanged();
    }

    public double getActivationDistance() {
        return activationDistance;
    }

    public double getSquareDistance() {
        return squareDistance;
    }

    public void setShowTime(int time) {
        showTimeTicks = time * 20L; // second -> ticks
        markChanged();
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

    public boolean isBeingShownTo(Player player) {
        return beingShownTo.contains(player.getUniqueId());
    }

    public boolean canSee(Player player) {
        return (!hasPermissions() || player.hasPermission(getPermissions()));
    }
    
    public void hideFrom(Player player) {
        if (plugin.getSettings().onDebug()) {
            String info = plugin.getMessages().getHologramInfoMessage(this, 1).replace("\n", ";");
            plugin.debug(String.format("Hiding from %s: %s", player.getName(), info));
        }
        hologram.getVisibilityManager().hideTo(player);
        beingShownTo.remove(player.getUniqueId());
    }

    public void show(Player player) {
        if (player == null) return;
        UUID id = player.getUniqueId();
        if (beingShownTo.contains(id)) return;
        if (plugin.getSettings().onDebug()) {
            String info = plugin.getMessages().getHologramInfoMessage(this, 1).replace("\n", ";");
            plugin.debug(String.format("Showing to %s: %s", player.getName(), info));
        }
        hologram.getVisibilityManager().showTo(player);
        beingShownTo.add(id);
        boolean scheduleHide = true;
        if (getType() == PeriodicType.ALWAYS) {
            AlwaysHologram always = (AlwaysHologram) this;
            if (always.isShownOnWorldJoin() || always.isShownWhileInArea()) {
                // in these cases taken care of elsewhere
                scheduleHide = false;
            }
        }
        if (scheduleHide) {
            plugin.getServer().getScheduler().runTaskLater(plugin, () -> hideFrom(player), showTimeTicks);
        }
    }

    public void defaultDistance(Settings settings) {
        setActivationDistance(settings.getDefaultActivationDistance());
    }

    public void defaultShowtime(Settings settings) {
        int time = settings.getDefaultShowTime();
        if (getType() == PeriodicType.ALWAYS) {
            time = PeriodicHologramBase.NO_SECONDS;
        }
        setShowTime(time);
    }
    
}