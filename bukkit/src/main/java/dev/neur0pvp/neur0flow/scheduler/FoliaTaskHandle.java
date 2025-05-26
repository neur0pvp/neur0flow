package dev.neur0pvp.neur0flow.scheduler;

import io.github.retrooper.packetevents.util.folia.TaskWrapper;
import org.jetbrains.annotations.NotNull;

public class FoliaTaskHandle implements AbstractTaskHandle {

    private final TaskWrapper scheduledTask; // Store as Object instead of ScheduledTask

    public FoliaTaskHandle(@NotNull TaskWrapper scheduledTask) {
        this.scheduledTask = scheduledTask;
    }

    @Override
    public void cancel() {
        scheduledTask.cancel();
    }
}