package me.ford.periodicholographicdisplays.commands;

import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

public abstract class SubCommand implements TabExecutor {

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		return onTabComplete(sender, Arrays.copyOfRange(args, 1, args.length));
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		return onCommand(sender, Arrays.copyOfRange(args, 1, args.length));
	}
	
	protected Player getPlayerOrNull(CommandSender sender) {
		if (sender instanceof Player) {
			return (Player) sender;
		} else {
			return null;
		}
	}
	
	public abstract List<String> onTabComplete(CommandSender sender, String[] args);
	
	public abstract boolean onCommand(CommandSender sender, String[] args);
	
	public abstract boolean hasPermission(CommandSender sender);
	
	public abstract String getUsage(CommandSender sender);

}