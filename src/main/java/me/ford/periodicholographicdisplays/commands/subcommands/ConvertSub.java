package me.ford.periodicholographicdisplays.commands.subcommands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

import me.ford.periodicholographicdisplays.Messages;
import me.ford.periodicholographicdisplays.PeriodicHolographicDisplays;
import me.ford.periodicholographicdisplays.commands.SubCommand;
import me.ford.periodicholographicdisplays.holograms.storage.SQLStorage;
import me.ford.periodicholographicdisplays.holograms.storage.YAMLStorage;
import me.ford.periodicholographicdisplays.holograms.storage.Storage.HDHologramInfo;
import me.ford.periodicholographicdisplays.holograms.storage.storageimport.HologramImporter;
import me.ford.periodicholographicdisplays.listeners.HologramsLoadedListener;

/**
 * ImportSub
 */
public class ConvertSub extends SubCommand {
    private static final String PERMS = "phd.convert";
    private static final String USAGE = "/phd convert <oldStorageType> <newStorageType>";
    private final PeriodicHolographicDisplays phd;
    private final Messages messages;

    public ConvertSub(PeriodicHolographicDisplays phd) {
        this.phd = phd;
        this.messages = phd.getMessages();
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        List<String> list = new ArrayList<>();
        if (args.length == 1) {
            if (!phd.getSettings().useDatabase()) {
                list.add("yaml");
            } else {
                list.add("sqlite");
            }
            return StringUtil.copyPartialMatches(args[0], list, new ArrayList<>());
        } else if (args.length == 2) {
            if (phd.getSettings().useDatabase()) {
                list.add("yaml");
            } else {
                list.add("sqlite");
            }
            return StringUtil.copyPartialMatches(args[1], list, new ArrayList<>());
        }
        return list;
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        if (args.length < 2) {
            return false;
        }
        String from = args[0];
        String to = args[1];
        if (from.equalsIgnoreCase(to)) {
            sender.sendMessage(messages.getCannotImportSameMessage(from));
            return true;
        }
        boolean useDatabase = phd.getSettings().useDatabase();
        if (useDatabase && !from.equalsIgnoreCase("sqlite")) {
            sender.sendMessage(messages.getCannotImportFromMessage(from));
            return true;
        } else if (!useDatabase && !to.equalsIgnoreCase("yaml")) {
            sender.sendMessage(messages.getCannotImportToMessage(to));
            return true;
        }
        HologramsLoadedListener listener = new HologramsLoadedListener(() -> {
            sender.sendMessage(messages.getDoneImportingMessage(from));
        });
        if (useDatabase) {
            new HologramImporter<SQLStorage>(new SQLStorage(phd), (info) -> loaded(info));
        } else {
            new HologramImporter<YAMLStorage>(new YAMLStorage(), (info) -> loaded(info));
        }
        sender.sendMessage(messages.getStartedImportingMessage(from));
        phd.getServer().getPluginManager().registerEvents(listener, phd);
        return true;
    }

    private void loaded(HDHologramInfo info) {
        phd.getHolograms().imported(info);
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