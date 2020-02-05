package me.ford.periodicholographicdisplays;

import org.bukkit.plugin.java.JavaPlugin;

import me.ford.periodicholographicdisplays.commands.PHDCommand;
import me.ford.periodicholographicdisplays.holograms.HologramStorage;
import me.ford.periodicholographicdisplays.listeners.HologramListener;
import me.ford.periodicholographicdisplays.listeners.JoinLeaveListener;
import me.ford.periodicholographicdisplays.listeners.WorldListener;

/**
 * PeriodicHolographicDisplays
 */
public class PeriodicHolographicDisplays extends JavaPlugin {
    private HologramStorage holograms;
    private Settings settings;    
    private Messages messages;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        messages = new Messages(this);
        settings = new Settings(this);
        holograms = new HologramStorage(this);
        getCommand("phd").setExecutor(new PHDCommand(this)); // TODO - set executor
        this.getServer().getPluginManager().registerEvents(new HologramListener(holograms), this);
        this.getServer().getPluginManager().registerEvents(new JoinLeaveListener(holograms), this);
        this.getServer().getPluginManager().registerEvents(new WorldListener(holograms), this);
    }

    @Override
    public void onDisable() {
        holograms.save();
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