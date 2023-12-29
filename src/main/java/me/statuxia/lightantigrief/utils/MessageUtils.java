package me.statuxia.lightantigrief.utils;

import me.statuxia.lightantigrief.LAG;
import me.statuxia.lightantigrief.trigger.actions.GriefAction;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class MessageUtils {

    public static Component generateMessage(Block block, GriefAction action, Material type) {
        return generateMessage(block.getType().toString(), block.getLocation(), action, type.toString());
    }

    public static Component generateMessage(Player player, Location location, GriefAction action, Material type) {
        return generateMessage(player.getName(), location, action, type.toString());
    }

    public static Component generateMessage(String name, Location location, GriefAction action, String type) {
        return LAG.getPrefix()
                .append(Component.text("§c" + name + "§7"))
                .append(Component.text("§e"))
                .append(action.getMessage())
                .append(Component.text(type))
                .append(Component.text("§r on "))
                .append(generateTeleport(location));
    }

    public static Component generateTeleport(Location location) {
        String coordinates = location.getBlockX() + " " + location.getBlockY() + " " + location.getBlockZ();
        TextComponent text = Component.text("(§b§n" + coordinates + "§r)");

        return text.clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/tpworld " + coordinates + " " + location.getWorld().getName()))
                .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, Component.text("Teleport!")));
    }

    public static String plainText(Component component) {
        return PlainTextComponentSerializer.plainText().serialize(component);
    }
}
