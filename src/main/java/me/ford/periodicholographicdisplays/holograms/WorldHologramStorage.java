package me.ford.periodicholographicdisplays.holograms;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import com.gmail.filoghost.holographicdisplays.object.NamedHologram;

import org.apache.commons.lang.Validate;
import org.bukkit.World;
import org.bukkit.entity.Player;

import me.ford.periodicholographicdisplays.PeriodicHolographicDisplays;
import me.ford.periodicholographicdisplays.holograms.storage.HologramInfo;
import me.ford.periodicholographicdisplays.holograms.storage.Storage;
import me.ford.periodicholographicdisplays.holograms.storage.TypeInfo;
import me.ford.periodicholographicdisplays.holograms.storage.TypeInfo.NullTypeInfo;
import me.ford.periodicholographicdisplays.hooks.NPCHook;
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
    private final NPCHook hook;

    public WorldHologramStorage(PeriodicHolographicDisplays plugin, World world, Storage storage) {
        super(plugin, world);
        this.plugin = plugin;
        this.storage = storage;
        this.hook = plugin.getNPCHook();
    }

    public PeriodicHologramBase getHologram(String name, PeriodicType type) {
        IndividualHologramHandler handler = getHandler(name);
        if (handler == null)
            return null;
        return handler.getHologram(type);
    }

    public List<PeriodicHologramBase> getHolograms() {
        return getHolograms(false);
    }

    public List<PeriodicHologramBase> getHolograms(boolean onlyLoaded) {
        List<PeriodicHologramBase> holos = new ArrayList<>();
        for (IndividualHologramHandler handler : getHandlers(onlyLoaded)) {
            holos.addAll(handler.getHolograms());
        }
        return holos;
    }

    public List<String> getHologramNames() {
        List<String> names = new ArrayList<>();
        for (IndividualHologramHandler handler : getHandlers(false)) {
            names.add(handler.getName());
        }
        return names;
    }

    public void loaded(NamedHologram holo, HDHologramInfo info, boolean imported) {
        if (holo.getWorld() != getWorld()) return; // don't load
        IndividualHologramHandler handler = null;
        for (HologramInfo hInfo : info.getInfos()) {
            PeriodicHologramBase hologram;
            TypeInfo typeInfo = hInfo.getTypeInfo();
            double distance = hInfo.getActivationDistance();
            long seconds = hInfo.getShowTime();
            switch (hInfo.getType()) {
                case MCTIME:
                hologram = new MCTimeHologram(holo, info.getHoloName(), distance, 
                                                seconds, ((MCTimeTypeInfo) typeInfo).getAtTime(), 
                                                false, hInfo.getPermissions());
                break;
                case IRLTIME:
                hologram = new IRLTimeHologram(holo, info.getHoloName(), distance,
                                                seconds, ((IRLTimeTypeInfo) typeInfo).getAtTime(), 
                                                false, hInfo.getPermissions());
                break;
                case NTIMES:
                NTimesTypeInfo ntimesInfo = (NTimesTypeInfo) typeInfo;
                NTimesHologram ntimes = new NTimesHologram(holo, info.getHoloName(), distance,
                                                seconds, ntimesInfo.getShowTimes(), 
                                                false, hInfo.getPermissions());
                ntimes.addAllShownTo(ntimesInfo.getShownToTimes());
                hologram = ntimes;
                break;
                case ALWAYS:
                hologram = new AlwaysHologram(holo, info.getHoloName(), distance, seconds, false, hInfo.getPermissions());
                break;
                default:
                throw new IllegalArgumentException("Unexpected pHD type " + hInfo.getType());
            }
            if (handler == null) {
                handler = new IndividualHologramHandler(holo);
            }
            handler.addHologram(hInfo.getType(), hologram, !imported);
            
        }
        plugin.debug("Loaded pHD " + handler.getName() + " with types " + handler.getTypes() + " in " + getWorld().getName());
        if (handler != null) {
            addHandler(handler.getName(), handler);
        }
    }

    public void resetAlwaysHologramPermissions(Player player) {
        if (hook != null && hook.isNPC(player)) return;
        for (PeriodicHologramBase holo : getHolograms()) {
            if (holo instanceof AlwaysHologram) {
                AlwaysHologram always = (AlwaysHologram) holo;
                if (!always.hasActivationDistance()) {
                    if (always.canSee(player)) {
                        always.attemptToShow(player);
                    } else {
                        always.hideFrom(player);
                    }
                }
            }
        }
    }

    @Override
    protected boolean saveHolograms(boolean inSync, HologramSaveReason reason) {
        Set<HDHologramInfo> infos = new HashSet<>();
        for (IndividualHologramHandler handler : getHandlers(false)) {
            if (handler.needsSaved()){
                infos.add(getInfo(handler));
                handler.markSaved();
            }
        }
        if (infos.isEmpty()) return false;
        if (plugin.getSettings().onDebug()) {
            plugin.debug("in world " + getWorld().getName() + " for reason " + reason.name() + " saving:");
            for (HDHologramInfo info : infos) {
                plugin.debug("" + info);
            }
        }
        storage.saveHolograms(infos, inSync);
        return !infos.isEmpty();
    }

    private HDHologramInfo getInfo(IndividualHologramHandler handler) {
        HDHologramInfo info = new HDHologramInfo(handler.getName());
        for (Entry<PeriodicType, PeriodicHologramBase> entry : handler.getToSave().entrySet()) {
            double distance = -1;
            long seconds = -1;
            String perms = null;
            PeriodicType type = entry.getKey();
            PeriodicHologramBase holo = entry.getValue();
            TypeInfo typeInfo = getTypeInfo(type, holo);
            if (!(typeInfo instanceof NullTypeInfo)) {
                distance = holo.getActivationDistance();
                seconds = holo.getShowTime();
                perms = holo.getPermissions();
            }
            HologramInfo hInfo = new HologramInfo(handler.getName(), type, distance, 
                                seconds, perms, typeInfo);
            info.addInfo(hInfo);
        }
        return info;
    }

    private TypeInfo getTypeInfo(PeriodicType type, PeriodicHologramBase holo) {
        if (holo == null) {
            return new NullTypeInfo(type);
        }
        switch (type) {
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
        if (handler.isEmpty()) {
            saveHolograms(false, HologramSaveReason.REMOVE);
            removeHandler(hologram.getName());
        }
    }
    
}