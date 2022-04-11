package me.ford.periodicholographicdisplays.commands.subcommands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import dev.ratas.slimedogcore.api.messaging.recipient.SDCRecipient;
import me.ford.periodicholographicdisplays.Messages;
import me.ford.periodicholographicdisplays.commands.PHDSubCommand;
import me.ford.periodicholographicdisplays.holograms.FlashingHologram;
import me.ford.periodicholographicdisplays.holograms.HologramStorage;
import me.ford.periodicholographicdisplays.holograms.NTimesHologram;
import me.ford.periodicholographicdisplays.holograms.PeriodicType;
import me.ford.periodicholographicdisplays.holograms.wrap.WrappedHologram;
import me.ford.periodicholographicdisplays.holograms.wrap.provider.HologramProvider;
import me.ford.periodicholographicdisplays.util.HintUtil;
import me.ford.periodicholographicdisplays.util.PageUtils;

/**
 * InfoSub
 */
public class InfoSub extends PHDSubCommand {
    private static final String PERMS = "phd.info";
    private static final String USAGE = "/phd info <hologram> <type> [page]";
    private final HologramStorage storage;
    private final Messages messages;

    public InfoSub(HologramProvider provider, HologramStorage storage, Messages messages) {
        super(provider, "info", PERMS, USAGE);
        this.storage = storage;
        this.messages = messages;
    }

    @Override
    public List<String> onTabComplete(SDCRecipient sender, String[] args) {
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
    public boolean onCommand(SDCRecipient sender, String[] args, List<String> options) {
        if (args.length < 1) {
            return false;
        }
        if (args.length == 1) {
            List<PeriodicType> availableTypes = storage.getAvailableTypes(args[0]);
            if (availableTypes.isEmpty()) {
                sender.sendRawMessage(messages.getHDHologramNotFoundMessage(args[0]));
            } else {
                sender.sendRawMessage(messages.getAvailableTypesMessage(args[0], availableTypes));
            }
            return true;
        }
        PeriodicType type;
        try {
            type = PeriodicType.valueOf(args[1].toUpperCase());
        } catch (IllegalArgumentException e) {
            sender.sendRawMessage(messages.getTypeNotRecognizedMessage(args[1]));
            return true;
        }
        int page = 1;
        if (args.length > 2) {
            try {
                page = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                sender.sendRawMessage(messages.getNeedAnIntegerMessage(args[2]));
                return true;
            }
        }
        WrappedHologram holo = provider.getByName(args[0]);
        if (holo == null) {
            sender.sendRawMessage(messages.getHDHologramNotFoundMessage(args[0]));
            return true;
        }
        FlashingHologram hologram = storage.getHologram(args[0], type);
        if (hologram == null) {
            sender.sendRawMessage(messages.getHologramNotFoundMessage(args[0], type));
            return true;
        }
        int maxPage = getMaxPages(hologram);
        if (maxPage == 0)
            maxPage++;
        if (page <= 0 || page > maxPage) {
            sender.sendRawMessage(messages.getInvalidPageMessage(maxPage));
            return true;
        }
        sender.sendRawMessage(messages.getHologramInfoMessage(hologram, page, sender instanceof Player));
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

}