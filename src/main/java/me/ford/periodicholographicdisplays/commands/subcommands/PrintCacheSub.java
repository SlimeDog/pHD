package me.ford.periodicholographicdisplays.commands.subcommands;

import java.util.ArrayList;
import java.util.List;

import dev.ratas.slimedogcore.api.commands.SDCCommandOptionSet;
import dev.ratas.slimedogcore.api.messaging.recipient.SDCRecipient;
import me.ford.periodicholographicdisplays.IPeriodicHolographicDisplays;
import me.ford.periodicholographicdisplays.commands.PHDSubCommand;

public class PrintCacheSub extends PHDSubCommand {
    private final IPeriodicHolographicDisplays phd;
    private static final String USAGE = "/phd printcache";
    private static final String PERMS = "phd.printcache";

    public PrintCacheSub(IPeriodicHolographicDisplays phd) {
        super(phd.getHologramProvider(), "printcache", PERMS, USAGE);
        this.phd = phd;
    }

    @Override
    public List<String> onTabComplete(SDCRecipient sender, String[] args) {
        return new ArrayList<>();
    }

    @Override
    public boolean onOptionedCommand(SDCRecipient sender, String[] args, SDCCommandOptionSet options) {
        sender.sendRawMessage("CACHE:\n" + phd.getUserCache().getEntireCache());
        return true;
    }

}