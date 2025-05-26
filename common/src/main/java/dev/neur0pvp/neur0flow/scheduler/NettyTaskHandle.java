package dev.neur0pvp.neur0flow.scheduler;

import io.netty.util.concurrent.ScheduledFuture;

public class NettyTaskHandle implements AbstractTaskHandle {

    private final ScheduledFuture scheduledFuture;

    public NettyTaskHandle(ScheduledFuture<?> scheduledFuture) {
        this.scheduledFuture = scheduledFuture;
    }

    @Override
    public void cancel() {
        scheduledFuture.cancel(true);
    }
}
