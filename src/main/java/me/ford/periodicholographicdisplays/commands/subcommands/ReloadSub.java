package me.ford.periodicholographicdisplays.commands.subcommands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;

import me.ford.periodicholographicdisplays.Messages;
import me.ford.periodicholographicdisplays.PeriodicHolographicDisplays;
import me.ford.periodicholographicdisplays.commands.SubCommand;

/**
 * ReloadSub
 */
public class ReloadSub extends SubCommand {
    private static final String PERMS = "phd.commands.phd.reload";
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
        if (phd.reload()) {
            sender.sendMessage(messages.getConfigReloadedMessage());
        } else {
            sender.sendMessage(messages.getProblemsReloadingConfigMessage());
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