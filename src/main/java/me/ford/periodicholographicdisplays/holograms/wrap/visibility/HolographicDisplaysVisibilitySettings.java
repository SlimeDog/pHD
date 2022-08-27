package me.ford.periodicholographicdisplays.holograms.wrap.visibility;

import org.bukkit.entity.Player;

import me.filoghost.holographicdisplays.api.hologram.VisibilitySettings.Visibility;

public class HolographicDisplaysVisibilitySettings implements VisibilitySettings {
    private final me.filoghost.holographicdisplays.api.hologram.VisibilitySettings delegate;

    public HolographicDisplaysVisibilitySettings(
            me.filoghost.holographicdisplays.api.hologram.VisibilitySettings delegate) {
        this.delegate = delegate;
    }

    @Override
    public void setGlobalVisibility(VisibilityState setting) {
        delegate.setGlobalVisibility(translate(setting));
    }

    @Override
    public void clearIndividualVisibilities() {
        delegate.clearIndividualVisibilities();
    }

    @Override
    public void setIndividualVisibility(Player player, VisibilityState setting) {
        delegate.setIndividualVisibility(player, translate(setting));
    }

    public static Visibility translate(VisibilityState state) {
        if (state == VisibilityState.HIDDEN) {
            return Visibility.HIDDEN;
        }
        if (state == VisibilityState.VISIBLE) {
            return Visibility.VISIBLE;
        }
        throw new IllegalArgumentException("Unknown state: " + state);
    }

    public static VisibilityState asState(Visibility visibility) {
        if (visibility == Visibility.HIDDEN) {
            return VisibilityState.HIDDEN;
        }
        if (visibility == Visibility.VISIBLE) {
            return VisibilityState.VISIBLE;
        }
        throw new IllegalArgumentException("Unknown visibility: " + visibility);
    }

}
