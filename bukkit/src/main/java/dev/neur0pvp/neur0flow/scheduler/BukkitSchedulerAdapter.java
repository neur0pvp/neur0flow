package dev.neur0pvp.neur0flow.scheduler;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

public class BukkitSchedulerAdapter implements SchedulerAdapter {
    private final Plugin plugin;
    private final BukkitScheduler scheduler;

    public BukkitSchedulerAdapter(Plugin plugin) {
        this.plugin = plugin;
        this.scheduler = Bukkit.getScheduler();
    }

    @Override
    public void runTask(Runnable task) {
        scheduler.runTask(plugin, task);
    }

    @Override
    public void runTaskAsynchronously(Runnable task) {
        scheduler.runTaskAsynchronously(plugin, task);
    }

    @Override
    public AbstractTaskHandle runTaskTimerAsynchronously(Runnable task, long delay, long period) {
        return new BukkitTaskHandle(scheduler.runTaskTimerAsynchronously(plugin, task, delay, period));
    }

}