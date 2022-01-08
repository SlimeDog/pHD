package me.ford.periodicholographicdisplays.holograms.visbility;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;

import me.filoghost.holographicdisplays.plugin.hologram.tracking.LineTracker;
import me.filoghost.holographicdisplays.plugin.hologram.tracking.MutableViewers;

public class LineTrackerWrapper {
    private final LineTracker<?> delegate;
    private final Map<Player, Object> delegateViewers; // not really Object
    private final Map<Player, Object> removedViewers = new HashMap<>();

    public LineTrackerWrapper(LineTracker<?> delegate) {
        this.delegate = delegate;
        delegateViewers = NMSPRovider.getViewers(delegate);
        // default behaviour to hide from everyone who isn't explicitly shown
        for (Player player : new ArrayList<>(delegateViewers.keySet())) {
            stopTracking(player);
        }
    }

    public LineTracker<?> getDelegate() {
        return delegate;
    }

    public boolean isHidingFor(Player player) {
        return removedViewers.containsKey(player);
    }

    public void stopTracking(Player player) {
        Object viewer = delegateViewers.remove(player);
        removedViewers.put(player, viewer);
        sendPacket(viewer, NMSPRovider.LINETRACKER_SEND_DESTROY_METHOD);
    }

    public void startRetracking(Player player) {
        Object viewer = removedViewers.remove(player);
        if (viewer == null) {
            return; // don't write null in case of initial join
        }
        delegateViewers.put(player, viewer);
        sendPacket(viewer, NMSPRovider.LINETRACKER_SEND_SPAWN_METHOD);
    }

    public void onQuit(Player player) {
        removedViewers.remove(player); // avoid memory leaks
    }

    public void reset() {
        for (Player player : new ArrayList<>(removedViewers.keySet())) {
            startRetracking(player);
        }
    }

    private void sendPacket(Object viewer, Method sendPacketMethod) {
        MutableViewers<?> viewers = new MutableViewers<>();
        NMSPRovider.sendPacket(delegate, viewer, viewers, sendPacketMethod);
    }

    private static class NMSPRovider {
        private static final Field VIEWERS_FIELD;
        private static final Class<?> VIEWER_CLASS;
        private static final Class<?> VIEWERS_CLASS;
        private static final Method LINETRACKER_SEND_SPAWN_METHOD;
        private static final Method LINETRACKER_SEND_DESTROY_METHOD;
        private static final Method VIEWERS_ADD_METHOD;
        static {
            Field f;
            try {
                f = LineTracker.class.getDeclaredField("viewers");
            } catch (NoSuchFieldException | SecurityException e) {
                throw new RuntimeException(e);
            }
            VIEWERS_FIELD = f;
            VIEWERS_FIELD.setAccessible(true);
            Class<?> c;
            try {
                c = Class.forName("me.filoghost.holographicdisplays.plugin.hologram.tracking.Viewer");
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            VIEWER_CLASS = c;
            try {
                c = Class.forName("me.filoghost.holographicdisplays.plugin.hologram.tracking.Viewers");
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            VIEWERS_CLASS = c;
            Method m;
            try {
                m = LineTracker.class.getDeclaredMethod("sendSpawnPackets", VIEWERS_CLASS);
            } catch (NoSuchMethodException | SecurityException e) {
                throw new RuntimeException(e);
            }
            LINETRACKER_SEND_SPAWN_METHOD = m;
            LINETRACKER_SEND_SPAWN_METHOD.setAccessible(true);
            try {
                m = LineTracker.class.getDeclaredMethod("sendDestroyPackets", VIEWERS_CLASS);
            } catch (NoSuchMethodException | SecurityException e) {
                throw new RuntimeException(e);
            }
            LINETRACKER_SEND_DESTROY_METHOD = m;
            LINETRACKER_SEND_DESTROY_METHOD.setAccessible(true);
            try {
                m = MutableViewers.class.getMethod("add", VIEWER_CLASS);
            } catch (NoSuchMethodException | SecurityException e) {
                throw new RuntimeException(e);
            }
            VIEWERS_ADD_METHOD = m;
        }

        @SuppressWarnings("unchecked")
        private static Map<Player, Object> getViewers(LineTracker<?> delegate) {
            try {
                return (Map<Player, Object>) VIEWERS_FIELD.get(delegate); // unchecked
            } catch (IllegalArgumentException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        private static void sendPacket(LineTracker<?> delegate, Object viewer, MutableViewers<?> viewers,
                Method sendPacketMethod) {
            try {
                VIEWERS_ADD_METHOD.invoke(viewers, viewer);
                sendPacketMethod.invoke(delegate, viewers);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
