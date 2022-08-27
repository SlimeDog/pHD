package me.ford.periodicholographicdisplays.mock;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import dev.ratas.slimedogcore.api.config.SDCCustomConfigManager;
import dev.ratas.slimedogcore.api.config.settings.SDCBaseSettings;
import dev.ratas.slimedogcore.api.messaging.recipient.SDCRecipient;
import dev.ratas.slimedogcore.api.reload.SDCReloadManager;
import dev.ratas.slimedogcore.api.scheduler.SDCScheduler;
import dev.ratas.slimedogcore.api.utils.logger.SDCDebugLogger;
import dev.ratas.slimedogcore.api.wrappers.SDCOnlinePlayerProvider;
import dev.ratas.slimedogcore.api.wrappers.SDCPluginInformation;
import dev.ratas.slimedogcore.api.wrappers.SDCPluginManager;
import dev.ratas.slimedogcore.api.wrappers.SDCResourceProvider;
import dev.ratas.slimedogcore.api.wrappers.SDCWorldProvider;
import me.filoghost.holographicdisplays.plugin.internal.hologram.InternalHologram;
import me.ford.periodicholographicdisplays.IPeriodicHolographicDisplays;
import me.ford.periodicholographicdisplays.Messages;
import me.ford.periodicholographicdisplays.PeriodicHolographicDisplays.ReloadIssue;
import me.ford.periodicholographicdisplays.Settings;
import me.ford.periodicholographicdisplays.holograms.HologramStorage;
import me.ford.periodicholographicdisplays.holograms.storage.SQLStorage;
import me.ford.periodicholographicdisplays.holograms.storage.Storage;
import me.ford.periodicholographicdisplays.holograms.wrap.command.CommandWrapper;
import me.ford.periodicholographicdisplays.holograms.wrap.platform.HologramPlatform;
import me.ford.periodicholographicdisplays.holograms.wrap.provider.HologramProvider;
import me.ford.periodicholographicdisplays.holograms.wrap.provider.HolographicDisplaysHologramProvider;
import me.ford.periodicholographicdisplays.hooks.LuckPermsHook;
import me.ford.periodicholographicdisplays.hooks.NPCHook;
import me.ford.periodicholographicdisplays.users.SimpleUserCache;
import me.ford.periodicholographicdisplays.users.UserCache;

/**
 * MockPeriodicHolographicDisplays
 */
public class MockPeriodicHolographicDisplays implements IPeriodicHolographicDisplays {
    private final File sourceFolder = new File("src");
    private final File testFolder = new File(sourceFolder, "test");
    private final File dataFolder = new File(testFolder, "resources");
    private final File configFile = new File(dataFolder, "config.yml");
    private final Logger logger = Logger.getLogger("Mock pHD");
    private final FileConfiguration config;
    private final Messages messages;
    private final Settings settings;
    private final HologramStorage holograms;
    private final UserCache userCache;
    private final MockInternalHologramManager ihm;
    private boolean debug = false;
    public final MockHolographicsDisplaysAPI api;
    private final MockScheduler scheduler;

    public MockPeriodicHolographicDisplays() {
        logger.setLevel(Level.WARNING);
        this.scheduler = new MockScheduler();
        this.ihm = new MockInternalHologramManager(api = new MockHolographicsDisplaysAPI());
        config = YamlConfiguration.loadConfiguration(configFile);
        try {
            messages = new Messages(this);
        } catch (InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }
        settings = new Settings(this);
        userCache = new SimpleUserCache(this);
        try {
            holograms = new HologramStorage(this, new MockPluginManager());
        } catch (InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }
        debug("Starting MOCK pHD");
    }

    @Override
    public File getDataFolder() {
        return dataFolder;
    }

    @Override
    public Logger getLogger() {
        return logger;
    }

    @Override
    public InputStream getResource(String name) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void saveResource(String resourcePath, boolean replace) {
        // TODO Auto-generated method stub
    }

    @Override
    public FileConfiguration getConfig() {
        return config;
    }

    @Override
    public JavaPlugin asPlugin() {
        return null;
    }

    @Override
    public Player getPlayer(UUID id) {
        return null;
    }

    @Override
    public OfflinePlayer getOfflinePlayer(UUID id) {
        return null;
    }

    @Override
    public UserCache getUserCache() {
        return userCache;
    }

    @Override
    public NPCHook getNPCHook() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public LuckPermsHook getLuckPermsHook() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<ReloadIssue> reload() {
        // TODO Auto-generated method stub
        return new ArrayList<>();
    }

    @Override
    public List<ReloadIssue> reloadMyConfig() {
        // TODO Auto-generated method stub
        return new ArrayList<>();
    }

    @Override
    public HologramStorage getHolograms() {
        return holograms;
    }

    @Override
    public Settings getSettings() {
        return settings;
    }

    @Override
    public Messages getMessages() {
        return messages;
    }

    @Override
    public void debug(String message) {
        if (debug)
            logger.info(String.format("[DEBUG] %s", message));
    }

    public void clear() {
        debug = false;
        holograms.getStorage().clear();
        if (configFile.exists()) {
            configFile.delete();
        }
        Storage storage = holograms.getStorage();
        if (storage instanceof SQLStorage) {
            ((SQLStorage) storage).close();
        }
        File hologramsFile = new File(getDataFolder(), "database." + (settings.useDatabase() ? "db" : "yml"));
        if (hologramsFile.exists()) {
            hologramsFile.delete();
        }
        File messagesFile = new File(getDataFolder(), "messages.yml");
        if (messagesFile.exists()) {
            messagesFile.delete();
        }
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    @Override
    public List<World> getWorlds() {
        return new ArrayList<>();
    }

    public void putHDHologram(String name, InternalHologram hologram) {
        ihm.putHDHologram(name, hologram);
    }

    @Override
    public File getWorldContainer() {
        return getDataFolder();
    }

    @Override
    public HologramProvider getHologramProvider() {
        return new HolographicDisplaysHologramProvider(ihm);
    }

    @Override
    public HologramPlatform getHologramPlatform() {
        return new HologramPlatform() {

            @Override
            public HologramProvider getHologramProvider() {
                return MockPeriodicHolographicDisplays.this.getHologramProvider();
            }

            @Override
            public CommandWrapper getHologramCommand() {
                return null;
            }

            @Override
            public String getName() {
                return "MockHologramPlatform";
            }

        };
    }

    @Override
    public SDCBaseSettings getBaseSettings() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public SDCRecipient getConsoleRecipient() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public SDCCustomConfigManager getCustomConfigManager() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public SDCDebugLogger getDebugLogger() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public SDCOnlinePlayerProvider getOnlinePlayerProvider() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public SDCPluginInformation getPluginInformation() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public SDCPluginManager getPluginManager() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public SDCReloadManager getReloadManager() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public SDCResourceProvider getResourceProvider() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public SDCScheduler getScheduler() {
        return scheduler;
    }

    @Override
    public File getWorldFolder() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public SDCWorldProvider getWorldProvider() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void pluginDisabled() {
        // TODO Auto-generated method stub

    }

    @Override
    public void pluginEnabled() {
        // TODO Auto-generated method stub

    }

}