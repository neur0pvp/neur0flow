package dev.neur0pvp.neur0flow.scheduler;

import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

public class BukkitTaskHandle implements AbstractTaskHandle {
    private final BukkitTask bukkitTask;

    public BukkitTaskHandle(@NotNull BukkitTask bukkitTask) {
        this.bukkitTask = bukkitTask;
    }

    @Override
    public void cancel() {
        this.bukkitTask.cancel();
    }
}
