package me.ford.periodicholographicdisplays.mock;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.VisibilityManager;
import com.gmail.filoghost.holographicdisplays.api.line.HologramLine;
import com.gmail.filoghost.holographicdisplays.api.line.ItemLine;
import com.gmail.filoghost.holographicdisplays.api.line.TextLine;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;

public class MockHologram implements Hologram {
    private final VisibilityManager vm = new MockVisibilityManager();
    private final World world = new MockWorld("mockery");
    private final Random rnd = ThreadLocalRandom.current();
    private final Location loc = new Location(world, rnd.nextDouble(), rnd.nextDouble(), rnd.nextDouble());

    @Override
    public ItemLine appendItemLine(ItemStack arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TextLine appendTextLine(String arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void clearLines() {
        // TODO Auto-generated method stub

    }

    @Override
    public void delete() {
        // TODO Auto-generated method stub

    }

    @Override
    public long getCreationTimestamp() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public double getHeight() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public HologramLine getLine(int arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Location getLocation() {
        return loc;
    }

    @Override
    public VisibilityManager getVisibilityManager() {
        return vm;
    }

    @Override
    public World getWorld() {
        return world;
    }

    @Override
    public double getX() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public double getY() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public double getZ() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public ItemLine insertItemLine(int arg0, ItemStack arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TextLine insertTextLine(int arg0, String arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isAllowPlaceholders() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isDeleted() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void removeLine(int arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setAllowPlaceholders(boolean arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public int size() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void teleport(Location arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void teleport(World arg0, double arg1, double arg2, double arg3) {
        // TODO Auto-generated method stub

    }

}