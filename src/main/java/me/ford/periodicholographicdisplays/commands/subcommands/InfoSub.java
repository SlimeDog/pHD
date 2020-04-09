package me.ford.periodicholographicdisplays.commands.subcommands;

import java.util.ArrayList;
import java.util.List;

import com.gmail.filoghost.holographicdisplays.commands.CommandValidator;
import com.gmail.filoghost.holographicdisplays.exception.CommandException;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
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
 * InfoSub
 */
public class InfoSub extends SubCommand {
    private static final String PERMS = "phd.info";
    private static final String USAGE = "/phd info <hologram> <type> [page]";
    private final HologramStorage storage;
    private final Messages messages;

    public InfoSub(HologramStorage storage, Messages messages) {
        this.storage = storage;
        this.messages = messages;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        List<String> list = new ArrayList<>();
        switch (args.length) {
            case 1:
                List<String> names = storage.getNames();
                names.sort(String.CASE_INSENSITIVE_ORDER);
                return StringUtil.copyPartialMatches(args[0], names, list);
            case 2:
                List<PeriodicType> types = storage.getAvailableTypes(args[0]);
                List<String> availableTypes = new ArrayList<>();
                for (PeriodicType type : types) {
                    availableTypes.add(type.name());
                }
                return StringUtil.copyPartialMatches(args[1], availableTypes, list);
        }
        return list;
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        if (args.length < 1) {
            return false;
        }
        if (args.length == 1) {
            List<PeriodicType> availableTypes = storage.getAvailableTypes(args[0]);
            if (availableTypes.isEmpty()) {
                sender.sendMessage(messages.getHDHologramNotFoundMessage(args[0]));
            } else {
                sender.sendMessage(messages.getAvailableTypesMessage(args[0], availableTypes));
            }
            return true;
        }
        PeriodicType type;
        try {
            type = PeriodicType.valueOf(args[1].toUpperCase());
        } catch (IllegalArgumentException e) {
            sender.sendMessage(messages.getTypeNotRecognizedMessage(args[1]));
            return true;
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
        try {
            CommandValidator.getNamedHologram(args[0]);
        } catch (CommandException e) {
            sender.sendMessage(messages.getHDHologramNotFoundMessage(args[0]));
            return true;
        }
        FlashingHologram hologram = storage.getHologram(args[0], type);
        if (hologram == null) {
            sender.sendMessage(messages.getHologramNotFoundMessage(args[0], type));
            return true;
        }
        int maxPage = getMaxPages(hologram);
        if (maxPage == 0)
            maxPage++;
        if (page <= 0 || page > maxPage) {
            sender.sendMessage(messages.getInvalidPageMessage(maxPage));
            return true;
        }
        sender.sendMessage(messages.getHologramInfoMessage(hologram, page, sender instanceof Player));
        if (page < maxPage && sender instanceof Player)
            HintUtil.sendHint(sender, messages.getNextPageHint("{command}"),
                    String.format("/phd info %s %s %d", hologram.getName(), type.name(), page + 1));
        return true;
    }

    private int getMaxPages(FlashingHologram hologram) {
        if (hologram.getType() != PeriodicType.NTIMES)
            return 1;
        NTimesHologram ntimes = (NTimesHologram) hologram;
        return PageUtils.getNumberOfPages(ntimes.getShownTo().size(), PageUtils.PLAYERS_PER_PAGE);
    }

    @Override
    public boolean hasPermission(CommandSender sender) {
        return sender.hasPermission(PERMS);
    }

    @Override
    public String getUsage(CommandSender sender, String[] args) {
        return USAGE;
    }

}