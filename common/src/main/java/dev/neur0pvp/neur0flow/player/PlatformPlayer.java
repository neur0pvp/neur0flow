package dev.neur0pvp.neur0flow.player;

import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.protocol.world.BoundingBox;
import com.github.retrooper.packetevents.util.Vector3d;
import dev.neur0pvp.neur0flow.world.PlatformWorld;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface PlatformPlayer {
    UUID getUUID();

    String getName();

    int getPing();

    boolean isGliding();

    PlatformWorld getWorld();

    Vector3d getLocation();

    void sendMessage(@NotNull String s);

    double getAttackCooldown();

    boolean isSprinting();

    int getMainHandKnockbackLevel();

    @Nullable Integer getNoDamageTicks();

    Vector3d getVelocity();

    void setVelocity(Vector3d adjustedVelocity);

    BoundingBox getBoundingBox();

    /**
     * If a player disconnects while we are running the constructor of a PlatformPlayer
     * PacketEvents.getAPI().getPlayerManager().getUser(bukkitPlayer) may return null
     * This will make the internal field null and thus this function may return null.
     */
    @Nullable User getUser();

    void setClientBrand(String brand);
}
