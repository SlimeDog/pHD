package me.ford.periodicholographicdisplays.mock;

import dev.ratas.slimedogcore.api.SlimeDogPlugin;
import dev.ratas.slimedogcore.api.scheduler.SDCTask;

public class MockSDCTask implements SDCTask {
    private final SlimeDogPlugin phd;
    private final int id;

    public MockSDCTask(SlimeDogPlugin phd, int id) {
        this.phd = phd;
        this.id = id;
    }

    @Override
    public int getTaskId() {
        return id;
    }

    @Override
    public boolean isSync() {
        return true; // TODO - support "async" mock tasks
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public void cancel() {
        // TODO Auto-generated method stub
    }

    @Override
    public SlimeDogPlugin getOwningPlugin() {
        return phd;
    }

}