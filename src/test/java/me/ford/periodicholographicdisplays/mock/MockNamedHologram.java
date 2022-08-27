package me.ford.periodicholographicdisplays.mock;

import java.util.concurrent.ThreadLocalRandom;

import me.filoghost.holographicdisplays.api.HolographicDisplaysAPI;
import me.filoghost.holographicdisplays.core.base.ImmutablePosition;
import me.filoghost.holographicdisplays.plugin.internal.hologram.InternalHologram;

public class MockNamedHologram extends InternalHologram {
    private static final ThreadLocalRandom rnd = ThreadLocalRandom.current();

    public MockNamedHologram(HolographicDisplaysAPI api, String name) {
        super(api, name, new ImmutablePosition(mockWorld("someMockWorld"), rnd.nextDouble(), rnd.nextDouble(),
                rnd.nextDouble()));
    }

    private static String mockWorld(String name) {
        new MockWorld(name); // make sure it's registered
        return name;
    }

}