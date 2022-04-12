package me.ford.periodicholographicdisplays.holograms.wrap.platform;

import me.ford.periodicholographicdisplays.holograms.wrap.command.CommandWrapper;
import me.ford.periodicholographicdisplays.holograms.wrap.provider.HologramProvider;

public interface HologramPlatform {

    HologramProvider getHologramProvider();

    CommandWrapper getHologramCommand();

}
