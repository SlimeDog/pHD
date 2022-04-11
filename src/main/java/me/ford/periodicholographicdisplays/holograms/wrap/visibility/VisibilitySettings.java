package me.ford.periodicholographicdisplays.holograms.wrap.visibility;

import org.bukkit.entity.Player;

public interface VisibilitySettings {

    void setGlobalVisibility(VisibilityState setting);

    void clearIndividualVisibilities();

    void setIndividualVisibility(Player player, VisibilityState setting);

}
