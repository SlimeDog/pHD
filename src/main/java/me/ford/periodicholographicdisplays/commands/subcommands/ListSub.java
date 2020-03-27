package me.ford.periodicholographicdisplays.commands.subcommands;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

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
    private static final String USAGE = "/phd list <type> [page]";
    private final HologramStorage storage;
    private final Messages messages;

    public ListSub(HologramStorage storage, Messages messages) {
        this.storage = storage;
        this.messages = messages;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        List<String> list = new ArrayList<>();;
        if (args.length == 1) {
            return StringUtil.copyPartialMatches(args[0], PeriodicType.names(), list);
        }
        return list;
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        String pageStr = null;
        String typeStr = null;
        boolean fuzzy = args.length == 1;
        if (fuzzy) {
            typeStr = args[0];
            pageStr = args[0];
        } else if (args.length > 1) {
            typeStr = args[0];
            pageStr = args[1];            
        }
        int page = 1;
        PeriodicType holoType = null;
        if (args.length > 0) {
            try {
                holoType = PeriodicType.valueOf(typeStr);
            } catch (IllegalArgumentException e) {
            }
            boolean gotPage = true;
            try {
                page = Integer.parseInt(pageStr);
            } catch (NumberFormatException e) {
                gotPage = false;
            }
            if (fuzzy && holoType == null && !gotPage) {
                sender.sendMessage(messages.getNeedTypeOrPageMessage(typeStr));
                return true;
            } else if (!fuzzy && holoType == null) {
                sender.sendMessage(messages.getTypeNotRecognizedMessage(typeStr));
                return true;
            } else if (!fuzzy && !gotPage) {
                sender.sendMessage(messages.getNeedAnIntegerMessage(pageStr));
                return true;
            }
        }
        List<String> names = storage.getNames(holoType);
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
        String typeName = holoType == null ? "" : " " + holoType.name();
        if (page < maxPage) HintUtil.sendHint(sender, messages.getNextPageHint("{command}"), String.format("/phd list%s %d", typeName, page +1));
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