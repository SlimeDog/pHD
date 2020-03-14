package me.ford.periodicholographicdisplays;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

import me.ford.periodicholographicdisplays.Settings.StorageTypeException;
import me.ford.periodicholographicdisplays.commands.PHDCommand;
import me.ford.periodicholographicdisplays.holograms.HologramStorage;
import me.ford.periodicholographicdisplays.hooks.CitizensHook;
import me.ford.periodicholographicdisplays.hooks.DummyCitizensHook;
import me.ford.periodicholographicdisplays.hooks.LuckPermsHook;
import me.ford.periodicholographicdisplays.hooks.SimpleCitizensHook;
import me.ford.periodicholographicdisplays.listeners.HologramListener;
import me.ford.periodicholographicdisplays.listeners.JoinLeaveListener;
import me.ford.periodicholographicdisplays.listeners.SimpleWorldTimeListener;
import me.ford.periodicholographicdisplays.listeners.WorldListener;
import me.ford.periodicholographicdisplays.listeners.WorldTimeListener;
import me.ford.periodicholographicdisplays.listeners.legacy.LegacyWorldTimeListener;

/**
 * PeriodicHolographicDisplays
 */
public class PeriodicHolographicDisplays extends JavaPlugin {
    private HologramStorage holograms;
    private Settings settings;    
    private Messages messages;
    private LuckPermsHook lpHook;
    private CitizensHook citizensHook = null;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        messages = new Messages(this);
        settings = new Settings(this);
        try {
            settings.useDatabase();
        } catch (StorageTypeException e) {
            getLogger().warning(messages.getIllegalStorageMessage(e.getType()));
            settings.setDefaultDatabaseInternal();
        }
        holograms = new HologramStorage(this);

        // LuckPerms hook if possible
        try {
            lpHook = new LuckPermsHook(this);
        } catch (IllegalStateException e) {
            getLogger().warning(messages.getNoLPMessage());
        }
        // Citizens hoook if possible
        try {
            citizensHook = new SimpleCitizensHook();
        } catch (ClassNotFoundException | NoClassDefFoundError e) {
            citizensHook = new DummyCitizensHook();
        }

        // listeners
        this.getServer().getPluginManager().registerEvents(new HologramListener(holograms, citizensHook), this);
        this.getServer().getPluginManager().registerEvents(new JoinLeaveListener(holograms), this);
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
        
        if (settings.checkForUpdates()) {
            // TODO - check for updates
        }
    }

    public CitizensHook getCitizensHook() {
        return citizensHook;
    }

    public LuckPermsHook getLuckPermsHook() {
        return lpHook;
    }

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
        boolean useDbBefore = settings.useDatabase();
        reloadConfig();
        messages.reloadCustomConfig();
        boolean useDbAfter;
        try {
            useDbAfter = settings.useDatabase();
        } catch (StorageTypeException e) {
            DefaultReloadIssue issue = DefaultReloadIssue.ILLEGA_STORAGE_TYPE;
            issue.setExtra(e.getType());
            issues.add(issue);
            useDbAfter = useDbBefore; // not used
            settings.setDefaultDatabaseInternal();
        }
        if (issues.isEmpty()) {
            holograms.reload(useDbBefore != useDbAfter);
        } else {
            holograms.reload(false); // assume we have the same database type
        }
        return issues;
    }

    @Override
    public void onDisable() {
        holograms.save(true);
    }

    public HologramStorage getHolograms() {
        return holograms;
    }

    public Settings getSettings() {
        return settings;
    }

    public Messages getMessages() {
        return messages;
    }

    public void debug(String message) {
        if (settings.onDebug()) {
            getLogger().info("[DEBUG] " + message);
        }
    }

    public static enum DefaultReloadIssue implements ReloadIssue {
        NONE(null),
        NO_FOLDER("folder had to be recreated!"),
        NO_CONFIG("the config had to be recreated"),
        NO_MESSAGES("the messages file had to be recreated"),
        ILLEGA_STORAGE_TYPE("storage type not understood");
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

    public static interface ReloadIssue {

        public String getIssue();

        public String getExtra();

    }
    
}