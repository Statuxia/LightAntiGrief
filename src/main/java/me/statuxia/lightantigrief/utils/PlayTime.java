package me.statuxia.lightantigrief.utils;

import org.bukkit.Statistic;
import org.bukkit.entity.Player;

public class PlayTime {

    public static int ofTicks(Player player) {
        return player.getStatistic(Statistic.PLAY_ONE_MINUTE);
    }

    public static int ofSeconds(Player player) {
        return ofTicks(player) / 20;
    }
}
