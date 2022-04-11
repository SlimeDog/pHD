package me.ford.periodicholographicdisplays.commands.subcommands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import me.filoghost.holographicdisplays.plugin.internal.hologram.InternalHologramManager;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.util.StringUtil;

import dev.ratas.slimedogcore.api.messaging.recipient.SDCRecipient;
import me.ford.periodicholographicdisplays.Messages;
import me.ford.periodicholographicdisplays.Settings;
import me.ford.periodicholographicdisplays.commands.PHDSubCommand;
import me.ford.periodicholographicdisplays.holograms.FlashingHologram;
import me.ford.periodicholographicdisplays.holograms.HologramStorage;
import me.ford.periodicholographicdisplays.holograms.NTimesHologram;
import me.ford.periodicholographicdisplays.holograms.PeriodicHologramBase;
import me.ford.periodicholographicdisplays.holograms.PeriodicType;
import me.ford.periodicholographicdisplays.holograms.WorldHologramStorageBase.HologramSaveReason;
import me.ford.periodicholographicdisplays.users.UserCache;

/**
 * UnsetSub
 */
public class UnsetSub extends PHDSubCommand {
    private static final String PERMS = "phd.unset";
    private static final String USAGE = "/phd unset <hologram> <type> <options>";
    private final HologramStorage storage;
    private final Settings settings;
    private final Messages messages;
    private final UserCache userCache;
    private final List<String> optionList = Arrays.asList("seconds", "distance", "permission", "playercount", "flash");

    public UnsetSub(InternalHologramManager man, HologramStorage storage, Settings settings, Messages messages,
            UserCache userCache) {
        super(man, "unset", PERMS, USAGE);
        this.storage = storage;
        this.settings = settings;
        this.messages = messages;
        this.userCache = userCache;
    }

    @Override
    public List<String> onTabComplete(SDCRecipient sender, String[] args) {
        List<String> list = new ArrayList<>();
        switch (args.length) {
            case 1:
                return StringUtil.copyPartialMatches(args[0], storage.getNames(), list);
            case 2:
                List<String> typeNames = new ArrayList<>();
                for (PeriodicType type : storage.getAvailableTypes(args[0])) {
                    typeNames.add(type.name());
                }
                return StringUtil.copyPartialMatches(args[1], typeNames, list);
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
                List<String> options = new ArrayList<>(optionList);
                PeriodicType type;
                try {
                    type = PeriodicType.valueOf(args[1].toUpperCase());
                } catch (IllegalArgumentException e) {
                    return list;
                }
                if (type == PeriodicType.NTIMES && args[args.length - 2].equalsIgnoreCase("playercount")) {
                    if (args[args.length - 1].length() < UserCache.MIN_NAME_MATCH) {
                        return null;
                    }
                    return userCache.getNamesStartingWith(args[args.length - 1]);
                }
                FlashingHologram hologram = storage.getHologram(args[0], type);
                if (hologram == null)
                    return list;
                if (!hologram.hasPermissions()) {
                    options.remove("permission");
                }
                if (hologram.getActivationDistance() == PeriodicHologramBase.NO_DISTANCE) {
                    options.remove("distance");
                }
                if (hologram.getShowTime() == PeriodicHologramBase.NO_SECONDS) {
                    options.remove("seconds");
                }
                if (type != PeriodicType.NTIMES) {
                    options.remove("playercount");
                }
                if (!hologram.flashes()) {
                    options.remove("flash");
                }
                for (String prevArg : Arrays.copyOfRange(args, 2, args.length - 1)) {
                    options.remove(prevArg);
                }
                return StringUtil.copyPartialMatches(args[args.length - 1], options, list);
        }
        return list;
    }

    @Override
    public boolean onCommand(SDCRecipient sender, String[] args, List<String> options) {
        if (args.length < 3)
            return false;
        PeriodicType type;
        try {
            type = PeriodicType.valueOf(args[1].toUpperCase());
        } catch (IllegalArgumentException e) {
            sender.sendRawMessage(messages.getTypeNotRecognizedMessage(args[1]));
            return true;
        }
        FlashingHologram hologram = storage.getHologram(args[0], type);
        if (hologram == null) {
            sender.sendRawMessage(messages.getHologramNotFoundMessage(args[0], type));
            return true;
        }
        String[] opts = Arrays.copyOfRange(args, 2, args.length);
        List<String> usedOptions = new ArrayList<>(Arrays.asList(opts));
        String prevOpt = "";
        boolean unsetPlayerCount = false;
        boolean unsetFlash = false;
        for (String opt : opts) {
            switch (opt) {
                case "distance":
                    if (hologram.getActivationDistance() == PeriodicHologramBase.NO_DISTANCE) {
                        sender.sendRawMessage(messages.getOptionNotSetMessage(opt));
                        usedOptions.remove(opt);
                    } else {
                        hologram.defaultDistance(settings);
                    }
                    break;
                case "seconds":
                    if (hologram.getShowTime() == PeriodicHologramBase.NO_SECONDS) {
                        sender.sendRawMessage(messages.getOptionNotSetMessage(opt));
                        usedOptions.remove(opt);
                    } else {
                        hologram.defaultShowtime(settings);
                    }
                    break;
                case "permission":
                    if (hologram.getPermissions() == null) {
                        sender.sendRawMessage(messages.getOptionNotSetMessage(opt));
                        usedOptions.remove(opt);
                    } else {
                        hologram.setPermissions(null);
                    }
                    break;
                case "flash":
                    if (!hologram.flashes()) {
                        sender.sendRawMessage(messages.getOptionNotSetMessage(opt));
                        usedOptions.remove(opt);
                        break;
                    }
                    hologram.setNoFlash();
                    usedOptions.remove(opt);
                    sender.sendRawMessage(messages.getUnsetFlashMessage());
                    unsetFlash = true;
                    break;
                case "playercount":
                    if (hologram.getType() != PeriodicType.NTIMES) {
                        sender.sendRawMessage(messages.getNoSuchOptionMessage(type, opt));
                        return true;
                    }
                    NTimesHologram ntimes = (NTimesHologram) hologram;
                    int optAt = 0;
                    for (String copt : opts) {
                        if (opt == copt)
                            break;
                        optAt++;
                    }
                    if (opts.length < optAt + 2) {
                        sender.sendRawMessage(messages.getNeedCountAfterPlayercount());
                        return true;
                    }
                    String playerName = opts[optAt + 1];
                    OfflinePlayer player = Bukkit.getPlayer(playerName);
                    if (player == null) {
                        UUID id = userCache.getUuid(playerName);
                        if (id != null) {
                            player = Bukkit.getOfflinePlayer(id);
                        }
                        if (player == null || !player.hasPlayedBefore()) {
                            sender.sendRawMessage(messages.getPlayerNotFoundMessage(playerName));
                            return true;
                        }
                    }
                    if (ntimes.getShownTo().get(player.getUniqueId()) == null) {
                        sender.sendRawMessage(
                                messages.getOptionNotSetMessage(String.format("%s (for %s)", opt, player.getName())));
                        usedOptions.remove(opt);
                        break;
                    }
                    ntimes.resetShownTo(player.getUniqueId());
                    unsetPlayerCount = true;
                    usedOptions.remove(opt);
                    sender.sendRawMessage(messages.getUnsetPlayerCountMessage(player));
                    break;
                case "time":
                    if (type == PeriodicType.MCTIME || type == PeriodicType.IRLTIME) {
                        sender.sendRawMessage(messages.getCannotUnSetRequiredMessage(opt, type));
                        usedOptions.remove(opt);
                        break;
                    }
                case "times":
                    if (type == PeriodicType.NTIMES) {
                        sender.sendRawMessage(messages.getCannotUnSetRequiredMessage(opt, type));
                        usedOptions.remove(opt);
                        break;
                    }
                default:
                    if (!prevOpt.equalsIgnoreCase("playercount")) {
                        sender.sendRawMessage(messages.getNoSuchOptionMessage(type, opt));
                    }
                    usedOptions.remove(opt);
                    break;
            }
            prevOpt = opt;
        }
        if (usedOptions.isEmpty()) {
            if (!unsetPlayerCount && !unsetFlash) {
                sender.sendRawMessage(messages.getNothingToUnsetMessage());
            } else {
                hologram.resetVisibility();
                storage.save(HologramSaveReason.CHANGE, false);
            }
            return true;
        }
        sender.sendRawMessage(messages.getUnsetOptionsMessage(usedOptions));
        hologram.resetVisibility();
        storage.save(HologramSaveReason.CHANGE, false);
        return true;
    }

}