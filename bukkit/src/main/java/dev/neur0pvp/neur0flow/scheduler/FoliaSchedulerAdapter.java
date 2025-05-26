package dev.neur0pvp.neur0flow.scheduler;

import io.github.retrooper.packetevents.util.folia.FoliaScheduler;
import org.bukkit.plugin.Plugin;

public class FoliaSchedulerAdapter implements SchedulerAdapter {
    private final Plugin plugin;

    public FoliaSchedulerAdapter(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void runTask(Runnable task) {
        FoliaScheduler.getGlobalRegionScheduler().run(plugin, scheduledTask -> task.run());
    }

    @Override
    public void runTaskAsynchronously(Runnable task) {
        FoliaScheduler.getAsyncScheduler().runNow(plugin, scheduledTask -> task.run());
    }

    @Override
    public AbstractTaskHandle runTaskTimerAsynchronously(Runnable task, long delay, long period) {
        return new FoliaTaskHandle(FoliaScheduler.getAsyncScheduler().runAtFixedRate(plugin, scheduledTask -> task.run(), delay, period));
    }

}