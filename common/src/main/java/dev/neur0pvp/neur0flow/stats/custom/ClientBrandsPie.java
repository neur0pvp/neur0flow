package dev.neur0pvp.neur0flow.stats.custom;

import com.github.retrooper.packetevents.protocol.player.User;
import dev.neur0pvp.neur0flow.Base;
import dev.neur0pvp.neur0flow.player.PlatformPlayer;
import dev.neur0pvp.neur0flow.stats.AdvancedPie;

import java.util.HashMap;
import java.util.Map;

public class ClientBrandsPie extends AdvancedPie {

    // Gets the client versions of players online
    public ClientBrandsPie() {
        super("client_brands", () -> {
            Map<String, Integer> valueMap = new HashMap<>();
            for (PlatformPlayer player : Base.INSTANCE.getPlatformServer().getOnlinePlayers()) {
                User user = player.getUser();
                if (user == null || user.getClientVersion() == null) {
                    valueMap.put("vanilla", valueMap.getOrDefault("vanilla", 0) + 1);
                } else {
                    valueMap.put(player.getClientBrand(), valueMap.getOrDefault(player.getClientBrand(), 0) + 1);
                }
            }
            return valueMap;
        });
    }
}
