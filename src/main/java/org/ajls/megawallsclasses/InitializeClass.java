package org.ajls.megawallsclasses;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import java.util.UUID;

import static org.ajls.megawallsclasses.EnergyAccumulate.autoEnergyAccumulation;
//import static org.ajls.megawallsclasses.MegaWallsClasses.tractorCompass;
import static org.ajls.megawallsclasses.ItemStackModify.*;
import static org.ajls.megawallsclasses.MegaWallsClasses.plugin;
import static org.ajls.megawallsclasses.MegaWallsClasses.setColor;
import static org.ajls.megawallsclasses.MyListener.levelEqualsEnergy;
import static org.ajls.tractorcompass.CompassItemStack.givePlayerCompass;

public class InitializeClass {
//    public static TractorCompass tractorCompass = new TractorCompass();
    //base
    public static void initializeClassBase(Player player) {
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
        for (PotionEffect effect : player.getActivePotionEffects())
            player.removePotionEffect(effect.getType());
        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0, false, false));
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
        player.getInventory().setHelmet(setUnbreakable(new ItemStack(Material.IRON_HELMET)));
        player.getInventory().setChestplate(setUnbreakable(new ItemStack(Material.IRON_CHESTPLATE)));
        player.getInventory().setLeggings(setUnbreakable(new ItemStack(Material.IRON_LEGGINGS)));
        player.getInventory().setBoots(setUnbreakable(new ItemStack(Material.IRON_BOOTS)));
        Configuration configuration = plugin.getConfig();
        String playerName = player.getName();
        player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".超级战墙工具箱"), setUnbreakable(addLore(new ItemStack(Material.CLOCK), "classItem")));
        givePlayerCompass(player, configuration.getInt("custom_inventory_order." + playerName + ".compass"));
        player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".iron_axe"), setUnbreakable(setAttributeAttackDamage(new ItemStack(Material.IRON_AXE), 4))); // 1+4 = 5 iron sword 6
        ItemStack diamond_pickaxe = setUnbreakable(new ItemStack(Material.DIAMOND_PICKAXE));
        diamond_pickaxe.addEnchantment(Enchantment.EFFICIENCY, 3);
        addLore(diamond_pickaxe, "classItem");
        player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".diamond_pickaxe"), diamond_pickaxe);
        player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".ender_chest"), setClassItem(new ItemStack(Material.ENDER_CHEST, 1)));
        player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".plank"), new ItemStack(Material.OAK_PLANKS, 64));
        player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".cobblestone"), new ItemStack(Material.COBBLESTONE, 64));
        player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".nether_star"), setClassItem(new ItemStack(Material.NETHER_STAR, 1)));
        player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".iron_sword"), setUnbreakable(new ItemStack(Material.IRON_SWORD, 1)));
        player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".cooked_porkchop"), new ItemStack(Material.COOKED_PORKCHOP, 64));
        player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".bow"), setClassItem(setUnbreakable(new ItemStack(Material.BOW, 1))));
        player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".arrow"), setUnbreakable(new ItemStack(Material.ARROW, 64)));
        player.getInventory().setItem(35, new ItemStack(Material.HAY_BLOCK, 16));
//        player.getInventory().setItem(35, setUnbreakable(new ItemStack(Material.ARROW, 64)));
    }

    //auto energy accumulate
    public static void initializeAutoEnergyAccumulation (Player player) {
        switch (MegaWallsClasses.getScore(player, "class")) {
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
            case 28:
                autoEnergyAccumulation(player, 1, 20);
                break;
        }
    }

    public static void initializeDeathMatchAutoEnergyAccumulation (Player player) {
        switch (MegaWallsClasses.getScore(player, "class")) {
            case 10:
                autoEnergyAccumulation(player, 1, 1);
                break;
        }
    }


    //entity 303
    public static void entity_303_initialize_class(Player player) {
        initializeClassBase(player);
        ItemStack leggings = new ItemStack(Material.IRON_LEGGINGS);
        leggings.addEnchantment(Enchantment.PROTECTION, 2);
        leggings.addEnchantment(Enchantment.FIRE_PROTECTION, 4);
        leggings.addEnchantment(Enchantment.PROJECTILE_PROTECTION, 1);
        leggings = setUnbreakable(leggings);
        ItemStack bow = new ItemStack(Material.BOW);
        bow.addEnchantment(Enchantment.FLAME, 1);
        bow.addEnchantment(Enchantment.POWER, 1);
        bow = setUnbreakable(bow);
        ItemStack speed_potion = new ItemStack(Material.POTION);
        speed_potion = MegaWallsClasses.setEffect(speed_potion, PotionEffectType.SPEED, 300, 1);
        speed_potion = setDisplayName(speed_potion, "15s II");
        ItemStack heal_potion = new ItemStack(Material.POTION);
        heal_potion = MegaWallsClasses.setEffect(heal_potion, PotionEffectType.INSTANT_HEALTH, 20, 0);
        heal_potion = setDisplayName(heal_potion, "20 HP");
        heal_potion = setLore(heal_potion, "heal_potion");
        player.getInventory().setLeggings(leggings);
        Configuration configuration = plugin.getConfig();
        String playerName = player.getName();
        player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".bow"), bow);
        player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".speed_potion_1"), speed_potion);
        player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".speed_potion_2"), speed_potion);
        player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".heal_potion_1"), heal_potion);
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
        ItemStack sword = new ItemStack(Material.IRON_SWORD);
        sword.addEnchantment(Enchantment.UNBREAKING, 3);
        sword = setUnbreakable(sword);
        setClassItem(sword);
        ItemStack speed_potion = new ItemStack(Material.POTION);
        speed_potion = MegaWallsClasses.setEffect(speed_potion, PotionEffectType.SPEED, 300, 1);
        speed_potion = setDisplayName(speed_potion, "15s II");
        setClassItem(speed_potion);
        ItemStack heal_potion = new ItemStack(Material.POTION);
        heal_potion = MegaWallsClasses.setEffect(heal_potion, PotionEffectType.INSTANT_HEALTH, 16, 0);
        heal_potion = setDisplayName(heal_potion, "16 HP");
        heal_potion = setLore(heal_potion, "heal_potion");
        setClassItem(heal_potion);
        player.getInventory().setHelmet(helmet);
        player.getInventory().setChestplate(chestplate);
        Configuration configuration = plugin.getConfig();
        String playerName = player.getName();
        player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".iron_sword"), sword);
        player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".speed_potion_1"), speed_potion);
        player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".speed_potion_2"), speed_potion);
        player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".heal_potion_1"), heal_potion);
        player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".heal_potion_2"), heal_potion);
//        autoEnergyAccumulation(player, 7, 20);
    }

    //undead_knight
    public static void undead_knight_initialize_class(Player player) {
        initializeClassBase(player);
        ItemStack helmet = new ItemStack(Material.IRON_HELMET);
        helmet.addEnchantment(Enchantment.PROTECTION, 3);
        helmet = setUnbreakable(helmet);
        ItemStack chestplate = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
        chestplate.addEnchantment(Enchantment.PROTECTION, 4);
        chestplate.addEnchantment(Enchantment.PROJECTILE_PROTECTION, 2);
        chestplate = setUnbreakable(chestplate);
        ItemStack sword = new ItemStack(Material.STONE_SWORD);
        sword.addEnchantment(Enchantment.SHARPNESS, 2);
        sword = setUnbreakable(sword);
        ItemStack bow = new ItemStack(Material.BOW);
        bow.addEnchantment(Enchantment.POWER, 2);
        bow = setUnbreakable(bow);
        ItemStack speed_potion = new ItemStack(Material.POTION);
        speed_potion = MegaWallsClasses.setEffect(speed_potion, PotionEffectType.SPEED, 300, 1);
        speed_potion = setDisplayName(speed_potion, "15s II");
        ItemStack heal_potion = new ItemStack(Material.POTION);
        heal_potion = MegaWallsClasses.setEffect(heal_potion, PotionEffectType.INSTANT_HEALTH, 14, 0);
        heal_potion = setDisplayName(heal_potion, "14 HP");
        heal_potion = setLore(heal_potion, "heal_potion");
        player.getInventory().setHelmet(helmet);
        player.getInventory().setChestplate(chestplate);
        Configuration configuration = plugin.getConfig();
        String playerName = player.getName();
        player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".iron_sword"), sword);
        player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".bow"), bow);
        player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".speed_potion_1"), speed_potion);
        player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".speed_potion_2"), speed_potion);
        player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".heal_potion_1"), heal_potion);
        player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".heal_potion_2"), heal_potion);
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
        ItemStack sword = new ItemStack(Material.TRIDENT);
        sword.addEnchantment(Enchantment.LOYALTY, 3);
        sword = setUnbreakable(sword);
        setClassItem(sword);
        ItemStack bow = new ItemStack(Material.BOW);
        bow.addEnchantment(Enchantment.UNBREAKING, 3);
        bow = setUnbreakable(bow);
        setClassItem(bow);
        ItemStack speed_potion = new ItemStack(Material.POTION);
        speed_potion = MegaWallsClasses.setEffect(speed_potion, PotionEffectType.SPEED, 300, 1);
        speed_potion = setDisplayName(speed_potion, "15s II");
        setClassItem(speed_potion);
        ItemStack heal_potion = new ItemStack(Material.POTION);
        heal_potion = MegaWallsClasses.setEffect(heal_potion, PotionEffectType.INSTANT_HEALTH, 14, 0);
        heal_potion = setDisplayName(heal_potion, "14 HP");
        heal_potion = setLore(heal_potion, "heal_potion");
        setClassItem(heal_potion);
        player.getInventory().setLeggings(leggings);
        player.getInventory().setBoots(boots);
        Configuration configuration = plugin.getConfig();
        String playerName = player.getName();
        player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".iron_sword"), sword);
        player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".bow"), bow);
        player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".speed_potion_1"), speed_potion);
        player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".speed_potion_2"), speed_potion);
        player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".heal_potion_1"), heal_potion);
        player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".heal_potion_2"), heal_potion);

    }

    //spider
    public static void spider_initialize_class(Player player) {
        initializeClassBase(player);
        ItemStack boots = new ItemStack(Material.DIAMOND_BOOTS);
        boots.addEnchantment(Enchantment.PROTECTION, 1);
//        boots.addEnchantment(Enchantment.FEATHER_FALLING, 4);
        boots = setUnbreakable(boots);
        setClassItem(boots);
        ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
        sword.addEnchantment(Enchantment.UNBREAKING, 3);
        sword = setUnbreakable(sword);
        setClassItem(sword);
        ItemStack speed_potion = new ItemStack(Material.POTION);
        speed_potion = MegaWallsClasses.setEffect(speed_potion, PotionEffectType.SPEED, 300, 1);
        speed_potion = setDisplayName(speed_potion, "15s II");
        setClassItem(speed_potion);
        ItemStack heal_potion = new ItemStack(Material.POTION);
        heal_potion = MegaWallsClasses.setEffect(heal_potion, PotionEffectType.INSTANT_HEALTH, 16, 0);
        heal_potion = setDisplayName(heal_potion, "16 HP");
        heal_potion = setLore(heal_potion, "heal_potion");
        setClassItem(heal_potion);
        player.getInventory().setBoots(boots);
        Configuration configuration = plugin.getConfig();
        String playerName = player.getName();
        player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".iron_sword"), sword);
        player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".speed_potion_1"), speed_potion);
        player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".speed_potion_2"), speed_potion);
        player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".heal_potion_1"), heal_potion);
        player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".heal_potion_2"), heal_potion);
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
        ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
        sword.addEnchantment(Enchantment.UNBREAKING, 3);
//        sword.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, 2);
//        ItemStackModify.setAttributeAttackDamage(sword, 5); //original 4
        setUnbreakable(sword);
        setClassItem(sword);
        ItemStack bow = new ItemStack(Material.BOW);
        bow.addEnchantment(Enchantment.UNBREAKING, 3);
//        bow.addEnchantment(Enchantment.INFINITY, 1);
        setUnbreakable(bow);
        setClassItem(bow);
        ItemStack speed_potion = new ItemStack(Material.POTION, 2);
//        ItemStackModify.setBasePotionTye(speed_potion, PotionType.SWIFTNESS);
        MegaWallsClasses.setEffect(speed_potion, PotionEffectType.SPEED, 300, 1);

//        ItemStackModify.setBasePotionTye(speed_potion, PotionType.SWIFTNESS);
        setDisplayName(speed_potion, "15s II");
        setLore(speed_potion, "speed_potion");
        ItemStackModify.setMaxStackSize(speed_potion, 2);
        setClassItem(speed_potion);
        ItemStack heal_potion = new ItemStack(Material.POTION, 2);
        ItemStackModify.setBasePotionTye(heal_potion, PotionType.HEALING);
        MegaWallsClasses.setEffect(heal_potion, PotionEffectType.INSTANT_HEALTH, 16, 0);
        setDisplayName(heal_potion, "16 HP");
        setLore(heal_potion, "heal_potion");
        ItemStackModify.setMaxStackSize(heal_potion, 2);
        setClassItem(heal_potion);
//        player.getInventory().setHelmet(helmet);
        player.getInventory().setBoots(boots);
        Configuration configuration = plugin.getConfig();
        String playerName = player.getName();
        player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".iron_sword"), sword);
        player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".bow"), bow);
        player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".speed_potion_1"), speed_potion);
//        player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".speed_potion_2"), speed_potion);
        player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".heal_potion_1"), heal_potion);
//        player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".heal_potion_2"), heal_potion);

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
        ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
        sword.addEnchantment(Enchantment.UNBREAKING, 3);
        sword = setUnbreakable(sword);
        setClassItem(sword);
        ItemStack speed_potion = new ItemStack(Material.POTION);
//        ItemStackModify.setBasePotionTye(speed_potion, PotionType.SWIFTNESS);
        speed_potion = MegaWallsClasses.setEffect(speed_potion, PotionEffectType.SPEED, 300, 1);
//        ItemStackModify.setBasePotionTye(speed_potion, PotionType.SWIFTNESS);
        speed_potion = setDisplayName(speed_potion, "15s II");
        setClassItem(speed_potion);
        ItemStack heal_potion = new ItemStack(Material.POTION, 2);
        ItemStackModify.setBasePotionTye(heal_potion, PotionType.HEALING);
        heal_potion = MegaWallsClasses.setEffect(heal_potion, PotionEffectType.INSTANT_HEALTH, 16, 0);
        heal_potion = setDisplayName(heal_potion, "16 HP");
        heal_potion = setLore(heal_potion, "heal_potion");
        ItemStackModify.setMaxStackSize(heal_potion, 2);
        setClassItem(heal_potion);
        player.getInventory().setBoots(boots);
        Configuration configuration = plugin.getConfig();
        String playerName = player.getName();
        player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".iron_sword"), sword);
        player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".speed_potion_1"), speed_potion);
        player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".speed_potion_2"), speed_potion);
        player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".heal_potion_1"), heal_potion);
//        player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".heal_potion_2"), heal_potion);
        player.getInventory().addItem(new ItemStack(Material.DIAMOND_SHOVEL));
        player.getInventory().addItem(new ItemStack(Material.SNOW_BLOCK, 64));
        player.getInventory().addItem(new ItemStack(Material.SNOW_BLOCK, 64));
        player.getInventory().addItem(new ItemStack(Material.PUMPKIN, 64));
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
        ItemStack sword = new ItemStack(Material.STICK);
        sword.addUnsafeEnchantment(Enchantment.KNOCKBACK, 2);
        sword.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, 2);
        ItemStackModify.setAttributeAttackDamage(sword, 5); //original 4
        setUnbreakable(sword);
        setClassItem(sword);
        ItemStack bow = new ItemStack(Material.BOW);
        bow.addEnchantment(Enchantment.POWER, 2);
        bow.addEnchantment(Enchantment.INFINITY, 1);
        setUnbreakable(bow);
        setClassItem(bow);
        ItemStack speed_potion = new ItemStack(Material.POTION, 2);
//        ItemStackModify.setBasePotionTye(speed_potion, PotionType.SWIFTNESS);
        MegaWallsClasses.setEffect(speed_potion, PotionEffectType.SPEED, 240, 2);

//        ItemStackModify.setBasePotionTye(speed_potion, PotionType.SWIFTNESS);
        setDisplayName(speed_potion, "12s III");
        setLore(speed_potion, "speed_potion");
        ItemStackModify.setMaxStackSize(speed_potion, 2);
        setClassItem(speed_potion);
        ItemStack heal_potion = new ItemStack(Material.POTION, 2);
        ItemStackModify.setBasePotionTye(heal_potion, PotionType.HEALING);
        MegaWallsClasses.setEffect(heal_potion, PotionEffectType.INSTANT_HEALTH, 16, 0);
        setDisplayName(heal_potion, "16 HP");
        setLore(heal_potion, "heal_potion");
        ItemStackModify.setMaxStackSize(heal_potion, 2);
        setClassItem(heal_potion);
        player.getInventory().setHelmet(helmet);
        player.getInventory().setChestplate(chestplate);
        Configuration configuration = plugin.getConfig();
        String playerName = player.getName();
        player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".iron_sword"), sword);
        player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".bow"), bow);
        player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".speed_potion_1"), speed_potion);
//        player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".speed_potion_2"), speed_potion);
        player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".heal_potion_1"), heal_potion);
//        player.getInventory().setItem(configuration.getInt("custom_inventory_order." + playerName + ".heal_potion_2"), heal_potion);
        player.getInventory().addItem(new ItemStack(Material.GOLDEN_CARROT, 5));
        player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, Integer.MAX_VALUE, 0, true, true));
        player.getInventory().addItem(new ItemStack(Material.IRON_SWORD, 1));
        player.getInventory().addItem(elaina_potion());
//        player.getInventory().addItem(elaina_potion());
//        player.getInventory().addItem(elaina_potion());
        UUID playerUUID = player.getUniqueId();
        PassiveSkills.elaina_mode.put(playerUUID, 0);
        PassiveSkills.elainaShootTask.put(playerUUID, PassiveSkills.elaina_passive_skill_1_task(player));
        PassiveSkills.elainaIcicleSpinTask.put(playerUUID, PassiveSkills.elaina_icicleSpinTask(player));
        PassiveSkills.elainaIcicleAccumulateTask.put(playerUUID, PassiveSkills.elaina_accumulate_icicle_task(player));
    }

    static ItemStack elaina_potion() {
        ItemStack elaina_potion = new ItemStack(Material.SPLASH_POTION, 3);
        ItemStackModify.setEffect(elaina_potion, PotionEffectType.REGENERATION, 80, 2);
        setDisplayName(elaina_potion, "4s III");
        setLore(elaina_potion, "elaina_potion");
        ItemStackModify.setMaxStackSize(elaina_potion, 3);
        return elaina_potion;
    }
}
