package me.ford.periodicholographicdisplays.commands.subcommands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import me.ford.periodicholographicdisplays.Messages;
import me.ford.periodicholographicdisplays.Settings;
import me.ford.periodicholographicdisplays.commands.SubCommand;
import me.ford.periodicholographicdisplays.holograms.HologramStorage;
import me.ford.periodicholographicdisplays.holograms.NTimesHologram;
import me.ford.periodicholographicdisplays.holograms.PeriodicHologramBase;
import me.ford.periodicholographicdisplays.holograms.PeriodicType;

/**
 * UnsetSub
 */
public class UnsetSub extends SubCommand {
    private static final String PERMS = "phd.set";
    private static final String USAGE = "/phd unset {hologram} {type} [seconds] [distance] [permission] [playercount {player}]";
    private final HologramStorage storage;
    private final Settings settings;
    private final Messages messages;
    private final List<String> optionList = Arrays.asList("seconds", "distance", "permission", "playercount");

    public UnsetSub(HologramStorage storage, Settings settings, Messages messages) {
        this.storage = storage;
        this.settings = settings;
        this.messages = messages;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        List<String> list = new ArrayList<>();
        switch(args.length) {
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
                type = PeriodicType.valueOf(args[1]);
            } catch (IllegalArgumentException e) {
                return list;
            }
            if (type == PeriodicType.NTIMES && args[args.length -2].equalsIgnoreCase("playercount")) {
                return null; // player names
            }
            PeriodicHologramBase hologram = storage.getHologram(args[0], type);
            if (hologram == null) return list;
            if (!hologram.hasPermissions()) {
                options.remove("permission");
            }
            if (hologram.getActivationDistance() == settings.getDefaultActivationDistance()) {
                options.remove("distance");
            }
            if (hologram.getShowTimeTicks() == settings.getDefaultShowTime() * 20L) {
                options.remove("seconds");
            }
            if (type != PeriodicType.NTIMES) {
                options.remove("playercount");
            }
            for (String prevArg : Arrays.copyOfRange(args, 2, args.length - 1)) {
                options.remove(prevArg);
            }
            return StringUtil.copyPartialMatches(args[args.length - 1], options, list);
        }
        return list;
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        if (args.length < 3) return false;
        PeriodicType type;
        try {
            type = PeriodicType.valueOf(args[1]);
        } catch (IllegalArgumentException e) {
            sender.sendMessage(messages.getTypeNotRecognizedMessage(args[1]));
            return true;
        }
        PeriodicHologramBase hologram = storage.getHologram(args[0], type);
        if (hologram == null) {
            sender.sendMessage(messages.getHologramNotFoundMessage(args[0], type));
            return true;
        }
        String[] opts = Arrays.copyOfRange(args, 2, args.length);
        List<String> usedOptions = new ArrayList<>(Arrays.asList(opts));
        String prevOpt = "";
        for (String opt : opts) {
            switch(opt) {
                case "distance":
                hologram.defaultDistance(settings);
                break;
                case "seconds":
                hologram.defaultShowtime(settings);
                break;
                case "permission":
                hologram.setPermissions(null);
                break;
                case "playercount":
                if (hologram.getType() != PeriodicType.NTIMES) {
                    sender.sendMessage(messages.getNoSuchOptionMessage(type, opt));
                    return true;
                }
                int optAt = 0;
                for (String copt : opts) {
                    if (opt == copt) break;
                    optAt++;
                }
                if (opts.length < optAt + 2) {
                    sender.sendMessage(messages.getNeedCountAfterPlayercount());
                    return true;
                }
                String playerName = opts[optAt + 1];
                Player player = Bukkit.getPlayer(playerName); // TODO - what about offline players?
                if (player == null) {
                    sender.sendMessage(messages.getPlayerNotFoundMessage(playerName));
                    return true;
                }
                ((NTimesHologram) hologram).resetShownTo(player.getUniqueId());
                usedOptions.remove(opt);
                usedOptions.add(String.format("%s:%s", opt, playerName));
                break;
                case "time":
                if (type == PeriodicType.MCTIME || type == PeriodicType.IRLTIME) {
                    sender.sendMessage(messages.getCannotUnSetRequiredMessage(opt, type));
                    usedOptions.remove(opt);
                    break;
                }
                case "times":
                if (type == PeriodicType.NTIMES) {
                    sender.sendMessage(messages.getCannotUnSetRequiredMessage(opt, type));
                    usedOptions.remove(opt);
                    break;
                }
                default:
                if (!prevOpt.equalsIgnoreCase("playercount")) {
                    sender.sendMessage(messages.getNoSuchOptionMessage(type, opt));
                }
                usedOptions.remove(opt);
                break;
            }
            prevOpt = opt;
        }
        if (usedOptions.isEmpty()) {
            sender.sendMessage(messages.getNothingToUnsetMessage());
            return true;
        }
        sender.sendMessage(messages.getUnsetOptionsMessage(usedOptions));
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