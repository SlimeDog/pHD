package me.ford.periodicholographicdisplays;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

import me.ford.periodicholographicdisplays.Settings.StorageTypeException;
import me.ford.periodicholographicdisplays.commands.PHDCommand;
import me.ford.periodicholographicdisplays.holograms.HologramStorage;
import me.ford.periodicholographicdisplays.hooks.LuckPermsHook;
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

    @Override
    public void onEnable() {
        saveDefaultConfig();
        messages = new Messages(this);
        settings = new Settings(this);
        try {
            settings.useDatabase();
        } catch (StorageTypeException e) {
            getLogger().warning("Illegal storage type " + e.getType() + " - reverting to default (SQLITE)");
            settings.setDefaultDatabaseInternal();
        }
        holograms = new HologramStorage(this);
        this.getServer().getPluginManager().registerEvents(new HologramListener(holograms), this);
        this.getServer().getPluginManager().registerEvents(new JoinLeaveListener(holograms), this);
        this.getServer().getPluginManager().registerEvents(new WorldListener(holograms), this);
        WorldTimeListener worldTimeListener;
        if (getServer().getBukkitVersion().contains("1.15")) {
            worldTimeListener = new SimpleWorldTimeListener(holograms);
        } else {
            getLogger().warning("MCTIME holograms can behave unpredicably because of the use of a legacy version of MC");
            worldTimeListener = new LegacyWorldTimeListener(holograms);
        }
        this.getServer().getPluginManager().registerEvents(worldTimeListener, this);

        // metrics
		if (settings.enableMetrics()) {
			new Metrics(this);
        }

        // LuckPerms hook if possible
        try {
            lpHook = new LuckPermsHook(this);
        } catch (IllegalStateException e) {
            getLogger().warning("LuckPerms not found - unable to readjust permissions on the fly");
        }

        // commands
        getCommand("phd").setExecutor(new PHDCommand(this));
        
        if (settings.checkForUpdates()) {
            // TODO - check for updates
        }
    }

    public LuckPermsHook getLuckPermsHook() {
        return lpHook;
    }

    public List<ReloadIssue> reload() {
        List<ReloadIssue> issues = new ArrayList<>();
        File df = getDataFolder();
        if (!df.exists() || !df.canRead()) {
            getLogger().warning("Plugin folder does not exist or is unreadable at reload; attempting to recreate.");
            df.mkdir();
            saveDefaultConfig();
            messages.saveDefaultConfig();
            issues.add(DefaultReloadIssue.NO_FOLDER);
        }
        boolean useDbBefore = settings.useDatabase();
        reloadConfig();
        messages.reloadCustomConfig();
        boolean useDbAfter;
        try {
            useDbAfter = settings.useDatabase();
        } catch (StorageTypeException e) {
            e.printStackTrace();
            DefaultReloadIssue issue = DefaultReloadIssue.ILLEGA_STORAGE_TYPE;
            issue.setExtra(e.getType());
            issues.add(issue);
            useDbAfter = useDbBefore; // not used
            settings.setDefaultDatabaseInternal();
        }
        if (issues.isEmpty()) holograms.reload(useDbBefore != useDbAfter);
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

    public static enum DefaultReloadIssue implements ReloadIssue {
        NONE(null),
        NO_FOLDER("folder had to be recreated!"),
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