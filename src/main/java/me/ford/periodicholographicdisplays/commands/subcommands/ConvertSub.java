package me.ford.periodicholographicdisplays.commands.subcommands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

import me.ford.periodicholographicdisplays.Messages;
import me.ford.periodicholographicdisplays.PeriodicHolographicDisplays;
import me.ford.periodicholographicdisplays.commands.SubCommand;
import me.ford.periodicholographicdisplays.holograms.storage.SQLStorage;
import me.ford.periodicholographicdisplays.holograms.storage.Storage;
import me.ford.periodicholographicdisplays.holograms.storage.YAMLStorage;
import me.ford.periodicholographicdisplays.holograms.storage.storageimport.StorageConverter;

/**
 * ImportSub
 */
public class ConvertSub extends SubCommand {
    private static final String PERMS = "phd.convert";
    private static final String USAGE = "/phd convert <sourceStorageType> <targetStorageType> [--force]";
    private final PeriodicHolographicDisplays phd;
    private final Messages messages;
    private final Map<Long, SQLStorage> sqlStorage = new HashMap<>(); // to close the SQLite connection
    private final List<String> storageTypes = Arrays.asList("sqlite", "yaml");

    public ConvertSub(PeriodicHolographicDisplays phd) {
        this.phd = phd;
        this.messages = phd.getMessages();
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        List<String> list = new ArrayList<>();
        if (args.length == 1) {
            return StringUtil.copyPartialMatches(args[0], storageTypes, list);
        } else if (args.length == 2) {
            List<String> types = new ArrayList<>(storageTypes);
            types.remove(args[0]);
            return StringUtil.copyPartialMatches(args[1], types, list);
        }
        return list;
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        if (args.length < 2) {
            return false;
        }
        boolean force = args.length > 2 && args[2].equalsIgnoreCase("--force");
        String from = args[0];
        String to = args[1];
        if (from.equalsIgnoreCase(to)) {
            sender.sendMessage(messages.getCannotConvertSameMessage(from));
            return true;
        }
        ConvertTypes type;
        if (from.equalsIgnoreCase("sqlite") && to.equalsIgnoreCase("yaml")) {
            type = ConvertTypes.SQLITE_TO_YAML;
        } else if (from.equalsIgnoreCase("yaml") && to.equalsIgnoreCase("sqlite")) {
            type = ConvertTypes.YAML_TO_SQLITE;
        } else {
            sender.sendMessage(messages.getUnrecognizedStorageTypeMessage(from, to));
            return true;
        }


        long start = System.currentTimeMillis();
        SQLStorage sqlStorage;
        YAMLStorage yamlStorage;
        if (phd.getSettings().useDatabase()) {
            sqlStorage = (SQLStorage) phd.getHolograms().getStorage();
            yamlStorage = new YAMLStorage();
        } else {
            sqlStorage = new SQLStorage(phd);
            this.sqlStorage.put(start, sqlStorage);
            yamlStorage = (YAMLStorage) phd.getHolograms().getStorage();
        }

        // set target and source
        Storage sourceStorage;
        Storage targetStorage;
        switch (type) {
            case YAML_TO_SQLITE:
            sourceStorage = yamlStorage;
            targetStorage = sqlStorage;
            break;
            case SQLITE_TO_YAML:
            sourceStorage = sqlStorage;
            targetStorage = yamlStorage;
            break;
            default:
            sender.sendMessage(messages.getUnrecognizedStorageTypeMessage(from, to));
            return true;
        }

        // find out if there is a previous instance of target storage type
        boolean hasData = targetStorage.hasData();
        if (hasData && !force) {
            sender.sendMessage(messages.getAlreadyHasDataMessage(to));
            return true;
        } else if (hasData) {
            targetStorage.clear();
        }

        WhenDone whenDone = new WhenDone(sender, start, from, to);
        StorageConverter<Storage, Storage> converter;
        converter = new StorageConverter<Storage, Storage>(sourceStorage, targetStorage, whenDone);
        converter.startConvert();
        sender.sendMessage(messages.getStartedConvertingMessage(from, to));
        return true;
    }

    private void closeSqlite(long start, String from, String to) {
        if (to.equals("sqlite")) {
            // TODO - perhaps an event?
            phd.getServer().getScheduler().runTaskLater(phd, () -> closeSqlite(start, from, "..."), 40L);
            return; // not saved yet
        }
        SQLStorage storage = sqlStorage.get(start);
        if (storage == null) {
            return;
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

    private final class WhenDone implements Runnable {
        private final CommandSender sender;
        private final long start;
        private final String from, to;

        private WhenDone(CommandSender sender, long start, String from, String to) {
            this.sender = sender;
            this.start = start;
            this.from = from;
            this.to = to;
        }

        @Override
        public void run() {
            phd.getServer().getScheduler().runTask(phd, () -> sender.sendMessage(messages.getDoneConvertingMessage(from, to))); // so it gets sent after the start message (for YAML mostly)
            closeSqlite(start, from, to);
        }

    }

    private enum ConvertTypes {
        SQLITE_TO_YAML,
        YAML_TO_SQLITE
    }
    
}