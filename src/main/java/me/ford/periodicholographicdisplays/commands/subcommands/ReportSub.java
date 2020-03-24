package me.ford.periodicholographicdisplays.commands.subcommands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

import me.ford.periodicholographicdisplays.Messages;
import me.ford.periodicholographicdisplays.commands.SubCommand;
import me.ford.periodicholographicdisplays.holograms.FlashingHologram;
import me.ford.periodicholographicdisplays.holograms.HologramStorage;
import me.ford.periodicholographicdisplays.holograms.NTimesHologram;
import me.ford.periodicholographicdisplays.holograms.PeriodicType;
import me.ford.periodicholographicdisplays.util.HintUtil;
import me.ford.periodicholographicdisplays.util.PageUtils;

/**
 * ReportSub
 */
public class ReportSub extends SubCommand {
    private static final String PERMS = "phd.commands.phd.report";
    private static final String USAGE = "/phd report NTIMES <name> [page]";
    private final HologramStorage storage;
    private final Messages messages;

    public ReportSub(HologramStorage storage, Messages messages) {
        this.storage = storage;
        this.messages = messages;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        List<String> list = new ArrayList<>();
        switch(args.length) {
        case 1:
            list.add(PeriodicType.NTIMES.name());
            return list;
        case 2:
            List<String> names = new ArrayList<>();
            for (World world : storage.getActiveWorlds()) {
                for (FlashingHologram hologram : storage.getHolograms(world).getHolograms(false)) {
                    if (hologram.getType() == PeriodicType.NTIMES) names.add(hologram.getName());
                }
            }
            names.sort(String.CASE_INSENSITIVE_ORDER);
            return StringUtil.copyPartialMatches(args[1], names, list);
        } 
        return list;
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        if (args.length < 2) {
            return false;
        }
        PeriodicType type;
        try {
            type = PeriodicType.valueOf(args[0]);
        } catch (IllegalArgumentException e) {
            return false;
        }
        if (type != PeriodicType.NTIMES) return false;
        int page = 1;
        if (args.length > 2) {
            try {
                page = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                sender.sendMessage(messages.getNeedAnIntegerMessage(args[2]));
                return true;
            }
        }
        NTimesHologram hologram = (NTimesHologram) storage.getHologram(args[1], type);
        if (hologram == null) {
            sender.sendMessage(messages.getHologramNotFoundMessage(args[1], type));
            return true;
        }
        int maxPage = getMaxPages(hologram);
        if (maxPage == 0) maxPage++;
        if (page <= 0 || page > maxPage) {
            sender.sendMessage(messages.getInvalidPageMessage(maxPage));
            return true;
        }
        sender.sendMessage(messages.getNtimesReportMessage(hologram, page));
        if (page < maxPage) HintUtil.sendHint(sender, messages.getNextPageHint("{command}"), String.format("/phd report NTIMES %s %d", hologram.getName(), page +1));
        return true;
    }

    private int getMaxPages(NTimesHologram hologram) {
        if (hologram.getType() != PeriodicType.NTIMES) return 1;
        NTimesHologram ntimes = (NTimesHologram) hologram;
        return PageUtils.getNumberOfPages(ntimes.getShownTo().size(), PageUtils.PLAYERS_PER_PAGE);
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