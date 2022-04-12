package me.ford.periodicholographicdisplays.holograms.wrap.provider;

import java.util.ArrayList;
import java.util.List;

import eu.decentsoftware.holograms.api.DecentHolograms;
import eu.decentsoftware.holograms.api.DecentHologramsAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import me.ford.periodicholographicdisplays.holograms.wrap.DecentHologramWrapper;
import me.ford.periodicholographicdisplays.holograms.wrap.WrappedHologram;

public class DecentHologramsProvider implements HologramProvider {
    private final DecentHolograms decentHolograms;

    public DecentHologramsProvider() {
        decentHolograms = DecentHologramsAPI.get();
    }

    @Override
    public WrappedHologram getByName(String name) {
        Hologram holo = decentHolograms.getHologramManager().getHologram(name);
        if (holo == null) {
            return null;
        }
        return new DecentHologramWrapper(holo);
    }

    @Override
    public List<WrappedHologram> getAllHolograms() {
        List<WrappedHologram> list = new ArrayList<>();
        for (Hologram holo : decentHolograms.getHologramManager().getHolograms()) {
            list.add(new DecentHologramWrapper(holo));
        }
        return list;
    }

}
