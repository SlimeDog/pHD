package me.ford.periodicholographicdisplays.mock;

import java.util.concurrent.ThreadLocalRandom;

import com.gmail.filoghost.holographicdisplays.object.NamedHologram;

import org.bukkit.Location;

public class MockNamedHologram extends NamedHologram {
    private static final ThreadLocalRandom rnd = ThreadLocalRandom.current();

    public MockNamedHologram(String name) {
        super(new Location(new MockWorld("someMockWorld"), rnd.nextDouble(), rnd.nextDouble(), rnd.nextDouble()), name);
    }

}