package me.ford.periodicholographicdisplays;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bstats.bukkit.Metrics;
import org.bstats.bukkit.Metrics.SimplePie;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;

import me.ford.periodicholographicdisplays.Settings.SettingIssue;
import me.ford.periodicholographicdisplays.Settings.StorageTypeException;
import me.ford.periodicholographicdisplays.commands.PHDCommand;
import me.ford.periodicholographicdisplays.holograms.HologramStorage;
import me.ford.periodicholographicdisplays.holograms.Zombificator;
import me.ford.periodicholographicdisplays.holograms.wrap.platform.HologramPlatform;
import me.ford.periodicholographicdisplays.holograms.wrap.platform.PlatformProvider;
import me.ford.periodicholographicdisplays.holograms.wrap.provider.HologramProvider;
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
import me.ford.periodicholographicdisplays.storage.yaml.CustomConfigHandler;
import me.ford.periodicholographicdisplays.users.SimpleUserCache;
import me.ford.periodicholographicdisplays.users.UserCache;

/**
 * PeriodicHolographicDisplays
 */
public class PeriodicHolographicDisplays extends AbstractPeriodicHolographicDisplays {
    private HologramPlatform platform;
    private HologramStorage holograms;
    private Settings settings;
    private Messages messages;
    private LuckPermsHook lpHook;
    private NPCHook citizensHook = null;
    private UserCache userCache;
    private PHDCommand command;
    private CustomConfigHandler config;

    @Override
    public void onEnable() {
        List<ReloadIssue> issues = new ArrayList<>();
        try {
            config = new CustomConfigHandler(this, "config.yml");
        } catch (InvalidConfigurationException e1) {
            issues.add(DefaultReloadIssue.INVALID_CONFIGURATION);
            disableMe(issues);
            return;
        }
        try {
            messages = new Messages(this);
        } catch (InvalidConfigurationException e1) {
            issues.add(DefaultReloadIssue.INVALID_MESSAGES);
            disableMe(issues);
            return;
        }
        settings = new Settings(this);

        // settings check
        issues.addAll(getSettingIssues());
        if (!issues.isEmpty()) {
            disableMe(issues);
            return;
        }

        // setup HD hook
        platform = new PlatformProvider(this).getHologramProvider();
        if (platform == null) {
            issues.add(DefaultReloadIssue.NO_PLATFORM);
            disableMe(issues);
            return;
        }
        getLogger().info("Hologram provider: " + platform.getName());

        try {
            holograms = new HologramStorage(this, getServer().getPluginManager());
        } catch (InvalidConfigurationException e1) {
            issues.add(DefaultReloadIssue.INVALID_HOLOGRAMS);
            disableMe(issues);
            return;
        }
        saveDefaultConfig();

        userCache = new SimpleUserCache(this);

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
        this.getServer().getPluginManager().registerEvents(new JoinLeaveListener(this, holograms), this);
        this.getServer().getPluginManager().registerEvents(new WorldListener(holograms), this);
        WorldTimeListener worldTimeListener;
        String version = getServer().getBukkitVersion();
        if (version.contains("1.15") || version.contains("1.16")
                || version.contains("1.17") || version.contains("1.18") || version.contains("1.19")) {
            worldTimeListener = new SimpleWorldTimeListener(holograms);
        } else {
            getLogger().warning(messages.getLegacyMessage());
            worldTimeListener = new LegacyWorldTimeListener(holograms);
        }
        this.getServer().getPluginManager().registerEvents(worldTimeListener, this);

        // metrics
        if (settings.enableMetrics()) {
            Metrics metrics = new Metrics(this, 7234);
            metrics.addCustomChart(new SimplePie("hologram_provider", () -> platform.getName()));
        }

        // commands
        command = new PHDCommand(this, getServer().getPluginManager());
        getCommand("phd").setExecutor(command);
        // listen to /holo delete/remove
        new Zombificator(this);

        int resourceId = 77631;
        if (settings.checkForUpdates() && resourceId != -1) {
            UpdateChecker.init(this, resourceId).requestUpdateCheck().whenComplete(
                    (result, e) -> getLogger().info(result.getReason() + ": " + result.getNewestVersion()));
        }
        getLogger().info(messages.getActiveStorageMessage(getSettings().useDatabase()));
    }

    private void disableMe(List<ReloadIssue> issues) {
        boolean canMessage = false;
        if (messages == null) {
            try {
                messages = new Messages(this, true);
                canMessage = true;
            } catch (InvalidConfigurationException e) {
                e.printStackTrace();
            }
        }
        if (canMessage) {
            getLogger().severe(messages.getProblemsReloadingConfigMessage(issues));
            getLogger().severe(messages.getDisablingMessage());
        } else {
            getLogger().severe("Disabling plugins. Problems: " + issues);
        }
        getServer().getPluginManager().disablePlugin(this);
    }

    @Override
    public File getWorldContainer() {
        return getServer().getWorldContainer();
    }

    @Override
    public UserCache getUserCache() {
        return userCache;
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
        boolean disablePlugin = false;
        try {
            if (!messages.reloadConfig()) {
                issues.add(new SimpleReloadIssue(messages.getIncorrectMessages(), null));
                disablePlugin = true;
            }
        } catch (InvalidConfigurationException e) {
            issues.add(DefaultReloadIssue.INVALID_MESSAGES);
            disablePlugin = true;
        }
        List<ReloadIssue> settingIssues = getSettingIssues();
        issues.addAll(settingIssues);
        if (!settingIssues.isEmpty()) {
            disablePlugin = true;
        }
        try {
            holograms.reload();
        } catch (InvalidConfigurationException e) {
            issues.add(DefaultReloadIssue.INVALID_HOLOGRAMS);
            disablePlugin = true;
        }
        if (disablePlugin) {
            getServer().getScheduler().runTask(this, () -> {
                disableMe(issues);
            });
        }
        command.reload();
        return issues;
    }

    private List<ReloadIssue> getSettingIssues() {
        List<ReloadIssue> issues = new ArrayList<>();
        try {
            issues.addAll(reloadMyConfig());
        } catch (StorageTypeException e) {
            DefaultReloadIssue issue = DefaultReloadIssue.ILLEGA_STORAGE_TYPE;
            issue.setExtra(e.getType());
            issues.add(issue);
            settings.setDefaultDatabaseInternal();
        }
        return issues;
    }

    public List<ReloadIssue> reloadMyConfig() {
        Map<SettingIssue, String> settingIssues = new HashMap<>();
        List<ReloadIssue> issues = new ArrayList<>();
        try {
            config.reloadConfig();
        } catch (InvalidConfigurationException e) {
            issues.add(DefaultReloadIssue.INVALID_CONFIGURATION);
        }
        if (settings != null) {
            settingIssues.putAll(settings.reload());
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
    public void onDisable() {
        if (holograms != null)
            holograms.save(true);
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

    // config

    @Override
    public FileConfiguration getConfig() {
        return config.getConfig();
    }

    public static enum DefaultReloadIssue implements ReloadIssue {
        NONE(null), NO_FOLDER("folder had to be recreated!"), NO_CONFIG("the config had to be recreated"),
        NO_MESSAGES("the messages file had to be recreated"), ILLEGA_STORAGE_TYPE("storage type not understood"),
        INVALID_CONFIGURATION("config.yml was incorrectly formatted"),
        INVALID_MESSAGES("messages.yml was incorrectly formatted"),
        INVALID_HOLOGRAMS("database.yml was incorrectly formatted"),
        NO_PLATFORM("no holgoram platform found. Currently supported:" + PlatformProvider.getSupportedPlatformNames()),
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

    @Override
    public HologramProvider getHologramProvider() {
        return platform.getHologramProvider();
    }

    @Override
    public HologramPlatform getHologramPlatform() {
        return platform;
    }

}