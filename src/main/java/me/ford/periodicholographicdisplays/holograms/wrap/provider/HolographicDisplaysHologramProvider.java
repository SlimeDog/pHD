package me.ford.periodicholographicdisplays.holograms.wrap.provider;

import java.util.ArrayList;
import java.util.List;

import me.filoghost.holographicdisplays.plugin.internal.hologram.InternalHologram;
import me.filoghost.holographicdisplays.plugin.internal.hologram.InternalHologramManager;
import me.ford.periodicholographicdisplays.holograms.wrap.HolographicDisplaysWrapper;
import me.ford.periodicholographicdisplays.holograms.wrap.WrappedHologram;

public class HolographicDisplaysHologramProvider implements HologramProvider {
    private final InternalHologramManager hologramManager;

    public HolographicDisplaysHologramProvider(InternalHologramManager hologramManager) {
        this.hologramManager = hologramManager;
    }

    @Override
    public WrappedHologram getByName(String name) {
        InternalHologram ih = hologramManager.getHologramByName(name);
        if (ih == null) {
            return null;
        }
        return new HolographicDisplaysWrapper(ih);
    }

    @Override
    public List<WrappedHologram> getAllHolograms() {
        List<WrappedHologram> list = new ArrayList<>();
        for (InternalHologram ih : hologramManager.getHolograms()) {
            list.add(new HolographicDisplaysWrapper(ih));
        }
        return list;
    }

}
