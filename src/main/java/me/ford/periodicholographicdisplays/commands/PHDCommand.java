package me.ford.periodicholographicdisplays.commands;

import org.bukkit.plugin.PluginManager;

import dev.ratas.slimedogcore.impl.commands.BukkitFacingParentCommand;
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
public class PHDCommand extends BukkitFacingParentCommand {
    private static final String USAGE = "/phd subcommand parameters (page {page}/{maxpage}):";
    private static final String HELP_HINT = "/phd help {page}";
    private final IPeriodicHolographicDisplays plugin;
    private final ConvertSub convertSub;

    public PHDCommand(IPeriodicHolographicDisplays plugin, PluginManager pm) {
        super();
        this.plugin = plugin;
        addSubCommand(new ListSub(plugin.getHDHoloManager(), plugin.getHolograms(), this.plugin.getMessages()));
        addSubCommand(new InfoSub(plugin.getHDHoloManager(), plugin.getHolograms(), this.plugin.getMessages()));
        addSubCommand(new ManageSub(plugin));
        addSubCommand(new ReportSub(plugin.getHDHoloManager(), plugin.getHolograms(), plugin.getMessages(),
                plugin.getUserCache()));
        addSubCommand(new SetSub(plugin.getHDHoloManager(), plugin.getHolograms(), plugin.getLuckPermsHook(),
                plugin.getSettings(), this.plugin.getMessages()));
        addSubCommand(new UnsetSub(plugin.getHDHoloManager(), plugin.getHolograms(), plugin.getSettings(),
                this.plugin.getMessages(), plugin.getUserCache()));
        addSubCommand(new UnmanageSub(plugin.getHDHoloManager(), plugin.getHolograms(), this.plugin.getMessages()));
        addSubCommand(new ReloadSub(plugin));
        convertSub = new ConvertSub(plugin, pm);
        addSubCommand(convertSub);
        if (plugin.getSettings().onDebug())
            addSubCommand(new PrintCacheSub(plugin));
    }

    public void reload() {
        getSubCommand("printcache"); // reload
    }

    public ConvertSub getConvertSub() {
        return convertSub;
    }

    protected String getUsage() {
        return USAGE;
    }

    protected String getHintCommand(int page) {
        return HELP_HINT.replace("{page}", String.valueOf(page));
    }

}