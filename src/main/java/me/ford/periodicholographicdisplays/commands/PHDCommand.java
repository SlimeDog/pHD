package me.ford.periodicholographicdisplays.commands;

import me.ford.periodicholographicdisplays.PeriodicHolographicDisplays;
import me.ford.periodicholographicdisplays.commands.subcommands.InfoSub;
import me.ford.periodicholographicdisplays.commands.subcommands.ListSub;
import me.ford.periodicholographicdisplays.commands.subcommands.ReloadSub;
import me.ford.periodicholographicdisplays.commands.subcommands.SetSub;
import me.ford.periodicholographicdisplays.commands.subcommands.UnmanageSub;
import me.ford.periodicholographicdisplays.commands.subcommands.UnsetSub;

/**
 * PHDCommand
 */
public class PHDCommand extends ParentCommand {
    private static final String USAGE = "/phd <subcommand> args";
    private final PeriodicHolographicDisplays plugin;

    public PHDCommand(PeriodicHolographicDisplays plugin) {
        this.plugin = plugin;
        addSubCommand("list", new ListSub(plugin.getHolograms(), this.plugin.getMessages()));
        addSubCommand("info", new InfoSub(plugin.getHolograms(), this.plugin.getMessages()));
        addSubCommand("set", new SetSub(plugin.getHolograms(), plugin.getSettings(), this.plugin.getMessages()));
        addSubCommand("unset", new UnsetSub(plugin.getHolograms(), plugin.getSettings(), this.plugin.getMessages()));
        addSubCommand("unmanage", new UnmanageSub(plugin.getHolograms(), this.plugin.getMessages()));
        addSubCommand("reload", new ReloadSub(plugin));
    }

    @Override
    protected String getUsage() {
        return USAGE;
    }
    
}