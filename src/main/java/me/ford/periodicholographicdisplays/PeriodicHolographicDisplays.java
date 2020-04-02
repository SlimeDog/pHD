package me.ford.periodicholographicdisplays;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bstats.bukkit.Metrics;
import org.bukkit.World;

import me.ford.periodicholographicdisplays.Settings.SettingIssue;
import me.ford.periodicholographicdisplays.Settings.StorageTypeException;
import me.ford.periodicholographicdisplays.commands.PHDCommand;
import me.ford.periodicholographicdisplays.holograms.HologramStorage;
import me.ford.periodicholographicdisplays.hooks.DummyNPCHook;
import me.ford.periodicholographicdisplays.hooks.LuckPermsHook;
import me.ford.periodicholographicdisplays.hooks.NPCHook;
import me.ford.periodicholographicdisplays.hooks.SimpleCitizensHook;
import me.ford.periodicholographicdisplays.listeners.HologramListener;
import me.ford.periodicholographicdisplays.listeners.JoinLeaveListener;
import me.ford.periodicholographicdisplays.listeners.SimpleWorldTimeListener;
import me.ford.periodicholographicdisplays.listeners.WorldListener;
import me.ford.periodicholographicdisplays.listeners.WorldTimeListener;
import me.ford.periodicholographicdisplays.listeners.legacy.LegacyWorldTimeListener;
import me.ford.periodicholographicdisplays.users.SQLUserStorage;
import me.ford.periodicholographicdisplays.users.UserStorage;
import me.ford.periodicholographicdisplays.users.YamlUserStorage;

/**
 * PeriodicHolographicDisplays
 */
public class PeriodicHolographicDisplays extends SchedulingPeriodicHolographicDisplays {
    private HologramStorage holograms;
    private Settings settings;
    private Messages messages;
    private LuckPermsHook lpHook;
    private NPCHook citizensHook = null;
    private UserStorage userStorage;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        messages = new Messages(this);
        settings = new Settings(this);
        holograms = new HologramStorage(this, getServer().getPluginManager());

        // user storage and cache
        if (settings.useDatabase()) {
            userStorage = new SQLUserStorage(this);
        } else {
            userStorage = new YamlUserStorage(this);
        }
        // checking cache a few ticks later - if empty, then populate
        scheduleCacheSizeCheck();

        // check messages
        try {
            messages.getConfig();
        } catch (IllegalStateException e) {
            getLogger().severe(messages.getDisablingMessage());
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // LuckPerms hook if possible
        try {
            lpHook = new LuckPermsHook(this);
        } catch (IllegalStateException | NoClassDefFoundError e) {
            getLogger().warning(messages.getNoLPMessage());
        }
        // Citizens hoook if possible
        try {
            citizensHook = new SimpleCitizensHook();
        } catch (ClassNotFoundException | NoClassDefFoundError e) {
            citizensHook = new DummyNPCHook();
        }

        // listeners
        this.getServer().getPluginManager().registerEvents(new HologramListener(holograms, citizensHook), this);
        this.getServer().getPluginManager().registerEvents(new JoinLeaveListener(holograms, userStorage), this);
        this.getServer().getPluginManager().registerEvents(new WorldListener(holograms), this);
        WorldTimeListener worldTimeListener;
        if (getServer().getBukkitVersion().contains("1.15")) {
            worldTimeListener = new SimpleWorldTimeListener(holograms);
        } else {
            getLogger().warning(messages.getLegacyMessage());
            worldTimeListener = new LegacyWorldTimeListener(holograms);
        }
        this.getServer().getPluginManager().registerEvents(worldTimeListener, this);

        // metrics
        if (settings.enableMetrics()) {
            new Metrics(this);
        }

        // commands
        getCommand("phd").setExecutor(new PHDCommand(this));

        // settings check
        List<ReloadIssue> issues = getSettingIssues();
        if (!issues.isEmpty()) {
            getLogger().severe(messages.getProblemsReloadingConfigMessage(issues));
            getLogger().severe(messages.getDisablingMessage());
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        if (settings.checkForUpdates()) {
            // TODO - check for updates
        }
        getLogger().info(messages.getActiveStorageMessage(getSettings().useDatabase()));
    }

    @Override
    public List<World> getWorlds() {
        return getServer().getWorlds();
    }
    
    private void scheduleCacheSizeCheck() {
        getServer().getScheduler().runTaskLater(this, () -> {
            if (userStorage.getCache().isEmpty()) {
                getLogger().info("Populating UUID to name cache with all players");
                userStorage.populate();
            }
        }, 20L);
    }

    @Override
    public UserStorage getUserStorage() {
        return userStorage;
    }

    @Override
    public NPCHook getNPCHook() {
        return citizensHook;
    }

    @Override
    public LuckPermsHook getLuckPermsHook() {
        return lpHook;
    }

    @Override
    public List<ReloadIssue> reload() {
        List<ReloadIssue> issues = new ArrayList<>();
        File df = getDataFolder();
        if (!df.exists() || !df.canRead()) {
            getLogger().warning(messages.getNoPluginFolderMessage());
            boolean wasCreated = df.mkdir();
            DefaultReloadIssue dri = DefaultReloadIssue.NO_FOLDER;
            dri.setExtra(String.valueOf(wasCreated));
            issues.add(dri);
        }
        File configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            issues.add(DefaultReloadIssue.NO_CONFIG);
            saveDefaultConfig();
        }
        messages.getConfigReloadedMessage(); // to make sure the configFile is not null
        File messagesFile = messages.getFile();
        if (!messagesFile.exists()) {
            messages.saveDefaultConfig();
            issues.add(DefaultReloadIssue.NO_MESSAGES);
        }
        if (!messages.reloadConfig()) {
            issues.add(new SimpleReloadIssue(messages.getIncorrectMessages(), null));
            getServer().getScheduler().runTask(this, () -> {
                getLogger().severe(messages.getDisablingMessage());
                getServer().getPluginManager().disablePlugin(this);
            });
        }
        List<ReloadIssue> settingIssues = getSettingIssues();
        if (!settingIssues.isEmpty()) {
            getServer().getScheduler().runTask(this, () -> {
                getLogger().severe(messages.getDisablingMessage());
                getServer().getPluginManager().disablePlugin(this);
            });
        }
        issues.addAll(settingIssues);
        holograms.reload();
        // reload UUID cache
        if (settings.useDatabase()) { // since the connection is shared, the connection of the old one is closed
            userStorage = new SQLUserStorage(this);
        } else {
            userStorage = new YamlUserStorage(this);
        }
        scheduleCacheSizeCheck();
        // commands (because userStorage instance might have changed)
        getCommand("phd").setExecutor(new PHDCommand(this));
        return issues;
    }

    private List<ReloadIssue> getSettingIssues() {
        List<ReloadIssue> issues = new ArrayList<>();
        Map<SettingIssue, String> settingIssues = null;
        try {
            settingIssues = reloadMyConfig();
        } catch (StorageTypeException e) {
            DefaultReloadIssue issue = DefaultReloadIssue.ILLEGA_STORAGE_TYPE;
            issue.setExtra(e.getType());
            issues.add(issue);
            settings.setDefaultDatabaseInternal();
        }
        if (settingIssues != null && !settingIssues.isEmpty()) {
            for (Entry<SettingIssue, String> entry : settingIssues.entrySet()) {
                if (entry.getKey() == SettingIssue.STORAGE) {
                    DefaultReloadIssue issue = DefaultReloadIssue.ILLEGA_STORAGE_TYPE;
                    issue.setExtra(entry.getValue());
                    issues.add(issue);
                } else {
                    issues.add(new SimpleReloadIssue(
                            messages.getProblemWithConfigMessage(entry.getKey(), entry.getValue()), null));
                }
            }
        }
        return issues;
    }

    @Override
    public Map<SettingIssue, String> reloadMyConfig() {
        super.reloadConfig();
        if (settings != null) {
            return settings.reload();
        }
        return null;
    }

    @Override
    public void onDisable() {
        holograms.save(true);
        userStorage.save(true);
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
        if (settings.onDebug()) {
            getLogger().info("[DEBUG] " + message);
        }
    }

    public static enum DefaultReloadIssue implements ReloadIssue {
        NONE(null), NO_FOLDER("folder had to be recreated!"), NO_CONFIG("the config had to be recreated"),
        NO_MESSAGES("the messages file had to be recreated"), ILLEGA_STORAGE_TYPE("storage type not understood");
        ;

        private final String issue;
        private String extra = null;

        private DefaultReloadIssue(String issue) {
            this.issue = issue;
        }

        @Override
        public String getIssue() {
            return issue;
        }

        @Override
        public String getExtra() {
            return extra;
        }

        public void setExtra(String extra) {
            this.extra = extra;
        }

    }

    public static class SimpleReloadIssue implements ReloadIssue {
        private final String issue;
        private final String extra;

        public SimpleReloadIssue(String issue, String extra) {
            this.issue = issue;
            this.extra = extra;
        }

        @Override
        public String getIssue() {
            return issue;
        }

        @Override
        public String getExtra() {
            return extra;
        }

    }

    public static interface ReloadIssue {

        public String getIssue();

        public String getExtra();

    }

}