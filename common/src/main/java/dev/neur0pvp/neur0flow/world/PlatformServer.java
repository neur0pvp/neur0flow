package dev.neur0pvp.neur0flow.world;

import dev.neur0pvp.neur0flow.player.PlatformPlayer;

import java.util.UUID;

public interface PlatformServer {

    PlatformPlayer getPlayer(UUID uuid);

    PlatformPlayer getPlayer(Object nativePlatformPlayer);
}