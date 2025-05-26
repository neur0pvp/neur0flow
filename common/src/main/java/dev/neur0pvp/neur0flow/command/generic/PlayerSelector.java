package dev.neur0pvp.neur0flow.command.generic;

import dev.neur0pvp.neur0flow.player.PlatformPlayer;

public interface PlayerSelector {

    PlatformPlayer getSinglePlayer(); // Throws an exception if not a single selection

}