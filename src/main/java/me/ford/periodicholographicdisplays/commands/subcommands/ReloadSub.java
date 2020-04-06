package me.ford.periodicholographicdisplays.commands.subcommands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.ford.periodicholographicdisplays.Messages;
import me.ford.periodicholographicdisplays.PeriodicHolographicDisplays;
import me.ford.periodicholographicdisplays.PeriodicHolographicDisplays.DefaultReloadIssue;
import me.ford.periodicholographicdisplays.PeriodicHolographicDisplays.ReloadIssue;
import me.ford.periodicholographicdisplays.commands.SubCommand;

/**
 * ReloadSub
 */
public class ReloadSub extends SubCommand {
    private static final String PERMS = "phd.reload";
    private static final String USAGE = "/phd reload";
    private final PeriodicHolographicDisplays phd;
    private final Messages messages;

    public ReloadSub(PeriodicHolographicDisplays phd) {
        this.phd = phd;
        this.messages = phd.getMessages();
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return new ArrayList<>();
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        List<ReloadIssue> issues = phd.reload();
        if (issues.isEmpty()) {
            if (phd.getSettings().useDatabase() && (sender instanceof Player)) {
                sender.sendMessage(messages.getSqlConnectionMessage());
            }
            sender.sendMessage(messages.getConfigReloadedMessage());
            String typeMessage = messages.getActiveStorageMessage(phd.getSettings().useDatabase());
            sender.sendMessage(typeMessage);
            if (sender instanceof Player) {
                phd.getLogger().info(typeMessage);
            }
            if (phd.getConfig().isSet("debug")) {
                String debug = "DEBUG is " + phd.getSettings().onDebug();
                sender.sendMessage(debug);
                if (sender instanceof Player) {
                    phd.getLogger().info(debug);
                }
            }
        } else {
            String msg = messages.getProblemsReloadingConfigMessage(issues);
            phd.getLogger().severe(msg);
            if (sender instanceof Player) {
                sender.sendMessage(msg);
                boolean isBeingDisabled = false;
                for (ReloadIssue issue : issues) {
                    // in these cases, not disabling the plugin, just recreating
                    if (issue != DefaultReloadIssue.NO_CONFIG && issue != DefaultReloadIssue.NO_FOLDER && issue != DefaultReloadIssue.NO_MESSAGES) {
                        isBeingDisabled = true;
                        break;
                    }
                }
                if (isBeingDisabled) {
                    sender.sendMessage(messages.getDisablingMessage());
                }
            }
        }
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