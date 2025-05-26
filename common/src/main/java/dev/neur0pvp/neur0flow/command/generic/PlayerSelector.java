package dev.neur0pvp.neur0flow.command.generic;

import dev.neur0pvp.neur0flow.player.PlatformPlayer;

import java.util.Collection;

public interface PlayerSelector {
    boolean isSingle();

    PlatformPlayer getSinglePlayer(); // Throws an exception if not a single selection

    Collection<PlatformPlayer> getPlayers();

    String inputString();
}