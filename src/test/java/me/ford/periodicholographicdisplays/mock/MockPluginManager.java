package me.ford.periodicholographicdisplays.mock;

import java.io.File;
import java.util.Set;

import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.permissions.Permissible;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.UnknownDependencyException;

/**
 * MockPluginManager
 */
public class MockPluginManager implements PluginManager {

	@Override
	public void registerInterface(Class<? extends PluginLoader> loader) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Plugin getPlugin(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Plugin[] getPlugins() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isPluginEnabled(String name) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isPluginEnabled(Plugin plugin) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Plugin loadPlugin(File file)
			throws InvalidPluginException, InvalidDescriptionException, UnknownDependencyException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Plugin[] loadPlugins(File directory) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void disablePlugins() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clearPlugins() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void callEvent(Event event) throws IllegalStateException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void registerEvents(Listener listener, Plugin plugin) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void registerEvent(Class<? extends Event> event, Listener listener,
			EventPriority priority, EventExecutor executor, Plugin plugin) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void registerEvent(Class<? extends Event> event, Listener listener,
			EventPriority priority, EventExecutor executor, Plugin plugin,
			boolean ignoreCancelled) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enablePlugin(Plugin plugin) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void disablePlugin(Plugin plugin) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Permission getPermission(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addPermission(Permission perm) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removePermission(Permission perm) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removePermission(String name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Set<Permission> getDefaultPermissions(boolean op) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void recalculatePermissionDefaults(Permission perm) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void subscribeToPermission(String permission, Permissible permissible) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unsubscribeFromPermission(String permission, Permissible permissible) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Set<Permissible> getPermissionSubscriptions(String permission) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void subscribeToDefaultPerms(boolean op, Permissible permissible) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unsubscribeFromDefaultPerms(boolean op, Permissible permissible) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Set<Permissible> getDefaultPermSubscriptions(boolean op) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Permission> getPermissions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean useTimings() {
		// TODO Auto-generated method stub
		return false;
	}

    
}