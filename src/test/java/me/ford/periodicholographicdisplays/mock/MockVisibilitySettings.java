package me.ford.periodicholographicdisplays.mock;

import org.bukkit.entity.Player;

import me.filoghost.holographicdisplays.api.hologram.VisibilitySettings;

public class MockVisibilitySettings implements VisibilitySettings {

    @Override
    public void clearIndividualVisibilities() {
        // TODO Auto-generated method stub

    }

    @Override
    public Visibility getGlobalVisibility() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isVisibleTo(Player arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void removeIndividualVisibility(Player arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setGlobalVisibility(Visibility arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setIndividualVisibility(Player arg0, Visibility arg1) {
        // TODO Auto-generated method stub

    }

}
