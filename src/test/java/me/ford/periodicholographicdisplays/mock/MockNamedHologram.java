package me.ford.periodicholographicdisplays.mock;

import java.util.concurrent.ThreadLocalRandom;

import com.gmail.filoghost.holographicdisplays.object.CraftVisibilityManager;
import com.gmail.filoghost.holographicdisplays.object.NamedHologram;

import org.bukkit.Location;

public class MockNamedHologram extends NamedHologram {
    private static final ThreadLocalRandom rnd = ThreadLocalRandom.current();
    private final CraftVisibilityManager vm = new MockVisibilityManager(this);

    public MockNamedHologram(String name) {
        super(new Location(new MockWorld("someMockWorld"), rnd.nextDouble(), rnd.nextDouble(), rnd.nextDouble()), name);
    }

    @Override
    public CraftVisibilityManager getVisibilityManager() {
        return vm;
    }

}