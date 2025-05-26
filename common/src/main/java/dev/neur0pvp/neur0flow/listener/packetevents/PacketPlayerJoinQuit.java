package dev.neur0pvp.neur0flow.listener.packetevents;

import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.UserDisconnectEvent;
import com.github.retrooper.packetevents.event.UserLoginEvent;
import com.github.retrooper.packetevents.protocol.player.User;
import com.google.common.base.Preconditions;
import dev.neur0pvp.neur0flow.Base;
import dev.neur0pvp.neur0flow.manager.PlayerDataManager;
import dev.neur0pvp.neur0flow.player.PlatformPlayer;
import dev.neur0pvp.neur0flow.player.PlayerData;
import dev.neur0pvp.neur0flow.util.ChatUtil;
import org.jetbrains.annotations.NotNull;

public class PacketPlayerJoinQuit extends PacketListenerAbstract {
    @Override
    public void onUserLogin(UserLoginEvent event) {
        Object nativePlayerObject = event.getPlayer();
        Preconditions.checkArgument(nativePlayerObject != null);

        @NotNull PlatformPlayer platformPlayer = Base.INSTANCE.getPlatformServer().getPlayer(nativePlayerObject);
        onPlayerJoin(event.getUser(), platformPlayer);
    }

    @Override
    public void onUserDisconnect(UserDisconnectEvent event) {
        onPlayerQuit(event.getUser());
    }

    public void onPlayerJoin(User user, PlatformPlayer player) {
        PlayerDataManager.addPlayerData(user, player);

        if (Base.INSTANCE.getConfigManager().isUpdateAvailable() && Base.INSTANCE.getConfigManager().isNotifyUpdate() && Base.INSTANCE.getPermissionChecker().hasPermission(player, "neur0flow.update"))
            player.sendMessage(ChatUtil.translateAlternateColorCodes(
                    '&',
                    "&6An updated version of &eneur0flow &6is now available for download at: &bhttps://github.com/CASELOAD7000/knockback-sync/releases/latest"
            ));
    }

    public void onPlayerQuit(@NotNull User user) {
        PlayerData playerData = PlayerDataManager.getPlayerData(user);
        if (playerData == null)
            return;

        playerData.quitCombat(true);
        PlayerDataManager.removePlayerData(user);
    }
}
