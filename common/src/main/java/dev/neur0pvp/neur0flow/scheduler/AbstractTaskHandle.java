package dev.neur0pvp.neur0flow.scheduler;

public interface AbstractTaskHandle {

    boolean getCancelled();

    void cancel();
}