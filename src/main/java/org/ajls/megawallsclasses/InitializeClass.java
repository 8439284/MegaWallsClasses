package org.ajls.megawallsclasses;

import org.ajls.lib.advanced.HaxhMap;
import org.ajls.lib.references.Time;
import org.ajls.lib.utils.ItemStackU;
import org.ajls.megawallsclasses.container.WardenDarknessTargetTimestamp;
import org.bukkit.*;
import org.bukkit.configuration.Configuration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;

//import static org.ajls.megawallsclasses.MegaWallsClasses.tractorCompass;
import static org.ajls.megawallsclasses.EnergyAccumulate.*;
import static org.ajls.megawallsclasses.ItemStackModify.*;
import static org.ajls.megawallsclasses.MegaWallsClasses.*;
import static org.ajls.megawallsclasses.MyListener.*;

public class InitializeClass {
//    static ItemStack speed_potion_1;
//    static ItemStack speed_potion_2;
//    static ItemStack health_potion_1;
//    static ItemStack health_potion_2;
    static ItemStack createSpeedPotion(int duration, int amplifier, int amount, int index) {
        ItemStack speed_potion = new ItemStack(Material.POTION, amount);
        ItemStackModify.setBasePotionTye(speed_potion, PotionType.SWIFTNESS);
        MegaWallsClasses.setEffect(speed_potion, PotionEffectType.SPEED, duration, amplifier, false , true);

//        ItemStackModify.setBasePotionTye(speed_potion, PotionType.SWIFTNESS);
        String romeNum = "I";
        for (int i = 0; i < amplifier; i++) {
            romeNum = romeNum + "I";
        }
        setDisplayName(speed_potion, "speed_potion_" + index);
        InventoryManager.setLoadDisplayName(speed_potion, ChatColor.AQUA + "" + (int) Math.floor(duration/20) + "s " + romeNum);
//        setDisplayName(speed_potion, (int) Math.floor(duration/20) + "s " + romeNum);  //"15s II"
        setLore(speed_potion, "speed_potion");
        addLore(speed_potion, "custom_potion");
        ItemStackModify.setMaxStackSize(speed_potion, amount);
        setClassItem(speed_potion);
        return speed_potion;
    }
    static ItemStack createHealthPotion(int duration, int amount, int index) {
        ItemStack health_potion = new ItemStack(Material.POTION, amount);
        ItemStackModify.setBasePotionTye(health_potion, PotionType.HEALING);
        MegaWallsClasses.setEffect(health_potion, PotionEffectType.INSTANT_HEALTH, duration, 0);

//        ItemStackModify.setBasePotionTye(speed_potion, PotionType.SWIFTNESS);
        setDisplayName(health_potion, "heal_potion_" + index);
        InventoryManager.setLoadDisplayName(health_potion, ChatColor.RED + "" + duration + " HP");
//        setDisplayName(health_potion, duration + " HP");  //"15s II"
        setLore(health_potion, "heal_potion");
        ItemStackModify.setMaxStackSize(health_potion, amount);
        setClassItem(health_potion);
        return health_potion;
    }
    static void setItem(Player player, String lastPath, ItemStack itemStack) {
        Configuration configuration = plugin.getConfig();
        String playerName = player.getName();
        player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + "." + lastPath), itemStack);
    }

    public static void setClassSpecificItem(Player player, int index, String lastPath, ItemStack itemStack) {
        Configuration configuration = plugin.getConfig();
        String playerName = player.getName();
        Inventory playerInventory = player.getInventory();
//        if (configuration.contains("custom_inventory_order." + playerName + "." + lastPath)) {
//            playerInventory.setItem(configuration.getInt("custom_inventory_order." + playerName + "." + lastPath), itemStack);;
//        }
//        else {
        int configClassItemIndex = getConfigClassItemIndex(player, index, lastPath);
        if (configClassItemIndex != -1) {
            playerInventory.setItem(configClassItemIndex, itemStack);
//            }
//            playerInventory.setItem(, itemStack);
//            playerInventory.setItem(configuration.getInt("class_inventory_order." + playerName + "." + index + "." + lastPath), itemStack);    ;
        }

    }
    public static void setClassSpecificItem(Player player, String lastPath, ItemStack itemStack) {
        setClassSpecificItem(player, ClassU.getClass(player), lastPath, itemStack);
    }
    static int getConfigItemIndex(Player player, String lastPath) {
        Configuration configuration = plugin.getConfig();
        String playerName = player.getName();
        if (configuration.contains("custom_inventory_order." + playerName + "." + lastPath)) {
            return configuration.getInt("custom_inventory_order." + playerName + "." + lastPath);
        }
        else {
            return -1;
        }
        //return configuration.getInt("custom_inventory_order." + playerName + "." + lastPath);
    }

    static int getConfigClassItemIndex(Player player, int index, String lastPath) {
        Configuration configuration = plugin.getConfig();
        String playerName = player.getName();
        Inventory playerInventory = player.getInventory();
        if (configuration.contains("class_inventory_order." + playerName + "." + index + "." + lastPath)) { //contain
            return configuration.getInt("class_inventory_order." + playerName + "." + index + "." + lastPath);
        }
        else {
            int configItemIndex = getConfigItemIndex(player, lastPath);  //not the class config
            if (configItemIndex != -1) {
                return configItemIndex;
            }
            else {
                Inventory classInventory = InventoryManager.createClassReorderInventory(player, index);
                for (int i = 0; i < 36; i++) {
                    ItemStack stack = classInventory.getItem(i);
                    if (stack != null && !stack.getType().equals(Material.AIR)) {
                        if (getDisplayName(stack) == lastPath) {
                            return i;
                        }
                    }
                }
                return -1;
            }
//            playerInventory.setItem(configuration.getInt("class_inventory_order." + playerName + "." + index + "." + lastPath), itemStack);    ;
        }
    }

    static int getConfigClassItemIndex(Player player, String lastPath) {
        return getConfigClassItemIndex(player, ClassU.getClass(player), lastPath);
    }
//    public static TractorCompass tractorCompass = new TractorCompass();
    //base
    public static void resetPlayerCondition(Player player) {
        World world = player.getWorld();
        //        player.setDisplayName(player.getName());
        player.getInventory().clear();
//        player.setMaxHealth(44);
        player.setHealth(player.getMaxHealth());
        player.setExhaustion(0);
        player.setFoodLevel(20);
        player.setSaturation(5);
//        BukkitTask task = tasks.remove(player.getUniqueId()); // remove from map if exist
//        if(task != null) { // task found
//            task.cancel();
//        }
        levelEqualsEnergy(player);
//        MegaWallsClasses.setScore(player, "energy", 0);
//        player.setLevel(0);
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, -1, 0, false, false));  //Integer.MAX_VALUE
        player.addPotionEffect(new PotionEffect(PotionEffectType.INSTANT_HEALTH, 20*5, 100, false, true));  //originally 3
        player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 20*5, 255, false, true));  //originally 3
        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 20*3, 255, false, true));  //originally 3
        for (Player otherPlayer: Bukkit.getOnlinePlayers()) {
            otherPlayer.hidePlayer(MegaWallsClasses.getPlugin(), player);
        }
        Bukkit.getScheduler().runTaskLater(MegaWallsClasses.getPlugin(), () -> {
            for (Player otherPlayer: Bukkit.getOnlinePlayers()) {
                otherPlayer.showPlayer(MegaWallsClasses.getPlugin(), player);
            }
        }, 20*3);

//        PassiveSkills.null_hide_armor(player);
    }
    public static void initializeClassBase(Player player) {

    }

    public static void refreshClassOnChangeClass(Player player) { // include class items
        //this method does not recognize tf
        disableClass(player);
        enableClass(player);
//        elaina_disable(player);
//        initializeClass(player);
//        disableAutoEnergyAccumulation(player);
//        InitializeClass.initializeAutoEnergyAccumulation(player);
//        InitializeClass.initializeDeathMatchAutoEnergyAccumulation(player);
//        Cooldown.displayCooldown(player);
//
//        BukkitTask task = player_activeSkillReady.get(player.getUniqueId());
//        if (task != null) {
//            task.cancel();
//        }
//        ScoreboardsAndTeams.setScore(player, "energy" , 0);
//        player.setLevel(0);
//        player.setExp(0);
    }

    public static void refreshClassOnDeath(Player player) {
        //TODO implement refreshClassOnDeath
    }


    public static void disableClass(Player player) {
        elaina_disable(player);
        warden_disable(player);
        disableAutoEnergyAccumulation(player);
        Cooldown.notDisplayCooldown(player);
        BukkitTask task = player_activeSkillReady.get(player.getUniqueId());
        if (task != null) {
            task.cancel();
        }
        ScoreboardsAndTeams.setScore(player, "energy" , 0);
        player.setLevel(0);
        player.setExp(0);
    }
    public static void enableClass(Player player) {
        initializeClass(player);
//        InitializeClass.initializeAutoEnergyAccumulation(player);
//        InitializeClass.initializeDeathMatchAutoEnergyAccumulation(player);
        addEnergy(player, 0);
        Cooldown.displayCooldown(player);
    }

    public static void initializeClassExtra(Player player) { // sword, bow, potions
//        player.getInventory().setItem();
    }

    public static void initializeClassSpecific(Player player) {
        initializeClassSpecific(player, false);
    }
    public static void initializeClassSpecific(Player player, boolean loadAsTF) {
        Inventory playerInventory = player.getInventory();
//        player.getInventory().clear();


        //        player.getInventory().setHelmet(setUnbreakable(new ItemStack(Material.IRON_HELMET)));
//        player.getInventory().setChestplate(setUnbreakable(new ItemStack(Material.IRON_CHESTPLATE)));
//        player.getInventory().setLeggings(setUnbreakable(new ItemStack(Material.IRON_LEGGINGS)));
//        player.getInventory().setBoots(setUnbreakable(new ItemStack(Material.IRON_BOOTS)));
//
////        player.getInventory().setItem(0, setUnbreakable(new ItemStack(Material.CLOCK)));
//        givePlayerCompass(player, 0);
//        player.getInventory().setItem(1, setUnbreakable(new ItemStack(Material.IRON_AXE)));
//        ItemStack diamond_pickaxe = setUnbreakable(new ItemStack(Material.DIAMOND_PICKAXE));
//        diamond_pickaxe.addEnchantment(Enchantment.EFFICIENCY, 3);
//        player.getInventory().setItem(2, diamond_pickaxe);
//        player.getInventory().setItem(3, new ItemStack(Material.OAK_PLANKS, 128));
//        player.getInventory().setItem(4, new ItemStack(Material.COBBLESTONE, 128));
//        player.getInventory().setItem(5, new ItemStack(Material.NETHER_STAR, 1));
//        player.getInventory().setItem(6, setUnbreakable(new ItemStack(Material.IRON_SWORD, 1)));
//        player.getInventory().setItem(7, new ItemStack(Material.COOKED_PORKCHOP, 5));
//        player.getInventory().setItem(8, setUnbreakable(new ItemStack(Material.BOW, 1)));
//        player.getInventory().setItem(34, setUnbreakable(new ItemStack(Material.ARROW, 128)));
////        player.getInventory().setItem(35, setUnbreakable(new ItemStack(Material.ARROW, 64)));
        player.getInventory().setHelmet(setClassItem(setUnbreakable(new ItemStack(Material.IRON_HELMET))));
        player.getInventory().setChestplate(setClassItem(setUnbreakable(new ItemStack(Material.IRON_CHESTPLATE))));
        player.getInventory().setLeggings(setClassItem(setUnbreakable(new ItemStack(Material.IRON_LEGGINGS))));
        player.getInventory().setBoots(setClassItem(setUnbreakable(new ItemStack(Material.IRON_BOOTS))));
        //start here
//        Configuration configuration = plugin.getConfig();
//        String playerName = player.getName();
//        player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".超级战墙工具箱"), setUnbreakable(addLore(new ItemStack(Material.CLOCK), "classItem")));
//        givePlayerCompass(player, configuration.getInt("custom_inventory_order." + playerName + ".compass"));
//        player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".iron_axe"), setUnbreakable(setAttributeAttackDamage(new ItemStack(Material.IRON_AXE), 4))); // 1+4 = 5 iron sword 6
//        ItemStack diamond_pickaxe = setUnbreakable(new ItemStack(Material.DIAMOND_PICKAXE));
//        diamond_pickaxe.addEnchantment(Enchantment.EFFICIENCY, 3);
//        addLore(diamond_pickaxe, "classItem");
//        player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".diamond_pickaxe"), diamond_pickaxe);
//        player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".ender_chest"), setClassItem(new ItemStack(Material.ENDER_CHEST, 1)));
//        player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".plank"), new ItemStack(Material.OAK_PLANKS, 64));
//        player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".cobblestone"), new ItemStack(Material.COBBLESTONE, 64));
//        player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".nether_star"), setClassItem(new ItemStack(Material.NETHER_STAR, 1)));
//        player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".iron_sword"), setUnbreakable(new ItemStack(Material.IRON_SWORD, 1)));
//        player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".cooked_porkchop"), new ItemStack(Material.COOKED_PORKCHOP, 64));
//        player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".bow"), setClassItem(setUnbreakable(new ItemStack(Material.BOW, 1))));
//        player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".arrow"), setUnbreakable(new ItemStack(Material.ARROW, 64)));
//        setItem(player, "hay_block", new ItemStack(Material.HAY_BLOCK, 16));
//        player.getInventory().setItem(35, new ItemStack(Material.HAY_BLOCK, 16));
//        player.getInventory().setItem(35, setUnbreakable(new ItemStack(Material.ARROW, 64)));
        //stop here
        Inventory classInventory = InventoryManager.loadClassReorderInventory(player, ClassU.getClass(player));
        if (loadAsTF) {
            classInventory = InventoryManager.loadClassReorderInventory(player, 29);
        }
        boolean isTF = ClassU.isTransformationMaster(player);
        for (int i = 0; i < 36; i++) {
            int slotToChange = i;
            ItemStack stack = classInventory.getItem(i);
            String itemType = ItemStackU.getStringPersistentData(stack, NameSpacedKeys.ITEM_TYPE);
            if (stack != null && !stack.getType().equals(Material.AIR)) {
//                if (!stack.getType().equals(Material.POTION)) {  // change display name to persistentData
//                    setDisplayName(stack, null);
//                }
                if(InventoryManager.whetherDontLoad(stack)) continue; //dont load
                if (isTF && !loadAsTF) {
                    boolean findSameItemType = false;
                    //squid_potion_for_everyone
                    if (ItemStackU.containsLore(stack, "speed_potion") || ItemStackU.containsLore(stack, "heal_potion") || ItemStackU.getDisplayName(stack).contains("squid_potion_everyone")) continue;  // || ItemStackU.containsLore(stack, "elaina_potion")  ItemStackU.containsLore(stack, "custom_potion")
                    for (int i2 = 0; i2 < playerInventory.getSize(); i2++) {
                        ItemStack playerItemStack = playerInventory.getItem(i2);
                        String playerItemType = ItemStackU.getStringPersistentData(playerItemStack, NameSpacedKeys.ITEM_TYPE);
                        if (playerItemType != null) {
                            if (playerItemType.equals(itemType)) {
                                slotToChange = i2;
                                findSameItemType = true;
                                break;
                            }
                        }
                    }
                    if (!findSameItemType) {
                        ItemStack movedItem = playerInventory.getItem(i);
                        if (movedItem != null) {
                            playerInventory.setItem(slotToChange, new ItemStack(Material.COMMAND_BLOCK));
                            playerInventory.addItem(movedItem);
                        }
                    }
                }
                setDisplayName(stack, getStringPersistentData(stack, NameSpacedKeys.DISPLAY_NAME));
//                if ()
                player.getInventory().setItem(slotToChange, stack);
            }
        }
//        Inventory classReorderInventory = InventoryManager.loadClassReorderInventory(player, ScoreboardsAndTeams.getScore(player, "class"), false);  //only changed to false so it can load normal items
//        for (int i = 0; i < 36; i++) {
//            ItemStack stack = classReorderInventory.getItem(i);
//            if (stack != null && !stack.getType().equals(Material.AIR)) {
//                setDisplayName(stack, null);
//                player.getInventory().setItem(i, stack);
//            }
//        }
    }

    public static void initializeAutoEnergyAccumulationFlexible(Player player) {
        if (GameManager.gameStage == 4 || true) {  //first deathmatch to prevent overridden
            InitializeClass.initializeDeathMatchAutoEnergyAccumulation(player);
        }
        if (GameManager.gameStage >= 0 && GameManager.gameStage <= 3) { // game stage >= 2 , 1 is preparation
            InitializeClass.initializeAutoEnergyAccumulation(player);
        }
    }

    //auto energy accumulate
    public static void initializeAutoEnergyAccumulation (Player player) {  //int amount int delay switch class set if not -1 accumulate same with attack energy accumulate
        switch (ScoreboardsAndTeams.getScore(player, "class")) {
            case 3:
                autoEnergyAccumulation(player, 1, 20);
                break;
            case 7:
                autoEnergyAccumulation(player, 1, 20);
                break;
            case 8:
                autoEnergyAccumulation(player, 7, 20);
                break;
            case 9:
                autoEnergyAccumulation(player, 1, 20);
                break;
            case 13:
                autoEnergyAccumulation(player, 1, 20);
                break;
            case 15:
                autoEnergyAccumulation(player, 17, 20);
                break;
            case 28:
                autoEnergyAccumulation(player, 1, 20);
                break;
        }
    }

    public static void initializeDeathMatchAutoEnergyAccumulation (Player player) {
        switch (ScoreboardsAndTeams.getScore(player, "class")) {
            case 10:
                autoEnergyAccumulation(player, 1, 20);
                break;
        }
    }

    static ItemStack getClassItem(Material material) {
        ItemStack classItem = new ItemStack(material);
//        setDisplayName(classHelmet, "iron_sword");
        setUnbreakable(classItem);
        setClassItem(classItem);
        classItem.addEnchantment(Enchantment.UNBREAKING, 3);
        return classItem;
    }


    //entity 303
    public static void entity_303_initialize_class(Player player) {
        initializeClassBase(player);
        ItemStack leggings = new ItemStack(Material.IRON_LEGGINGS);
        leggings.addEnchantment(Enchantment.PROTECTION, 2);
        leggings.addEnchantment(Enchantment.FIRE_PROTECTION, 4);
        leggings.addEnchantment(Enchantment.PROJECTILE_PROTECTION, 1);
        leggings = setUnbreakable(leggings);
        setClassItem(leggings);
//        ItemStack bow = new ItemStack(Material.BOW);
//        bow.addEnchantment(Enchantment.FLAME, 1);
//        bow.addEnchantment(Enchantment.POWER, 1);
//        bow = setUnbreakable(bow);
//        ItemStack speed_potion = new ItemStack(Material.POTION);
//        speed_potion = MegaWallsClasses.setEffect(speed_potion, PotionEffectType.SPEED, 300, 1);
//        speed_potion = setDisplayName(speed_potion, "15s II");
//        ItemStack heal_potion = new ItemStack(Material.POTION);
//        heal_potion = MegaWallsClasses.setEffect(heal_potion, PotionEffectType.INSTANT_HEALTH, 20, 0);
//        heal_potion = setDisplayName(heal_potion, "20 HP");
//        heal_potion = setLore(heal_potion, "heal_potion");
        player.getInventory().setLeggings(leggings);
//        Configuration configuration = plugin.getConfig();
//        String playerName = player.getName();
//        player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".bow"), bow);
//        player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".speed_potion_1"), speed_potion);
//        player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".speed_potion_2"), speed_potion);
//        player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".heal_potion_1"), heal_potion);
//        autoEnergyAccumulation(player, 1, 20);
    }

    //creeper
    public static void creeper_initialize_class(Player player) {
        initializeClassBase(player);
        ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
        setColor(helmet, Color.RED);
        helmet.addUnsafeEnchantment(Enchantment.BLAST_PROTECTION, 2);
        helmet.addUnsafeEnchantment(Enchantment.PROTECTION, 5);
        helmet = setUnbreakable(helmet);
        setClassItem(helmet);
        ItemStack chestplate = new ItemStack(Material.IRON_CHESTPLATE);
        chestplate.addUnsafeEnchantment(Enchantment.BLAST_PROTECTION, 1);
        chestplate.addUnsafeEnchantment(Enchantment.PROTECTION, 4);
        chestplate = setUnbreakable(chestplate);
        setClassItem(chestplate);
//        ItemStack sword = new ItemStack(Material.IRON_SWORD);
//        sword.addEnchantment(Enchantment.UNBREAKING, 3);
//        sword = setUnbreakable(sword);
//        setClassItem(sword);
//        ItemStack speed_potion = new ItemStack(Material.POTION);
//        speed_potion = MegaWallsClasses.setEffect(speed_potion, PotionEffectType.SPEED, 300, 1);
//        speed_potion = setDisplayName(speed_potion, "15s II");
//        setClassItem(speed_potion);
//        ItemStack heal_potion = new ItemStack(Material.POTION);
//        heal_potion = MegaWallsClasses.setEffect(heal_potion, PotionEffectType.INSTANT_HEALTH, 16, 0);
//        heal_potion = setDisplayName(heal_potion, "16 HP");
//        heal_potion = setLore(heal_potion, "heal_potion");
//        setClassItem(heal_potion);
        player.getInventory().setHelmet(helmet);
        player.getInventory().setChestplate(chestplate);
//        Configuration configuration = plugin.getConfig();
//        String playerName = player.getName();
//        player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".iron_sword"), sword);
//        player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".speed_potion_1"), speed_potion);
//        player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".speed_potion_2"), speed_potion);
//        player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".heal_potion_1"), heal_potion);
//        player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".heal_potion_2"), heal_potion);
//        autoEnergyAccumulation(player, 7, 20);
    }

    //undead_knight
    public static void undead_knight_initialize_class(Player player) {
        initializeClassBase(player);
        ItemStack helmet = new ItemStack(Material.IRON_HELMET);
        helmet.addEnchantment(Enchantment.PROTECTION, 3);
        helmet = setUnbreakable(helmet);
        setClassItem(helmet);
        ItemStack chestplate = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
        chestplate.addEnchantment(Enchantment.PROTECTION, 4);
        chestplate.addEnchantment(Enchantment.PROJECTILE_PROTECTION, 2);
        chestplate = setUnbreakable(chestplate);
        setClassItem(chestplate);
//        ItemStack sword = new ItemStack(Material.STONE_SWORD);
//        sword.addEnchantment(Enchantment.SHARPNESS, 2);
//        sword = setUnbreakable(sword);
//        ItemStack bow = new ItemStack(Material.BOW);
//        bow.addEnchantment(Enchantment.POWER, 2);
//        bow = setUnbreakable(bow);
//        ItemStack speed_potion = new ItemStack(Material.POTION);
//        speed_potion = MegaWallsClasses.setEffect(speed_potion, PotionEffectType.SPEED, 300, 1);
//        speed_potion = setDisplayName(speed_potion, "15s II");
//        ItemStack heal_potion = new ItemStack(Material.POTION);
//        heal_potion = MegaWallsClasses.setEffect(heal_potion, PotionEffectType.INSTANT_HEALTH, 14, 0);
//        heal_potion = setDisplayName(heal_potion, "14 HP");
//        heal_potion = setLore(heal_potion, "heal_potion");
        player.getInventory().setHelmet(helmet);
        player.getInventory().setChestplate(chestplate);
//        Configuration configuration = plugin.getConfig();
//        String playerName = player.getName();
//        player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".iron_sword"), sword);
//        player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".bow"), bow);
//        player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".speed_potion_1"), speed_potion);
//        player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".speed_potion_2"), speed_potion);
//        player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".heal_potion_1"), heal_potion);
//        player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".heal_potion_2"), heal_potion);
//        autoEnergyAccumulation(player, 1, 20);
    }

    //drownking
    public static void drownking_initialize_class(Player player) {
        initializeClassBase(player);
        ItemStack leggings = new ItemStack(Material.DIAMOND_LEGGINGS);
        leggings.addEnchantment(Enchantment.UNBREAKING, 3);
        leggings = setUnbreakable(leggings);
        setClassItem(leggings);
        ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
        setColor(boots, Color.BLUE);
        boots.addEnchantment(Enchantment.PROTECTION, 4);
        boots.addEnchantment(Enchantment.DEPTH_STRIDER, 3);
        boots = setUnbreakable(boots);
        setClassItem(boots);
//        ItemStack sword = new ItemStack(Material.TRIDENT);
//        sword.addEnchantment(Enchantment.LOYALTY, 3);
//        sword = setUnbreakable(sword);
//        setClassItem(sword);
//        ItemStack bow = new ItemStack(Material.BOW);
//        bow.addEnchantment(Enchantment.UNBREAKING, 3);
//        bow = setUnbreakable(bow);
//        setClassItem(bow);
//        ItemStack speed_potion = new ItemStack(Material.POTION);
//        speed_potion = MegaWallsClasses.setEffect(speed_potion, PotionEffectType.SPEED, 300, 1);
//        speed_potion = setDisplayName(speed_potion, "15s II");
//        setClassItem(speed_potion);
//        ItemStack heal_potion = new ItemStack(Material.POTION);
//        heal_potion = MegaWallsClasses.setEffect(heal_potion, PotionEffectType.INSTANT_HEALTH, 14, 0);
//        heal_potion = setDisplayName(heal_potion, "14 HP");
//        heal_potion = setLore(heal_potion, "heal_potion");
//        setClassItem(heal_potion);
        player.getInventory().setLeggings(leggings);
        player.getInventory().setBoots(boots);
//        Configuration configuration = plugin.getConfig();
//        String playerName = player.getName();
//        player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".iron_sword"), sword);
//        player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".bow"), bow);
//        player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".speed_potion_1"), speed_potion);
//        player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".speed_potion_2"), speed_potion);
//        player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".heal_potion_1"), heal_potion);
//        player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".heal_potion_2"), heal_potion);

    }

    //spider
    public static void spider_initialize_class(Player player) {
        initializeClassBase(player);
        ItemStack boots = new ItemStack(Material.DIAMOND_BOOTS);
        boots.addEnchantment(Enchantment.PROTECTION, 1);
//        boots.addEnchantment(Enchantment.FEATHER_FALLING, 4);
        boots = setUnbreakable(boots);
        setClassItem(boots);
//        ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
//        sword.addEnchantment(Enchantment.UNBREAKING, 3);
//        sword = setUnbreakable(sword);
//        setClassItem(sword);
//        ItemStack speed_potion = new ItemStack(Material.POTION);
//        speed_potion = MegaWallsClasses.setEffect(speed_potion, PotionEffectType.SPEED, 300, 1);
//        speed_potion = setDisplayName(speed_potion, "15s II");
//        setClassItem(speed_potion);
//        ItemStack heal_potion = new ItemStack(Material.POTION);
//        heal_potion = MegaWallsClasses.setEffect(heal_potion, PotionEffectType.INSTANT_HEALTH, 16, 0);
//        heal_potion = setDisplayName(heal_potion, "16 HP");
//        heal_potion = setLore(heal_potion, "heal_potion");
//        setClassItem(heal_potion);
        player.getInventory().setBoots(boots);
//        Configuration configuration = plugin.getConfig();
//        String playerName = player.getName();
//        player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".iron_sword"), sword);
//        player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".speed_potion_1"), speed_potion);
//        player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".speed_potion_2"), speed_potion);
//        player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".heal_potion_1"), heal_potion);
//        player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".heal_potion_2"), heal_potion);
    }

    //shaman
    public static void shaman_initialize_class(Player player) {
        initializeClassBase(player);
//        ItemStack helmet = new ItemStack(Material.CHAINMAIL_HELMET);
//        helmet.addEnchantment(Enchantment.PROTECTION, 2);
//        setUnbreakable(helmet);
//        setClassItem(helmet);
        ItemStack boots = new ItemStack(Material.DIAMOND_BOOTS);
        boots.addEnchantment(Enchantment.PROTECTION, 2);
        boots.addEnchantment(Enchantment.FEATHER_FALLING, 2);
        setUnbreakable(boots);
        setClassItem(boots);
//        ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
//        sword.addEnchantment(Enchantment.UNBREAKING, 3);
////        sword.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, 2);
////        ItemStackModify.setAttributeAttackDamage(sword, 5); //original 4
//        setUnbreakable(sword);
//        setClassItem(sword);
//        ItemStack bow = new ItemStack(Material.BOW);
//        bow.addEnchantment(Enchantment.UNBREAKING, 3);
////        bow.addEnchantment(Enchantment.INFINITY, 1);
//        setUnbreakable(bow);
//        setClassItem(bow);
//        ItemStack speed_potion = new ItemStack(Material.POTION, 2);
////        ItemStackModify.setBasePotionTye(speed_potion, PotionType.SWIFTNESS);
//        MegaWallsClasses.setEffect(speed_potion, PotionEffectType.SPEED, 300, 1);
//
////        ItemStackModify.setBasePotionTye(speed_potion, PotionType.SWIFTNESS);
//        setDisplayName(speed_potion, "15s II");
//        setLore(speed_potion, "speed_potion");
//        ItemStackModify.setMaxStackSize(speed_potion, 2);
//        setClassItem(speed_potion);
//        ItemStack heal_potion = new ItemStack(Material.POTION, 2);
//        ItemStackModify.setBasePotionTye(heal_potion, PotionType.HEALING);
//        MegaWallsClasses.setEffect(heal_potion, PotionEffectType.INSTANT_HEALTH, 16, 0);
//        setDisplayName(heal_potion, "16 HP");
//        setLore(heal_potion, "heal_potion");
//        ItemStackModify.setMaxStackSize(heal_potion, 2);
//        setClassItem(heal_potion);
//        player.getInventory().setHelmet(helmet);
        player.getInventory().setBoots(boots);
//        Configuration configuration = plugin.getConfig();
//        String playerName = player.getName();
//        player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".iron_sword"), sword);
//        player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".bow"), bow);
//        player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".speed_potion_1"), speed_potion);
////        player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".speed_potion_2"), speed_potion);
//        player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".heal_potion_1"), heal_potion);
////        player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".heal_potion_2"), heal_potion);

//        player.getInventory().addItem(new ItemStack(Material.GOLDEN_CARROT, 5));
//        player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, Integer.MAX_VALUE, 0, true, true));
//        player.getInventory().addItem(new ItemStack(Material.IRON_SWORD, 1));
//        player.getInventory().addItem(elaina_potion());
////        player.getInventory().addItem(elaina_potion());
////        player.getInventory().addItem(elaina_potion());
//        UUID playerUUID = player.getUniqueId();
//        PassiveSkills.elaina_mode.put(playerUUID, 0);
//        PassiveSkills.elainaShootTask.put(playerUUID, PassiveSkills.elaina_passive_skill_1_task(player));
//        PassiveSkills.elainaIcicleSpinTask.put(playerUUID, PassiveSkills.elaina_icicleSpinTask(player));
//        PassiveSkills.elainaIcicleAccumulateTask.put(playerUUID, PassiveSkills.elaina_accumulate_icicle_task(player));
    }

    //snowman
    public static void snowman_initialize_class(Player player) {
        initializeClassBase(player);
        ItemStack boots = new ItemStack(Material.CHAINMAIL_BOOTS);
        boots.addEnchantment(Enchantment.PROTECTION, 4);
        boots.addEnchantment(Enchantment.FEATHER_FALLING, 1);
        boots = setUnbreakable(boots);
        setClassItem(boots);
//        ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
//        sword.addEnchantment(Enchantment.UNBREAKING, 3);
//        sword = setUnbreakable(sword);
//        setClassItem(sword);
//        ItemStack speed_potion = new ItemStack(Material.POTION);
////        ItemStackModify.setBasePotionTye(speed_potion, PotionType.SWIFTNESS);
//        speed_potion = MegaWallsClasses.setEffect(speed_potion, PotionEffectType.SPEED, 300, 1);
////        ItemStackModify.setBasePotionTye(speed_potion, PotionType.SWIFTNESS);
//        speed_potion = setDisplayName(speed_potion, "15s II");
//        setClassItem(speed_potion);
//        ItemStack heal_potion = new ItemStack(Material.POTION, 2);
//        ItemStackModify.setBasePotionTye(heal_potion, PotionType.HEALING);
//        heal_potion = MegaWallsClasses.setEffect(heal_potion, PotionEffectType.INSTANT_HEALTH, 16, 0);
//        heal_potion = setDisplayName(heal_potion, "16 HP");
//        heal_potion = setLore(heal_potion, "heal_potion");
//        ItemStackModify.setMaxStackSize(heal_potion, 2);
//        setClassItem(heal_potion);
        player.getInventory().setBoots(boots);
//        Configuration configuration = plugin.getConfig();
//        String playerName = player.getName();
//        player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".iron_sword"), sword);
//        player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".speed_potion_1"), speed_potion);
//        player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".speed_potion_2"), speed_potion);
//        player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".heal_potion_1"), heal_potion);
//        player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".heal_potion_2"), heal_potion);
//        Order.setPlayerItem(player, "diamond_shovel", new ItemStack(Material.DIAMOND_SHOVEL));
//        Order.setPlayerItem(player, "snow_block_1", new ItemStack(Material.DIAMOND_SHOVEL));
//        Order.setPlayerItem(player, "diamond_shovel", new ItemStack(Material.DIAMOND_SHOVEL));
//        Order.setPlayerItem(player, "diamond_shovel", new ItemStack(Material.DIAMOND_SHOVEL));
//        player.getInventory().addItem(new ItemStack(Material.DIAMOND_SHOVEL));
//        player.getInventory().addItem(new ItemStack(Material.SNOW_BLOCK, 64));
//        player.getInventory().addItem(new ItemStack(Material.SNOW_BLOCK, 64));
//        player.getInventory().addItem(new ItemStack(Material.PUMPKIN, 64));
    }

    //elaina
    public static void elaina_initialize_class(Player player) {
        initializeClassBase(player);
        ItemStack helmet = new ItemStack(Material.CHAINMAIL_HELMET);
        helmet.addEnchantment(Enchantment.PROTECTION, 2);
        setUnbreakable(helmet);
        setClassItem(helmet);
        ItemStack chestplate = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
        chestplate.addEnchantment(Enchantment.PROTECTION, 2);
//        chestplate.addEnchantment(Enchantment.PROJECTILE_PROTECTION, 2);
        setUnbreakable(chestplate);
        setClassItem(chestplate);
//        ItemStack sword = new ItemStack(Material.STICK);
//        sword.addUnsafeEnchantment(Enchantment.KNOCKBACK, 2);
//        sword.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, 2);
//        ItemStackModify.setAttributeAttackDamage(sword, 5); //original 4
//        setUnbreakable(sword);
//        setClassItem(sword);
//        ItemStack bow = new ItemStack(Material.BOW);
//        bow.addEnchantment(Enchantment.POWER, 2);
//        bow.addEnchantment(Enchantment.INFINITY, 1);
//        setUnbreakable(bow);
//        setClassItem(bow);
//        ItemStack speed_potion = new ItemStack(Material.POTION, 2);
//        ItemStackModify.setBasePotionTye(speed_potion, PotionType.SWIFTNESS);
//        MegaWallsClasses.setEffect(speed_potion, PotionEffectType.SPEED, 240, 2);
//
////        ItemStackModify.setBasePotionTye(speed_potion, PotionType.SWIFTNESS);
//        setDisplayName(speed_potion, "12s III");
//        setLore(speed_potion, "speed_potion");
//        ItemStackModify.setMaxStackSize(speed_potion, 2);
//        setClassItem(speed_potion);
//        ItemStack heal_potion = new ItemStack(Material.POTION, 2);
//        ItemStackModify.setBasePotionTye(heal_potion, PotionType.HEALING);
//        MegaWallsClasses.setEffect(heal_potion, PotionEffectType.INSTANT_HEALTH, 16, 0);
//        setDisplayName(heal_potion, "16 HP");
//        setLore(heal_potion, "heal_potion");
//        ItemStackModify.setMaxStackSize(heal_potion, 2);
//        setClassItem(heal_potion);
        player.getInventory().setHelmet(helmet);
        player.getInventory().setChestplate(chestplate);
//        Configuration configuration = plugin.getConfig();
//        String playerName = player.getName();
//        player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".iron_sword"), sword);
//        player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".bow"), bow);
//        player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".speed_potion_1"), speed_potion);
////        player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".speed_potion_2"), speed_potion);
//        player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".heal_potion_1"), heal_potion);
////        player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".heal_potion_2"), heal_potion);
//        player.getInventory().addItem(new ItemStack(Material.GOLDEN_CARROT, 5));
        player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, Integer.MAX_VALUE, 0, true, true));
//        player.getInventory().addItem(new ItemStack(Material.IRON_SWORD, 1));
//        player.getInventory().addItem(elaina_potion());
////        player.getInventory().addItem(elaina_potion());
////        player.getInventory().addItem(elaina_potion());
        UUID playerUUID = player.getUniqueId();
        PassiveSkills.elaina_mode.put(playerUUID, 0);
        PassiveSkills.elainaShootTask.put(playerUUID, PassiveSkills.elaina_passive_skill_1_task(player));
        PassiveSkills.elainaIcicleSpinTask.put(playerUUID, PassiveSkills.elaina_icicleSpinTask(player));
        PassiveSkills.elainaIcicleAccumulateTask.put(playerUUID, PassiveSkills.elaina_accumulate_icicle_task(player));
    }

    static ItemStack elaina_potion() {
        ItemStack elaina_potion = new ItemStack(Material.SPLASH_POTION, 3);
        ItemStackModify.setEffect(elaina_potion, PotionEffectType.REGENERATION, 80, 2);
        setDisplayName(elaina_potion, ChatColor.LIGHT_PURPLE + "4s 6HP(III)");  //"4s III"
//        ChatColor.valueOf()
//        setDisplayName(elaina_potion, "elaina_potion_1");
//        InventoryManager.setLoadDisplayName(elaina_potion, "4s III");
        setLore(elaina_potion, "elaina_potion");
        ItemStackModify.setMaxStackSize(elaina_potion, 3);
        return elaina_potion;
    }

    public static void elaina_disable(Player player) {
        UUID playerUUID = player.getUniqueId();
        PassiveSkills.elaina_mode.remove(playerUUID);
        PassiveSkills.elainaShootTask.remove(playerUUID);
        PassiveSkills.elainaIcicleSpinTask.remove(playerUUID);
        if (PassiveSkills.elainaIcicleAccumulateTask.remove(playerUUID) != null) {  //unstable , and weakness should check level = 1
            player.removePotionEffect(PotionEffectType.WEAKNESS);
        };
        PassiveSkills.elaina_icicle.remove(playerUUID);
        PassiveSkills.elainaIcicle_elaina.remove(playerUUID);
        HashSet<UUID> icicles = HashMapUtils.getKeys(playerUUID, PassiveSkills.elainaIcicle_elaina);
        if (icicles != null) {
            for (UUID icicleUUID: icicles) {
                // added null check
//                Bukkit.getEntity(icicleUUID).remove();
                Entity entity = Bukkit.getEntity(icicleUUID);
                if (entity != null) {
                    entity.remove();
                }
                PassiveSkills.elainaIcicle_degree.remove(icicleUUID);
            }
        }
        PassiveSkills.elaina_enemy.remove(playerUUID);
//        BukkitTaskUtils.cancelTask(playerUUID, PassiveSkills.elainaShootTask.tasks);
    }

    public static void transformation_master_initialize_class(Player player) {
        InitializeClass.initializeClassSpecific(player, true);
        ActiveSkills.transformation_master_active_skill(player);
    }

    public static BukkitTaskMap<UUID> warden_darkness = new BukkitTaskMap<>();
    public static HaxhMap<UUID, WardenDarknessTargetTimestamp> warden_darknessTargetTimestamp = new org.ajls.lib.advanced.HaxhMap<>();
    public static void warden_initialize_class(Player player) {
        UUID playerUUID = player.getUniqueId();
        BukkitScheduler scheduler = Bukkit.getScheduler();
        BukkitTask task = scheduler.runTaskTimer(MegaWallsClasses.getPlugin(), () -> {
//            if (player.isOnline()) {
//                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20, 1, true, true));
//                player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 20, 1, true, true));
//                player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20, 0, true, true));
//            } else {
//                warden_darkness.remove(playerUUID);
//            }
            Player playerFromUUID = Bukkit.getPlayer(playerUUID);
            int time = Time.getTime();
            if (playerFromUUID != null) {
                ArrayList<Player> players = MyListener.getNearbyPlayers(player,  10, 114514);
                HashSet<WardenDarknessTargetTimestamp> targetTimestamps = warden_darknessTargetTimestamp.getValues(playerUUID);
                for (Player nearbyPlayer: players) {
                    if (nearbyPlayer != null && nearbyPlayer.isOnline()) {
                        UUID nearbyPlayerUUID = nearbyPlayer.getUniqueId();
                        boolean containsPlayer = false;
//                        targetTimestamps.forEach((targetUUID, timestamp) -> {
//                            if (timestamp. != null) {
//                                if (timestamp.() > System.currentTimeMillis()) {
//                                    nearbyPlayer.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20, 1, true, true));
//                                    nearbyPlayer.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 20, 1, true, true));
//                                    nearbyPlayer.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20, 0, true, true));
//                                } else {
//                                    warden_darknessTargetTimestamp.remove(nearbyPlayerUUID);
//                                }
//                            } else {
//                                warden_darknessTargetTimestamp.remove(nearbyPlayerUUID);
//                            }
//                        });
                        if (targetTimestamps != null) {
                            for (WardenDarknessTargetTimestamp targetTimestamp: targetTimestamps) {
                                if (targetTimestamp != null) {
                                    if (targetTimestamp.getTargetUUID().equals(nearbyPlayerUUID)) {
                                        containsPlayer = true;
                                        if (targetTimestamp.getTimestamp() <= time) {
                                            addDarkness(nearbyPlayer);

                                            targetTimestamp.setTimestamp(time + 200);  //original 600
                                            warden_darknessTargetTimestamp.put(playerUUID, targetTimestamp);//TODO: is hashmap is get by reference then it don't need to be push back. Test it
                                        }
                                        break;
//                                    else {
//                                        warden_darknessTargetTimestamp.remove(nearbyPlayerUUID);
//                                    }
                                    }
                                }
//                            else {
//                                warden_darknessTargetTimestamp.remove(nearbyPlayerUUID);
//                            }
                            }
                        }
                        if (!containsPlayer) {
                            addDarkness(nearbyPlayer);
                            WardenDarknessTargetTimestamp wardenDarknessTargetTimestamp = new WardenDarknessTargetTimestamp(nearbyPlayerUUID, time + 200);  //original 600
                            nearbyPlayer.removePotionEffect(PotionEffectType.NIGHT_VISION);
                            warden_darknessTargetTimestamp.put(playerUUID, wardenDarknessTargetTimestamp);
                        }

                    }
                }
            }
        }, 0L, 20L);
        warden_darkness.put(playerUUID, task);
    }

    public static void addDarkness(Player nearbyPlayer) {
        int darknessTime = 50;
        nearbyPlayer.addPotionEffect(new PotionEffect(PotionEffectType.DARKNESS, darknessTime, 0, false, true));
        nearbyPlayer.removePotionEffect(PotionEffectType.NIGHT_VISION);
        BukkitScheduler scheduler = Bukkit.getScheduler();
        scheduler.runTaskLater(MegaWallsClasses.getPlugin(), () -> {
            nearbyPlayer.removePotionEffect(PotionEffectType.DARKNESS);
            nearbyPlayer.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, -1, 0, false, false));
        }, darknessTime);
    }

    public static void warden_disable(Player player) {

        UUID playerUUID = player.getUniqueId();
        warden_darkness.remove(playerUUID);
//        BukkitTask task = warden_darkness.remove(playerUUID);
//        if (task != null) {
//            task.cancel();
//        }
//        warden_darknessTargetTimestamp.remove(playerUUID);
    }
}
