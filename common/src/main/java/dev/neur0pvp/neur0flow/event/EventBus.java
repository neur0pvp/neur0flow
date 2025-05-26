package dev.neur0pvp.neur0flow.event;

public interface EventBus {
    void registerListeners(Object listener);

    void unregisterListeners(Object listener);

    void post(Event event);
}