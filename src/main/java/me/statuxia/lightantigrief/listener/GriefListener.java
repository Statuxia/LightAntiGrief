package me.statuxia.lightantigrief.listener;

import lombok.Getter;
import me.statuxia.lightantigrief.config.LAGConfig;
import net.kyori.adventure.text.Component;
import me.statuxia.lightantigrief.LAG;
import me.statuxia.lightantigrief.trigger.BufferTrigger;
import me.statuxia.lightantigrief.trigger.actions.GriefAction;
import me.statuxia.lightantigrief.utils.BanUtils;
import me.statuxia.lightantigrief.utils.IdentifyUtils;
import me.statuxia.lightantigrief.utils.MessageUtils;
import me.statuxia.lightantigrief.utils.PlayTime;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.entity.minecart.HopperMinecart;
import org.bukkit.entity.minecart.StorageMinecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.projectiles.ProjectileSource;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;

public class GriefListener implements Listener {

    @Getter
    private static final int trustedTime = LAGConfig.getTrustedTime();

    private static final List<Material> itemTriggers = List.of(
            Material.DIAMOND_BLOCK,
            Material.DIAMOND_AXE,
            Material.DIAMOND_HOE,
            Material.DIAMOND_PICKAXE,
            Material.DIAMOND_SHOVEL,
            Material.DIAMOND_SWORD,
            Material.NETHERITE_AXE,
            Material.NETHERITE_HOE,
            Material.NETHERITE_PICKAXE,
            Material.NETHERITE_SHOVEL,
            Material.NETHERITE_SWORD,
            Material.DIAMOND_HELMET,
            Material.DIAMOND_CHESTPLATE,
            Material.DIAMOND_LEGGINGS,
            Material.DIAMOND_BOOTS,
            Material.NETHERITE_HELMET,
            Material.NETHERITE_CHESTPLATE,
            Material.NETHERITE_LEGGINGS,
            Material.NETHERITE_BOOTS,
            Material.ELYTRA,
            Material.DIAMOND,
            Material.NETHERITE_INGOT,
            Material.TNT,
            Material.END_CRYSTAL,
            Material.BEACON,
            Material.GOLDEN_CARROT,
            Material.TRIDENT,
            Material.SHULKER_BOX,
            Material.NETHERITE_SCRAP,
            Material.NETHER_STAR,
            Material.TOTEM_OF_UNDYING,
            Material.SHULKER_SHELL,
            Material.ENCHANTED_GOLDEN_APPLE,
            Material.NETHERITE_UPGRADE_SMITHING_TEMPLATE,
            Material.DIAMOND_ORE,
            Material.DEEPSLATE_DIAMOND_ORE,
            Material.WITHER_SKELETON_SKULL
    );

    private static final List<Material> blockTriggers = List.of(
            Material.CHEST,
            Material.BARREL,
            Material.SHULKER_BOX,
            Material.WHITE_SHULKER_BOX,
            Material.ORANGE_SHULKER_BOX,
            Material.MAGENTA_SHULKER_BOX,
            Material.LIGHT_BLUE_SHULKER_BOX,
            Material.YELLOW_SHULKER_BOX,
            Material.LIME_SHULKER_BOX,
            Material.PINK_SHULKER_BOX,
            Material.GRAY_SHULKER_BOX,
            Material.LIGHT_GRAY_SHULKER_BOX,
            Material.CYAN_SHULKER_BOX,
            Material.PURPLE_SHULKER_BOX,
            Material.BLUE_SHULKER_BOX,
            Material.BROWN_SHULKER_BOX,
            Material.GREEN_SHULKER_BOX,
            Material.RED_SHULKER_BOX,
            Material.BLACK_SHULKER_BOX,
            Material.TRAPPED_CHEST
    );

    private static final List<Material> railTriggers = List.of(
            Material.RAIL,
            Material.POWERED_RAIL,
            Material.DETECTOR_RAIL,
            Material.ACTIVATOR_RAIL
    );

    private static final List<Material> explosiveBlockTriggers = List.of(
            Material.TNT,
            Material.RESPAWN_ANCHOR,
            Material.WITHER_SKELETON_SKULL
    );

    private static final List<Material> explosiveBedTriggers = List.of(
            Material.RED_BED,
            Material.ORANGE_BED,
            Material.YELLOW_BED,
            Material.LIME_BED,
            Material.GREEN_BED,
            Material.LIGHT_BLUE_BED,
            Material.CYAN_BED,
            Material.BLUE_BED,
            Material.PURPLE_BED,
            Material.MAGENTA_BED,
            Material.PINK_BED,
            Material.BROWN_BED,
            Material.WHITE_BED,
            Material.LIGHT_GRAY_BED,
            Material.GRAY_BED,
            Material.BLACK_BED
    );

    private static final List<Material> ignoreBurnable = List.of(
            Material.VINE,
            Material.BAMBOO,
            Material.ACACIA_LEAVES,
            Material.BIRCH_LEAVES,
            Material.DARK_OAK_LEAVES,
            Material.JUNGLE_LEAVES,
            Material.OAK_LEAVES,
            Material.SPRUCE_LEAVES,
            Material.AZALEA_LEAVES,
            Material.CHERRY_LEAVES,
            Material.FLOWERING_AZALEA_LEAVES,
            Material.MANGROVE_LEAVES,
            Material.MANGROVE_LOG,
            Material.JUNGLE_LOG
    );

    @EventHandler
    public void onClickInventory(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) {
            return;
        }

        if (isTrusted(player)) {
            return;
        }

        Inventory top = event.getView().getTopInventory();
        Location topLocation = top.getLocation();

        Inventory clickedInventory = event.getClickedInventory();
        if (clickedInventory == null) {
            return;
        }

        if (PlayTime.ofSeconds(player) > trustedTime) {
            return;
        }

        if (isEntityHolder(top.getHolder()) && clickedInventory instanceof PlayerInventory) {
            if (topLocation != null) {
                checkForTrigger(event, topLocation, player, GriefAction.PUT_ITEM);
            }
            return;
        }

        if (clickedInventory instanceof PlayerInventory) {
            return;
        }

        Location location = clickedInventory.getLocation();
        if (location == null) {
            return;
        }

        if (isEntityHolder(clickedInventory.getHolder())) {
            if ((event.getAction() == InventoryAction.PLACE_ALL || event.getAction() == InventoryAction.PLACE_ONE || event.getAction() == InventoryAction.PLACE_SOME)) {
                checkForTrigger(event, location, player, GriefAction.PUT_ITEM);
                return;
            }
            checkForTrigger(event, location, player, GriefAction.GET_ITEM);
            return;
        }

        Block block = location.getBlock();
        if (!blockTriggers.contains(block.getType())) {
            return;
        }

        if (IdentifyUtils.isOwner(block, player)) {
            return;
        }
        AtomicBoolean isNearOwner = new AtomicBoolean(false);
        player.getNearbyEntities(20, 20, 20).forEach(entity -> {
            if (entity instanceof Player nearPlayer) {
                if (IdentifyUtils.isOwner(block, nearPlayer)) {
                    isNearOwner.set(true);
                }
            }
        });
        if (isNearOwner.get()) {
            return;
        }
        if ((event.getAction() == InventoryAction.PLACE_ALL || event.getAction() == InventoryAction.PLACE_ONE || event.getAction() == InventoryAction.PLACE_SOME)) {
            return;
        }
        checkForTrigger(event, location, player, GriefAction.GET_ITEM);
    }

    private boolean isEntityHolder(InventoryHolder holder) {
        return (holder instanceof HopperMinecart || holder instanceof AbstractHorse ||
                holder instanceof ChestBoat || holder instanceof StorageMinecart);
    }

    private void checkForTrigger(InventoryClickEvent event, Location location, Player player, GriefAction action) {
        ItemStack currentItem = event.getCurrentItem();
        if (currentItem == null) {
            return;
        }

        if (!itemTriggers.contains(currentItem.getType())) {
            return;
        }

        Component component = MessageUtils.generateMessage(player, location, action, currentItem.getType());
        Bukkit.getLogger().info(MessageUtils.plainText(component).replaceAll("§[0-9abcdefkmonlr]", ""));
        if (LAG.getModerators().isEmpty()) {
            BufferTrigger buffer = new BufferTrigger(player.getUniqueId(), action);
            BufferTrigger.trigger(buffer);

            if (BufferTrigger.isLimitReached(buffer)) {
                BanUtils.ban(player.getName());
            }
            return;
        }

        LAG.getModerators().forEach(moderator -> moderator.sendMessage(component));
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();

        Block block = event.getBlock();
        if (!blockTriggers.contains(block.getType())) {
            return;
        }

        if (isTrusted(player)) {
            return;
        }

        if (PlayTime.ofSeconds(player) > trustedTime) {
            return;
        }

        if (IdentifyUtils.isOwner(block, player)) {
            return;
        }
        AtomicBoolean isNearOwner = new AtomicBoolean(false);
        player.getNearbyEntities(20, 20, 20).forEach(entity -> {
            if (entity instanceof Player nearPlayer) {
                if (IdentifyUtils.isOwner(block, nearPlayer)) {
                    isNearOwner.set(true);
                }
            }
        });
        if (isNearOwner.get()) {
            return;
        }


        Component component = MessageUtils.generateMessage(player, block.getLocation(), GriefAction.BREAK_BLOCK, block.getType());
        Bukkit.getLogger().info(MessageUtils.plainText(component).replaceAll("§[0-9abcdefkmonlr]", ""));
        if (LAG.getModerators().isEmpty()) {
            BufferTrigger buffer = new BufferTrigger(player.getUniqueId(), GriefAction.BREAK_BLOCK);
            BufferTrigger.trigger(buffer);

            if (BufferTrigger.isLimitReached(buffer)) {
                BanUtils.ban(player.getName());
            }
            return;
        }

        LAG.getModerators().forEach(moderator -> moderator.sendMessage(component));
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();

        Block block = event.getBlock();
        Material type = block.getType();
        if (!explosiveBlockTriggers.contains(type) && !explosiveBedTriggers.contains(type)) {
            return;
        }

        if (isTrusted(player)) {
            return;
        }

        if (PlayTime.ofSeconds(player) > trustedTime) {
            return;
        }

        if (explosiveBedTriggers.contains(type) && player.getWorld().equals(Bukkit.getWorld("world")) || player.getLocation().getBlockY() < 40) {
            return;
        }

        Component component = MessageUtils.generateMessage(player, block.getLocation(), GriefAction.PLACE_BLOCK, type);
        Bukkit.getLogger().info(MessageUtils.plainText(component).replaceAll("§[0-9abcdefkmonlr]", ""));
        if (LAG.getModerators().isEmpty()) {
            Bukkit.getLogger().info("PLACE");
            BufferTrigger buffer = new BufferTrigger(player.getUniqueId(), GriefAction.PLACE_BLOCK);
            Bukkit.getLogger().info("buffer");
            BufferTrigger.trigger(buffer);
            Bukkit.getLogger().info("total - " + BufferTrigger.getTotal(buffer));
            Bukkit.getLogger().info("is reached - " + BufferTrigger.isLimitReached(buffer));
            if (BufferTrigger.isLimitReached(buffer)) {
                BanUtils.ban(player.getName());
            }
            return;
        }

        LAG.getModerators().forEach(moderator -> moderator.sendMessage(component));
    }

    @EventHandler
    public void onMineCartPlace(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        Block clickedBlock = event.getClickedBlock();
        if (clickedBlock == null) {
            return;
        }
        ItemStack item = event.getItem();
        if (item == null) {
            return;
        }

        if (!railTriggers.contains(clickedBlock.getType())) {
            return;
        }

        Player player = event.getPlayer();
        if (isTrusted(event.getPlayer())) {
            return;
        }

        if (PlayTime.ofSeconds(player) > trustedTime) {
            return;
        }

        if (!(item.getType() == Material.HOPPER_MINECART || item.getType() == Material.TNT_MINECART)) {
            return;
        }

        Component component = MessageUtils.generateMessage(player, clickedBlock.getLocation(), GriefAction.MINECART, item.getType());
        Bukkit.getLogger().info(MessageUtils.plainText(component).replaceAll("§[0-9abcdefkmonlr]", ""));
        if (LAG.getModerators().isEmpty()) {
            BufferTrigger buffer = new BufferTrigger(player.getUniqueId(), GriefAction.MINECART);
            BufferTrigger.trigger(buffer);

            if (BufferTrigger.isLimitReached(buffer)) {
                BanUtils.ban(player.getName());
            }
            return;
        }

        LAG.getModerators().forEach(moderator -> moderator.sendMessage(component));
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockExplode(BlockExplodeEvent event) {
        Block explodedBlock = event.getBlock();

        if (!explosiveBlockTriggers.contains(explodedBlock.getType())) {
            return;
        }

        event.blockList().forEach(block -> {
            if (block.getType() == Material.AIR) {
                return;
            }
            if (!blockTriggers.contains(block.getType())) {
                return;
            }

            Component component = MessageUtils.generateMessage(block, GriefAction.EXPLODE, block.getType());
            Bukkit.getLogger().info(MessageUtils.plainText(component).replaceAll("§[0-9abcdefkmonlr]", ""));
            LAG.getModerators().forEach(moderator -> moderator.sendMessage(component));
        });
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerFireCharge(PlayerInteractEvent event) {
        if (!event.getAction().isRightClick()) {
            return;
        }

        Block clickedBlock = event.getClickedBlock();
        if (clickedBlock == null) {
            return;
        }
        if (!clickedBlock.getType().isBurnable() || ignoreBurnable.contains(clickedBlock.getType())) {
            return;
        }

        Player player = event.getPlayer();
        if (isTrusted(event.getPlayer())) {
            return;
        }

        if (PlayTime.ofSeconds(player) > trustedTime) {
            return;
        }

        ItemStack item = event.getItem();
        if (!(item != null && (item.getType() == Material.FLINT_AND_STEEL || item.getType() == Material.FIRE_CHARGE))) {
            return;
        }

        Component component = MessageUtils.generateMessage(player, clickedBlock.getLocation(), GriefAction.FIRE_CHARGE, clickedBlock.getType());
        Bukkit.getLogger().info(MessageUtils.plainText(component).replaceAll("§[0-9abcdefkmonlr]", ""));
        if (LAG.getModerators().isEmpty()) {
            BufferTrigger buffer = new BufferTrigger(player.getUniqueId(), GriefAction.FIRE_CHARGE);
            BufferTrigger.trigger(buffer);

            if (BufferTrigger.isLimitReached(buffer)) {
                BanUtils.ban(player.getName());
            }
            return;
        }

        LAG.getModerators().forEach(moderator -> moderator.sendMessage(component));
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerPlaceCrystal(PlayerInteractEvent event) {
        if (!event.getAction().isRightClick()) {
            return;
        }

        Block clickedBlock = event.getClickedBlock();
        if (clickedBlock == null) {
            return;
        }
        if (clickedBlock.getType() != Material.OBSIDIAN) {
            return;
        }

        if (isTrusted(event.getPlayer())) {
            return;
        }

        if (PlayTime.ofSeconds(event.getPlayer()) > trustedTime) {
            return;
        }

        ItemStack item = event.getItem();
        Player player = event.getPlayer();
        if (!(item != null && item.getType() == Material.END_CRYSTAL)) {
            return;
        }

        Component component = MessageUtils.generateMessage(player, clickedBlock.getLocation(), GriefAction.PLACE_BLOCK, item.getType());
        Bukkit.getLogger().info(MessageUtils.plainText(component).replaceAll("§[0-9abcdefkmonlr]", ""));
        if (LAG.getModerators().isEmpty()) {
            BufferTrigger buffer = new BufferTrigger(player.getUniqueId(), GriefAction.PLACE_BLOCK);
            BufferTrigger.trigger(buffer);

            if (BufferTrigger.isLimitReached(buffer)) {
                BanUtils.ban(player.getName());
            }
            return;
        }

        LAG.getModerators().forEach(moderator -> moderator.sendMessage(component));

    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        Entity entity = event.getEntity();

        if (event.getEntity() instanceof TNTPrimed primed) {
            if (primed.getSource() != null && primed.getSource() instanceof Player player) {
                checkExplode(player, event.blockList());
                return;
            }

            if (!(primed.getSource() instanceof Projectile projectile)) {
                return;
            }
            ProjectileSource shooter = projectile.getShooter();
            if (shooter instanceof Player player) {
                checkExplode(player, event.blockList());
                return;
            }
            if (shooter instanceof Mob mob && mob.getTarget() instanceof Player player) {
                checkExplode(player, event.blockList());
                return;
            }
        }

        if (entity instanceof EnderCrystal crystal) {
            EntityDamageEvent lastDamageCause = crystal.getLastDamageCause();
            if (lastDamageCause == null) {
                return;
            }

            if (!(lastDamageCause instanceof EntityDamageByEntityEvent damageByEntityEvent)) {
                return;
            }

            if (damageByEntityEvent.getDamager() instanceof Player player) {
                checkExplode(player, event.blockList());
                return;
            }
            if (!(damageByEntityEvent.getDamager() instanceof Projectile projectile)) {
                return;
            }
            ProjectileSource shooter = projectile.getShooter();
            if (shooter instanceof Player player) {
                checkExplode(player, event.blockList());
                return;
            }
            if (shooter instanceof Mob mob && mob.getTarget() instanceof Player player) {
                checkExplode(player, event.blockList());
                return;
            }
        }

        if (entity instanceof Projectile projectile) {
            ProjectileSource shooter = projectile.getShooter();
            if (shooter instanceof Player player) {
                checkExplode(player, event.blockList());
                return;
            }
            if (shooter instanceof Mob mob && mob.getTarget() instanceof Player player) {
                checkExplode(player, event.blockList());
                return;
            }
        }

        if (entity instanceof Creeper creeper) {
            LivingEntity target = creeper.getTarget();
            if (!(target instanceof Player player)) {
                return;
            }
            checkExplode(player, event.blockList());
        }
    }

    private void checkExplode(Player player, List<Block> blockList) {
        if (isTrusted(player)) {
            return;
        }
        if (PlayTime.ofSeconds(player) > trustedTime) {
            return;
        }

        blockList.forEach(block -> {
            if (block.getType() == Material.AIR) {
                return;
            }
            if (!blockTriggers.contains(block.getType())) {
                return;
            }

            Component component = MessageUtils.generateMessage(player, block.getLocation(), GriefAction.EXPLODE, block.getType());
            Bukkit.getLogger().info(MessageUtils.plainText(component).replaceAll("§[0-9abcdefkmonlr]", ""));
            if (LAG.getModerators().isEmpty()) {
                BufferTrigger buffer = new BufferTrigger(player.getUniqueId(), GriefAction.EXPLODE);
                BufferTrigger.trigger(buffer);

                if (BufferTrigger.isLimitReached(buffer)) {
                    BanUtils.ban(player.getName());
                }
                return;
            }

            LAG.getModerators().forEach(moderator -> moderator.sendMessage(component));
        });
    }

    public static boolean isTrusted(Player player) {
        return isTrusted(player.getName()) || LAG.getTrustedIPs().contains(player.getAddress().getAddress().getHostAddress());
    }

    private static boolean isTrusted(String player) {
        return LAG.getTrustedPlayers().contains(player.toLowerCase(Locale.ROOT));
    }
}
