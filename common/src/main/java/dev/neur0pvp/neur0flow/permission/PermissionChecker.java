package dev.neur0pvp.neur0flow.permission;

import dev.neur0pvp.neur0flow.player.PlatformPlayer;
import dev.neur0pvp.neur0flow.sender.Sender;

public interface PermissionChecker {
    boolean hasPermission(Object nativeType, String s, boolean defaultIfUnset);

    boolean hasPermission(Sender source, String s, boolean defaultIfUnset);

    boolean hasPermission(PlatformPlayer platform, String s);
}
