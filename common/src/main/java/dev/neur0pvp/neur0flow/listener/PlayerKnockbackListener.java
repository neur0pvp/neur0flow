package dev.neur0pvp.neur0flow.listener;

import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import com.github.retrooper.packetevents.protocol.world.states.type.StateTypes;
import com.github.retrooper.packetevents.util.Vector3d;
import dev.neur0pvp.neur0flow.Base;
import dev.neur0pvp.neur0flow.manager.PlayerDataManager;
import dev.neur0pvp.neur0flow.player.PlatformPlayer;
import dev.neur0pvp.neur0flow.player.PlayerData;

public abstract class PlayerKnockbackListener {

    public void onPlayerVelocity(PlatformPlayer victim, Vector3d velocity) {
        if (!Base.INSTANCE.getConfigManager().isToggled())
            return;

        User user = victim.getUser();
        if (user == null)
            return; // Prevent errors with players disconnecting while this is running (or with fake player?)

        PlayerData victimPlayerData = PlayerDataManager.getPlayerData(user);
        if (victimPlayerData == null)
            return;

        if (victimPlayerData.getNotNullPing() < PlayerData.PING_OFFSET)
            return;

        double distanceToGround = victimPlayerData.getDistanceToGround();
        if (distanceToGround <= 0)
            return; // minecraft already does the work for us

        WrappedBlockState blockState = victim.getWorld().getBlockStateAt(victim.getLocation());
        if (victim.isGliding() ||
                blockState.getType() == StateTypes.WATER ||
                blockState.getType() == StateTypes.LAVA ||
                blockState.getType() == StateTypes.COBWEB ||
                blockState.getType() == StateTypes.SCAFFOLDING)
            return;

        Vector3d adjustedVelocity;
        if (victimPlayerData.isOnGroundClientSide(velocity.getY(), distanceToGround)) {
            Integer damageTicks = victimPlayerData.getLastDamageTicks();
            if (damageTicks != null && damageTicks > 8)
                return;

            adjustedVelocity = velocity.withY(victimPlayerData.getVerticalVelocity()); // Should be impossible to produce a NPE in this context
        } else if (victimPlayerData.isOffGroundSyncEnabled())
            adjustedVelocity = velocity.withY(victimPlayerData.getCompensatedOffGroundVelocity());
        else
            return;

        victim.setVelocity(adjustedVelocity);
    }
}