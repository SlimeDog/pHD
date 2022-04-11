package me.ford.periodicholographicdisplays.commands.subcommands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import dev.ratas.slimedogcore.api.messaging.recipient.SDCRecipient;
import me.ford.periodicholographicdisplays.Messages;
import me.ford.periodicholographicdisplays.IPeriodicHolographicDisplays;
import me.ford.periodicholographicdisplays.PeriodicHolographicDisplays.DefaultReloadIssue;
import me.ford.periodicholographicdisplays.PeriodicHolographicDisplays.ReloadIssue;
import me.ford.periodicholographicdisplays.commands.PHDSubCommand;

/**
 * ReloadSub
 */
public class ReloadSub extends PHDSubCommand {
    private static final String PERMS = "phd.reload";
    private static final String USAGE = "/phd reload";
    private final IPeriodicHolographicDisplays phd;
    private final Messages messages;

    public ReloadSub(IPeriodicHolographicDisplays phd) {
        super(phd.getHologramProvider(), "reload", PERMS, USAGE);
        this.phd = phd;
        this.messages = phd.getMessages();
    }

    @Override
    public List<String> onTabComplete(SDCRecipient sender, String[] args) {
        return new ArrayList<>();
    }

    @Override
    public boolean onCommand(SDCRecipient sender, String[] args, List<String> options) {
        List<ReloadIssue> issues = phd.reload();
        if (issues.isEmpty()) {
            if (phd.getSettings().useDatabase() && (sender instanceof Player)) {
                sender.sendRawMessage(messages.getSqlConnectionMessage());
            }
            sender.sendRawMessage(messages.getConfigReloadedMessage());
            String typeMessage = messages.getActiveStorageMessage(phd.getSettings().useDatabase());
            sender.sendRawMessage(typeMessage);
            if (sender instanceof Player) {
                phd.getLogger().info(typeMessage);
            }
            if (phd.getConfig().isSet("debug")) {
                String debug = "DEBUG is " + phd.getSettings().onDebug();
                sender.sendRawMessage(debug);
                if (sender instanceof Player) {
                    phd.getLogger().info(debug);
                }
            }
        } else {
            String msg = messages.getProblemsReloadingConfigMessage(issues);
            phd.getLogger().severe(msg);
            if (sender instanceof Player) {
                sender.sendRawMessage(msg);
                boolean isBeingDisabled = false;
                for (ReloadIssue issue : issues) {
                    // in these cases, not disabling the plugin, just recreating
                    if (issue != DefaultReloadIssue.NO_CONFIG && issue != DefaultReloadIssue.NO_FOLDER
                            && issue != DefaultReloadIssue.NO_MESSAGES) {
                        isBeingDisabled = true;
                        break;
                    }
                }
                if (isBeingDisabled) {
                    sender.sendRawMessage(messages.getDisablingMessage());
                }
            }
        }
        return true;
    }

}