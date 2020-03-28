package me.ford.periodicholographicdisplays.commands.subcommands;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

import me.ford.periodicholographicdisplays.Messages;
import me.ford.periodicholographicdisplays.commands.SubCommand;
import me.ford.periodicholographicdisplays.holograms.FlashingHologram;
import me.ford.periodicholographicdisplays.holograms.HologramStorage;
import me.ford.periodicholographicdisplays.holograms.NTimesHologram;
import me.ford.periodicholographicdisplays.holograms.PeriodicType;
import me.ford.periodicholographicdisplays.users.UserCache;
import me.ford.periodicholographicdisplays.users.UserStorage;
import me.ford.periodicholographicdisplays.util.PageUtils;

/**
 * ReportSub
 */
public class ReportSub extends SubCommand {
    private static final String PERMS = "phd.report";
    private static final String USAGE = "/phd report NTIMES <player> [page]";
    private final HologramStorage storage;
    private final Messages messages;
    private final UserStorage userStorage;

    public ReportSub(HologramStorage storage, Messages messages, UserStorage userStorage) {
        this.storage = storage;
        this.messages = messages;
        this.userStorage = userStorage;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        List<String> list = new ArrayList<>();
        switch (args.length) {
            case 1:
                list.add(PeriodicType.NTIMES.name());
                return list;
            case 2:
                if (args[1].length() < UserCache.MIN_NAME_MATCH) {
                    return null;
                }
                return userStorage.getCache().getNamesStartingWith(args[1]);
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
        if (type != PeriodicType.NTIMES)
            return false;
        OfflinePlayer player = Bukkit.getPlayer(args[1]); // TODO - do I need offline players?
        if (player == null) {
            UUID id = userStorage.getCache().getUuid(args[1]);
            if (id != null) {
                player = Bukkit.getOfflinePlayer(id);
            }
            if (player == null || !player.hasPlayedBefore()) {
                sender.sendMessage(messages.getPlayerNotFoundMessage(args[1]));
                return true;
            }
        }

        int page = 1;
        if (args.length > 2) {
            try {
                page = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                sender.sendMessage(messages.getNeedAnIntegerMessage(args[2]));
                return true;
            }
        }

        // get all NTIMES holograms
        List<NTimesHologram> holograms = new ArrayList<>();
        for (World world : storage.getActiveWorlds()) {
            for (FlashingHologram holo : storage.getHolograms(world).getHolograms()) {
                if (holo.getType() == type) {
                    holograms.add((NTimesHologram) holo);
                }
            }
        }
        holograms.sort(new Comparator<NTimesHologram>() {
            @Override
            public int compare(NTimesHologram o1, NTimesHologram o2) {
                return String.CASE_INSENSITIVE_ORDER.compare(o1.getName(), o2.getName());
            }
        });

        int maxPage = PageUtils.getNumberOfPages(holograms.size(), PageUtils.HOLOGRAMS_PER_PAGE);
        if (maxPage == 0)
            maxPage++;
        if (page < 1 || page > maxPage) {
            sender.sendMessage(messages.getInvalidPageMessage(maxPage));
            return true;
        }

        sender.sendMessage(messages.getNtimesReportMessage(player, holograms, page));
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