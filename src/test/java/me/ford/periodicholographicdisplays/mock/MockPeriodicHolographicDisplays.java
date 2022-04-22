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
import org.bukkit.scheduler.BukkitTask;

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
    private final MockLineTrackerManager ltm;
    private final MockInternalHologramManager ihm;
    private boolean debug = false;

    public MockPeriodicHolographicDisplays(MockLineTrackerManager ltm) {
        logger.setLevel(Level.WARNING);
        this.ltm = ltm;
        this.ihm = new MockInternalHologramManager(this.ltm);
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

    @Override
    public BukkitTask runTask(Runnable runnable) {
        // TODO Auto-generated method stub
        debug("Usually would run this a tick later, but just running now ...");
        return runNow(runnable);
    }

    @Override
    public BukkitTask runTaskLater(Runnable runnable, long delay) {
        // TODO Auto-generated method stub
        debug("Was supposed to run task later, but just running now ...");
        return runNow(runnable);
    }

    @Override
    public BukkitTask runTaskTimer(Runnable runnable, long delay, long period) {
        // TODO Auto-generated method stub
        debug("Was supposed to run task timer, but just running now ...");
        return runNow(runnable);
    }

    @Override
    public BukkitTask runTaskAsynchronously(Runnable runnable) {
        // TODO Auto-generated method stub
        debug("Was supposed to run task async, but just running in sync ...");
        return runNow(runnable);
    }

    @Override
    public BukkitTask runTaskLaterAsynchronously(Runnable runnable, long delay) {
        // TODO Auto-generated method stub
        debug("Was supposed to run task later async, but just running now ...");
        return runNow(runnable);
    }

    @Override
    public BukkitTask runTaskTimerAsynchronously(Runnable runnable, long delay, long period) {
        // TODO Auto-generated method stub
        debug("Was supposed to run task timer async, but just running now ...");
        return runNow(runnable);
    }

    private MockBukkitTask runNow(Runnable runnable) {
        try {
            runnable.run();
        } catch (Throwable e) {
            if (e instanceof AssertionError)
                throw e;
            getLogger().log(Level.SEVERE, "Unexpected error while running task:", e);
        }
        return new MockBukkitTask();
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

}