package me.ford.periodicholographicdisplays.hooks;

import org.bukkit.entity.Entity;

/**
 * CitizensHook
 */
public abstract class CitizensHook {

    public boolean isNPC(Entity ent) {
        return ent.hasMetadata("NPC");
    }
    
}