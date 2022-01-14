package me.ford.periodicholographicdisplays.mock;

import java.lang.reflect.Field;

import org.bukkit.Bukkit;

public final class MockStarterUtil {

    private MockStarterUtil() {
    }

    public static MockServer startMocking() {
        if (Bukkit.getServer() != null) {
            throw new IllegalStateException(
                    "Cannot start mocking since a previously mocked server has not been removed. "
                            + "Call MockStarterUtil#stopMocking first.");
        }
        MockServer server;
        Bukkit.setServer(server = new MockServer());
        return server;
    }

    public static void stopMocking() {
        try {
            Field field = Bukkit.class.getDeclaredField("server");
            field.setAccessible(true);
            field.set(null, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static MockServer getServer() {
        return (MockServer) Bukkit.getServer();
    }

}
