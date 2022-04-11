package me.ford.periodicholographicdisplays.commands.subcommands;

import java.util.ArrayList;
import java.util.List;

import dev.ratas.slimedogcore.api.messaging.recipient.SDCRecipient;
import me.ford.periodicholographicdisplays.IPeriodicHolographicDisplays;
import me.ford.periodicholographicdisplays.commands.PHDSubCommand;

public class PrintCacheSub extends PHDSubCommand {
    private final IPeriodicHolographicDisplays phd;
    private static final String USAGE = "/phd printcache";
    private static final String PERMS = "phd.printcache";

    public PrintCacheSub(IPeriodicHolographicDisplays phd) {
        super(phd.getHDHoloManager(), "printcache", PERMS, USAGE);
        this.phd = phd;
    }

    @Override
    public List<String> onTabComplete(SDCRecipient sender, String[] args) {
        return new ArrayList<>();
    }

    @Override
    public boolean onCommand(SDCRecipient sender, String[] args, List<String> options) {
        sender.sendRawMessage("CACHE:\n" + phd.getUserCache().getEntireCache());
        return true;
    }

}