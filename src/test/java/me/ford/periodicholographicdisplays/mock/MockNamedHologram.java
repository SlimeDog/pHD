package me.ford.periodicholographicdisplays.mock;

import java.util.concurrent.ThreadLocalRandom;

import me.filoghost.holographicdisplays.plugin.hologram.base.ImmutablePosition;
import me.filoghost.holographicdisplays.plugin.hologram.tracking.LineTrackerManager;
import me.filoghost.holographicdisplays.plugin.internal.hologram.InternalHologram;

public class MockNamedHologram extends InternalHologram {
    private static final ThreadLocalRandom rnd = ThreadLocalRandom.current();

    public MockNamedHologram(String name, LineTrackerManager ltm) {
        super(new ImmutablePosition("someMockWorld", rnd.nextDouble(), rnd.nextDouble(), rnd.nextDouble()),
                name, ltm);
    }

}