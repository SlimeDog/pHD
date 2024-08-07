package me.ford.periodicholographicdisplays.holograms.wrap.visibility;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

import org.bukkit.entity.Player;

import com.google.common.base.Supplier;

import de.oliver.fancyholograms.api.hologram.Hologram;

public class FancyHologramsVisibilitySettings implements VisibilitySettings {
    private final Function<UUID, Player> playerGetter;
    private final Supplier<Collection<Player>> allPlayersGetter;
    private final Hologram hologram;
    private final Consumer<Runnable> asyncScheduler;

    public FancyHologramsVisibilitySettings(Function<UUID, Player> playerGetter,
            Supplier<Collection<Player>> allPlayersGetter, Hologram hologram,
            Consumer<Runnable> asyncScheduler) {
        this.playerGetter = playerGetter;
        this.allPlayersGetter = allPlayersGetter;
        this.hologram = hologram;
        this.asyncScheduler = asyncScheduler;
    }

    @Override
    public void setGlobalVisibility(VisibilityState setting) {
        if (setting == VisibilityState.HIDDEN) {
            for (UUID id : hologram.getViewers()) {
                Player player = playerGetter.apply(id);
                hologram.hideHologram(player);
                hologram.refreshHologram(player);
            }
        } else if (setting == VisibilityState.VISIBLE) {
            Set<UUID> shown = hologram.getViewers();
            for (Player player : allPlayersGetter.get()) {
                if (!shown.contains(player.getUniqueId())) {
                    hologram.showHologram(player);
                    hologram.refreshHologram(player);
                }
            }
        }
    }

    @Override
    public void clearIndividualVisibilities() {
        // TODO - does this make any sense?
    }

    @Override
    public void setIndividualVisibility(Player player, VisibilityState setting) {
        if (setting == VisibilityState.HIDDEN) {
            asyncScheduler.accept(() -> hologram.forceHideHologram(player));
        } else {
            asyncScheduler.accept(() -> hologram.forceShowHologram(player));
        }
        hologram.refreshHologram(player);
    }

}
