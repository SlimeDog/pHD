package me.ford.periodicholographicdisplays.holograms.wrap.visibility;

import org.bukkit.entity.Player;

import eu.decentsoftware.holograms.api.holograms.Hologram;

public class DecentHologramVisibilitySettings implements VisibilitySettings {
    private final Hologram delegate;

    public DecentHologramVisibilitySettings(Hologram delegate) {
        this.delegate = delegate;
    }

    @Override
    public void setGlobalVisibility(VisibilityState setting) {
        if (setting == VisibilityState.VISIBLE) {
            delegate.showAll();
        } else {
            delegate.hideAll();
        }
    }

    @Override
    public void clearIndividualVisibilities() {
        delegate.showAll(); // by default
    }

    @Override
    public void setIndividualVisibility(Player player, VisibilityState setting) {
        if (setting == VisibilityState.VISIBLE) {
            delegate.show(player, delegate.getPlayerPage(player));
        } else {
            delegate.hide(player);
        }
    }

}
