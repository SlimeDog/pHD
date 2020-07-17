package me.ford.periodicholographicdisplays.hooks;

import java.util.List;
import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import me.ford.periodicholographicdisplays.PeriodicHolographicDisplays;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.event.node.NodeAddEvent;
import net.luckperms.api.event.node.NodeRemoveEvent;
import net.luckperms.api.event.user.track.UserDemoteEvent;
import net.luckperms.api.event.user.track.UserPromoteEvent;

/**
 * LuckPermsHook
 */
public class LuckPermsHook {
    private final PeriodicHolographicDisplays phd;
    private final JavaPlugin lp;
    private final LuckPerms api;

    public LuckPermsHook(PeriodicHolographicDisplays phd) {
        this.phd = phd;
        lp = (JavaPlugin) this.phd.getServer().getPluginManager().getPlugin("LuckPerms");
        if (lp == null) {
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
        for (Player player : phd.getServer().getOnlinePlayers()) {
            if (player.hasPermission("group." + name)) {
                resetHolograms(player);
            }
        }
    }

    private void nodeRemoved(NodeRemoveEvent event) {
        String name = event.getTarget().getIdentifier().getName();
        UUID id;
        try {
            id = UUID.fromString(name);
        } catch (IllegalArgumentException e) { // GROUP
            handleGroup(name);
            return;
        }
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

    private final String lpCommand = "luckperms";
    private final String[] lpArgs = {"user", "N/A", "permission", "set", null};
    
    public List<String> tabCompletePermissions(CommandSender sender, String cur) {
        PluginCommand command = lp.getCommand(lpCommand);
        String[] args = lpArgs.clone();
        args[args.length-1] = cur;
        return command.getTabCompleter().onTabComplete(sender, command, lpCommand, args);
    }
    
}