package me.ford.periodicholographicdisplays.holograms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import org.apache.commons.lang.Validate;
import org.bukkit.World;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

import me.ford.periodicholographicdisplays.IPeriodicHolographicDisplays;
import me.ford.periodicholographicdisplays.holograms.WorldHologramStorageBase.HologramSaveReason;
import me.ford.periodicholographicdisplays.holograms.storage.HologramInfo;
import me.ford.periodicholographicdisplays.holograms.storage.SQLStorage;
import me.ford.periodicholographicdisplays.holograms.storage.Storage;
import me.ford.periodicholographicdisplays.holograms.storage.TypeInfo;
import me.ford.periodicholographicdisplays.holograms.storage.YAMLStorage;
import me.ford.periodicholographicdisplays.holograms.storage.Storage.HDHologramInfo;
import me.ford.periodicholographicdisplays.holograms.wrap.WrappedHologram;
import me.ford.periodicholographicdisplays.holograms.wrap.provider.HologramProvider;
import me.ford.periodicholographicdisplays.hooks.NPCHook;

/**
 * HologramStorage
 */
public class HologramStorage {
    private Storage storage;
    private final IPeriodicHolographicDisplays plugin;
    private final HologramProvider provider;
    private final PluginManager pm;
    private final NPCHook hook;
    private final Map<World, WorldHologramStorage> holograms = new HashMap<>();
    private final Set<HDHologramInfo> danglingInfos = new HashSet<>();

    public HologramStorage(IPeriodicHolographicDisplays plugin, PluginManager pm) throws InvalidConfigurationException {
        if (plugin.getSettings().useDatabase()) {
            this.storage = new SQLStorage(plugin, pm);
        } else {
            this.storage = new YAMLStorage(plugin, pm);
        }
        this.plugin = plugin;
        this.provider = plugin.getHologramProvider();
        this.pm = pm;
        hook = plugin.getNPCHook();
        initWorldStorage();
        scheduleLoad();
        scheduleDanglingCheck();
        scheduleSave();
    }

    private void scheduleSave() {
        long seconds = plugin.getSettings().getSaveDelay();
        if (seconds < 20) {
            plugin.getLogger().warning(plugin.getMessages().getLowSaveDelayMessage().createWith(seconds).getFilled());
        }
        long delay = seconds * 20L;
        plugin.getScheduler().runTaskTimer(() -> save(HologramSaveReason.PERIODIC, false), delay,
                delay);
    }

    private void scheduleLoad() {
        long delayTicks = 40L;
        if (plugin.getHologramPlatform().requiresLongerLoadDelay()) {
            delayTicks = 150L;
        }
        plugin.getScheduler().runTaskLater(() -> {
            storage.loadHolograms((info) -> loaded(info, false));
        }, delayTicks); // need to do this later so the holograms are loaded
    }

    private void scheduleDanglingCheck() {
        plugin.getScheduler().runTaskLater(() -> {
            if (!danglingInfos.isEmpty()) {
                plugin.getLogger().warning(
                        "Some pHD holograms were loaded such that they have not found their corresponding hologram:"
                                + danglingInfos);
            }
        }, 200L);
    }

    private void loaded(HDHologramInfo info, boolean imported) {
        danglingInfos.add(info); // removed if not left danlging
        WrappedHologram holo = provider.getByName(info.getHoloName());
        WorldHologramStorage whs = holograms.get(holo.getWorldIfLoaded());
        if (whs == null) {
            plugin.getLogger().info("Loaded hologram before world was initialized: " + holo.getName()
                    + " - it should be sorted out once the world loads");
            return;
        } else {
            danglingInfos.remove(info);
        }
        whs.loaded(holo, info, imported);
    }

    public Storage getStorage() {
        return storage;
    }

    private void initWorldStorage() {
        for (World world : plugin.getWorlds()) {
            newWorld(world);
        }
    }

    public void newWorld(World world) {
        holograms.put(world, new WorldHologramStorage(plugin, pm, world, storage));
        for (HDHologramInfo info : danglingInfos) {
            loaded(info, false); // it'll be removed if it fits
        }
    }

    public Set<World> getActiveWorlds() {
        return new HashSet<>(holograms.keySet());
    }

    public WorldHologramStorage getHolograms(World world) {
        Validate.notNull(world, "Cannot get holograms of a null world!");
        WorldHologramStorage storage = holograms.get(world);
        if (storage == null) {
            storage = new WorldHologramStorage(plugin, pm, world, this.storage);
            holograms.put(world, storage);
        }
        return storage;
    }

    public void reload() throws InvalidConfigurationException {
        danglingInfos.clear();
        for (WorldHologramStorage storage : holograms.values()) {
            for (FlashingHologram hologram : storage.getHolograms()) {
                storage.removeHologram(hologram, false);
            }
        }
        holograms.clear();
        boolean db = plugin.getSettings().useDatabase();
        if (storage instanceof SQLStorage)
            ((SQLStorage) storage).close();
        if (db) {
            storage = new SQLStorage(plugin, pm);
        } else {
            storage = new YAMLStorage(plugin, pm);
        }
        initWorldStorage();
        scheduleLoad();
        scheduleDanglingCheck();
    }

    public void imported(HDHologramInfo info) {
        loaded(info, true);
    }

    // adding

    public void addHologram(FlashingHologram hologram) {
        WorldHologramStorage storage = getHolograms(hologram.getLocation().getWorld());
        storage.addHologram(hologram);
        storage.saveHolograms(false, HologramSaveReason.ADD);
    }

    public void removeHologram(FlashingHologram hologram) {
        Validate.notNull(hologram, "Cannot remove null hologram");
        WorldHologramStorage storage = holograms.get(hologram.getLocation().getWorld());
        storage.removeHologram(hologram);
    }

    // saving

    public void save() {
        save(false);
    }

    public void save(boolean inSync) {
        save(HologramSaveReason.MANUAL, inSync);
    }

    public void save(HologramSaveReason reason, boolean inSync) {
        for (WorldHologramStorage storage : holograms.values()) {
            storage.saveHolograms(inSync, reason);
        }
        if (inSync && storage instanceof SQLStorage) {
            ((SQLStorage) storage).close();
        }
    }

    public List<PeriodicType> getAvailableTypes(String name) {
        return getAvailableTypes(name, false);
    }

    public List<PeriodicType> getAvailableTypes(String name, boolean includeZombies) {
        for (WorldHologramStorage storage : holograms.values()) {
            IndividualHologramHandler handler = storage.getHandler(name);
            if (handler != null) {
                List<PeriodicType> types = new ArrayList<>();
                for (FlashingHologram holo : handler.getHolograms()) {
                    types.add(holo.getType());
                }
                return types;
            }
        }
        if (includeZombies) {
            for (HDHologramInfo info : danglingInfos) {
                if (info.getHoloName().equalsIgnoreCase(name)) {
                    List<PeriodicType> types = new ArrayList<>();
                    for (HologramInfo i : info.getInfos()) {
                        types.add(i.getType());
                    }
                    return types;
                }
            }
        }
        return new ArrayList<>();
    }

    public FlashingHologram getHologram(String name, PeriodicType type) {
        for (WorldHologramStorage storage : holograms.values()) {
            FlashingHologram holo = storage.getHologram(name, type);
            if (holo != null)
                return holo;
        }
        return null;
    }

    public void mcTimeChanged(World world, long amount) {
        WorldHologramStorage storage = holograms.get(world);
        if (storage == null)
            return; // nothing being tracked
        for (FlashingHologram holo : storage.getHolograms()) {
            if (holo.getType() == PeriodicType.MCTIME) {
                ((MCTimeHologram) holo).timeChanged(amount);
            }
        }
    }

    // onJoin holgorams

    public void joined(Player player) {
        joinedWorld(player, player.getWorld());
    }

    public void joinedWorld(Player player, World world) {
        if (hook != null && hook.isNPC(player))
            return;
        WorldHologramStorage worldStorage = getHolograms(world);
        for (FlashingHologram holo : worldStorage.getHolograms()) {
            if (holo.getType() == PeriodicType.ALWAYS) {
                AlwaysHologram always = (AlwaysHologram) holo;
                if (always.isShownOnWorldJoin()) {
                    always.attemptToShow(player);
                }
            }
        }
    }

    public void left(Player player) { // left server
        leftWorld(player, player.getWorld());
    }

    public void leftWorld(Player player, World world) {
        WorldHologramStorage worldStorage = getHolograms(world);
        for (FlashingHologram holo : worldStorage.getHolograms()) {
            if (holo.getType() == PeriodicType.ALWAYS) {
                AlwaysHologram always = (AlwaysHologram) holo;
                if (always.isShownOnWorldJoin()) {
                    always.hideFrom(player);
                }
            }
        }
    }

    public List<String> getNames(boolean withZombies) {
        List<String> names = getNames();
        if (withZombies) {
            for (HDHologramInfo info : danglingInfos) {
                names.add(info.getHoloName());
            }
        }
        return names;
    }

    public List<String> getNames() {
        return getNames(null);
    }

    public List<String> getNames(PeriodicType type) {
        List<String> names = new ArrayList<>();
        for (WorldHologramStorage storage : holograms.values()) {
            for (IndividualHologramHandler holo : storage.getHandlers(false)) {
                if (type == null || holo.getHologram(type) != null) {
                    names.add(holo.getName());
                }
            }
        }
        return names;
    }

    // zombies
    public boolean hasZombies() {
        return !danglingInfos.isEmpty();
    }

    public Set<HDHologramInfo> getZombies() {
        return new HashSet<>(danglingInfos);
    }

    public void checkForZombies() {
        List<IndividualHologramHandler> toZombie = new ArrayList<>();
        for (WorldHologramStorage storage : holograms.values()) {
            for (IndividualHologramHandler handler : storage.getHandlers(false)) {
                if (handler.getHologram().isDeleted()) { // DEPRECATED
                    plugin.debug("The hologram '" + handler.getName()
                            + "' has been detected as being deleted and thus all its pHD handlers will now be zombified");
                    toZombie.add(handler);
                }
            }
        }
        for (IndividualHologramHandler handler : toZombie) {
            handler.setAllNeedingSaved();
            WorldHologramStorage storage = getHolograms(handler.getHologram().getBukkitLocation().getWorld());
            danglingInfos.add(storage.getInfo(handler));
            for (FlashingHologram hologram : handler.getHolograms()) {
                removeHologram(hologram);
            }
        }
    }

    public void removeZombie(HologramInfo info) {
        HDHologramInfo parent = null;
        for (HDHologramInfo hdInfo : danglingInfos) {
            if (hdInfo.getHoloName().equals(info.getName())) {
                parent = hdInfo;
                break;
            }
        }
        if (parent == null) {
            plugin.getLogger().log(Level.WARNING,
                    "Attempting to remove zombie but did not find it in the list(1):" + info);
            return;
        }
        if (parent.removeInfo(info)) {
            HDHologramInfo delInfo = new HDHologramInfo(info.getName());
            TypeInfo ti = TypeInfo.of(info.getType(), null);
            HologramInfo holoInfo = new HologramInfo(delInfo.getHoloName(), info.getType(), -1.0, -1, null, ti, -1.0,
                    -1.0);
            delInfo.addInfo(holoInfo);
            Set<HDHologramInfo> set = new HashSet<>();
            set.add(delInfo);
            plugin.debug("Removing 'zombie' hologram: " + info);
            storage.saveHolograms(set, false);
            if (parent.getInfos().isEmpty())
                danglingInfos.remove(parent);
        } else {
            plugin.getLogger().log(Level.WARNING,
                    "Attempting to remove zombie but did not find it in the list(2):" + info);
        }
    }

}