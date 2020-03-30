package me.ford.periodicholographicdisplays.holograms.storage;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.logging.Level;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import me.ford.periodicholographicdisplays.PeriodicHolographicDisplays;
import me.ford.periodicholographicdisplays.holograms.FlashingHologram;
import me.ford.periodicholographicdisplays.holograms.PeriodicHologramBase;
import me.ford.periodicholographicdisplays.holograms.PeriodicType;
import me.ford.periodicholographicdisplays.holograms.events.HologramsLoadedEvent;
import me.ford.periodicholographicdisplays.holograms.storage.TypeInfo.IRLTimeTypeInfo;
import me.ford.periodicholographicdisplays.holograms.storage.TypeInfo.MCTimeTypeInfo;
import me.ford.periodicholographicdisplays.holograms.storage.TypeInfo.NTimesTypeInfo;
import me.ford.periodicholographicdisplays.holograms.storage.TypeInfo.NullTypeInfo;

/**
 * YAMLStorage
 */
public class YAMLStorage implements Storage {
    private final String fileName = "database.yml";
    private final File storageFile;
    private final PeriodicHolographicDisplays phd;
    private FileConfiguration storage;

    public YAMLStorage() {
        phd = JavaPlugin.getPlugin(PeriodicHolographicDisplays.class);
        storageFile = new File(this.phd.getDataFolder(), fileName);
    }

    @Override
    public void saveHolograms(Set<HDHologramInfo> holograms, boolean inSync) {
        for (HDHologramInfo hdHoloInfo : holograms) {
            ConfigurationSection nameSection = getConfig().getConfigurationSection(hdHoloInfo.getHoloName());
            if (nameSection == null)
                nameSection = getConfig().createSection(hdHoloInfo.getHoloName());
            for (HologramInfo info : hdHoloInfo.getInfos()) {
                saveInfo(nameSection.createSection(info.getType().name()), info);
            }
        }
        save();
    }

    private void saveInfo(ConfigurationSection section, HologramInfo info) {
        TypeInfo typeInfo = info.getTypeInfo();
        if (typeInfo instanceof NullTypeInfo) { // just created the section - therefore I can just return
            return;
        }
        section.set("type", info.getType().name());
        if (info.getActivationDistance() != PeriodicHologramBase.NO_DISTANCE) {
            section.set("activation-distance", info.getActivationDistance());
        }
        if (info.getShowTime() != PeriodicHologramBase.NO_SECONDS) {
            section.set("show-time", info.getShowTime());
        }
        if (info.getFlashOn() != FlashingHologram.NO_FLASH && info.getFlashOff() != FlashingHologram.NO_FLASH) {
            section.set("flash-on", info.getFlashOn());
            section.set("flash-off", info.getFlashOff());
        }
        section.set("permission", info.getPermissions());
        if (typeInfo.getType() == PeriodicType.NTIMES) {
            NTimesTypeInfo ntimes = (NTimesTypeInfo) typeInfo;
            section.set("times-to-show", ntimes.getShowTimes());
            ConfigurationSection shownToSection = section.getConfigurationSection("shown-to");
            if (shownToSection == null) shownToSection = section.createSection("shown-to");
            for (Map.Entry<UUID, Integer> entry : ntimes.getShownToTimes().entrySet()) {
                int value = entry.getValue();
                shownToSection.set(entry.getKey().toString(), value == 0 ? null : value);
            }
        } else if (typeInfo instanceof MCTimeTypeInfo) {
            MCTimeTypeInfo mctime = (MCTimeTypeInfo) typeInfo;
            section.set("show-at", mctime.getAtTime());
        } else if (typeInfo instanceof IRLTimeTypeInfo) {
            IRLTimeTypeInfo irltime = (IRLTimeTypeInfo) typeInfo;
            section.set("show-at", irltime.getAtTime());
        }
    }

    @Override
    public void loadHolograms(Consumer<HDHologramInfo> consumer) {
        for (String name : getConfig().getKeys(false)) {
            loadHologram(name, consumer);
        }
        phd.getServer().getPluginManager().callEvent(new HologramsLoadedEvent());
    }

    private void loadHologram(String name, Consumer<HDHologramInfo> consumer) {
        ConfigurationSection section = getConfig().getConfigurationSection(name);
        HDHologramInfo info = new HDHologramInfo(name);
        for (String typeStr : section.getKeys(false)) {
            HologramInfo holo;
            try {
                holo = loadType(name, section.getConfigurationSection(typeStr));
            } catch (HologramLoadException e) {
                phd.getLogger().log(Level.WARNING,
                        "Problem loading hologram of type " + typeStr + " from file for hologram " + name, e);
                continue;
            }
            info.addInfo(holo);
        }
        consumer.accept(info);
    }

    private HologramInfo loadType(String name, ConfigurationSection section) throws HologramLoadException {
        if (section == null) {
            throw new HologramLoadException(
                    "Unable to parse hologram because of incorrect config (using the old system?): " + name);
        }
        PeriodicType type;
        try {
            type = PeriodicType.valueOf(section.getName());
        } catch (IllegalArgumentException e) {
            throw new HologramLoadException("Unable to parse type of hologram: " + section.getName());
        }
        double distance = section.getDouble("activation-distance", PeriodicHologramBase.NO_DISTANCE);
        long showTime = section.getLong("show-time", PeriodicHologramBase.NO_SECONDS);
        String perms = section.getString("permission"); // defaults to null
        double flashOn = section.getDouble("flash-on", FlashingHologram.NO_FLASH);
        double flashOff = section.getDouble("flash-off", FlashingHologram.NO_FLASH);
        final TypeInfo typeInfo;
        switch (type) {
            case IRLTIME:
                long atTime = section.getLong("show-at", 0); // seconds from 00:00
                typeInfo = new IRLTimeTypeInfo(atTime);
                break;
            case MCTIME:
                long time = section.getLong("show-at", 0);
                typeInfo = new MCTimeTypeInfo(time);
                break;
            case ALWAYS:
            case NTIMES:
                int timesToShow;
                if (type == PeriodicType.ALWAYS) {
                    timesToShow = -1;
                } else {
                    timesToShow = section.getInt("times-to-show", -1);
                }
                typeInfo = new NTimesTypeInfo(timesToShow,
                        getShownToTimes(section.getConfigurationSection("shown-to")));
                break;
            default:
                phd.getLogger().info("Undefined loading behavour with type: " + type);
                return null;
        }
        return new HologramInfo(name, type, distance, showTime, perms, typeInfo, flashOn, flashOff);
    }

    private Map<UUID, Integer> getShownToTimes(ConfigurationSection section) {
        Map<UUID, Integer> map = new HashMap<>();
        if (section == null)
            return map;
        for (String uuid : section.getKeys(false)) {
            UUID id;
            try {
                id = UUID.fromString(uuid);
            } catch (IllegalArgumentException e) {
                phd.getLogger().warning(
                        "Unable to parse UUID of Periodic hologram " + section.getCurrentPath() + " : " + uuid);
                continue;
            }
            map.put(id, section.getInt(uuid));
        }
        return map;
    }

    public void reload() {
        storage = YamlConfiguration.loadConfiguration(storageFile);
    }

    @Override
    public boolean hasData() {
        return !getConfig().getKeys(true).isEmpty();
    }

    @Override
    public void clear() {
        for (String key : storage.getKeys(false)) {
            storage.set(key, null);
        }
        save();
    }

    protected FileConfiguration getConfig() {
        if (storage == null) {
            reload();
        }
        return storage;
    }

    public void save() {
        if (storage == null || storageFile == null) {
            return;
        }
        try {
            getConfig().save(storageFile);
        } catch (IOException ex) {
            phd.getLogger().log(Level.SEVERE, "Could not save config to " + storageFile, ex);
        }
    }

}