package dev.neur0pvp.neur0flow.event.events;

import lombok.Getter;
import dev.neur0pvp.neur0flow.event.Event;
import dev.neur0pvp.neur0flow.manager.ConfigManager;

@Getter
public class ConfigReloadEvent extends Event {
    private final ConfigManager configManager;

    public ConfigReloadEvent(ConfigManager configManager) {
        this.configManager = configManager;
    }

}