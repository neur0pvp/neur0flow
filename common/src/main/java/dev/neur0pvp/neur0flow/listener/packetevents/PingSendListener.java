package dev.neur0pvp.neur0flow.listener.packetevents;

import com.github.retrooper.packetevents.event.*;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerKeepAlive;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPing;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerWindowConfirmation;
import dev.neur0pvp.neur0flow.Base;
import dev.neur0pvp.neur0flow.manager.PlayerDataManager;
import dev.neur0pvp.neur0flow.player.PingStrategy;
import dev.neur0pvp.neur0flow.player.PlayerData;
import dev.neur0pvp.neur0flow.util.data.Pair;

import java.util.UUID;

public class PingSendListener extends PacketListenerAbstract {

    public PingSendListener() {
        // See all actually outgoing, not cancelled, ping packets
        super(PacketListenerPriority.MONITOR);
    }

    @Override
    public void onPacketSend(PacketSendEvent event) {
        if (!Base.INSTANCE.getConfigManager().isToggled()) return;
        if (event.isCancelled()) return;

        PacketTypeCommon packetType = event.getPacketType();
        UUID playerUUID = event.getUser().getUUID();
        if (playerUUID == null) return;
        PlayerData playerData = PlayerDataManager.getPlayerData(event.getUser());
        if (playerData == null) return;

        if (playerData.pingStrategy == PingStrategy.KEEPALIVE && packetType.equals(PacketType.Play.Server.KEEP_ALIVE)) {
            WrapperPlayServerKeepAlive keepAlive = new WrapperPlayServerKeepAlive(event);
            long id = keepAlive.getId();

//            System.out.println("Adding ping to queue - ID: " + id + " Time: " + System.nanoTime() + " Queue size before: " + playerData.keepaliveMap.size());
            playerData.keepaliveMap.add(new Pair<>(id, System.nanoTime()));
        } else if (playerData.pingStrategy == PingStrategy.TRANSACTION && packetType.equals(PacketType.Play.Server.PING)) {
            WrapperPlayServerPing ping = new WrapperPlayServerPing(event);
            int id = ping.getId();

            playerData.transactionsSent.add(new Pair<>(id, System.nanoTime()));
        } else if (playerData.pingStrategy == PingStrategy.TRANSACTION && packetType.equals(PacketType.Play.Server.WINDOW_CONFIRMATION)) {
            WrapperPlayServerWindowConfirmation confirmation = new WrapperPlayServerWindowConfirmation(event);
            int id = confirmation.getActionId();

            playerData.transactionsSent.add(new Pair<>(id, System.nanoTime()));
        }
    }
}