package me.ford.periodicholographicdisplays.mock;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

public class MockBukkitTask implements BukkitTask {

    @Override
    public int getTaskId() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Plugin getOwner() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isSync() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isCancelled() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void cancel() {
        // TODO Auto-generated method stub

    }

}