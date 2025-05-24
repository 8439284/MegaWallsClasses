package org.ajls.megawallsclasses.commands;

import org.ajls.lib.utils.ItemStackU;
import org.ajls.megawallsclasses.ArrayListUtils;
import org.ajls.megawallsclasses.InventoryManager;
import org.ajls.megawallsclasses.NameSpacedKeys;
import org.ajls.megawallsclasses.ScoreboardsAndTeams;
import org.ajls.tractorcompass.CompassItemStack;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

import static org.ajls.megawallsclasses.GameManager.gameStage;
import static org.ajls.megawallsclasses.ItemStackModify.*;
import static org.ajls.megawallsclasses.MegaWallsClasses.plugin;

public class Order implements CommandExecutor {
    public static ArrayList<UUID> ReorderInventory = new ArrayList<>();
    public static String MW_TOOLKIT = "超级战墙工具箱";
    public static String COMPASS = "compass";
    public static String DIAMOND_PICKAXE = "diamond_pickaxe";
    public static String ENDER_CHEST = "ender_chest";
    public static String NETHER_STAR = "nether_star";
    public static String IRON_SWORD = "iron_sword";
    public static String COOKED_PORKCHOP = "cooked_porkchop";
    public static String BOW = "bow";
    public static String SPEED_POTION_1 = "speed_potion_1";
    public static String SPEED_POTION_2 = "speed_potion_2";
    public static String HEAL_POTION_1 = "heal_potion_1";
    public static String HEAL_POTION_2 = "heal_potion_2";
    public static String ARROW = "arrow";
    public static String COBBLESTONE = "cobblestone";
    public static String PLANK = "plank";
    public static String IRON_AXE = "iron_axe";
    public static String HAY_BLOCK = "hay_block";


//    public static HashMap<String, ItemStack> itemName_itemStack = new HashMap<>();
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


    //The heart of class inventory
    public static Inventory createReorderInventory(Player player) {
        World world = Bukkit.getWorld("world");
        Location loc = new Location(world, 0, 114514, 0);
        Entity entity = world.spawnEntity(loc, EntityType.VILLAGER);
        ReorderInventory.add(entity.getUniqueId());
        Inventory inventory = Bukkit.createInventory((InventoryHolder) entity, 45, "Reorder inventory");
        entity.remove();
        ItemStack clock = new ItemStack(Material.CLOCK);
        setDisplayName(clock, "超级战墙工具箱");
        setClassItem(clock);
//        ItemStack compass = new ItemStack(Material.COMPASS);
//        setDisplayName(compass, "compass");
        ItemStack compass = CompassItemStack.tractorCompass.clone();
        setDisplayName(compass, "compass");
        ItemStack diamond_pickaxe = new ItemStack(Material.DIAMOND_PICKAXE);
        setDisplayName(diamond_pickaxe, "diamond_pickaxe");
        setUnbreakable(diamond_pickaxe);
        diamond_pickaxe.addEnchantment(Enchantment.EFFICIENCY, 3);
        setClassItem(diamond_pickaxe);
        ItemStack ender_chest = new ItemStack(Material.ENDER_CHEST);
        setDisplayName(ender_chest, "ender_chest");
        setClassItem(ender_chest);
        ItemStack nether_star = new ItemStack(Material.NETHER_STAR);
        setDisplayName(nether_star, "nether_star");
        setClassItem(nether_star);
        ItemStack iron_sword = InventoryManager.getClassSword(Material.IRON_SWORD);
//        ItemStack iron_sword = new ItemStack(Material.IRON_SWORD);
//        setDisplayName(iron_sword, "iron_sword");
//        setUnbreakable(iron_sword);
//        iron_sword.addEnchantment(Enchantment.UNBREAKING);
        ItemStack cooked_porkchop = new ItemStack(Material.COOKED_PORKCHOP, 64);
        setDisplayName(cooked_porkchop, "cooked_porkchop");
        ItemStack bow = InventoryManager.getClassBow();
//        ItemStack bow = new ItemStack(Material.BOW);
//        setDisplayName(bow, "bow");
//        setUnbreakable(bow);
//        bow.addEnchantment(Enchantment.UNBREAKING, 3);
        ItemStack speed_potion_1 = new ItemStack(Material.POTION);
        setBasePotionTye(speed_potion_1, PotionType.SWIFTNESS);
//        setEffect(speed_potion_1, PotionEffectType.SPEED, 1, 0);
        setDisplayName(speed_potion_1, "speed_potion_1");
        InventoryManager.setDontLoad(speed_potion_1);
        ItemStack speed_potion_2 = new ItemStack(Material.POTION);
//        setEffect(speed_potion_2, PotionEffectType.SPEED, 1, 0);
        setBasePotionTye(speed_potion_2, PotionType.SWIFTNESS);
        setDisplayName(speed_potion_2, "speed_potion_2");
        InventoryManager.setDontLoad(speed_potion_2);
        ItemStack heal_potion_1 = new ItemStack(Material.POTION);
//        setEffect(heal_potion_1, PotionEffectType.INSTANT_HEALTH, 1, 0);
        setBasePotionTye(heal_potion_1, PotionType.HEALING);
        setDisplayName(heal_potion_1, "heal_potion_1");
        InventoryManager.setDontLoad(heal_potion_1);
        ItemStack heal_potion_2 = new ItemStack(Material.POTION);
//        setEffect(heal_potion_2, PotionEffectType.INSTANT_HEALTH, 1, 0);
        setBasePotionTye(heal_potion_2, PotionType.HEALING);
        setDisplayName(heal_potion_2, "heal_potion_2");
        InventoryManager.setDontLoad(heal_potion_2);
        ItemStack arrow = new ItemStack(Material.ARROW, 64);
        setDisplayName(arrow, "arrow");
        ItemStack cobblestone = new ItemStack(Material.COBBLESTONE, 64);
        setDisplayName(cobblestone, "cobblestone");
        ItemStack plank = new ItemStack(Material.OAK_PLANKS, 64);
        setDisplayName(plank, "plank");
        ItemStack iron_axe = new ItemStack(Material.IRON_AXE);
        setDisplayName(iron_axe, "iron_axe");
        setUnbreakable(setAttributeAttackDamage(iron_axe, 4));  //new ItemStack(Material.IRON_AXE)

        setClassItem(iron_axe);

        ItemStack hay_block = new ItemStack(Material.HAY_BLOCK, 16);
        setDisplayName(hay_block, "hay_block");
        ItemStack squid_potion_for_everyone = InventoryManager.squid_potion_for_everyone();
        ItemStack green_concrete = new ItemStack(Material.GREEN_CONCRETE);
        setDisplayName(green_concrete, "confirm");
        ItemStack reset = new ItemStack(Material.YELLOW_CONCRETE);
        setDisplayName(reset, "reset_to_default");  //重置到默认
        inventory.setItem(0, clock);
        inventory.setItem(1, compass);
        inventory.setItem(2, diamond_pickaxe);
        inventory.setItem(13, ender_chest);
        inventory.setItem(17, nether_star);
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
        inventory.setItem(16, hay_block);
        inventory.setItem(4, squid_potion_for_everyone);
        inventory.setItem(40, green_concrete);
        inventory.setItem(36, reset);

        inventory = injectItemType(inventory);

        return inventory;
    }

    public static Inventory injectItemType(Inventory inventory) {
        //inject item type
        for (int i = 0 ; i < inventory.getSize(); i++) {
            ItemStack item = inventory.getItem(i);
            if (item != null && !item.getType().equals(Material.AIR)) {
                String displayName = item.getItemMeta().getDisplayName();

                ItemStackU.setStringPersistentData(item, NameSpacedKeys.ITEM_TYPE, displayName);
//                if (displayName.equals("confirm")) {
//                    setClassItem(item);
//                }
//                else if (displayName.equals("reset_to_default")) {
//                    setClassItem(item);
//                }
            }
        }
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

    public static void saveClassInventoryOrder(Player player, Inventory inventory, int index) {
        String playerName = player.getName();
        Configuration configuration = plugin.getConfig();
//        ConfigurationSection inventorySection = plugin.getConfig().getConfigurationSection("custom_inventory_order");
        configuration.set("class_inventory_order." + playerName + "." + index, null);
//        inventorySection.createSection(playerName);
        configuration.createSection("class_inventory_order." + playerName + "." + index);
//        ConfigurationSection playerSection = inventorySection.getConfigurationSection(playerName);
        for (int i = 0; i < 36; i++) {
            ItemStack stack = inventory.getItem(i);
            if (stack != null && !stack.getType().equals(Material.AIR)) {
                String displayName = stack.getItemMeta().getDisplayName();
                configuration.set("class_inventory_order." + playerName + "." + index + "." + displayName, i);
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


//    public static void saveInventoryOrder()


    //creates a new inventory and compare names and copy to an empty inventory
    public static Inventory loadReorderInventoryFromConfig(Player player, String title) {  //getItemFromName
        String playerName = player.getName();
        Inventory reorderInventory = createReorderInventory(player);
        Inventory newReorderInventory = Bukkit.createInventory((InventoryHolder) player, 45, title);
        Configuration configuration = plugin.getConfig();
//        configuration.getInt("custom_inventory_order." + playerName + ".iron_sword");
        for(String itemName :configuration.getConfigurationSection("custom_inventory_order." + playerName).getKeys(false)){
            int index = configuration.getInt("custom_inventory_order." + playerName + "." + itemName);
            for (int i = 0; i < 36; i++) {
                ItemStack stack = reorderInventory.getItem(i);
                if (stack != null && !stack.getType().equals(Material.AIR)) {
                    String displayName = stack.getItemMeta().getDisplayName();
                    if (displayName.equals(itemName)) {
                        newReorderInventory.setItem(index, stack);
                        break;
                    }
//                    configuration.set("custom_inventory_order." + playerName+ "." + displayName, i);
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
        }
        for(int i = 36; i < 45; i++) {
            ItemStack stack = reorderInventory.getItem(i);
            newReorderInventory.setItem(i, stack);
        }
        return newReorderInventory;
    }

    public static Inventory loadReorderInventoryFromConfig(Player player) {
        return loadReorderInventoryFromConfig(player, "Reorder inventory");
    }


    //rubbish code start


    public static void setPlayerItem(Player player, String lastPath, ItemStack itemStack) {
        Configuration configuration = plugin.getConfig();
        String playerName = player.getName();
        if (configuration.get("class_inventory_order." + playerName + "." + ScoreboardsAndTeams.getScore(player, "class") + "." + lastPath) == null) {
            player.getInventory().addItem(itemStack);
        }
        else {
            player.getInventory().setItem(getPlayerItemIndex(player, lastPath), itemStack);
        }
    }

    static int getPlayerItemIndex(Player player, String lastPath) {
        Configuration configuration = plugin.getConfig();
        String playerName = player.getName();
        if (configuration.get("class_inventory_order." + playerName + "." + ScoreboardsAndTeams.getScore(player, "class") + "." + lastPath) == null) { // + playerName + ".hay_block"
//        class_inventory_order:
            return getMinimalUnoccupiedItemIndex(player, ScoreboardsAndTeams.getScore(player, "class"));
        }
        else {
            return configuration.getInt("class_inventory_order." + playerName + "." + ScoreboardsAndTeams.getScore(player, "class") + "." + lastPath);
        }
    }

    static int getMinimalUnoccupiedItemIndex(Player player, int classIndex) {
        HashSet<Integer> occupied_slots = new HashSet<>();
        Configuration configuration = plugin.getConfig();
        String playerName = player.getName();
        if (configuration.get("class_inventory_order." + playerName + "." + classIndex) == null) {
//            if (configuration.get())
            return getMinimalUnoccupiedItemIndex("custom_inventory_order." + playerName);
        }
        for (String itemName :configuration.getConfigurationSection("class_inventory_order." + playerName + "." + classIndex).getKeys(false)) {
            int index = configuration.getInt("class_inventory_order." + playerName + "." + classIndex + "." + itemName);  //didn't include the base ReorderInventory, bug
            occupied_slots.add(index);
        }
        for (int i = 0; i <36 ; i ++) {
            if (!occupied_slots.contains(i)) {
                return i;
//                configuration.set("custom_inventory_order." + playerName + ".hay_block", i);
//                plugin.saveConfig();
//                break;
            }
        }
        return -1;
    }

    static int getMinimalUnoccupiedItemIndex(String path) {
        Configuration configuration = plugin.getConfig();
        if (configuration.get(path) == null) {
            return -1;
        }
        HashSet<Integer> occupied_slots = new HashSet<>();
        for (String itemName :configuration.getConfigurationSection(path).getKeys(false)) {
            int index = configuration.getInt(path + "." + itemName);
            occupied_slots.add(index);
        }
        for (int i = 0; i <36 ; i ++) {
            if (!occupied_slots.contains(i)) {
                return i;
//                configuration.set("custom_inventory_order." + playerName + ".hay_block", i);
//                plugin.saveConfig();
//                break;
            }
        }
        return -1;
    }

    public static ArrayList<Integer> getUnoccupiedItemIndices(String path) {
        ArrayList<Integer> unoccupied_slots = new ArrayList<>();
        for (int i = 0; i < 36; i++) {
            unoccupied_slots.add(i);
        }
        Configuration configuration = plugin.getConfig();
        ArrayList<Integer> occupied_slots = new ArrayList<>();
        for (String itemName :configuration.getConfigurationSection(path).getKeys(false)) {
            int index = configuration.getInt(path + "." + itemName);
            if (index < 36) {
                occupied_slots.add(index);
            }
        }
        return ArrayListUtils.removeArrayListIndexesClone(unoccupied_slots, occupied_slots);
    }
    //rubbish code enc

//    static {
//        itemName_itemStack.put()
//    }
}
