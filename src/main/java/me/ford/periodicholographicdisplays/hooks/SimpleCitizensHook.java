package me.ford.periodicholographicdisplays.hooks;

import org.bukkit.plugin.java.JavaPlugin;

import net.citizensnpcs.Citizens;

/**
 * SimplyCitizensHook
 */
public class SimpleCitizensHook extends CitizensHook {
    private final Citizens citizens;

    public SimpleCitizensHook() throws ClassNotFoundException {
        citizens = JavaPlugin.getPlugin(Citizens.class);
    }

    public Citizens getCitizens() {
        return citizens;
    }
    
}