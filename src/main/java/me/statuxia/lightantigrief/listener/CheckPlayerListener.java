package me.statuxia.lightantigrief.listener;

import me.statuxia.lightantigrief.LAG;
import me.statuxia.lightantigrief.utils.PlayTime;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Locale;

public class CheckPlayerListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String name = player.getName();
        if (LAG.getTrustedIPs().contains(player.getAddress().getAddress().getHostAddress())) {
            return;
        }
        if (LAG.getTrustedPlayers().contains(name.toLowerCase(Locale.ROOT))) {
            return;
        }

        if (PlayTime.ofSeconds(player) > GriefListener.getTrustedTime()) {
            LAG.addTrustedPlayer(name);
            LAG.addTrustedIp(player.getAddress().getAddress().getHostAddress());
        }
    }
}
