package me.ford.periodicholographicdisplays.commands;

import org.bukkit.plugin.PluginManager;

import me.ford.periodicholographicdisplays.IPeriodicHolographicDisplays;
import me.ford.periodicholographicdisplays.commands.subcommands.ConvertSub;
import me.ford.periodicholographicdisplays.commands.subcommands.InfoSub;
import me.ford.periodicholographicdisplays.commands.subcommands.ListSub;
import me.ford.periodicholographicdisplays.commands.subcommands.ManageSub;
import me.ford.periodicholographicdisplays.commands.subcommands.PrintCacheSub;
import me.ford.periodicholographicdisplays.commands.subcommands.ReloadSub;
import me.ford.periodicholographicdisplays.commands.subcommands.ReportSub;
import me.ford.periodicholographicdisplays.commands.subcommands.SetSub;
import me.ford.periodicholographicdisplays.commands.subcommands.UnmanageSub;
import me.ford.periodicholographicdisplays.commands.subcommands.UnsetSub;

/**
 * PHDCommand
 */
public class PHDCommand extends ParentCommand {
    private static final String USAGE = "/phd subcommand parameters (page {page}/{maxpage}):";
    private static final String HELP_HINT = "/phd help {page}";
    private final IPeriodicHolographicDisplays plugin;
    private final ConvertSub convertSub;

    public PHDCommand(IPeriodicHolographicDisplays plugin, PluginManager pm) {
        super(plugin.getMessages());
        this.plugin = plugin;
        addSubCommand("list", new ListSub(plugin.getHolograms(), this.plugin.getMessages()));
        addSubCommand("info", new InfoSub(plugin.getHolograms(), this.plugin.getMessages()));
        addSubCommand("manage", new ManageSub(plugin));
        addSubCommand("report", new ReportSub(plugin.getHolograms(), plugin.getMessages(), plugin.getUserCache()));
        addSubCommand("set", new SetSub(plugin.getHolograms(), plugin.getLuckPermsHook(), plugin.getSettings(),
                this.plugin.getMessages()));
        addSubCommand("unset", new UnsetSub(plugin.getHolograms(), plugin.getSettings(), this.plugin.getMessages(),
                plugin.getUserCache()));
        addSubCommand("unmanage", new UnmanageSub(plugin.getHolograms(), this.plugin.getMessages()));
        addSubCommand("reload", new ReloadSub(plugin));
        convertSub = new ConvertSub(plugin, pm);
        addSubCommand("convert", convertSub);
        if(plugin.getSettings().onDebug()) addSubCommand("printcache", new PrintCacheSub(plugin));
    }

    public void reload() {
        removeSubCommand("printcache");
        if(plugin.getSettings().onDebug()) addSubCommand("printcache", new PrintCacheSub(plugin));
    }

    public ConvertSub getConvertSub() {
        return convertSub;
    }

    @Override
    protected String getUsage() {
        return USAGE;
    }

    @Override
    protected String getHintCommand(int page) {
        return HELP_HINT.replace("{page}", String.valueOf(page));
    }

}