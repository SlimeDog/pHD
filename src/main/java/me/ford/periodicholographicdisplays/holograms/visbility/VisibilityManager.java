package me.ford.periodicholographicdisplays.holograms.visbility;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.bukkit.entity.Player;

import me.filoghost.holographicdisplays.plugin.hologram.base.BaseHologram;
import me.filoghost.holographicdisplays.plugin.hologram.base.BaseHologramLine;
import me.filoghost.holographicdisplays.plugin.hologram.tracking.LineTracker;
import me.filoghost.holographicdisplays.plugin.hologram.tracking.LineTrackerManager;
import me.filoghost.holographicdisplays.plugin.internal.hologram.InternalHologram;
import me.filoghost.holographicdisplays.plugin.internal.hologram.InternalHologramLine;

public class VisibilityManager {
    public static final VisibilityManagerRegistry REGISTRY = new VisibilityManagerRegistry();
    public static final Provider PROVIDER = new Provider();
    private final Collection<LineVisibilityManager> lineTrackers; // only lines belonging to this hologram

    public VisibilityManager(
            InternalHologram hologram,
            Collection<LineVisibilityManager> lineTrackers) {
        this.lineTrackers = lineTrackers;
        for (LineVisibilityManager wrapper : lineTrackers) {
            REGISTRY.register(wrapper.getDelegate(), wrapper);
        }
    }

    public void hideFrom(Player player) {
        for (LineVisibilityManager tracker : lineTrackers) {
            tracker.stopTracking(player);
        }
    }

    public void showTo(Player player) {
        for (LineVisibilityManager tracker : lineTrackers) {
            tracker.startRetracking(player);
        }
    }

    public void resetVisibilityAll() {
        for (LineVisibilityManager tracker : lineTrackers) {
            tracker.reset();
        }
    }

    public void playerLeft(Player player) {
        for (LineVisibilityManager tracker : lineTrackers) {
            tracker.onQuit(player);
        }
    }

    public static class Provider {
        private static final String GETTER_METHOD_NAME = "getTrackerManager";
        private static final Method GETTER_METHOD;
        private static final String LINE_TRACKERS_FIELD_NAME = "lineTrackers";
        private static final Field LINE_TRACKERS_FIELD;
        private static final String GET_LINE_METHOD_NAME = "getLine";
        private static final Method GET_LINE_METHOD;
        static {
            Method m;
            try {
                m = BaseHologram.class.getDeclaredMethod(GETTER_METHOD_NAME);
            } catch (NoSuchMethodException | SecurityException e) {
                throw new RuntimeException(e);
            }
            GETTER_METHOD = m;
            GETTER_METHOD.setAccessible(true);
            Field f;
            try {
                f = LineTrackerManager.class.getDeclaredField(LINE_TRACKERS_FIELD_NAME);
            } catch (NoSuchFieldException | SecurityException e) {
                throw new RuntimeException(e);
            }
            LINE_TRACKERS_FIELD = f;
            LINE_TRACKERS_FIELD.setAccessible(true);
            try {
                m = LineTracker.class.getDeclaredMethod(GET_LINE_METHOD_NAME);
            } catch (NoSuchMethodException | SecurityException e) {
                throw new RuntimeException(e);
            }
            GET_LINE_METHOD = m;
            GET_LINE_METHOD.setAccessible(true);
        }

        private LineTrackerManager getTracker(InternalHologram hologram) {
            try {
                return (LineTrackerManager) GETTER_METHOD.invoke(hologram);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }

        @SuppressWarnings("unchecked")
        public Collection<LineTracker<?>> getAllLineTrackers(LineTrackerManager manager) {
            try {
                return (Collection<LineTracker<?>>) LINE_TRACKERS_FIELD.get(manager); // unchecked cast
            } catch (IllegalArgumentException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        private BaseHologramLine getLine(LineTracker<?> tracker) {
            try {
                return (BaseHologramLine) GET_LINE_METHOD.invoke(tracker);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }

        private Collection<LineVisibilityManager> getHologramLineTrackers(Collection<LineTracker<?>> allTrackers,
                InternalHologram hologram) {
            Set<InternalHologramLine> hologramLines = new HashSet<>();
            Iterator<InternalHologramLine> iter = hologram.getLines().iterator();
            while (iter.hasNext()) {
                hologramLines.add(iter.next());
            }
            List<LineVisibilityManager> list = new ArrayList<>();
            for (LineTracker<?> tracker : allTrackers) {
                BaseHologramLine line = getLine(tracker);
                if (hologramLines.contains(line)) {
                    list.add(new LineVisibilityManager(tracker));
                }

            }
            return list;
        }

        public VisibilityManager provide(InternalHologram hologram) {
            LineTrackerManager tracker = getTracker(hologram);
            Collection<LineTracker<?>> all = getAllLineTrackers(tracker);
            return new VisibilityManager(hologram, getHologramLineTrackers(all, hologram));
        }

    }

}
