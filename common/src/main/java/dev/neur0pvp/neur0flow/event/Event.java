package dev.neur0pvp.neur0flow.event;

import lombok.Setter;

public abstract class Event {
    @Setter
    private static EventBus eventBus;
    @Setter
    private boolean cancelled = false;

    public void post() {
        if (eventBus != null) {
            eventBus.post(this);
        } else {
            throw new IllegalStateException("EventBus has not been set");
        }
    }

    public boolean isCancelled() {
        return cancelled;
    }
}