package me.ford.periodicholographicdisplays.hooks;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import me.ford.periodicholographicdisplays.PeriodicHolographicDisplays;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.event.node.NodeAddEvent;
import net.luckperms.api.event.node.NodeRemoveEvent;
import net.luckperms.api.event.user.track.UserDemoteEvent;
import net.luckperms.api.event.user.track.UserPromoteEvent;
import net.luckperms.api.model.group.Group;

/**
 * LuckPermsHook
 */
public class LuckPermsHook {
    private final PeriodicHolographicDisplays phd;
    private final LuckPerms api;

    public LuckPermsHook(PeriodicHolographicDisplays phd) {
        this.phd = phd;
        if (this.phd.getServer().getPluginManager().getPlugin("LuckPerms") == null) {
            throw new IllegalStateException("Need LuckPerms to be enabled for the LuckPermsHook to work");
        }
        RegisteredServiceProvider<LuckPerms> provider = phd.getServer().getServicesManager()
                .getRegistration(LuckPerms.class);
        if (provider != null) {
            api = provider.getProvider();
        } else {
            throw new IllegalStateException("The LuckPerms service was not provided!");
        }
        // hook
        api.getEventBus().subscribe(NodeAddEvent.class, (node) -> nodeAdded(node));
        api.getEventBus().subscribe(NodeRemoveEvent.class, (node) -> nodeRemoved(node));
        api.getEventBus().subscribe(UserPromoteEvent.class, (node) -> userPromote(node));
        api.getEventBus().subscribe(UserDemoteEvent.class, (node) -> userDemote(node));
    }

    private void nodeAdded(NodeAddEvent event) {
        String name = event.getTarget().getIdentifier().getName();
        UUID id;
        try {
            id = UUID.fromString(name);
        } catch (IllegalArgumentException e) { // GROUP
            handleGroup(name);
            return;
        }
        Player player = phd.getServer().getPlayer(id);
        if (player == null)
            return; // don't worry about it - they're offline
        resetHolograms(player);
    }

    private void resetHolograms(Player player) {
        phd.getHolograms().getHolograms(player.getWorld()).resetAlwaysHologramPermissions(player);
    }

    private void handleGroup(String name) {
        Group group = api.getGroupManager().getGroup(name);
        if (group == null) {
            phd.getLogger().warning("LuckPerms updateded group '" + name + "', but could not find the group");
            return;
        }
        for (Player player : phd.getServer().getOnlinePlayers()) {
            if (player.hasPermission("group." + name)) {
                resetHolograms(player);
            }
        }
    }

    private void nodeRemoved(NodeRemoveEvent event) {
        UUID id = UUID.fromString(event.getTarget().getIdentifier().getName());
        Player player = phd.getServer().getPlayer(id);
        if (player == null) return; // don't worry about it - they're offline    
        phd.getHolograms().getHolograms(player.getWorld()).resetAlwaysHologramPermissions(player);
    }

    private void userPromote(UserPromoteEvent event) {
        Player player = phd.getServer().getPlayer(event.getUser().getUniqueId());
        if (player == null) return; // don't worry about it - they're offline
        phd.getHolograms().getHolograms(player.getWorld()).resetAlwaysHologramPermissions(player);
    }

    private void userDemote(UserDemoteEvent event) {
        Player player = phd.getServer().getPlayer(event.getUser().getUniqueId());
        if (player == null) return; // don't worry about it - they're offline
        phd.getHolograms().getHolograms(player.getWorld()).resetAlwaysHologramPermissions(player);        
    }
    
}