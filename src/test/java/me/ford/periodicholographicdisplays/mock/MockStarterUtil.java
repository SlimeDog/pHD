package me.ford.periodicholographicdisplays.mock;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;

import org.bukkit.Bukkit;

public final class MockStarterUtil {
    private static Exception lastStart = null;
    private static Exception lastFinish = null;

    private MockStarterUtil() {
    }

    public static MockServer startMocking() {
        // System.out.println("startMocking:");
        if (Bukkit.getServer() != null) {
            System.out.println("Server not null when trying to mock...");
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            if (lastStart == null) {
                System.out.println("Not stopped before");
            } else {
                System.out.println("Last Finish:");
                lastFinish.printStackTrace(pw);
                System.out.println(sw.toString());
            }
            if (lastStart == null) {
                System.out.println("Not started before???");
            } else {
                System.out.println("Last start:");
                lastStart.printStackTrace(pw);
                System.out.println(sw.toString());
            }
            stopMocking();
        }
        lastStart = new IllegalStateException();
        MockServer server;
        Bukkit.setServer(server = new MockServer());
        return server;
    }

    public static void stopMocking() {
        // System.out.println("STOP MOCKING");
        try {
            Field field = Bukkit.class.getDeclaredField("server");
            field.setAccessible(true);
            field.set(null, null);
        } catch (Exception e) {
            System.out.println("EXCEPTION: " + e);
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        lastFinish = new IllegalStateException();
    }

    public static MockServer getServer() {
        return (MockServer) Bukkit.getServer();
    }

}
