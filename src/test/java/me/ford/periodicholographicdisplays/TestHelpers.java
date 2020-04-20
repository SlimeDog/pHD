package me.ford.periodicholographicdisplays;

import java.util.Collection;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Location;
import org.junit.Assert;

public class TestHelpers {
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

}