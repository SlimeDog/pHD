package me.ford.periodicholographicdisplays.commands;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.util.StringUtil;

import me.ford.periodicholographicdisplays.Messages;
import me.ford.periodicholographicdisplays.util.PageUtils;

/**
 * ParentCommand
 */
public abstract class ParentCommand implements TabExecutor {
	private final Messages messages;
	private static final int PER_PAGE = 8;
	private final Map<String, SubCommand> subCommands = new LinkedHashMap<>();
	
	public ParentCommand(Messages messages) {
		this.messages = messages;
	}
    
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
	
	private String getUsage(CommandSender sender, int page) {
		List<String> msgs = new ArrayList<>();
		String header = getUsage().replace("{page}", String.valueOf(page));
		for (SubCommand cmd : subCommands.values()) {
			if (cmd.hasPermission(sender)) {
				for (String part : cmd.getUsage(sender).split("\n")) {
					msgs.add(part);
				}
			}
		}
		int maxPage = PageUtils.getNumberOfPages(msgs.size(), PER_PAGE);
		if (page < 1 || page > maxPage) {
			return messages.getInvalidPageMessage(maxPage);
		}
		header = header.replace("{maxpage}", String.valueOf(maxPage));
		int start = (page - 1) * PER_PAGE;
		if (start > msgs.size()) start = msgs.size();
		int end = page * PER_PAGE;
		if (end > msgs.size()) end = msgs.size();
		List<String> onPage = msgs.subList(start, end);
		return header + "\n" + String.join("\n", onPage);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length < 1) {
            return noArgs(sender);
		}
		SubCommand cmd = subCommands.get(args[0]);
		if (cmd == null) {
			int page = 1;
			String pageStr = "1";
			if (args[0].equalsIgnoreCase("help")) {
				if (args.length > 1) {
					pageStr = args[1];
				}
			} else {
				pageStr = args[0];
			}
			try {
				page = Integer.parseInt(pageStr);
			} catch (NumberFormatException e) {
				if (!args[0].equalsIgnoreCase("help")) {
					sender.sendMessage(messages.getUnrecognizedCommandMessage(args[0]));
					return true;
				}
			}
			sender.sendMessage(getUsage(sender, page));
			return true;
		}
		
		if (!cmd.hasPermission(sender)) {
            sender.sendMessage(getUsage(sender, 1));
        } else if (!cmd.onCommand(sender, command, label, args)) {
			sender.sendMessage(cmd.getUsage(sender));
		}
		return true;
    }

    protected boolean noArgs(CommandSender sender) { // can be overwritten
        sender.sendMessage(getUsage(sender, 1));
        return true;
    }

    protected abstract String getUsage();
    
}