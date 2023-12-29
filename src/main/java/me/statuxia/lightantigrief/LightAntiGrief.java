package me.statuxia.lightantigrief;

import lombok.Getter;
import me.statuxia.lightantigrief.commands.MarkTrusted;
import me.statuxia.lightantigrief.commands.TPWorld;
import me.statuxia.lightantigrief.listener.CheckPlayerListener;
import me.statuxia.lightantigrief.listener.GriefListener;
import me.statuxia.lightantigrief.listener.ModeratorListener;
import me.statuxia.lightantigrief.utils.PlayTime;
import net.coreprotect.CoreProtect;
import net.coreprotect.CoreProtectAPI;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

public final class LightAntiGrief extends JavaPlugin {

    private static final Set<UUID> moderators = new HashSet<>();
    @Getter
    private static final Component prefix = Component.text("§6[LAG] §r");
    @Getter
    private static final Set<String> trustedPlayers = new HashSet<>();
    @Getter
    private static final Set<String> trustedIPs = new HashSet<>();
    private static LightAntiGrief INSTANCE;
    @Getter
    private static CoreProtectAPI coreProtectAPI;

    public static LightAntiGrief getInstance() {
        return INSTANCE;
    }

    public static void addModerator(Player player) {
        moderators.add(player.getUniqueId());
    }

    public static void removeModerator(Player player) {
        moderators.remove(player.getUniqueId());
    }

    public static Set<Player> getModerators() {
        Set<Player> players = new HashSet<>();
        Set<UUID> toRemove = new HashSet<>();
        moderators.forEach(moderator -> {
            Player player = Bukkit.getPlayer(moderator);
            if (player != null) {
                players.add(player);
            } else {
                toRemove.add(moderator);
            }
        });
        moderators.removeAll(toRemove);
        return new HashSet<>(players);
    }

    public static void removeTrustedPlayer(String playerName) {
        trustedPlayers.remove(playerName.toLowerCase(Locale.ROOT));
    }

    public static boolean addTrustedPlayer(String playerName) {
        return trustedPlayers.add(playerName.toLowerCase(Locale.ROOT));
    }

    @Override
    public void onEnable() {
        INSTANCE = this;
        coreProtectAPI = getCoreProtect();
        if (coreProtectAPI == null) {
            System.out.println("CoreProtect not found. Are you using CoreProtect v21.0+?");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        System.out.println("It is not recommended to use at the beginning of the game. It is advisable to wait 2-3 days irl");

        Bukkit.getOnlinePlayers().forEach(player -> {
            if (player.hasPermission("lag.moder")) {
                moderators.add(player.getUniqueId());
            }
            if (!GriefListener.isTrusted(player) && PlayTime.ofSeconds(player) > 21600) {
                LAG.addTrustedPlayer(player.getName());
                LAG.addTrustedIp(player.getAddress().getAddress().getHostAddress());
            }
        });

        Bukkit.getPluginCommand("tpworld").setExecutor(new TPWorld());
        Bukkit.getPluginCommand("marktrusted").setExecutor(new MarkTrusted());
        Bukkit.getPluginManager().registerEvents(new GriefListener(), this);
        Bukkit.getPluginManager().registerEvents(new ModeratorListener(), this);
        Bukkit.getPluginManager().registerEvents(new CheckPlayerListener(), this);
    }

    private CoreProtectAPI getCoreProtect() {
        Plugin plugin = getServer().getPluginManager().getPlugin("CoreProtect");

        if (!(plugin instanceof CoreProtect)) {
            return null;
        }

        CoreProtectAPI CoreProtect = ((CoreProtect) plugin).getAPI();
        if (!CoreProtect.isEnabled()) {
            return null;
        }

        if (CoreProtect.APIVersion() < 9) {
            return null;
        }

        System.out.println("CoreProtect Loaded");
        return CoreProtect;
    }
}
