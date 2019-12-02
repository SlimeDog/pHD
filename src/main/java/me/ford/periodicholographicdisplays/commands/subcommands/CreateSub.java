package me.ford.periodicholographicdisplays.commands.subcommands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.object.NamedHologram;
import com.gmail.filoghost.holographicdisplays.object.NamedHologramManager;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.StringUtil;

import me.ford.periodicholographicdisplays.PeriodicHolographicDisplays;
import me.ford.periodicholographicdisplays.Settings;
import me.ford.periodicholographicdisplays.commands.SubCommand;
import me.ford.periodicholographicdisplays.holograms.EverytimeHologram;
import me.ford.periodicholographicdisplays.holograms.HologramStorage;
import me.ford.periodicholographicdisplays.holograms.OnceHologram;
import me.ford.periodicholographicdisplays.holograms.PeriodicHologram;
import me.ford.periodicholographicdisplays.holograms.PeriodicType;
import me.ford.periodicholographicdisplays.util.TimeUtils;

/**
 * CreateSub
 */
public class CreateSub extends SubCommand {
    private static final String PERMS = "phd.commands.phd.create";
    private static final String USAGE = "/phd create <type> <name> <distance> <time> <args>";
    private final PeriodicHolographicDisplays plugin;
    private final HologramStorage storage;
    private final Settings settings;

    public CreateSub(HologramStorage storage, Settings settings) {
        this.plugin = JavaPlugin.getPlugin(PeriodicHolographicDisplays.class);
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
        if (!(sender instanceof Player)) {
            sender.sendMessage(settings.getNeedAPlayerMessage());
            return true;
        }
        Player player = (Player) sender;
        if (args.length < 3) {
            return false;
        }
        String name = args[1];
        PeriodicType type;
        try {
            type = PeriodicType.valueOf(args[0].toUpperCase());
        } catch (IllegalArgumentException e) {
            sender.sendMessage(settings.getTypeNotRecognizedMessage(args[0]));
            return true;
        }
        switch(type) {
            case PERIODIC:
            return createPeriodic(player, name, args);
            case ONCE:
            return createOnce(player, name, args);
            case EVERYTIME:
            return createEverytime(player, name, args);
        }
        return false;
    }

    private boolean createPeriodic(Player sender, String name, String[] args) {
        String usage = "/phd create PERIODIC <name> <distance> <time> <delay> <msg>";
        if (args.length < 6) {
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
        long showTimeTicks = showTimeMS/50L; // /1000L*20
        long showDelayMs;
        try {
            showDelayMs = TimeUtils.parseDateDiff(args[4]);
        } catch (IllegalArgumentException e) {
            sender.sendMessage(settings.getIllegalTimeMessage(args[3]));
            sender.sendMessage(usage); // TODO - should this be here?
            return true;
        }
        long showDelaySeconds = showDelayMs/1000L;
        String msg = String.join(" ", Arrays.copyOfRange(args, 5, args.length));
        Hologram hologram = HologramsAPI.createHologram(plugin, sender.getLocation());
        hologram.appendTextLine(settings.color(msg));
        storage.addHologram(new PeriodicHologram(hologram, name, activationDistance, showTimeTicks, hologram.getLocation(), showDelaySeconds));
        sender.sendMessage(settings.getCreatedPeriodicMessage(name, showTimeTicks, showDelaySeconds));
        return true;
    }

    private boolean createOnce(Player sender, String name, String[] args) {
        String usage = "/phd create ONCE <name> <distance> <time> <msg>";
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
        long showTimeTicks = showTimeMS/50L; // /1000L*20
        String msg = String.join(" ", Arrays.copyOfRange(args, 4, args.length));
        Hologram hologram = HologramsAPI.createHologram(plugin, sender.getLocation());
        hologram.appendTextLine(settings.color(msg));
        storage.addHologram(new OnceHologram(hologram, name, activationDistance, showTimeTicks, hologram.getLocation()));
        sender.sendMessage(settings.getCreatedOnceMessage(name, showTimeTicks));
        return true;
    }

    private boolean createEverytime(Player sender, String name, String[] args) {
        String usage = "/phd create EVERYTIME <name> <distance> <time> <msg>";
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
        long showTimeTicks = showTimeMS/50L; // /1000L*20
        String msg = String.join(" ", Arrays.copyOfRange(args, 4, args.length));
        Hologram hologram = HologramsAPI.createHologram(plugin, sender.getLocation());
        hologram.appendTextLine(settings.color(msg));
        storage.addHologram(new EverytimeHologram(hologram, name, activationDistance, showTimeTicks, hologram.getLocation()));
        sender.sendMessage(settings.getCreatedEveryTimeMessage(name, showTimeTicks));
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