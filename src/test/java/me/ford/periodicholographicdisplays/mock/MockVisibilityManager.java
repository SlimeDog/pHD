package me.ford.periodicholographicdisplays.mock;

import com.gmail.filoghost.holographicdisplays.object.CraftHologram;
import com.gmail.filoghost.holographicdisplays.object.CraftVisibilityManager;

import org.bukkit.entity.Player;

public class MockVisibilityManager extends CraftVisibilityManager {

    public MockVisibilityManager(CraftHologram hologram) {
        super(hologram);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void hideTo(Player player) {
        // TODO Auto-generated method stub
    }

    @Override
    public boolean isVisibleByDefault() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isVisibleTo(Player player) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void resetVisibility(Player player) {
        // TODO Auto-generated method stub
    }

    @Override
    public void resetVisibilityAll() {
        // TODO Auto-generated method stub
    }

    @Override
    public void setVisibleByDefault(boolean player) {
        // TODO Auto-generated method stub
    }

    @Override
    public void showTo(Player player) {
        // TODO Auto-generated method stub
    }

}