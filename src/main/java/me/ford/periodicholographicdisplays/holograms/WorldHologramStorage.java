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

import me.ford.periodicholographicdisplays.IPeriodicHolographicDisplays;
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
    private final IPeriodicHolographicDisplays plugin;
    private final Storage storage;
    private final NPCHook hook;

    public WorldHologramStorage(IPeriodicHolographicDisplays plugin, World world, Storage storage) {
        super(plugin, world);
        this.plugin = plugin;
        this.storage = storage;
        this.hook = plugin.getNPCHook();
    }

    public FlashingHologram getHologram(String name, PeriodicType type) {
        IndividualHologramHandler handler = getHandler(name);
        if (handler == null)
            return null;
        return handler.getHologram(type);
    }

    public List<FlashingHologram> getHolograms() {
        return getHolograms(false);
    }

    public List<FlashingHologram> getHolograms(boolean onlyLoaded) {
        List<FlashingHologram> holos = new ArrayList<>();
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
        if (holo.getWorld() != getWorld())
            return; // don't load
        IndividualHologramHandler handler = null;
        for (HologramInfo hInfo : info.getInfos()) {
            FlashingHologram hologram;
            TypeInfo typeInfo = hInfo.getTypeInfo();
            double distance = hInfo.getActivationDistance();
            long seconds = hInfo.getShowTime();
            double flashOn = hInfo.getFlashOn();
            double flashOff = hInfo.getFlashOff();
            switch (hInfo.getType()) {
                case MCTIME:
                    hologram = new MCTimeHologram(plugin, holo, info.getHoloName(), distance, seconds,
                            ((MCTimeTypeInfo) typeInfo).getAtTime(), false, hInfo.getPermissions(), flashOn, flashOff);
                    break;
                case IRLTIME:
                    hologram = new IRLTimeHologram(plugin, holo, info.getHoloName(), distance, seconds,
                            ((IRLTimeTypeInfo) typeInfo).getAtTime(), false, hInfo.getPermissions(), flashOn, flashOff);
                    break;
                case NTIMES:
                    NTimesTypeInfo ntimesInfo = (NTimesTypeInfo) typeInfo;
                    NTimesHologram ntimes = new NTimesHologram(plugin, holo, info.getHoloName(), distance, seconds,
                            ntimesInfo.getShowTimes(), false, hInfo.getPermissions(), flashOn, flashOff);
                    ntimes.addAllShownTo(ntimesInfo.getShownToTimes());
                    hologram = ntimes;
                    break;
                case ALWAYS:
                    hologram = new AlwaysHologram(plugin, holo, info.getHoloName(), distance, seconds, false,
                            hInfo.getPermissions(), flashOn, flashOff);
                    break;
                default:
                    throw new IllegalArgumentException("Unexpected pHD type " + hInfo.getType());
            }
            if (handler == null) {
                handler = new IndividualHologramHandler(holo);
            }
            handler.addHologram(hInfo.getType(), hologram, !imported);

        }
        plugin.debug("Loaded pHD " + handler.getName() + " with types " + handler.getTypes() + " in "
                + getWorld().getName());
        if (handler != null) {
            addHandler(handler.getName(), handler);
        }
    }

    public void resetAlwaysHologramPermissions(Player player) {
        if (hook != null && hook.isNPC(player))
            return;
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
            if (handler.needsSaved()) {
                infos.add(getInfo(handler));
                handler.markSaved();
            }
        }
        if (infos.isEmpty())
            return false;
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
        for (Entry<PeriodicType, FlashingHologram> entry : handler.getToSave().entrySet()) {
            double distance = PeriodicHologramBase.NO_DISTANCE;
            long seconds = PeriodicHologramBase.NO_SECONDS;
            double flashOn = FlashingHologram.NO_FLASH;
            double flashOff = FlashingHologram.NO_FLASH;
            String perms = null;
            PeriodicType type = entry.getKey();
            FlashingHologram holo = entry.getValue();
            TypeInfo typeInfo = TypeInfo.of(type, holo);
            if (!(typeInfo instanceof NullTypeInfo)) {
                distance = holo.getActivationDistance();
                seconds = holo.getShowTime();
                perms = holo.getPermissions();
                flashOn = holo.getFlashOn();
                flashOff = holo.getFlashOff();
            }
            HologramInfo hInfo = new HologramInfo(handler.getName(), type, distance, seconds, perms, typeInfo, flashOn,
                    flashOff);
            info.addInfo(hInfo);
        }
        return info;
    }

    void addHologram(FlashingHologram hologram) {
        addHologram(hologram, false);
    }

    void addHologram(FlashingHologram hologram, boolean wasLoaded) {
        Validate.notNull(hologram, "Cannot add null hologram!");
        Validate.isTrue(hologram.getLocation().getWorld() == getWorld(), "Cannot add holograms in a different world!");
        IndividualHologramHandler handler = getHandler(hologram.getName());
        if (handler == null) {
            handler = new IndividualHologramHandler((NamedHologram) hologram.getHologram());
            addHandler(hologram.getName(), handler);
        }
        handler.addHologram(hologram.getType(), hologram, wasLoaded);
    }

    void removeHologram(FlashingHologram hologram) {
        removeHologram(hologram, true);
    }

    void removeHologram(FlashingHologram hologram, boolean markForRemoval) {
        Validate.notNull(hologram, "Cannot remove null hologram!");
        Validate.isTrue(hologram.getLocation().getWorld() == getWorld(),
                "Cannot remove holograms in a different world!");
        IndividualHologramHandler handler = getHandler(hologram.getName());
        handler.removeHologram(hologram, markForRemoval);
        saveHolograms(false, HologramSaveReason.REMOVE);
        if (handler.isEmpty()) {
            removeHandler(hologram.getName());
        }
    }

}