package me.ford.periodicholographicdisplays.commands.subcommands;

import java.util.ArrayList;
import java.util.List;

import com.gmail.filoghost.holographicdisplays.commands.CommandValidator;
import com.gmail.filoghost.holographicdisplays.exception.CommandException;
import com.gmail.filoghost.holographicdisplays.object.NamedHologram;
import com.gmail.filoghost.holographicdisplays.object.NamedHologramManager;

import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

import me.ford.periodicholographicdisplays.Settings;
import me.ford.periodicholographicdisplays.commands.SubCommand;
import me.ford.periodicholographicdisplays.holograms.HologramStorage;
import me.ford.periodicholographicdisplays.holograms.PeriodicType;
import me.ford.periodicholographicdisplays.util.TimeUtils;

/**
 * AdpotSub
 */
public class AdoptSub extends SubCommand {
    private static final String PERMS = "phd.commands.phd.adopt";
    private static final String USAGE = "/phd adopt <type> <name> <distance> <time> <args>";
    private final HologramStorage storage;
    private final Settings settings;

    public AdoptSub(HologramStorage storage, Settings settings) {
        this.storage = storage;
        this.settings = settings;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        List<String> list = new ArrayList<>();
        if (args.length == 1) {
            return StringUtil.copyPartialMatches(args[0], PeriodicType.names(), list);
        } else if (args.length == 2) {
            List<String> names = new ArrayList<>();
            for (NamedHologram holo : NamedHologramManager.getHolograms()) {
                if (holo != null) names.add(holo.getName());
            }
            return StringUtil.copyPartialMatches(args[1], names, list);
        } else if (args.length == 3) {
            return list;
        }
        return list;
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        if (args.length < 3) {
            return false;
        }
        String name = args[1];
        NamedHologram holo;
        try {
            holo = CommandValidator.getNamedHologram(name);
        } catch (CommandException e) {
            sender.sendMessage(settings.getHologramNotFoundMessage(name));
            return true;
        }
        PeriodicType type;
        try {
            type = PeriodicType.valueOf(args[0].toUpperCase());
        } catch (IllegalArgumentException e) {
            sender.sendMessage(settings.getTypeNotRecognizedMessage(args[0]));
            return true;
        }
        switch(type) {
            case PERIODIC:
            return adoptPeriodic(sender, name, holo, args);
            case ONCE:
            return adoptOnce(sender, name, holo, args);
            case EVERYTIME:
            return adoptEverytime(sender, name, holo, args);
            case NTIMES:
            return adoptNTimes(sender, name, holo, args);
            case ONJOIN:
            return adoptOnJoin(sender, name, holo, args);
            case WORLDJOIN:
            return adoptOnWorldJoinJoin(sender, name, holo, args);
        }
        return false;
    }

    private boolean adoptNTimes(CommandSender sender, String name, NamedHologram holo, String[] args) {
        String usage = "/phd adopt NTIMES <name> <distance> <time> [times]";
        if (args.length < 4) {
            sender.sendMessage(usage);
            return true;
        }
        double activationDistance;
        try {
            activationDistance = Double.parseDouble(args[2]);
        } catch (NumberFormatException e) {
            sender.sendMessage(settings.getNeedANumberMessage(args[2]));
            sender.sendMessage(usage); // TODO - should this be here?
            return true;
        }
        long showTimeMS;
        try {
            showTimeMS = TimeUtils.parseDateDiff(args[3]);
        } catch (IllegalArgumentException e) {
            sender.sendMessage(settings.getIllegalTimeMessage(args[3]));
            sender.sendMessage(usage); // TODO - should this be here?
            return true;
        }
        long showTimeTicks = showTimeMS/50L; // /1000L * 20
        int showTimes = 1;
        if (args.length > 4) {
            showTimes = Integer.parseInt(args[4]);
        }
        storage.adoptNTimes(holo, name, activationDistance, showTimeTicks, showTimes);
        sender.sendMessage(settings.getAdoptedNTimesMessage(name, showTimeTicks, showTimes));
        return true;
    }

    private boolean adoptOnWorldJoinJoin(CommandSender sender, String name, NamedHologram holo, String[] args) {
        String usage = "/phd adopt WORLDJOIN <name> <distance> <time>";
        if (args.length < 4) {
            sender.sendMessage(usage);
            return true;
        }
        double activationDistance;
        try {
            activationDistance = Double.parseDouble(args[2]);
        } catch (NumberFormatException e) {
            sender.sendMessage(settings.getNeedANumberMessage(args[2]));
            sender.sendMessage(usage); // TODO - should this be here?
            return true;
        }
        long showTimeMS;
        try {
            showTimeMS = TimeUtils.parseDateDiff(args[3]);
        } catch (IllegalArgumentException e) {
            sender.sendMessage(settings.getIllegalTimeMessage(args[3]));
            sender.sendMessage(usage); // TODO - should this be here?
            return true;
        }
        long showTimeTicks = showTimeMS/50L; // /1000L * 20
        storage.adoptOnWorldJoin(holo, name, activationDistance, showTimeTicks);
        sender.sendMessage(settings.getAdoptedOnWorldJoinMessage(name, showTimeTicks));
        return true;
    }

    private boolean adoptOnJoin(CommandSender sender, String name, NamedHologram holo, String[] args) {
        String usage = "/phd adopt ONJOIN <name> <distance> <time>";
        if (args.length < 4) {
            sender.sendMessage(usage);
            return true;
        }
        double activationDistance;
        try {
            activationDistance = Double.parseDouble(args[2]);
        } catch (NumberFormatException e) {
            sender.sendMessage(settings.getNeedANumberMessage(args[2]));
            sender.sendMessage(usage); // TODO - should this be here?
            return true;
        }
        long showTimeMS;
        try {
            showTimeMS = TimeUtils.parseDateDiff(args[3]);
        } catch (IllegalArgumentException e) {
            sender.sendMessage(settings.getIllegalTimeMessage(args[3]));
            sender.sendMessage(usage); // TODO - should this be here?
            return true;
        }
        long showTimeTicks = showTimeMS/50L; // /1000L * 20
        storage.adoptOnJoin(holo, name, activationDistance, showTimeTicks);
        sender.sendMessage(settings.getAdoptedOnJoinMessage(name, showTimeTicks));
        return true;
    }

    private boolean adoptPeriodic(CommandSender sender, String name, NamedHologram holo, String[] args) {
        String usage = "/phd adopt PERIODIC <name> <distance> <time> <delay>";
        if (args.length < 5) {
            sender.sendMessage(usage);
            return true;
        }
        double activationDistance;
        try {
            activationDistance = Double.parseDouble(args[2]);
        } catch (NumberFormatException e) {
            sender.sendMessage(settings.getNeedANumberMessage(args[2]));
            sender.sendMessage(usage); // TODO - should this be here?
            return true;
        }
        long showTimeMS;
        try {
            showTimeMS = TimeUtils.parseDateDiff(args[3]);
        } catch (IllegalArgumentException e) {
            sender.sendMessage(settings.getIllegalTimeMessage(args[3]));
            sender.sendMessage(usage); // TODO - should this be here?
            return true;
        }
        long showTimeTicks = showTimeMS/50L; // /1000L * 20
        long showDelayMs;
        try {
            showDelayMs = TimeUtils.parseDateDiff(args[4]);
        } catch (IllegalArgumentException e) {
            sender.sendMessage(settings.getIllegalTimeMessage(args[3]));
            sender.sendMessage(usage); // TODO - should this be here?
            return true;
        }
        long showDelaySeconds = showDelayMs/1000L;
        storage.adoptPeriodic(holo, name, activationDistance, showTimeTicks, showDelaySeconds);
        sender.sendMessage(settings.getAdoptedPeriodicMessage(name, showTimeTicks, showDelaySeconds));
        return true;
    }

    private boolean adoptOnce(CommandSender sender, String name, NamedHologram holo, String[] args) {
        String usage = "/phd adopt ONCE <name> <distance> <time>";
        if (args.length < 4) {
            sender.sendMessage(usage);
            return true;
        }
        double activationDistance;
        try {
            activationDistance = Double.parseDouble(args[2]);
        } catch (NumberFormatException e) {
            sender.sendMessage(settings.getNeedANumberMessage(args[2]));
            sender.sendMessage(usage); // TODO - should this be here?
            return true;
        }
        long showTimeMS;
        try {
            showTimeMS = TimeUtils.parseDateDiff(args[3]);
        } catch (IllegalArgumentException e) {
            sender.sendMessage(settings.getIllegalTimeMessage(args[3]));
            sender.sendMessage(usage); // TODO - should this be here?
            return true;
        }
        long showTimeTicks = showTimeMS/50L; // /1000L * 20
        storage.adoptOnce(holo, name, activationDistance, showTimeTicks);
        sender.sendMessage(settings.getAdoptedOnceMessage(name, showTimeTicks));
        return true;
    }

    private boolean adoptEverytime(CommandSender sender, String name, NamedHologram holo, String[] args) {
        String usage = "/phd adopt EVERYTIME <name> <distance> <time>";
        if (args.length < 4) {
            sender.sendMessage(usage);
            return true;
        }
        double activationDistance;
        try {
            activationDistance = Double.parseDouble(args[2]);
        } catch (NumberFormatException e) {
            sender.sendMessage(settings.getNeedANumberMessage(args[2]));
            sender.sendMessage(usage); // TODO - should this be here?
            return true;
        }
        long showTimeMS;
        try {
            showTimeMS = TimeUtils.parseDateDiff(args[3]);
        } catch (IllegalArgumentException e) {
            sender.sendMessage(settings.getIllegalTimeMessage(args[3]));
            sender.sendMessage(usage); // TODO - should this be here?
            return true;
        }
        long showTimeTicks = showTimeMS/50L; // /1000L * 20
        storage.adoptEveryTime(holo, name, activationDistance, showTimeTicks);
        sender.sendMessage(settings.getAdoptedEveryTimeMessage(name, showTimeTicks));
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