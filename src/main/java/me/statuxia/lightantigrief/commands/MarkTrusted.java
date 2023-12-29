package me.statuxia.lightantigrief.commands;

import net.kyori.adventure.text.Component;
import me.statuxia.lightantigrief.LAG;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Locale;

public class MarkTrusted implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player player)) {
            return false;
        }

        if (!player.hasPermission("lag.moder")) {
            player.sendMessage(Component.text("§7Not enough permissions!"));
            return false;
        }

        try {
            String playerName = strings[0];
            if (playerName.length() > 16) {
                player.sendMessage(Component.text("§7Nickname must not be more than 16 characters"));
                return false;
            }
            if (!LAG.addTrustedPlayer(playerName)) {
                LAG.removeTrustedPlayer(playerName);
                player.sendMessage(Component.text("§7Nickname Deleted"));
            } else {
                player.sendMessage(Component.text("§7Nickname Added"));
            }
            return true;
        } catch (Exception ignored) {
            player.sendMessage(Component.text("§7There is no target. Type /marktrusted Player"));
            return false;
        }
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return Bukkit.getOnlinePlayers().stream().map(Player::getName).filter(str -> str.toLowerCase(Locale.ROOT).startsWith(strings[0])).toList();
    }
}
