package me.ford.periodicholographicdisplays.holograms.visbility;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import org.bukkit.scheduler.BukkitTask;

import me.filoghost.holographicdisplays.nms.common.NMSManager;
import me.filoghost.holographicdisplays.plugin.listener.LineClickListener;
import me.filoghost.holographicdisplays.plugin.placeholder.tracking.ActivePlaceholderTracker;
import me.filoghost.holographicdisplays.plugin.tick.TickingTask;
import me.filoghost.holographicdisplays.plugin.tick.CachedPlayer;
import me.filoghost.holographicdisplays.plugin.HolographicDisplays;
import me.filoghost.holographicdisplays.plugin.hologram.tracking.LineTracker;
import me.filoghost.holographicdisplays.plugin.hologram.tracking.LineTrackerManager;

public class VisibilityAwareLineTrackerManager extends LineTrackerManager {
    private final Collection<LineTracker<?>> lineTrackers;

    public VisibilityAwareLineTrackerManager(LineTrackerManager delegate) {
        super(NMSProvider.getNMSManager(delegate), NMSProvider.getPlaceholderTracker(delegate),
                NMSProvider.getLineClickListener(delegate));
        lineTrackers = VisibilityManager.PROVIDER.getAllLineTrackers(delegate);
    }

    @Override
    public void update(Collection<CachedPlayer> onlinePlayers) {
        Collection<CachedPlayer> usedPlayers = new HashSet<>(onlinePlayers);
        Iterator<LineTracker<?>> iterator = lineTrackers.iterator();
        while (iterator.hasNext()) {
            LineTracker<?> lineTracker = iterator.next();
            LineVisibilityManager wrapper = VisibilityManager.REGISTRY.getWrapper(lineTracker);
            if (wrapper != null) {
                for (CachedPlayer p : onlinePlayers) {
                    if (wrapper.isHidingFor(p.getBukkitPlayer())) {
                        usedPlayers.remove(p);
                    }
                }
            }

            // Remove deleted trackers
            if (NMSProvider.shouldBeRemoved(lineTracker)) {
                iterator.remove();
                lineTracker.onRemoval();
                continue;
            }
            NMSProvider.update(lineTracker, usedPlayers);
            usedPlayers.addAll(onlinePlayers); // refresh list
        }
    }

    public static VisibilityAwareLineTrackerManager exchangeLineTrackerManager(HolographicDisplays plugin) {
        LineTrackerManager delegate = NMSProvider.getLineTrackerManager(plugin);
        VisibilityAwareLineTrackerManager ltm = new VisibilityAwareLineTrackerManager(delegate);
        NMSProvider.setLineTrackerManager(plugin, ltm);
        boolean changed = false;
        for (BukkitTask task : plugin.getServer().getScheduler().getPendingTasks()) {
            if (task.getOwner() == plugin && NMSProvider.isTaskOfCorrectType(task)) {
                NMSProvider.switchLineTrackerManager((TickingTask) NMSProvider.getBukkitTaskRunnable(task), ltm);
                changed = true;
                break;
            }
        }
        if (!changed) {
            throw new IllegalStateException(
                    "Could not change HolographidDisplays' TickingTask "
                            + " LineTrackerManager to the custom one. The plugin cannot function like this.");
        }
        return ltm;
    }

    private static class NMSProvider {
        private static final Field NMS_MANAGER_FIELD;
        private static final Field PH_TRACKER_FIELD;
        private static final Field LC_LISTENER_FIELD;
        private static final Field LT_MANAGER_FIELD;
        private static final Method SHOULD_BE_REMOVED;
        private static final Method UPDATE;
        private static final boolean IS_SPIGOT;
        static {
            Field f1, f2, f3, f4;
            Method m1, m2;
            try {
                f1 = LineTrackerManager.class.getDeclaredField("nmsManager");
                f1.setAccessible(true);
                f2 = LineTrackerManager.class.getDeclaredField("placeholderTracker");
                f2.setAccessible(true);
                f3 = LineTrackerManager.class.getDeclaredField("lineClickListener");
                f3.setAccessible(true);
                f4 = HolographicDisplays.class.getDeclaredField("lineTrackerManager");
                f4.setAccessible(true);
                m1 = LineTracker.class.getDeclaredMethod("shouldBeRemoved");
                m1.setAccessible(true);
                m2 = LineTracker.class.getDeclaredMethod("update", Collection.class);
                m2.setAccessible(true);
            } catch (NoSuchFieldException | SecurityException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
            NMS_MANAGER_FIELD = f1;
            PH_TRACKER_FIELD = f2;
            LC_LISTENER_FIELD = f3;
            LT_MANAGER_FIELD = f4;
            SHOULD_BE_REMOVED = m1;
            UPDATE = m2;
            @SuppressWarnings("deprecation")
            Package p = Package.getPackage("io.papermc.paper");
            IS_SPIGOT = p == null;
        }

        private static NMSManager getNMSManager(LineTrackerManager delegate) {
            try {
                return (NMSManager) NMS_MANAGER_FIELD.get(delegate);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        private static ActivePlaceholderTracker getPlaceholderTracker(LineTrackerManager delegate) {
            try {
                return (ActivePlaceholderTracker) PH_TRACKER_FIELD.get(delegate);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        private static LineClickListener getLineClickListener(LineTrackerManager delegate) {
            try {
                return (LineClickListener) LC_LISTENER_FIELD.get(delegate);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        private static LineTrackerManager getLineTrackerManager(HolographicDisplays plugin) {
            try {
                return (LineTrackerManager) LT_MANAGER_FIELD.get(plugin);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        private static void setLineTrackerManager(HolographicDisplays plugin, VisibilityAwareLineTrackerManager ltm) {
            try {
                LT_MANAGER_FIELD.set(plugin, ltm);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        private static Runnable getBukkitTaskRunnable(BukkitTask task) {
            Runnable runnable;
            try {
                Field taskField = IS_SPIGOT ? task.getClass().getDeclaredField("rTask")
                        : task.getClass().getField("rTask");
                taskField.setAccessible(true);
                runnable = (Runnable) taskField.get(task);
            } catch (NoSuchFieldException e) {
                return null; // different type of task (i.e CraftAsyncTask)
            } catch (SecurityException | IllegalArgumentException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            return runnable;
        }

        private static final boolean isTaskOfCorrectType(BukkitTask task) {
            return getBukkitTaskRunnable(task) instanceof TickingTask;
        }

        private static void switchLineTrackerManager(TickingTask ticker, VisibilityAwareLineTrackerManager ltm) {
            Field f;
            try {
                f = TickingTask.class.getDeclaredField("lineTrackerManager");
                f.setAccessible(true);
                f.set(ticker, ltm);
            } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        private static boolean shouldBeRemoved(LineTracker<?> tracker) {
            try {
                return (boolean) SHOULD_BE_REMOVED.invoke(tracker);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }

        private static void update(LineTracker<?> tracker, Collection<CachedPlayer> players) {
            try {
                UPDATE.invoke(tracker, players);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }

    }

}
