package me.ford.periodicholographicdisplays.hooks;

import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;

import me.ford.periodicholographicdisplays.PeriodicHolographicDisplays;
import net.citizensnpcs.Citizens;

/**
 * CitizensHook
 */
public class CitizensHook {
    private final Citizens citizens;

    public CitizensHook(PeriodicHolographicDisplays phd) throws ClassNotFoundException {
        citizens = JavaPlugin.getPlugin(Citizens.class);
    }

    public boolean isNPC(Entity ent) {
        return ent.hasMetadata("NPC");
    }

    public Citizens getCitizens() {
        return citizens;
    }

    
}