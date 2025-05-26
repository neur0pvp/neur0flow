package dev.neur0pvp.neur0flow.event;

public interface EventBus {
    void registerListeners(Object listener);

    void registerStaticListeners(Class<?> clazz);

    void unregisterListeners(Object listener);

    void unregisterStaticListeners(Class<?> clazz);

    void post(Event event);
}