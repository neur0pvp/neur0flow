package dev.neur0pvp.neur0flow.listener;

import com.github.retrooper.packetevents.protocol.player.User;
import dev.neur0pvp.neur0flow.Base;
import dev.neur0pvp.neur0flow.manager.PlayerDataManager;
import dev.neur0pvp.neur0flow.player.PlatformPlayer;
import dev.neur0pvp.neur0flow.player.PlayerData;

public abstract class PlayerDamageListener {
    public void onPlayerDamage(PlatformPlayer victim, PlatformPlayer attacker) {
        if (!Base.INSTANCE.getConfigManager().isToggled())
            return;

        User user = victim.getUser();
        if (user == null)
            return; // Prevent errors with players disconnecting while this is running (or with fake player?)

        PlayerData playerData = PlayerDataManager.getPlayerData(user);
        if (playerData == null)
            return;

        playerData.setVerticalVelocity(playerData.calculateVerticalVelocity(attacker)); // do not move this calculation
        playerData.setLastDamageTicks(victim.getNoDamageTicks());
        playerData.updateCombat();

        if (!Base.INSTANCE.getConfigManager().isRunnableEnabled())
            playerData.sendPing(true);
    }
}
