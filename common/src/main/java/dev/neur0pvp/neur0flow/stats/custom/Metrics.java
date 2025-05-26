package dev.neur0pvp.neur0flow.stats.custom;

import dev.neur0pvp.neur0flow.stats.CustomChart;

public interface Metrics {

    void addCustomChart(CustomChart chart);
    void shutdown();
}
