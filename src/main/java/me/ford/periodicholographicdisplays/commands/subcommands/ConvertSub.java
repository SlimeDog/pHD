package me.ford.periodicholographicdisplays.commands.subcommands;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

import me.ford.periodicholographicdisplays.Messages;
import me.ford.periodicholographicdisplays.PeriodicHolographicDisplays;
import me.ford.periodicholographicdisplays.commands.SubCommand;
import me.ford.periodicholographicdisplays.holograms.storage.SQLStorage;
import me.ford.periodicholographicdisplays.holograms.storage.Storage;
import me.ford.periodicholographicdisplays.holograms.storage.YAMLStorage;
import me.ford.periodicholographicdisplays.holograms.storage.storageimport.StorageConverter;
import me.ford.periodicholographicdisplays.storage.sqlite.SQLStorageBase;
import me.ford.periodicholographicdisplays.users.YamlUserStorage;

/**
 * ImportSub
 */
public class ConvertSub extends SubCommand {
    private static final String SQLITE = "SQLITE";
    private static final String YAML = "YAML";
    private static final String PERMS = "phd.convert";
    private static final String USAGE = "/phd convert <sourceStorageType> <targetStorageType>";
    private final PeriodicHolographicDisplays phd;
    private final Messages messages;
    private SQLStorage sqlStorage = null; // to close the SQLite connection
    private final List<String> storageTypes = Arrays.asList(SQLITE, YAML);

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
        String from = args[0];
        String to = args[1];
        if (from.equalsIgnoreCase(to)) {
            sender.sendMessage(messages.getCannotConvertSameMessage(from));
            return true;
        }
        ConvertTypes type;
        if (from.equalsIgnoreCase(SQLITE) && to.equalsIgnoreCase(YAML)) {
            type = ConvertTypes.SQLITE_TO_YAML;
        } else if (from.equalsIgnoreCase(YAML) && to.equalsIgnoreCase(SQLITE)) {
            type = ConvertTypes.YAML_TO_SQLITE;
        } else {
            sender.sendMessage(messages.getUnrecognizedStorageTypeMessage(from, to));
            return true;
        }

        // find out if the source file exists
        File sourceFile = new File(phd.getDataFolder(), type == ConvertTypes.SQLITE_TO_YAML ? SQLStorageBase.DATABSE_NAME : YAMLStorage.FILE_NAME);
        if (!sourceFile.exists()) {
            sender.sendMessage(messages.getStorageTypeDoesNotExistMessage(from));
            return true;
        }

        YAMLStorage yamlStorage;
        if (phd.getSettings().useDatabase()) {
            if (sqlStorage == null)
                sqlStorage = (SQLStorage) phd.getHolograms().getStorage();
            yamlStorage = new YAMLStorage(phd);
        } else {
            if (sqlStorage == null)
                sqlStorage = new SQLStorage(phd);
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
        if (hasData) {
            sender.sendMessage(messages.getAlreadyHasDataMessage(to, targetStorage instanceof SQLStorage));
            return true;
        }

        WhenDone whenDone = new WhenDone(sender, from, to);
        StorageConverter<Storage, Storage> converter;
        converter = new StorageConverter<Storage, Storage>(sourceStorage, targetStorage, whenDone);
        if (type == ConvertTypes.SQLITE_TO_YAML) { // create .yml
            new YamlUserStorage(phd); // saves empty YAML file
        }
        converter.startConvert();
        sender.sendMessage(messages.getStartedConvertingMessage(from, to));
        return true;
    }

    private void closeSqlite(String from, String to) {
        if (phd.getSettings().useDatabase())
            return;
        if (to.equals(SQLITE)) {
            // TODO - perhaps an event?
            phd.getServer().getScheduler().runTaskLater(phd, () -> closeSqlite(from, "..."), 40L);
            return; // not saved yet
        }
        if (sqlStorage == null) {
            return;
        }
        phd.debug("Closing SQLite after convert");
        sqlStorage.close();
    }

    public SQLStorage getSqlStorage() {
        return sqlStorage;
    }

    @Override
    public boolean hasPermission(CommandSender sender) {
        return sender.hasPermission(PERMS);
    }

    @Override
    public String getUsage(CommandSender sender, String[] args) {
        return USAGE;
    }

    private final class WhenDone implements Runnable {
        private final CommandSender sender;
        private final String from, to;

        private WhenDone(CommandSender sender, String from, String to) {
            this.sender = sender;
            this.from = from;
            this.to = to;
        }

        @Override
        public void run() {
            phd.getServer().getScheduler().runTask(phd,
                    () -> sender.sendMessage(messages.getDoneConvertingMessage(from, to))); // so it gets sent after the
                                                                                            // start message (for YAML
                                                                                            // mostly)
            closeSqlite(from, to);
        }

    }

    private enum ConvertTypes {
        SQLITE_TO_YAML, YAML_TO_SQLITE
    }

}