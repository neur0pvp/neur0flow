package dev.neur0pvp.neur0flow.sender;

import dev.neur0pvp.neur0flow.command.generic.PlayerSelector;
import dev.neur0pvp.neur0flow.player.BukkitPlayer;
import dev.neur0pvp.neur0flow.player.PlatformPlayer;

public class BukkitPlayerSelectorAdapter implements PlayerSelector {
    private final org.incendo.cloud.bukkit.data.SinglePlayerSelector bukkitSelector;

    public BukkitPlayerSelectorAdapter(org.incendo.cloud.bukkit.data.SinglePlayerSelector bukkitSelector) {
        this.bukkitSelector = bukkitSelector;
    }

    @Override
    public PlatformPlayer getSinglePlayer() {
        return new BukkitPlayer(bukkitSelector.single());
    }

}
