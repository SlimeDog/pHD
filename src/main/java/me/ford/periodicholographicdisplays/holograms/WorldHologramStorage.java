package me.ford.periodicholographicdisplays.holograms;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

import com.gmail.filoghost.holographicdisplays.commands.CommandValidator;
import com.gmail.filoghost.holographicdisplays.exception.CommandException;
import com.gmail.filoghost.holographicdisplays.object.NamedHologram;

import org.apache.commons.lang.Validate;
import org.bukkit.World;

import me.ford.periodicholographicdisplays.PeriodicHolographicDisplays;
import me.ford.periodicholographicdisplays.holograms.storage.HologramInfo;
import me.ford.periodicholographicdisplays.holograms.storage.Storage;
import me.ford.periodicholographicdisplays.holograms.storage.TypeInfo;
import me.ford.periodicholographicdisplays.holograms.storage.TypeInfo.MCTimeTypeInfo;
import me.ford.periodicholographicdisplays.holograms.storage.TypeInfo.IRLTimeTypeInfo;
import me.ford.periodicholographicdisplays.holograms.storage.TypeInfo.NTimesTypeInfo;
import me.ford.periodicholographicdisplays.holograms.storage.Storage.HDHologramInfo;

/**
 * Storage
 */
public class WorldHologramStorage extends WorldHologramStorageBase {
    private final PeriodicHolographicDisplays plugin;
    private final Storage storage;

    public WorldHologramStorage(PeriodicHolographicDisplays plugin, World world, Storage storage) {
        super(plugin, world);
        this.plugin = plugin;
        this.storage = storage;
        scheduleLoad();
        scheduleSave();
    }

    private void scheduleLoad() { // TODO - maybe there's an event?
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            storage.loadHolograms((info) -> loaded(info));
        }, 40L); // need to do this later so the holograms are loaded
    }

    private void scheduleSave() {
        long delay = plugin.getSettings().getSaveDelay() * 20L;
        plugin.getServer().getScheduler().runTaskTimer(plugin, () -> saveHolograms(false, HologramSaveReason.PERIODIC), delay, delay);
    }

    public PeriodicHologramBase getHologram(String name, PeriodicType type) {
        IndividualHologramHandler handler = getHandler(name);
        if (handler == null)
            return null;
        return handler.getHologram(type);
    }

    public List<PeriodicHologramBase> getHolograms() { // TODO - potentially only show those in loaded chunks
        return getHolograms(false);
    }

    public List<PeriodicHologramBase> getHolograms(boolean onlyLoaded) {
        List<PeriodicHologramBase> holos = new ArrayList<>();
        for (IndividualHologramHandler handler : getHandlers(onlyLoaded)) {
            holos.addAll(handler.getHolograms());
        }
        return holos;
    }

    public List<String> getHologramNames() { // TODO - potentially only show those in loaded chunks
        List<String> names = new ArrayList<>();
        for (IndividualHologramHandler handler : getHandlers(false)) {
            names.add(handler.getName());
        }
        return names;
    }

    private void loaded(HDHologramInfo info) {
        NamedHologram holo;
        try {
            holo = CommandValidator.getNamedHologram(info.getHoloName());
        } catch (CommandException e) {
            plugin.getLogger().log(Level.WARNING, "Problem loading hologram " + info.getHoloName() + ": HD hologram not found", e);
            return;
        }
        if (holo.getWorld() != getWorld()) return; // don't load
        IndividualHologramHandler handler = null;
        for (HologramInfo hInfo : info.getInfos()) {
            PeriodicHologramBase hologram;
            TypeInfo typeInfo = hInfo.getTypeInfo();
            switch (hInfo.getType()) {
                case MCTIME:
                hologram = new MCTimeHologram(holo, info.getHoloName(), hInfo.getActivationDistance(), 
                                                hInfo.getShowTime(), ((MCTimeTypeInfo) typeInfo).getAtTime(), 
                                                false, hInfo.getPermissions());
                break;
                case IRLTIME:
                hologram = new IRLTimeHologram(holo, info.getHoloName(), hInfo.getActivationDistance(),
                                                hInfo.getShowTime(), ((IRLTimeTypeInfo) typeInfo).getAtTime(), 
                                                false, hInfo.getPermissions());
                break;
                case NTIMES:
                NTimesTypeInfo ntimesInfo = (NTimesTypeInfo) typeInfo;
                NTimesHologram ntimes = new NTimesHologram(holo, info.getHoloName(), hInfo.getActivationDistance(),
                                                hInfo.getShowTime(), ntimesInfo.getShowTimes(), 
                                                false, hInfo.getPermissions());
                ntimes.addAllShownTo(ntimesInfo.getShownToTimes());
                hologram = ntimes;
                break;
                case ALWAYS:
                hologram = new AlwaysHologram(holo, info.getHoloName(), hInfo.getActivationDistance(), hInfo.getShowTime(), false, hInfo.getPermissions());
                break;
                default:
                throw new IllegalArgumentException("Unexpected pHD type " + hInfo.getType());
            }
            if (handler == null) {
                handler = new IndividualHologramHandler(holo);
            }
            handler.addHologram(hInfo.getType(), hologram, true);
            
        }
        if (handler != null) {
            addHandler(handler.getName(), handler);
        }
    }

    @Override
    protected boolean saveHolograms(boolean inSync, HologramSaveReason reason) {
        Set<HDHologramInfo> infos = new HashSet<>();
        for (IndividualHologramHandler handler : getHandlers(false)) {
            if (handler.needsSaved()){
                handler.markSaved();
                infos.add(getInfo(handler));
            }
        }
        if (infos.isEmpty()) return false;
        plugin.getLogger().info("in world " + getWorld().getName() + " for reason " + reason.name() + " saving:" + infos); // TODO - remove debug message
        storage.saveHolograms(infos, inSync);
        return !infos.isEmpty();
    }

    private HDHologramInfo getInfo(IndividualHologramHandler handler) {
        HDHologramInfo info = new HDHologramInfo(handler.getName());
        for (PeriodicHologramBase holo : handler.getHolograms()) {
            TypeInfo typeInfo = getTypeInfo(holo);
            HologramInfo hInfo = new HologramInfo(holo.getName(), holo.getType(), holo.getActivationDistance(), 
                                holo.getShowTimeTicks()/20L, holo.getPermissions(), typeInfo);
            info.addInfo(hInfo);
        }
        return info;
    }

    private TypeInfo getTypeInfo(PeriodicHologramBase holo) {
        switch (holo.getType()) {
            case MCTIME:
            return new MCTimeTypeInfo(((MCTimeHologram) holo).getTime());
            case IRLTIME:
            return new IRLTimeTypeInfo(((IRLTimeHologram) holo).getTime());
            case ALWAYS:
            case NTIMES:
            NTimesHologram ntimes = (NTimesHologram) holo;
            return new NTimesTypeInfo(ntimes.getTimesToShow(), ntimes.getShownTo());
            default:
            throw new IllegalArgumentException("Need to specify type of hologram to get type info, got " + holo.getType());
        } 
    }

    void addHologram(PeriodicHologramBase hologram) {
        addHologram(hologram, false);
    }

    void addHologram(PeriodicHologramBase hologram, boolean wasLoaded) {
        Validate.notNull(hologram, "Cannot add null hologram!");
        Validate.isTrue(hologram.getLocation().getWorld() == getWorld(), "Cannot add holograms in a different world!");
        IndividualHologramHandler handler = getHandler(hologram.getName());
        if (handler == null) {
            handler = new IndividualHologramHandler((NamedHologram) hologram.getHologram());
            addHandler(hologram.getName(), handler);
        }
        handler.addHologram(hologram.getType(), hologram, wasLoaded);
    }

    void removeHologram(PeriodicHologramBase hologram) {
        removeHologram(hologram, true);
    }

    void removeHologram(PeriodicHologramBase hologram, boolean markForRemoval) {
        Validate.notNull(hologram, "Cannot remove null hologram!");
        Validate.isTrue(hologram.getLocation().getWorld() == getWorld(), "Cannot remove holograms in a different world!");
        IndividualHologramHandler handler = getHandler(hologram.getName());
        handler.removeHologram(hologram, markForRemoval);
    }
    
}