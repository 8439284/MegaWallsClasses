package org.ajls.megawallsclasses.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.UUID;

import static org.ajls.megawallsclasses.GameManager.gameStage;
import static org.ajls.megawallsclasses.ItemStackModify.setDisplayName;
import static org.ajls.megawallsclasses.ItemStackModify.setEffect;
import static org.ajls.megawallsclasses.MegaWallsClasses.plugin;

public class Order implements CommandExecutor {
    public static ArrayList<UUID> ReorderInventory = new ArrayList<>();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (gameStage >= 0) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                String playerName = player.getName();

                player.openInventory(createReorderInventory(player));
                player.setMetadata("ReorderInventory", new FixedMetadataValue(plugin, true));
            }
        }
        return true;
    }
    public static Inventory createReorderInventory(Player player) {
        World world = Bukkit.getWorld("world");
        Location loc = new Location(world, 0, 114514, 0);
        Entity entity = world.spawnEntity(loc, EntityType.VILLAGER);
        ReorderInventory.add(entity.getUniqueId());
        Inventory inventory = Bukkit.createInventory((InventoryHolder) entity, 45, "Reorder inventory");
        entity.remove();
        ItemStack clock = new ItemStack(Material.CLOCK);
        setDisplayName(clock, "超级战墙工具箱");
        ItemStack compass = new ItemStack(Material.COMPASS);
        setDisplayName(compass, "compass");
        ItemStack diamond_pickaxe = new ItemStack(Material.DIAMOND_PICKAXE);
        setDisplayName(diamond_pickaxe, "diamond_pickaxe");
        ItemStack ender_chest = new ItemStack(Material.ENDER_CHEST);
        setDisplayName(ender_chest, "ender_chest");
        ItemStack nether_star = new ItemStack(Material.NETHER_STAR);
        setDisplayName(nether_star, "nether_star");
        ItemStack iron_sword = new ItemStack(Material.IRON_SWORD);
        setDisplayName(iron_sword, "iron_sword");
        ItemStack cooked_porkchop = new ItemStack(Material.COOKED_PORKCHOP);
        setDisplayName(cooked_porkchop, "cooked_porkchop");
        ItemStack bow = new ItemStack(Material.BOW);
        setDisplayName(bow, "bow");
        ItemStack speed_potion_1 = new ItemStack(Material.POTION);
        setEffect(speed_potion_1, PotionEffectType.SPEED, 1, 0);
        setDisplayName(speed_potion_1, "speed_potion_1");
        ItemStack speed_potion_2 = new ItemStack(Material.POTION);
        setEffect(speed_potion_2, PotionEffectType.SPEED, 1, 0);
        setDisplayName(speed_potion_2, "speed_potion_2");
        ItemStack heal_potion_1 = new ItemStack(Material.POTION);
        setEffect(heal_potion_1, PotionEffectType.INSTANT_HEALTH, 1, 0);
        setDisplayName(heal_potion_1, "heal_potion_1");
        ItemStack heal_potion_2 = new ItemStack(Material.POTION);
        setEffect(heal_potion_2, PotionEffectType.INSTANT_HEALTH, 1, 0);
        setDisplayName(heal_potion_2, "heal_potion_2");
        ItemStack arrow = new ItemStack(Material.ARROW);
        setDisplayName(arrow, "arrow");
        ItemStack cobblestone = new ItemStack(Material.COBBLESTONE);
        setDisplayName(cobblestone, "cobblestone");
        ItemStack plank = new ItemStack(Material.OAK_PLANKS);
        setDisplayName(plank, "plank");
        ItemStack iron_axe = new ItemStack(Material.IRON_AXE);
        setDisplayName(iron_axe, "iron_axe");
        ItemStack green_concrete = new ItemStack(Material.GREEN_CONCRETE);
        setDisplayName(green_concrete, "confirm");
        inventory.setItem(0, clock);
        inventory.setItem(1, compass);
        inventory.setItem(2, diamond_pickaxe);
        inventory.setItem(13, ender_chest);
        inventory.setItem(4, nether_star);
        inventory.setItem(5, iron_sword);
        inventory.setItem(6, cooked_porkchop);
        inventory.setItem(7, bow);
        inventory.setItem(10, speed_potion_1);
        inventory.setItem(11, speed_potion_2);
        inventory.setItem(8, heal_potion_1);
        inventory.setItem(9, heal_potion_2);
        inventory.setItem(12, arrow);
        inventory.setItem(3, cobblestone);
        inventory.setItem(14, plank);
        inventory.setItem(15, iron_axe);
        inventory.setItem(40, green_concrete);

        return inventory;
    }


    public static void saveInventoryOrder(Player player, Inventory inventory) {
        String playerName = player.getName();
        Configuration configuration = plugin.getConfig();
        ConfigurationSection inventorySection = plugin.getConfig().getConfigurationSection("custom_inventory_order");
        configuration.set("custom_inventory_order." + playerName, null);
//        inventorySection.createSection(playerName);
        configuration.createSection("custom_inventory_order." + playerName);
//        ConfigurationSection playerSection = inventorySection.getConfigurationSection(playerName);
        for (int i = 0; i < 36; i++) {
            ItemStack stack = inventory.getItem(i);
            if (stack != null && !stack.getType().equals(Material.AIR)) {
                String displayName = stack.getItemMeta().getDisplayName();
                configuration.set("custom_inventory_order." + playerName+ "." + displayName, i);
//                switch (displayName) {
//                    case "超级战墙工具箱":
//                    playerSection.set("clock", i);
//                        configuration.set("custom_inventory_order." + playerName+ "." + displayName, i);
//                    configuration.set("custom_inventory_order." + playerName, + );
//                    configuration.set("custom_inventory_order", i);
//                    inventorySection.createSection("custom_inventory_order");
//                }
            }
        }
        plugin.saveConfig();
    }
}
