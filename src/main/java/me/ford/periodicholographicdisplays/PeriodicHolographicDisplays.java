package me.ford.periodicholographicdisplays;

import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

import me.ford.periodicholographicdisplays.commands.PHDCommand;
import me.ford.periodicholographicdisplays.holograms.HologramStorage;
import me.ford.periodicholographicdisplays.hooks.LuckPermsHook;
import me.ford.periodicholographicdisplays.listeners.AlwaysHologramListener;
import me.ford.periodicholographicdisplays.listeners.HologramListener;
import me.ford.periodicholographicdisplays.listeners.JoinLeaveListener;
import me.ford.periodicholographicdisplays.listeners.WorldListener;
import me.ford.periodicholographicdisplays.listeners.WorldTimeListener;

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
        holograms = new HologramStorage(this);
        getCommand("phd").setExecutor(new PHDCommand(this));
        this.getServer().getPluginManager().registerEvents(new HologramListener(holograms), this);
        this.getServer().getPluginManager().registerEvents(new JoinLeaveListener(holograms), this);
        this.getServer().getPluginManager().registerEvents(new WorldListener(holograms), this);
        this.getServer().getPluginManager().registerEvents(new WorldTimeListener(holograms), this);
        this.getServer().getPluginManager().registerEvents(new AlwaysHologramListener(holograms), this);

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
        
        if (settings.checkForUpdates()) {
            // TODO - check for updates
        }
    }

    public LuckPermsHook getLuckPermsHook() {
        return lpHook;
    }

    public boolean reload() {
        boolean useDbBefore = settings.useDatabase();
        reloadConfig();
        messages.reloadCustomConfig();
        boolean useDbAfter;
        try {
            useDbAfter = settings.useDatabase();
        } catch (IllegalStateException e) {
            e.printStackTrace();
            return false;
        }
        holograms.reload(useDbBefore != useDbAfter);
        return true;
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
    
}