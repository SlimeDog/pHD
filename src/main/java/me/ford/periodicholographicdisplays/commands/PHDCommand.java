package me.ford.periodicholographicdisplays.commands;

import me.ford.periodicholographicdisplays.PeriodicHolographicDisplays;
import me.ford.periodicholographicdisplays.commands.subcommands.AdoptSub;
import me.ford.periodicholographicdisplays.commands.subcommands.CreateSub;
import me.ford.periodicholographicdisplays.commands.subcommands.InfoSub;
import me.ford.periodicholographicdisplays.commands.subcommands.ListSub;

/**
 * PHDCommand
 */
public class PHDCommand extends ParentCommand {
    private static final String USAGE = "/phd <subcommand> args";
    private final PeriodicHolographicDisplays plugin;

    public PHDCommand(PeriodicHolographicDisplays plugin) {
        this.plugin = plugin;
        addSubCommand("adopt", new AdoptSub(plugin.getHolograms(), this.plugin.getSettings()));
        addSubCommand("create", new CreateSub(plugin.getHolograms(), this.plugin.getSettings()));
        addSubCommand("list", new ListSub(plugin.getHolograms(), this.plugin.getSettings()));
        addSubCommand("info", new InfoSub(plugin.getHolograms(), this.plugin.getSettings()));
    }

    @Override
    protected String getUsage() {
        return USAGE;
    }
    
}