package dev.neur0pvp.neur0flow.listener.bukkit;

import dev.neur0pvp.neur0flow.listener.PlayerDamageListener;
import dev.neur0pvp.neur0flow.player.BukkitPlayer;
import dev.neur0pvp.neur0flow.util.MultiLibUtil;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class BukkitPlayerDamageListener extends PlayerDamageListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        Entity victim = event.getEntity();
        Entity attacker = event.getDamager();
        if (!(victim instanceof Player) || !(attacker instanceof Player))
            return;

        if (MultiLibUtil.isExternalPlayer((Player) victim))
            return;

        onPlayerDamage(new BukkitPlayer((Player) victim), new BukkitPlayer((Player) attacker));
    }
}