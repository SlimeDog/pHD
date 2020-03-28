package me.ford.periodicholographicdisplays.hooks;

import org.bukkit.plugin.java.JavaPlugin;

import net.citizensnpcs.Citizens;

/**
 * SimpleCitizensHook
 */
public class SimpleCitizensHook extends NPCHook {
    private final Citizens citizens;

    public SimpleCitizensHook() throws ClassNotFoundException {
        citizens = JavaPlugin.getPlugin(Citizens.class);
    }

    public Citizens getCitizens() {
        return citizens;
    }

}