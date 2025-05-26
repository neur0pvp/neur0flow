package dev.neur0pvp.neur0flow.scheduler;

public interface SchedulerAdapter {
    void runTask(Runnable task);

    void runTaskAsynchronously(Runnable task);

    AbstractTaskHandle runTaskTimerAsynchronously(Runnable task, long delay, long period);
}