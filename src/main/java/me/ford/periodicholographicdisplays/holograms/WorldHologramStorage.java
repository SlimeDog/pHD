package me.ford.periodicholographicdisplays.holograms;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

import com.gmail.filoghost.holographicdisplays.commands.CommandValidator;
import com.gmail.filoghost.holographicdisplays.exception.CommandException;
import com.gmail.filoghost.holographicdisplays.object.NamedHologram;

import org.apache.commons.lang.Validate;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

import me.ford.periodicholographicdisplays.PeriodicHolographicDisplays;
import me.ford.periodicholographicdisplays.Settings;

/**
 * Storage
 */
public class WorldHologramStorage extends WorldHologramStorageBase {
    private final PeriodicHolographicDisplays plugin;

    public WorldHologramStorage(PeriodicHolographicDisplays plugin, World world) {
        super(plugin, world);
        this.plugin = plugin;
        scheduleLoad();
        scheduleSave();
    }

    private void scheduleLoad() { // TODO - maybe there's an event?
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            for (String name : getConfig().getKeys(false)) {
                IndividualHologramHandler holo = loadHologram(name);
                if (holo == null)
                    continue;
                addHandler(name, holo);
            }
        }, 40L); // need to do this later so the holograms are loaded
    }

    private void scheduleSave() {
        long delay = plugin.getSettings().getSaveDelay() * 20L;
        plugin.getServer().getScheduler().runTaskTimer(plugin, () -> save(), delay, delay);
    }

    private PeriodicHologramBase loadType(String name, ConfigurationSection section) throws HologramLoadException {
        if (section == null) {
            throw new HologramLoadException("Unable to parse hologram because of incorrect config (using the old system?): " + name);
        }
        PeriodicType type;
        try {
            type = PeriodicType.valueOf(section.getName());
        } catch (IllegalArgumentException e) {
            throw new HologramLoadException("Unable to parse type of hologram: " + section.getName());
        }
        NamedHologram hologram;
        try {
            hologram = CommandValidator.getNamedHologram(name);
        } catch (CommandException e) {
            throw new HologramLoadException("Hologram by the name of '" + name + "' does not exist'");
        }
        Settings settings = plugin.getSettings();
        double distance = section.getDouble("activation-distance", settings.getDefaultActivationDistance());
        long showTime = section.getLong("show-time", settings.getDefaultShowTime());
        String perms = section.getString("permission"); // defaults to null
        final PeriodicHologramBase holo;
        switch (type) {
        case IRLTIME:
            long atTime = section.getLong("show-at", 0); // seconds from 00:00
            holo = new IRLTimeHologram(hologram, name, distance, showTime, atTime, false, perms);
            break;
        case MCTIME:
            long time = section.getLong("show-at", 0);
            holo = new MCTimeHologram(hologram, name, distance, showTime, time, false, perms);
            break;
        case ALWAYS:
        case NTIMES:
            int timesToShow;
            if (type == PeriodicType.ALWAYS) {
                timesToShow = -1;
            } else {
                timesToShow = section.getInt("times-to-show", 1);
            }
            NTimesHologram ntimes = new NTimesHologram(hologram, name, distance, showTime, timesToShow, false, perms);
            if (type != PeriodicType.ALWAYS) {
                addShownToTimes(ntimes, section.getConfigurationSection("shown-to"));
            }
            holo = ntimes;
            break;
        default:
            plugin.getLogger().info("Undefined loading behavour with type: " + type);
            return null;
        }
        return holo;
    }

    private IndividualHologramHandler loadHologram(String name) {
        ConfigurationSection section = getConfig().getConfigurationSection(name);
        IndividualHologramHandler handler = null;
        for (String typeStr : section.getKeys(false)) {
            PeriodicHologramBase holo;
            try {
                holo = loadType(name, section.getConfigurationSection(typeStr));
            } catch (HologramLoadException e) {
                plugin.getLogger().log(Level.WARNING, "Problem loading hologram of type " + typeStr + " from file for hologram " + name, e);
                continue;
            }
            if (handler == null) {
                handler = new IndividualHologramHandler(holo.getType(), holo);
            } else {
                handler.addHologram(holo.getType(), holo);
            }
        }
        return handler;
    }

    private void addShownToTimes(NTimesHologram holo, ConfigurationSection section) {
        for (String uuid : section.getKeys(false)) {
            UUID id;
            try {
                id = UUID.fromString(uuid);
            } catch (IllegalArgumentException e) {
                plugin.getLogger().warning("Unable to parse UUID of Periodic hologram " + holo.getName() + " : " + uuid);
                continue;
            }
            holo.addShownTo(id, section.getInt(uuid));
        }
    }

    public PeriodicHologramBase getHologram(String name, PeriodicType type) {
        IndividualHologramHandler handler = getHandler(name);
        if (handler == null) return null;
        if (type == PeriodicType.ALWAYS) type = PeriodicType.NTIMES;
        return handler.getHologram(type);
    }

    public List<PeriodicHologramBase> getHolograms() { // TODO - potentially only show those in loaded chunks
        List<PeriodicHologramBase> holos = new ArrayList<>();
        for (IndividualHologramHandler handler : getHandlers()) {
            holos.addAll(handler.getHolograms());
        }
        return holos;
    }

    public List<String> getHologramNames() { // TODO - potentially only show those in loaded chunks
        List<String> names = new ArrayList<>();
        for (PeriodicHologramBase holo : getHolograms()) {
            names.add(holo.getName());
        }
        return names;
    }

    @Override
    protected boolean saveHolograms() {
        boolean madeChanges = false;
        for (IndividualHologramHandler handler : getHandlers()) {
            if (handler.needsSaved()){
                saveHologram(handler);
                handler.markSaved();
                madeChanges = true;
            }
        }
        return madeChanges;
    }

    private void saveType(ConfigurationSection section, PeriodicHologramBase holo) {
        Settings settings = plugin.getSettings();
        String typeStr = (holo.getType() == PeriodicType.NTIMES && ((NTimesHologram) holo).getTimesToShow() < 0) ? PeriodicType.ALWAYS.name() : holo.getType().name();
        section = section.getParent().getConfigurationSection(typeStr);
        section.set("type", typeStr);
        if (holo.getActivationDistance() != settings.getDefaultActivationDistance()) {
            section.set("activation-distance", holo.getActivationDistance());
        }
        if (holo.getShowTimeTicks() != settings.getDefaultShowTime() * 20L) {
            section.set("show-time", holo.getShowTimeTicks()/20L);
        }
        section.set("permission", holo.getPermissions());
        if (holo instanceof NTimesHologram) {
            NTimesHologram ntimes = (NTimesHologram) holo;
            if (ntimes.getTimesToShow() > -1) {
                section.set("times-to-show", ntimes.getTimesToShow());
                ConfigurationSection shownToSection = section.createSection("shown-to"); 
                for (Map.Entry<UUID, Integer> entry : ntimes.getShownTo().entrySet()) {
                    shownToSection.set(entry.getKey().toString(), entry.getValue());
                }
            }
        } else if (holo instanceof MCTimeHologram) {
            MCTimeHologram mctime = (MCTimeHologram) holo;
            section.set("show-at", mctime.getTime());
        } else if (holo instanceof IRLTimeHologram) {
            IRLTimeHologram irltime = (IRLTimeHologram) holo;
            section.set("show-at", irltime.getTime());
        }
    }

    private void saveHologram(IndividualHologramHandler handler) {
        ConfigurationSection section = getConfig().createSection(handler.getName());
        for (PeriodicHologramBase holo : handler.getHolograms()) {
            saveType(section.createSection(holo.getType().name()), holo);
        }
        if (!handler.hasHolograms()) {
            removeHandler(handler.getName());
        }
    }

    void addHologram(PeriodicHologramBase hologram) {
        Validate.notNull(hologram, "Cannot add null hologram!");
        Validate.isTrue(hologram.getLocation().getWorld() == getWorld(), "Cannot add holograms in a different world!");
        IndividualHologramHandler handler = getHandler(hologram.getName());
        if (handler == null) {
            handler = new IndividualHologramHandler(hologram.getType(), hologram);
            addHandler(hologram.getName(), handler);
        } else {
            handler.addHologram(hologram.getType(), hologram);
        }
    }

    void removeHologram(PeriodicHologramBase hologram) {
        Validate.notNull(hologram, "Cannot renove null hologram!");
        Validate.isTrue(hologram.getLocation().getWorld() == getWorld(), "Cannot remove holograms in a different world!");
        IndividualHologramHandler handler = getHandler(hologram.getName());
        handler.removeHologram(hologram);
    }

    public class HologramLoadException extends IllegalArgumentException {

        /**
         *
         */
        private static final long serialVersionUID = -2998543533635166107L;
        
        public HologramLoadException(String msg) {
            super(msg);
        }

    }
    
}