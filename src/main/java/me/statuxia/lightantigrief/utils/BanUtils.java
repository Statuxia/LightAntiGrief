package me.statuxia.lightantigrief.utils;

import me.statuxia.lightantigrief.LAG;
import me.statuxia.lightantigrief.config.LAGConfig;
import org.bukkit.Bukkit;

import java.util.concurrent.ThreadLocalRandom;

public class BanUtils {

    private static final String banReason = LAGConfig.getBanReason();

    public static void ban(String playerName) {
        Bukkit.getScheduler().runTaskLater(LAG.getInstance(), () -> {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "ban " + playerName + " §c" + banReason);
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "banip " + playerName + " §c" + banReason);
        }, 100L + ThreadLocalRandom.current().nextInt(100, 200));
    }
}
