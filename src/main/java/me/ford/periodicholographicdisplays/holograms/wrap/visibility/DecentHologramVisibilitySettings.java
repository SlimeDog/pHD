package me.ford.periodicholographicdisplays.holograms.wrap.visibility;

import org.bukkit.entity.Player;

import eu.decentsoftware.holograms.api.holograms.Hologram;
import eu.decentsoftware.holograms.api.holograms.enums.EnumFlag;

public class DecentHologramVisibilitySettings implements VisibilitySettings {
    private final Hologram delegate;

    public DecentHologramVisibilitySettings(Hologram delegate) {
        this.delegate = delegate;
        delegate.addFlags(EnumFlag.DISABLE_UPDATING);
    }

    @Override
    public void setGlobalVisibility(VisibilityState setting) {
        if (setting == VisibilityState.VISIBLE) {
            delegate.setDefaultVisibleState(true);
        } else {
            delegate.setDefaultVisibleState(false);
        }
        delegate.updateAll();
    }

    @Override
    public void clearIndividualVisibilities() {
        delegate.setDefaultVisibleState(true);
        delegate.showAll(); // by default
        delegate.updateAll();
    }

    @Override
    public void setIndividualVisibility(Player player, VisibilityState setting) {
        if (setting == VisibilityState.HIDDEN) {
            delegate.removeShowPlayer(player);
            delegate.hide(player);
        } else {
            delegate.setShowPlayer(player);
            delegate.show(player, 0);
        }
        delegate.update(player);
    }

}
