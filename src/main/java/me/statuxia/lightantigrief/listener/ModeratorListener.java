package me.statuxia.lightantigrief.listener;

import me.statuxia.lightantigrief.LAG;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ModeratorListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (player.hasPermission("lag.moder")) {
            LAG.addModerator(player);
        }
    }

    @EventHandler
    public void onLeft(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (player.hasPermission("lag.moder")) {
            LAG.removeModerator(player);
        }
    }
}
