package me.ford.periodicholographicdisplays.commands.subcommands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private final Map<Long, SQLStorage> sqlStorage = new HashMap<>(); // to close the SQLite connection

    public ConvertSub(PeriodicHolographicDisplays phd) {
        this.phd = phd;
        this.messages = phd.getMessages();
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        List<String> list = new ArrayList<>();
        if (args.length == 1) {
            if (!phd.getSettings().useDatabase()) {
                list.add("sqlite");
            } else {
                list.add("yaml");
            }
            return StringUtil.copyPartialMatches(args[0], list, new ArrayList<>());
        } else if (args.length == 2) {
            if (phd.getSettings().useDatabase()) {
                list.add("sqlite");
            } else {
                list.add("yaml");
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
        if (useDatabase && (!from.equalsIgnoreCase("yaml") || !to.equalsIgnoreCase("sqlite"))) {
            sender.sendMessage(messages.getCannotImportFromMessage(from));
            return true;
        } else if (!useDatabase && (!from.equalsIgnoreCase("sqlite") || !to.equalsIgnoreCase("yaml"))) {
            sender.sendMessage(messages.getCannotImportToMessage(to));
            return true;
        }
        long start = System.currentTimeMillis();
        HologramsLoadedListener listener = new HologramsLoadedListener(() -> {
            phd.getServer().getScheduler().runTask(phd, () -> sender.sendMessage(messages.getDoneImportingMessage(from))); // so it gets sent after the start message (for YAML mostly)
            if (!useDatabase) closeSqlite(start);
        });
        phd.getServer().getPluginManager().registerEvents(listener, phd);
        if (useDatabase) {
            new HologramImporter<YAMLStorage>(new YAMLStorage(), (info) -> loaded(info)).startImport();
        } else {
            SQLStorage sqlite = new SQLStorage(phd);
            new HologramImporter<SQLStorage>(sqlite, (info) -> loaded(info)).startImport();
            sqlStorage.put(start, sqlite);
        }
        sender.sendMessage(messages.getStartedImportingMessage(from));
        return true;
    }

    private void loaded(HDHologramInfo info) {
        phd.getHolograms().imported(info);
    }

    private void closeSqlite(long start) {
        SQLStorage storage = sqlStorage.get(start);
        if (storage == null) {
            phd.getLogger().warning("Problem while importing data from SQLite database - trying to close a non-existant database");
        }
        storage.close();
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