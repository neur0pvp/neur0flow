package dev.neur0pvp.neur0flow.runnable;

import com.github.retrooper.packetevents.protocol.player.User;
import dev.neur0pvp.neur0flow.Base;
import dev.neur0pvp.neur0flow.manager.CombatManager;
import dev.neur0pvp.neur0flow.manager.PlayerDataManager;
import dev.neur0pvp.neur0flow.player.PlayerData;

public class PingRunnable implements Runnable {

    @Override
    public void run() {
        if (!Base.INSTANCE.getConfigManager().isToggled())
            return;

        for (User user : CombatManager.getPlayers()) {
            PlayerData playerData = PlayerDataManager.getPlayerData(user);
            if (playerData != null)
                playerData.sendPing(true);
        }
    }
}
