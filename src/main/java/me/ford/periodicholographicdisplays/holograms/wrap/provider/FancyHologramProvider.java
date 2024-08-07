package me.ford.periodicholographicdisplays.holograms.wrap.provider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.google.common.base.Supplier;

import de.oliver.fancyholograms.api.FancyHologramsPlugin;
import de.oliver.fancyholograms.api.hologram.Hologram;
import de.oliver.fancyholograms.api.events.HologramShowEvent;
import me.ford.periodicholographicdisplays.PeriodicHolographicDisplays;
import me.ford.periodicholographicdisplays.holograms.PeriodicType;
import me.ford.periodicholographicdisplays.holograms.wrap.FancyHologramWrapper;
import me.ford.periodicholographicdisplays.holograms.wrap.WrappedHologram;

public class FancyHologramProvider implements HologramProvider {
    private final FancyHologramsPlugin fhPlugin;
    private final PeriodicHolographicDisplays phd;
    private final Function<UUID, Player> playerProvider;
    private final Supplier<Collection<Player>> allPlayersProvider;
    private final Consumer<Runnable> asyncScheduler;

    public FancyHologramProvider(FancyHologramsPlugin plugin) {
        fhPlugin = plugin;
        phd = PeriodicHolographicDisplays.getPlugin(PeriodicHolographicDisplays.class);
        playerProvider = (id) -> fhPlugin.getPlugin().getServer().getPlayer(id);
        allPlayersProvider = () -> new ArrayList<>(fhPlugin.getPlugin().getServer().getOnlinePlayers());
        asyncScheduler = (runnable) -> phd.getScheduler().runTaskAsync(runnable);
        phd.getServer().getPluginManager().registerEvents(new HologramStatusListener(), phd);
    }

    @Override
    public WrappedHologram getByName(String name) {
        Hologram hologram;
        try {
            hologram = fhPlugin.getHologramManager().getHologram(name).get();
        } catch (NoSuchElementException e) {
            return null;
        }
        if (hologram == null) {
            return null;
        }
        return new FancyHologramWrapper(playerProvider, allPlayersProvider, hologram, asyncScheduler);
    }

    @Override
    public List<WrappedHologram> getAllHolograms() {
        List<WrappedHologram> allHolograms = new ArrayList<>();
        for (Hologram holo : fhPlugin.getHologramManager().getHolograms()) {
            allHolograms.add(new FancyHologramWrapper(playerProvider, allPlayersProvider, holo, asyncScheduler));
        }
        return allHolograms;
    }

    private class HologramStatusListener implements Listener {

        @EventHandler
        public void onHologramShow(HologramShowEvent event) {
            Hologram hologram = event.getHologram();
            String hologramName = hologram.getData().getName();
            List<PeriodicType> availableTypes = phd.getHolograms().getAvailableTypes(hologramName);
            if (availableTypes.isEmpty()) {
                return;
            }
            Player player = event.getPlayer();

            for (PeriodicType type : availableTypes) {
                if (phd.getHolograms().getHologram(hologramName, type).isBeingShownTo(player)) {
                    return;
                }
            }
            // if none should be shown, cancel event
            event.setCancelled(true);
        }
    }

}
