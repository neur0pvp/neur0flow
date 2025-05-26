package dev.neur0pvp.neur0flow.stats.custom;

import org.bukkit.plugin.Plugin;

public class BukkitStatsManager extends StatsManager {

    public BukkitStatsManager(Plugin plugin) {
        super(new MetricsBukkit(plugin, 23568));
    }
}
