package me.ford.periodicholographicdisplays.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.util.StringUtil;

/**
 * ParentCommand
 */
public abstract class ParentCommand implements TabExecutor {
    private final Map<String, SubCommand> subCommands = new HashMap<>();
    
    protected void addSubCommand(String name, SubCommand subCommand) {
        subCommands.put(name.toLowerCase(), subCommand);
    }

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		List<String> list = new ArrayList<>();
		if (args.length == 1) {
			for (Entry<String, SubCommand> entry : subCommands.entrySet()) {
				if (entry.getValue().hasPermission(sender)) {
					list.add(entry.getKey());
				}
			}
			return StringUtil.copyPartialMatches(args[0], list, new ArrayList<>());
		} else {
			SubCommand sub = subCommands.get(args[0]);
			if (sub == null || !sub.hasPermission(sender)) {
				return list;
			} else {
				return sub.onTabComplete(sender, command, alias, args);
			}
		}
	}
	
	private String getUsage(CommandSender sender) {
		String msg = getUsage();
		for (SubCommand cmd : subCommands.values()) {
			if (cmd.hasPermission(sender)) msg += "\n" + cmd.getUsage(sender);
		}
		return msg;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length < 1) {
            return noArgs(sender);
		}
		SubCommand cmd = subCommands.get(args[0]);
		if (cmd == null) {
			sender.sendMessage(getUsage(sender));
			return true;
		}
		
		if (!cmd.hasPermission(sender)) {
            sender.sendMessage(getUsage(sender));
        } else if (!cmd.onCommand(sender, command, label, args)) {
			sender.sendMessage(cmd.getUsage(sender));
		}
		return true;
    }

    protected boolean noArgs(CommandSender sender) { // can be overwritten
        sender.sendMessage(getUsage(sender));
        return true;
    }

    protected abstract String getUsage();
    
}