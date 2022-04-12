package me.ford.periodicholographicdisplays.holograms.wrap.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;

import me.ford.periodicholographicdisplays.holograms.Zombificator;

public class ExecutorWrapper implements CommandExecutor, CommandWrapper {
    // private final HologramStorage holograms;
    private final CommandExecutor hologramCommandExecutor;
    private Zombificator zombificator;

    public ExecutorWrapper(PluginCommand hologramCommand) {
        if (hologramCommand == null) {
            throw new IllegalStateException("Expected command the main command to be registered by platform");
        }
        hologramCommandExecutor = hologramCommand.getExecutor();
        if (hologramCommandExecutor instanceof TabCompleter) { // in case there's a TabExecutor that's not been
                                                               // registered as an
            // executor explicitly. If the command executor implements TabCompleter,
            // its instance is used for tab completion in case none is specified.
            // This way I'm making sure this defualt behaviour is not gone (if
            // previously present - which it's not at the time of writing)
            hologramCommand.setTabCompleter((TabCompleter) hologramCommandExecutor);
        }
        hologramCommand.setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        boolean response;
        try {
            response = hologramCommandExecutor.onCommand(sender, command, label, args);
        } catch (Exception e) {
            response = false;
            JavaPlugin.getProvidingPlugin(ExecutorWrapper.class).getLogger()
                    .severe("Problem while executing HD command");
            e.printStackTrace();
        }
        if (response && args.length > 1 && (args[0].equalsIgnoreCase("delete") || args[0].equalsIgnoreCase("remove"))) {
            zombificator.foundRemoved(args[1]);
        }
        return response;
    }

    @Override
    public void wrapWith(Zombificator zombificator) {
        this.zombificator = zombificator;
    }

}
