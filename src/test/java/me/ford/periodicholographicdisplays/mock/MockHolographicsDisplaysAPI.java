package me.ford.periodicholographicdisplays.mock;

import java.util.Collection;

import org.bukkit.Location;

import me.filoghost.holographicdisplays.api.HolographicDisplaysAPI;
import me.filoghost.holographicdisplays.api.Position;
import me.filoghost.holographicdisplays.api.hologram.Hologram;
import me.filoghost.holographicdisplays.api.placeholder.GlobalPlaceholder;
import me.filoghost.holographicdisplays.api.placeholder.GlobalPlaceholderFactory;
import me.filoghost.holographicdisplays.api.placeholder.GlobalPlaceholderReplaceFunction;
import me.filoghost.holographicdisplays.api.placeholder.IndividualPlaceholder;
import me.filoghost.holographicdisplays.api.placeholder.IndividualPlaceholderFactory;
import me.filoghost.holographicdisplays.api.placeholder.IndividualPlaceholderReplaceFunction;

public class MockHolographicsDisplaysAPI implements HolographicDisplaysAPI {

    @Override
    public Hologram createHologram(Location arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Hologram createHologram(Position pos) {
        return new FakeHologram(pos);
    }

    @Override
    public void deleteHolograms() {
        // TODO Auto-generated method stub

    }

    @Override
    public Collection<Hologram> getHolograms() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Collection<String> getRegisteredPlaceholders() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isRegisteredPlaceholder(String arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void registerGlobalPlaceholder(String arg0, GlobalPlaceholder arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void registerGlobalPlaceholder(String arg0, int arg1,
            GlobalPlaceholderReplaceFunction arg2) {
        // TODO Auto-generated method stub

    }

    @Override
    public void registerGlobalPlaceholderFactory(String arg0, GlobalPlaceholderFactory arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void registerIndividualPlaceholder(String arg0, IndividualPlaceholder arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void registerIndividualPlaceholder(String arg0, int arg1,
            IndividualPlaceholderReplaceFunction arg2) {
        // TODO Auto-generated method stub

    }

    @Override
    public void registerIndividualPlaceholderFactory(String arg0, IndividualPlaceholderFactory arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void unregisterPlaceholder(String arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void unregisterPlaceholders() {
        // TODO Auto-generated method stub

    }

}
