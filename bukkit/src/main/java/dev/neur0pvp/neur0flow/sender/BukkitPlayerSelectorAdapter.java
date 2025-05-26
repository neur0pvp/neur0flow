package dev.neur0pvp.neur0flow.sender;

import dev.neur0pvp.neur0flow.command.generic.PlayerSelector;
import dev.neur0pvp.neur0flow.player.BukkitPlayer;
import dev.neur0pvp.neur0flow.player.PlatformPlayer;

import java.util.Collection;
import java.util.Collections;

public class BukkitPlayerSelectorAdapter implements PlayerSelector {
    private final org.incendo.cloud.bukkit.data.SinglePlayerSelector bukkitSelector;

    public BukkitPlayerSelectorAdapter(org.incendo.cloud.bukkit.data.SinglePlayerSelector bukkitSelector) {
        this.bukkitSelector = bukkitSelector;
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public PlatformPlayer getSinglePlayer() {
        return new BukkitPlayer(bukkitSelector.single());
    }

    @Override
    public Collection<PlatformPlayer> getPlayers() {
        return Collections.singletonList(new BukkitPlayer(bukkitSelector.single()));
    }

    @Override
    public String inputString() {
        return bukkitSelector.inputString();
    }
}
