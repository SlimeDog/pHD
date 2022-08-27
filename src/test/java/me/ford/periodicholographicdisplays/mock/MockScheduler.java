package me.ford.periodicholographicdisplays.mock;

import java.util.function.Consumer;

import dev.ratas.slimedogcore.api.scheduler.SDCScheduler;
import dev.ratas.slimedogcore.api.scheduler.SDCTask;

public class MockScheduler implements SDCScheduler {

    public void runNow(Runnable runnable) {
        runnable.run();
    }

    @Override
    public void runTask(Runnable runnable) {
        runNow(runnable);
    }

    @Override
    public void runTaskAsync(Runnable runnable) {
        runNow(runnable);
    }

    @Override
    public void runTaskLater(Runnable runnable, long delayTicks) {
        runNow(runnable);
    }

    @Override
    public void runTaskLaterAsync(Runnable runnable, long delayTicks) {
        runNow(runnable);
    }

    @Override
    public void runTaskTimer(Runnable runnable, long delay, long period) {
        runNow(runnable);
    }

    @Override
    public void runTaskTimerAsync(Runnable runnable, long delay, long period) {
        runNow(runnable);
    }

    @Override
    public void runTask(Consumer<SDCTask> consumer) {
        runNow(() -> consumer.accept(null));
    }

    @Override
    public void runTaskAsync(Consumer<SDCTask> consumer) {
        runNow(() -> consumer.accept(null));
    }

    @Override
    public void runTaskLater(Consumer<SDCTask> consumer, long delayTicks) {
        runNow(() -> consumer.accept(null));
    }

    @Override
    public void runTaskLaterAsync(Consumer<SDCTask> consumer, long delayTicks) {
        runNow(() -> consumer.accept(null));
    }

    @Override
    public void runTaskTimer(Consumer<SDCTask> consumer, long delay, long period) {
        runNow(() -> consumer.accept(null));
    }

    @Override
    public void runTaskTimerAsync(Consumer<SDCTask> consumer, long delay, long period) {
        runNow(() -> consumer.accept(null));
    }

}
