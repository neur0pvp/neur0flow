package dev.neur0pvp.neur0flow.world;

import dev.neur0pvp.neur0flow.player.PlatformPlayer;

import java.util.Collection;
import java.util.UUID;

public interface PlatformServer {
    Collection<PlatformPlayer> getOnlinePlayers();

    PlatformPlayer getPlayer(UUID uuid);

    PlatformPlayer getPlayer(Object nativePlatformPlayer);
}