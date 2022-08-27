package me.ford.periodicholographicdisplays.mock;

import org.bukkit.Location;
import org.bukkit.World;

import me.filoghost.holographicdisplays.api.beta.Position;
import me.filoghost.holographicdisplays.api.beta.hologram.Hologram;
import me.filoghost.holographicdisplays.api.beta.hologram.HologramLines;
import me.filoghost.holographicdisplays.api.beta.hologram.PlaceholderSetting;
import me.filoghost.holographicdisplays.api.beta.hologram.VisibilitySettings;

public class FakeHologram implements Hologram {
    private final Position pos;
    private final MockVisibilitySettings visSettings;

    public FakeHologram(Position pos) {
        this.pos = pos;
        visSettings = new MockVisibilitySettings();
    }

    @Override
    public void delete() {
        // TODO Auto-generated method stub

    }

    @Override
    public HologramLines getLines() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public PlaceholderSetting getPlaceholderSetting() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Position getPosition() {
        return pos;
    }

    @Override
    public VisibilitySettings getVisibilitySettings() {
        return visSettings;
    }

    @Override
    public boolean isDeleted() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setPlaceholderSetting(PlaceholderSetting arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setPosition(Position arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setPosition(Location arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setPosition(String arg0, double arg1, double arg2, double arg3) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setPosition(World arg0, double arg1, double arg2, double arg3) {
        // TODO Auto-generated method stub

    }

}
