package me.ford.periodicholographicdisplays.holograms.wrap.provider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

import org.bukkit.entity.Player;

import com.google.common.base.Supplier;

import de.oliver.fancyholograms.api.FancyHologramsPlugin;
import de.oliver.fancyholograms.api.Hologram;
import me.ford.periodicholographicdisplays.holograms.wrap.FancyHologramWrapper;
import me.ford.periodicholographicdisplays.holograms.wrap.WrappedHologram;

public class FancyHologramProvider implements HologramProvider {
    private final FancyHologramsPlugin fhPlugin;
    private final Function<UUID, Player> playerProvider;
    private final Supplier<Collection<Player>> allPlayersProvider;

    public FancyHologramProvider(FancyHologramsPlugin plugin) {
        fhPlugin = plugin;
        playerProvider = (id) -> fhPlugin.getPlugin().getServer().getPlayer(id);
        allPlayersProvider = () -> new ArrayList<>(fhPlugin.getPlugin().getServer().getOnlinePlayers());
    }

    @Override
    public WrappedHologram getByName(String name) {
        return new FancyHologramWrapper(playerProvider, allPlayersProvider,
                fhPlugin.getHologramManager().getHologram(name).get());
    }

    @Override
    public List<WrappedHologram> getAllHolograms() {
        List<WrappedHologram> allHolograms = new ArrayList<>();
        for (Hologram holo : fhPlugin.getHologramManager().getHolograms()) {
            allHolograms.add(new FancyHologramWrapper(playerProvider, allPlayersProvider, holo));
        }
        return allHolograms;
    }

}
