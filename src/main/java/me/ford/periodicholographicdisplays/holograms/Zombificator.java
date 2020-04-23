package me.ford.periodicholographicdisplays.holograms;

import com.gmail.filoghost.holographicdisplays.HolographicDisplays;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;

import me.ford.periodicholographicdisplays.IPeriodicHolographicDisplays;

public class Zombificator implements CommandExecutor {
    private final IPeriodicHolographicDisplays phd;
    private final HolographicDisplays hdPlugin;
    private final PluginCommand hologramCommand;
    private final HologramStorage holograms;
    private CommandExecutor hologramCommandExecutor;

    public Zombificator(IPeriodicHolographicDisplays phd) {
        this.phd = phd;
        this.holograms = phd.getHolograms();
        hdPlugin = JavaPlugin.getPlugin(HolographicDisplays.class);
        if (hdPlugin == null) {
            throw new IllegalStateException("HolographicDisplays not found!");
        }
        hologramCommand = hdPlugin.getCommand("holograms");
        if (hologramCommand == null) {
            throw new IllegalStateException("Expected command 'holograms' to be registered by HolographicDisplays");
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
            phd.getLogger().severe("Problem while executing HD command");
            e.printStackTrace();
        }
        if (response && args.length > 0 && (args[0].equalsIgnoreCase("delete") || args[0].equalsIgnoreCase("remove"))) {
            phd.debug("HD Hologram might have been removed - checking for zombies");
            holograms.checkForZombies();
        }
        return response;
    }

}