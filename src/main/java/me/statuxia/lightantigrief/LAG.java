package me.statuxia.lightantigrief;

import net.coreprotect.CoreProtectAPI;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.Set;

public class LAG {

    public static Set<String> getTrustedPlayers() {
        return LightAntiGrief.getTrustedPlayers();
    }

    public static Set<String> getTrustedIPs() {
        return LightAntiGrief.getTrustedIPs();
    }

    public static void removeTrustedPlayer(String playerName) {
        LightAntiGrief.removeTrustedPlayer(playerName);
    }

    public static boolean addTrustedPlayer(String playerName) {
        return LightAntiGrief.addTrustedPlayer(playerName);
    }

    public static CoreProtectAPI getCoreProtectAPI() {
        return LightAntiGrief.getCoreProtectAPI();
    }

    public static void addModerator(Player player) {
        LightAntiGrief.addModerator(player);
    }

    public static void removeModerator(Player player) {
        LightAntiGrief.removeModerator(player);
    }

    public static Set<Player> getModerators() {
        return LightAntiGrief.getModerators();
    }

    public static LightAntiGrief getInstance() {
        return LightAntiGrief.getInstance();
    }

    public static Component getPrefix() {
        return LightAntiGrief.getPrefix();
    }

    public static void addTrustedIp(String hostAddress) {
        LightAntiGrief.getTrustedIPs().add(hostAddress);
    }
}
