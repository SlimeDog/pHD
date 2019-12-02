package me.ford.periodicholographicdisplays;

import org.bukkit.plugin.java.JavaPlugin;

import me.ford.periodicholographicdisplays.commands.PHDCommand;
import me.ford.periodicholographicdisplays.holograms.HologramStorage;
import me.ford.periodicholographicdisplays.listeners.HologramListener;

/**
 * PeriodicHolographicDisplays
 */
public class PeriodicHolographicDisplays extends JavaPlugin {
    private HologramStorage holograms;
    private Settings settings;    

    @Override
    public void onEnable() {
        saveDefaultConfig();
        settings = new Settings(this);
        holograms = new HologramStorage(this);
        getCommand("phd").setExecutor(new PHDCommand(this)); // TODO - set executor
        this.getServer().getPluginManager().registerEvents(new HologramListener(holograms), this);
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
    
}