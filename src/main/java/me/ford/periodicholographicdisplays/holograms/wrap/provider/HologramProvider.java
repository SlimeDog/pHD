package me.ford.periodicholographicdisplays.holograms.wrap.provider;

import java.util.List;

import me.ford.periodicholographicdisplays.holograms.wrap.WrappedHologram;

public interface HologramProvider {

    WrappedHologram getByName(String name);

    List<WrappedHologram> getAllHolograms();

}
