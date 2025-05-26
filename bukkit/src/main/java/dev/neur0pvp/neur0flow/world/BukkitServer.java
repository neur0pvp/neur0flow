package dev.neur0pvp.neur0flow.world;

import dev.neur0pvp.neur0flow.player.BukkitPlayer;
import dev.neur0pvp.neur0flow.player.PlatformPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class BukkitServer implements PlatformServer {

    @Override
    public PlatformPlayer getPlayer(UUID uuid) {
        return new BukkitPlayer(Bukkit.getPlayer(uuid));
    }

    @Override
    public PlatformPlayer getPlayer(Object nativePlatformPlayer) {
        return new BukkitPlayer((Player) nativePlatformPlayer);
    }
}
