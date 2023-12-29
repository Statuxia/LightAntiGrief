package me.statuxia.lightantigrief.commands;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TPWorld implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player player)) {
            return false;
        }

        if (!player.hasPermission("lag.moder")) {
            player.sendMessage(Component.text("§7Not enough permissions!"));
            return false;
        }

        if (strings.length != 4) {
            player.sendMessage(Component.text("§7Not enough arguments! /tpworld {x} {y} {z} {world}"));
            return false;
        }

        World world;
        switch (strings[3]) {
            case "world_nether" -> world = Bukkit.getWorld("world_nether");
            case "world_the_end" -> world = Bukkit.getWorld("world_the_end");
            default -> world = Bukkit.getWorld("world");
        }

        int x, y, z;
        try {
            x = Integer.parseInt(strings[0]);
            y = Integer.parseInt(strings[1]);
            z = Integer.parseInt(strings[2]);
        } catch (NumberFormatException exception) {
            player.sendMessage("Incorrect coordinate format!");
            return false;
        }

        Location location = new Location(world, x, y, z);
        player.teleport(location);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        List<String> completions = new ArrayList<>();

        switch (strings.length) {
            case 1 -> completions.add("{x}");
            case 2 -> completions.add("{y}");
            case 3 -> completions.add("{z}");
            case 4 -> {
                completions.add("world");
                completions.add("world_nether");
                completions.add("world_the_end");
            }
        }

        return completions;
    }
}
