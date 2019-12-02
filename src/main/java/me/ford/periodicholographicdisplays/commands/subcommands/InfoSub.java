package me.ford.periodicholographicdisplays.commands.subcommands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.StringUtil;

import me.ford.periodicholographicdisplays.PeriodicHolographicDisplays;
import me.ford.periodicholographicdisplays.Settings;
import me.ford.periodicholographicdisplays.commands.SubCommand;
import me.ford.periodicholographicdisplays.holograms.HologramStorage;
import me.ford.periodicholographicdisplays.holograms.PeriodicHologramBase;
import me.ford.periodicholographicdisplays.holograms.WorldHologramStorage;

/**
 * InfoSub
 */
public class InfoSub extends SubCommand {
    private static final String PERMS = "phd.commands.phd.info";
    private static final String USAGE = "/phd info <world> <name>";
    private final PeriodicHolographicDisplays plugin;
    private final HologramStorage storage;
    private final Settings settings;

    public InfoSub(HologramStorage storage, Settings settings) {
        this.plugin = JavaPlugin.getPlugin(PeriodicHolographicDisplays.class);
        this.storage = storage;
        this.settings = settings;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        List<String> list = new ArrayList<>();
        if (args.length == 1) {
            List<String> worldNames = new ArrayList<>();
            for (World world : plugin.getServer().getWorlds()) {
                worldNames.add(world.getName());
            }
            return StringUtil.copyPartialMatches(args[0], worldNames, list);
        }
        if (args.length == 2) {
            World world = plugin.getServer().getWorld(args[0]);
            if (world == null) return list;
            WorldHologramStorage holograms = storage.getHolograms(world);
            return StringUtil.copyPartialMatches(args[1], holograms.getHologramNames(), list);
        }
        return list;
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        if (args.length < 1) {
            return false;
        }
        World world = plugin.getServer().getWorld(args[0]);
        if (world == null) {
            sender.sendMessage(settings.getWorldNotFoundMessage(args[0]));
            return true;
        }
        WorldHologramStorage holograms = storage.getHolograms(world);
        PeriodicHologramBase hologram = holograms.getHologram(args[1]);
        if (hologram == null) {
            sender.sendMessage(settings.getHologramNotFoundMessage(args[1]));
            return true;
        }
        sender.sendMessage(settings.getHologramInfoMessage(hologram));
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