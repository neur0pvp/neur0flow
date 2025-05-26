package dev.neur0pvp.neur0flow.stats.custom;

import lombok.Getter;
import dev.neur0pvp.neur0flow.Base;

@Getter
public abstract class StatsManager {

    Metrics metrics;

    public StatsManager(Metrics metrics) {
        this.metrics = metrics;
    }

    public void init() {
        Base.INSTANCE.getScheduler().runTaskAsynchronously(() -> {
            BuildTypePie.determineBuildType(); // Function to calculate hash
            metrics.addCustomChart(new PlayerVersionsPie());
            metrics.addCustomChart(new BuildTypePie());
            metrics.addCustomChart(new ClientBrandsPie());
        });
    }
}
