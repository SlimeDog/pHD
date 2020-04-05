package me.ford.periodicholographicdisplays.commands.subcommands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;

import me.ford.periodicholographicdisplays.IPeriodicHolographicDisplays;
import me.ford.periodicholographicdisplays.commands.SubCommand;

public class PrintCacheSub extends SubCommand {
    private final IPeriodicHolographicDisplays phd;

    public PrintCacheSub(IPeriodicHolographicDisplays phd) {
        this.phd = phd;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return new ArrayList<>();
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        sender.sendMessage("CACHE:\n" + phd.getUserStorage().getCache().getEntireCache());
        return true;
    }

    @Override
    public boolean hasPermission(CommandSender sender) {
        return true;
    }

    @Override
    public String getUsage(CommandSender sender) {
        return "/phd printcache";
    }

}