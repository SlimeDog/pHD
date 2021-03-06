package me.ford.periodicholographicdisplays.commands.subcommands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;

import me.ford.periodicholographicdisplays.IPeriodicHolographicDisplays;
import me.ford.periodicholographicdisplays.commands.SubCommand;

public class PrintCacheSub extends SubCommand {
    private final IPeriodicHolographicDisplays phd;
    private static final String USAGE = "/phd printcache";
    private static final String PERMS = "phd.printcache";

    public PrintCacheSub(IPeriodicHolographicDisplays phd) {
        this.phd = phd;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return new ArrayList<>();
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        sender.sendMessage("CACHE:\n" + phd.getUserCache().getEntireCache());
        return true;
    }

    @Override
    public boolean hasPermission(CommandSender sender) {
        return sender.hasPermission(PERMS);
    }

    @Override
    public String getUsage(CommandSender sender, String[] args) {
        return USAGE;
    }

}