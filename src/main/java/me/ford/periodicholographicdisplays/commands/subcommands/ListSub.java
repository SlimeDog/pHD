package me.ford.periodicholographicdisplays.commands.subcommands;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.bukkit.command.CommandSender;

import me.ford.periodicholographicdisplays.Messages;
import me.ford.periodicholographicdisplays.commands.SubCommand;
import me.ford.periodicholographicdisplays.holograms.HologramStorage;
import me.ford.periodicholographicdisplays.holograms.PeriodicType;
import me.ford.periodicholographicdisplays.util.HintUtil;
import me.ford.periodicholographicdisplays.util.PageUtils;

/**
 * ListSub
 */
public class ListSub extends SubCommand {
    private static final String PERMS = "phd.list";
    private static final String USAGE = "/phd list [page]";
    private final HologramStorage storage;
    private final Messages messages;

    public ListSub(HologramStorage storage, Messages messages) {
        this.storage = storage;
        this.messages = messages;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return new ArrayList<>();
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        int page = 1;
        if (args.length > 0) {
            try {
                page = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                sender.sendMessage(messages.getNeedAnIntegerMessage(args[0]));
                return true;
            }
        } 
        List<String> names = storage.getNames();
        int maxPage = PageUtils.getNumberOfPages(names.size(), PageUtils.HOLOGRAMS_PER_PAGE);
        if (maxPage == 0) maxPage++;
        if (page <= 0 || page > maxPage) {
            sender.sendMessage(messages.getInvalidPageMessage(maxPage));
            return true;
        }
        names.sort(String.CASE_INSENSITIVE_ORDER);
        Map<String, String> hologramTypes = new TreeMap<>();
        for (String name : names) {
            List<PeriodicType> types = storage.getAvailableTypes(name);
            List<String> typesStr = new ArrayList<>();
            for (PeriodicType type : types) {
                typesStr.add(type.name());
            }
            hologramTypes.put(name, String.join(", ", typesStr));
        }
        sender.sendMessage(messages.getHologramListMessage(hologramTypes, page));
        if (page < maxPage) HintUtil.sendHint(sender, messages.getNextPageHint("{command}"), String.format("/phd list %d", page +1));
        return true;
    }

    @Override
    public boolean hasPermission(CommandSender sender) {
        return sender.hasPermission(PERMS);
    }

    @Override
    public String getUsage(CommandSender sender) {
        return USAGE;
    }
    
}