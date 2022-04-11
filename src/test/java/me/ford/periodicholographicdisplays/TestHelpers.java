package me.ford.periodicholographicdisplays;

import java.util.Collection;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Location;
import org.junit.Assert;

import me.ford.periodicholographicdisplays.holograms.AlwaysHologram;
import me.ford.periodicholographicdisplays.holograms.FlashingHologram;
import me.ford.periodicholographicdisplays.holograms.IRLTimeHologram;
import me.ford.periodicholographicdisplays.holograms.MCTimeHologram;
import me.ford.periodicholographicdisplays.holograms.NTimesHologram;
import me.ford.periodicholographicdisplays.holograms.PeriodicType;
import me.ford.periodicholographicdisplays.holograms.wrap.HolographicDisplaysWrapper;
import me.ford.periodicholographicdisplays.mock.MockLineTrackerManager;
import me.ford.periodicholographicdisplays.mock.MockNamedHologram;

public abstract class TestHelpers {
    private final ThreadLocalRandom random = ThreadLocalRandom.current();
    private final String[] randomWords = new String[] { "word", "world", "location", "random", "who", "end", "of",
            "the", "1337", "l33t", "working", "for", "test", "4test", "MC", "minecraft", "Spigot", "SpigotMC", "when",
            "life", "gives", "you", "lemons", "make", "lemonade", "A", "END", "OF", "_", "mc", "-", "OF", "OFF", "on",
            "ON", "THE", "WHO", "UN", "EU", "NA" };

    protected void assertNoPlaceholder(String msg) {
        Assert.assertFalse(msg.contains("{"));
        Assert.assertFalse(msg.contains("}"));
    }

    protected void assertContains(String msg, Object... objects) {
        for (Object o : objects) {
            if (o instanceof Collection<?>) {
                assertContains(msg, ((Collection<?>) o).toArray());
            } else if (o instanceof Double || o instanceof Float) {
                String f1 = String.format("%.1f", o);
                String f2 = String.format("%.2f", o);
                Assert.assertTrue(msg + " should contain:" + o, msg.contains(f1) || msg.contains(f2));
            } else if (o instanceof Location) {
                Location loc = (Location) o;
                assertContains(msg, loc.getWorld().getName(), loc.getX(), loc.getY(), loc.getZ());
            } else {
                Assert.assertTrue(msg + " should contain:" + o, msg.contains(String.valueOf(o)));
            }
        }
    }

    protected String getRandomName() {
        return getRandomName(null);
    }

    protected String getRandomName(String start) {
        return getRandomName(start, 5);
    }

    protected String getRandomName(String start, int nr) {
        StringBuilder builder = new StringBuilder(start == null ? "" : start);
        int max = randomWords.length;
        for (int i = 0; i < nr; i++) {
            builder.append(randomWords[random.nextInt(max)]);
        }
        return builder.toString();
    }

    protected FlashingHologram getRandomHolgram(PeriodicType type, MockLineTrackerManager ltm) {
        double activationDistance = random.nextDouble() * 10;
        int showTime = random.nextInt(20);
        double flashOn = random.nextDouble() * 5;
        double flashOff = random.nextDouble() * 5;
        String perms = getRandomName("perms.");
        String name = getRandomName("name");
        MockNamedHologram holo = new MockNamedHologram(name, ltm);
        HolographicDisplaysWrapper hologram = new HolographicDisplaysWrapper(holo);
        switch (type) {
            case ALWAYS:
                return new AlwaysHologram(getPhd(), hologram, name, activationDistance, showTime, true, perms, flashOn,
                        flashOff);
            case NTIMES:
                int timesToShow = random.nextInt(18);
                return new NTimesHologram(getPhd(), hologram, name, activationDistance, showTime, timesToShow, true,
                        perms, flashOn, flashOff);
            case IRLTIME:
                long atTime = random.nextLong(86400);
                return new IRLTimeHologram(getPhd(), hologram, name, activationDistance, showTime, atTime, true, perms,
                        flashOn, flashOff);
            case MCTIME:
                long time = random.nextLong(24000);
                return new MCTimeHologram(getPhd(), hologram, name, activationDistance, showTime, time, true, perms,
                        flashOn, flashOff);
        }
        throw new IllegalArgumentException("Expected PeriodicType, got " + type);
    }

    public abstract IPeriodicHolographicDisplays getPhd();

}