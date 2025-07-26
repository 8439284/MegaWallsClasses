package org.ajls.megawallsclasses;

import org.ajls.lib.utils.ItemStackU;
import org.ajls.megawallsclasses.commands.Order;
import org.ajls.megawallsclasses.utils.InventoryU;
import org.ajls.megawallsclasses.utils.PotionU;
import org.bukkit.*;
import org.bukkit.configuration.Configuration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import static org.ajls.lib.utils.BookU.setPage;
import static org.ajls.megawallsclasses.ItemStackModify.*;
import static org.ajls.megawallsclasses.MegaWallsClasses.getPlugin;
import static org.ajls.megawallsclasses.MegaWallsClasses.plugin;

public class InventoryManager {
//    static ArrayList<Integer> unoccupied_slots;
    static Inventory classReorderInventory;
    static ArrayList<Integer> speed;
    static ArrayList<Integer> health;

    public static Inventory createLobbyMenuInventory(Player player) {
        Inventory inventory = Bukkit.createInventory(player, 9, "MW Menu");  //MW Tool Kit"
        ItemStack class_selection = new ItemStack(Material.IRON_SWORD, 1);
        setDisplayName(class_selection,"选择你的职业");
        ItemStack reorder_inventory = new ItemStack(Material.CHEST, 1);
        setDisplayName(reorder_inventory, "自定义物品栏顺序");
        ItemStack class_reorder_inventory = new ItemStack(Material.TRAPPED_CHEST, 1);
        setDisplayName(class_reorder_inventory, "自定义职业物品栏顺序");
        ItemStack team_selection = new ItemStack(Material.WHITE_WOOL, 1);
        setDisplayName(team_selection,"选择你的队伍");
        ItemStack documents = new ItemStack(Material.WRITABLE_BOOK, 1);
        setDisplayName(documents, "文档介绍");
        inventory.setItem(0,class_selection);
        inventory.setItem(1,reorder_inventory);
        inventory.setItem(2,class_reorder_inventory);
        inventory.setItem(3,team_selection);
        inventory.setItem(4,documents);
        return inventory;
    }

    public static Inventory createClassSelectionInventory(Player player) {
        Inventory inventory = Bukkit.createInventory(player, 36, "Select Class");
        ItemStack zombie = new ItemStack(Material.ROTTEN_FLESH, 1);
        setDisplayName(zombie,"免费送的满级");
        ItemStack herobrine = new ItemStack(Material.DIAMOND_SWORD, 1);
        setDisplayName(herobrine, "雷神");
        ItemStack skeleton = new ItemStack(Material.BONE, 1);
        setDisplayName(skeleton, "抢钻工具人");
        ItemStack enderman = new ItemStack(Material.ENDER_PEARL, 1);
        setDisplayName(enderman, "小心你背后快");
        ItemStack skeleton_lord = new ItemStack(Material.SKELETON_SKULL, 1);
        setDisplayName(skeleton_lord, "骷髅王");
        ItemStack n5ll = new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
        setDisplayName(n5ll, "跑男");
        ItemStack dreadlord = new ItemStack(Material.WITHER_SKELETON_SKULL, 1);
        setDisplayName(dreadlord, "死亡加载");
        ItemStack entity_303 = new ItemStack(Material.TOTEM_OF_UNDYING, 1);
        setDisplayName(entity_303, "15HP爆炸伤害你拿什么扛");
        ItemStack creeper = new ItemStack(Material.GUNPOWDER, 1);
        setDisplayName(creeper, "夜yeye也");
        ItemStack undead_knight = new ItemStack(Material.STONE_SWORD, 1);
        setDisplayName(undead_knight, "凋零弓");
        ItemStack drown_king = new ItemStack(Material.TRIDENT, 1);
        setDisplayName(drown_king, "14HP是我的斩杀线");
        ItemStack spider = new ItemStack(Material.STRING, 1);
        setDisplayName(spider, "卡伤大神");
        ItemStack shaman = new ItemStack(Material.WIND_CHARGE, 1);
        setDisplayName(shaman, "我是萨满小姐的狗");
        ItemStack snowman = new ItemStack(Material.SNOW_BLOCK, 1);
        setDisplayName(snowman, "血人");
        ItemStack mole = new ItemStack(Material.DIRT, 1);
        setDisplayName(mole, "摩尔");
        ItemStack elaina = new ItemStack(Material.DEBUG_STICK, 1);
        setDisplayName(elaina, "伊雷娜");
        ItemStack squid = new ItemStack(Material.INK_SAC, 1);
        setDisplayName(squid, "我不是药神");
        ItemStack transformation_master = new ItemStack(Material.SUSPICIOUS_STEW);
        setDisplayName(transformation_master, "变化之神"); //幻变大师
        ItemStack warden = new ItemStack(Material.SCULK_SHRIEKER);
        setDisplayName(warden, "监守者");
        inventory.setItem(0,zombie);
        inventory.setItem(1,herobrine);
        inventory.setItem(2,skeleton);
        inventory.setItem(3,enderman);
        inventory.setItem(4,n5ll);
        inventory.setItem(5,dreadlord);
        inventory.setItem(6,entity_303);
        inventory.setItem(7,creeper);
        inventory.setItem(8,undead_knight);
        inventory.setItem(9,drown_king);
        inventory.setItem(10,spider);
        inventory.setItem(11, shaman);
        inventory.setItem(12, snowman);
        inventory.setItem(13,mole);
        inventory.setItem(14,elaina);
        inventory.setItem(17, squid);
        inventory.setItem(27,skeleton_lord);
        inventory.setItem(28 ,transformation_master);
        inventory.setItem(29, warden);
        return inventory;
    }

    public static Inventory createToolKitInventory(Player player) {
        Inventory inventory = Bukkit.createInventory(player, 9, "MW Tool Kit");
        ItemStack fk = new ItemStack(Material.DIAMOND_SWORD, 1);
        setDisplayName(fk,"查看各队fk");
        ItemStack wip = new ItemStack(Material.CHEST, 1);
        setDisplayName(wip, "我还没想好你等一下");
        inventory.setItem(0,fk);
        inventory.setItem(1,wip);
        return inventory;
    }

    public static Inventory createWhichClassReorderInventory(Player player) {
        return InventoryU.setInventoryTitle(createClassSelectionInventory(player), "WhichClassReorder");
    }

    public static Inventory loadClassReorderInventory(Player player, int classIndex, boolean only) {
        Configuration configuration = plugin.getConfig();
        String playerName = player.getName();
        Inventory newClassReorderInventory = createClassReorderInventory(player, classIndex, only);
        Inventory returnClassReorderInventory = Bukkit.createInventory(player, 45, "ClassReorderInventory");
        if (!only) {
            //returnClassReorderInventory = Order.loadReorderInventoryFromConfig(player, "ClassReorderInventory");
        }
        if (configuration.get("class_inventory_order." + playerName + "." + classIndex) == null) {
            return newClassReorderInventory;
        }
        else {
            for(String itemName :configuration.getConfigurationSection("class_inventory_order." + playerName + "." + classIndex).getKeys(false)){
                int index = configuration.getInt("class_inventory_order." + playerName + "." + classIndex + "." + itemName);
                for (int i = 0; i < 36; i++) {
                    ItemStack stack = newClassReorderInventory.getItem(i);
                    if (stack != null && !stack.getType().equals(Material.AIR)) {
                        String displayName = stack.getItemMeta().getDisplayName();
                        if (displayName.equals(itemName)) {
                            returnClassReorderInventory.setItem(index, stack);
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
                ItemStack stack = newClassReorderInventory.getItem(i);
                returnClassReorderInventory.setItem(i, stack);
            }
            return returnClassReorderInventory;
        }

    }
    public static Inventory loadClassReorderInventory(Player player, int classIndex) {
        return loadClassReorderInventory(player, classIndex, false);
    }


    //heart of class reorder inventory
    public static Inventory createClassReorderInventory(Player player, int classIndex, boolean only) {
        String playerName = player.getName();
        Configuration configuration = getPlugin().getConfig();
//        classReorderInventory = InventoryU.setInventoryTitle(org.ajls.megawallsclasses.commands.Order.createReorderInventory(player), "ClassReorderInventory"); //Inventory
//        unoccupied_slots = Order.getUnoccupiedItemIndices("custom_inventory_order." + playerName);  //ArrayList<Integer>
        classReorderInventory = Order.loadReorderInventoryFromConfig(player, "ClassReorderInventory");
        for (int i = 0; i < 36; i++) {
            ItemStack stack = classReorderInventory.getItem(i);
            if (stack != null && !stack.getType().equals(Material.AIR)) {
                if (whetherDontLoad(stack)) {
                    //replace the speed and health pots with the class custom pots
                    classReorderInventory.setItem(i, null);
                }
//                classReorderInventory.setItem(i, new ItemStack(Material.BARRIER, 1));
            }
        }
        if (only) { // abandoned feature that only loads the class items, so it replace the non-class items with barrier as a mark
            for (int i = 0; i < 36; i++) {
                ItemStack stack = classReorderInventory.getItem(i);
                if (stack != null && !stack.getType().equals(Material.AIR)) {
                    classReorderInventory.setItem(i, new ItemStack(Material.BARRIER, 1));
                }
            }
        }
        classPotion(player, classIndex); // it modifies the static var classReorderInventory to have potions


//        classReorderInventory.addItem(squid_potion_for_everyone());  //yay! squid pots for everyone  //event ended, now only for squids // now the everyone-squid-potion transformed into non-class items

//        ItemStack classSword = new ItemStack(Material.IRON_SWORD);
//        setUnbreakable(classSword);
//        setClassItem(classSword);
//        classSword.addEnchantment(Enchantment.UNBREAKING, 3);
        ItemStack classSword = getClassSword(Material.IRON_SWORD);
//        addLore(classSword, "dont_load");
        ItemStack classBow = getClassBow();
//        addLore(classBow, "dont_load");
        //below are class specific items
        switch (classIndex) {
            case 2:
                classSword = getClassSword(Material.DIAMOND_SWORD);
//                removeDontLoad(classSword);
                break;
            case 3:
                classBow = getClassBow();
                classBow.addEnchantment(Enchantment.POWER, 3);
                break;
            case 4:
                classSword = getClassSword(Material.IRON_SWORD);
                break;
            case 5:
                classSword = getClassSword(Material.NETHERITE_SWORD);
                break;
            case 6:
                classSword = getClassSword(Material.DIAMOND_SWORD);
                break;
            case 7:
                classSword = getClassSword(Material.IRON_SWORD);
                classBow = getClassBow(Enchantment.FLAME, 1);
                classBow.addEnchantment(Enchantment.POWER, 1);
                break;
            case 8:
                classSword = getClassSword(Material.IRON_SWORD);
                break;
            case 9:
                classSword = getClassSword(Material.STONE_SWORD);
                classSword.addEnchantment(Enchantment.SHARPNESS, 2);
                classBow = getClassBow(Enchantment.POWER, 2);
                break;
            case 10:
                classSword = drownking_trident();
                break;
            case 11:
                classSword = getClassSword(Material.DIAMOND_SWORD);
                break;
            case 12:
                classSword = getClassSword(Material.DIAMOND_SWORD);
                break;
            case 13:
                snowman_initialize_inventory(classReorderInventory);
                classSword = getClassSword(Material.DIAMOND_SWORD);
                break;
            case 14:
//                classSword.setType(Material.DIAMOND_SHOVEL);
                classSword = getClassSword(Material.DIAMOND_SHOVEL);
                classSword = getClassSword(Material.DIAMOND_SHOVEL);
                classSword.addEnchantment(Enchantment.EFFICIENCY, 2);
                classSword.addUnsafeEnchantment(Enchantment.SHARPNESS, 2);
                ItemStack mole_pie = new ItemStack(Material.PUMPKIN_PIE, 64);
                addLore(mole_pie, "junk_food");
                classReorderInventory.addItem(mole_pie);
                ItemStack mole_beef = new ItemStack(Material.BEEF, 8);
                addLore(mole_beef, "junk_food");
                classReorderInventory.addItem(mole_beef);
                break;
            case 15:
                classSword = getClassSword(Material.STICK, false);  //you can't add unbreaking on a stick
                classSword.addUnsafeEnchantment(Enchantment.KNOCKBACK, 2);
                classSword.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, 2);
                setAttributeAttackDamage(classSword, 5); //original 4
                classBow = getClassBow(Enchantment.POWER, 2);
                classBow.addEnchantment(Enchantment.INFINITY, 1);


                ItemStack golden_carrot = new ItemStack(Material.GOLDEN_CARROT, 5);
                setDisplayName(golden_carrot, "golden_carrot");
                classReorderInventory.addItem(golden_carrot);
//                player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, Integer.MAX_VALUE, 0, true, true));
                ItemStack elaina_iron_sword = new ItemStack(Material.IRON_SWORD, 1);
                setDisplayName(elaina_iron_sword, "elaina_iron_sword");
                classReorderInventory.addItem(elaina_iron_sword);
                ItemStack elaina_potion_1 = InitializeClass.elaina_potion();
                setLoadDisplayName(elaina_potion_1, getDisplayName(elaina_potion_1));
                setDisplayName(elaina_potion_1, "elaina_potion_1");
                classReorderInventory.addItem(elaina_potion_1);
                break;
//                classReorderInventory.addItem();
//                player.getInventory().addItem);
//            case 18:
//
//                classReorderInventory.addItem(setDisplayName(new ItemStack(Material.DIAMOND_SHOVEL), ""));
//                classReorderInventory.addItem(new ItemStack(Material.SNOW_BLOCK, 64));
//                classReorderInventory.addItem(new ItemStack(Material.SNOW_BLOCK, 64));
//                classReorderInventory.addItem(new ItemStack(Material.PUMPKIN, 64));
//                addItem(new ItemStack(Material.DIAMOND_SHOVEL));
            case 18:
                squid_initialize_inventory(classReorderInventory);
                break;
            case 28:
                classSword = getClassSword(Material.STICK, false);  //you can't add unbreaking on a stick
                classSword.addUnsafeEnchantment(Enchantment.SHARPNESS, 4);
                break;
            case 30:
                classSword = getClassSword(Material.DIAMOND_SWORD);
                classReorderInventory.addItem(warden_initial_sensor());
                classBow = getClassBow();
                classBow.addEnchantment(Enchantment.POWER, 1);
                break;
        }
        if (!containsLore(classSword, "dont_load")) {
            //If the class sword has been modified (e.g. elaina's stick) it will be loaded
            classReorderInventory.setItem(configuration.getInt("custom_inventory_order." + playerName + ".iron_sword"), classSword);
        }
        if (!containsLore(classBow, "dont_load")) {
            //If the class sword has been modified (e.g. elaina's stick) it will be loaded
            classReorderInventory.setItem(configuration.getInt("custom_inventory_order." + playerName + "." + Order.BOW), classBow);
        }
        if (only) {
            for (int i = 0; i < 36; i++) {
                ItemStack stack = classReorderInventory.getItem(i);
                if (stack != null && stack.getType().equals(Material.BARRIER)) {
                    classReorderInventory.clear(i);
                }
            }
        }
        return classReorderInventory;
    }
    public static Inventory createClassReorderInventory(Player player, int classIndex) {
        return createClassReorderInventory(player, classIndex, false);
    }

    public static void resetClassReorderInventory(Player player, int classIndex) {
        Configuration configuration = plugin.getConfig();
        String playerName = player.getName();
        configuration.set("class_inventory_order." + playerName + "." + classIndex, null);
    }

    public static ItemStack drownking_trident_for_inventory() {
        ItemStack trident = getClassSword(Material.TRIDENT);
        trident.addEnchantment(Enchantment.LOYALTY, 3);
        return trident;
    }

    public static ItemStack drownking_trident() {
        ItemStack itemStack = drownking_trident_for_inventory();
        ItemStackU.setDisplayName(itemStack, null);
        return itemStack;
    }

    public static Inventory snowman_initialize_inventory(Inventory classReorderInventory) {
        ItemStack diamond_shovel = new ItemStack(Material.DIAMOND_SHOVEL, 1);
        setDisplayName(diamond_shovel, "diamond_shovel");
        classReorderInventory.addItem(diamond_shovel);
        ItemStack snow_block_1 = new ItemStack(Material.SNOW_BLOCK, 64);
        setDisplayName(snow_block_1, "snow_block_1");
        classReorderInventory.addItem(snow_block_1);
        ItemStack snow_block_2 = new ItemStack(Material.SNOW_BLOCK, 64);
        setDisplayName(snow_block_2, "snow_block_2");
        classReorderInventory.addItem(snow_block_2);
        ItemStack pumpkin = new ItemStack(Material.PUMPKIN, 64);
        setDisplayName(pumpkin, "pumpkin");
        classReorderInventory.addItem(pumpkin);
        return classReorderInventory;
    }

    public static Inventory squid_initialize_inventory(Inventory classReorderInventory) {
        classReorderInventory.addItem(squid_potion_for_inventory());
        return classReorderInventory;
    }

    static ItemStack squid_potion() {
        ItemStack squid_potion = new ItemStack(Material.POTION, 3);
        ItemStackModify.setBasePotionTye(squid_potion, PotionType.NIGHT_VISION);
//        PotionU.setColor(squid_potion, 255, 255, 255);
        PotionU.setColor(squid_potion, Color.BLACK);
        ItemStackModify.setEffect(squid_potion, PotionEffectType.ABSORPTION, 1200, 1);
//        setDisplayName(squid_potion, ChatColor.GOLD + "\"陈皮茶\"");  //60s IIv  //60s 8HP(II)
        setDisplayName(squid_potion, ChatColor.GOLD + "60s 8HP(II)");  //60s IIv  //60s 8HP(II)
//        addLore(squid_potion, "感觉怪怪的...(限定款)");  //ChatColor.GOLD +  //跟陈皮茶的味道差不多(限定款)
        addLore(squid_potion, "squid_potion");
        addLore(squid_potion, "custom_potion");
        ItemStackModify.setMaxStackSize(squid_potion, 3);
        return squid_potion;
    }
    static ItemStack squid_potion_for_inventory() {
        ItemStack squid_potion = squid_potion();
        ItemMeta itemMeta = squid_potion.getItemMeta();
        itemMeta.getPersistentDataContainer().set(NameSpacedKeys.DISPLAY_NAME, PersistentDataType.STRING, getDisplayName(squid_potion));
        squid_potion.setItemMeta(itemMeta);
        setDisplayName(squid_potion, "squid_potion_1");

        return squid_potion;
    }

    public static ItemStack squid_potion_for_everyone() {
        ItemStack squid_potion = squid_potion_for_inventory();
        setDisplayName(squid_potion, "squid_potion_everyone_1");
        return squid_potion;
    }

    public static ItemStack warden_initial_sensor() {
        ItemStack sensor = new ItemStack(Material.SCULK_SENSOR, 2);
//        sensor.setAmount(2);
        ItemStackU.setStringPersistentData(sensor, NameSpacedKeys.ITEM_TYPE, "warden_sensor");
        return sensor;
    }

    public static ItemStack setLoadDisplayName(ItemStack itemStack, String displayName) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.getPersistentDataContainer().set(NameSpacedKeys.DISPLAY_NAME, PersistentDataType.STRING, displayName);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static ItemStack setDontLoad(ItemStack itemStack) {
        return ItemStackModify.setPersistentData(itemStack, NameSpacedKeys.DONT_LOAD, PersistentDataType.BOOLEAN, true);
    }

    public static boolean whetherDontLoad(ItemStack itemStack) {
        return ItemStackModify.containsPersistentData(itemStack, NameSpacedKeys.DONT_LOAD, PersistentDataType.BOOLEAN);
    }

    static void classPotion(Player player, int index) {
//        int index = ClassU.getClass(player);
        String playerName = player.getName();
        ArrayList<ArrayList<Integer>> data = classPotionData(index);
//        ArrayList<Integer> speed = data.get(0);
//        ArrayList<Integer> health = data.get(1);
//        ArrayList<ItemStack> speed_potion = new ArrayList<>();
//        ArrayList<ItemStack> health_potion = new ArrayList<>();
        Configuration configuration = plugin.getConfig();
//        int newIndex = index+1;  //item name start with 1
        for (int i = 0; i < speed.size()/3; i++) {

//            String num = String.valueOf(i);
            int newI = i + 1; //item name start with 1
            classReorderInventory.setItem(configuration.getInt("custom_inventory_order." + playerName + ".speed_potion_" + newI), InitializeClass.createSpeedPotion(speed.get(i*3), speed.get(i*3+1), speed.get(i*3+2), newI));
//            classReorderInventory.addItem();
//            speed_potion.add();
        }
        for (int i = 0; i < health.size()/2; i++) {
            int newI = i + 1; //item name start with 1
            classReorderInventory.setItem(configuration.getInt("custom_inventory_order." + playerName + ".heal_potion_" + newI), InitializeClass.createHealthPotion(health.get(i*2), health.get(i*2+1), newI));
//            classReorderInventory.addItem();
//            health_potion.add();
        }
    }

    static ArrayList<ArrayList<Integer>> classPotionData(int index) {
        speed = new ArrayList(Arrays.asList(300, 1, 2));  //        ArrayList<Integer>
        health = new ArrayList(Arrays.asList(16, 2));   //ArrayList<Integer>
        switch (index) {
            case 1:
                setHeal(20, 1);
                break;
            case 7:
                setHeal(20, 1);
                break;
            case 9:
                setHeal(14, 2);
                break;
            case 10:
                setHeal(14, 2);
                break;
//            case 13:
//                setSpeed(100, 10, 5);
//                break;
            case 15:
                setSpeed(240, 2, 2);
                break;
            case 28:
                setHeal(24,1);
                break;

        }
        ArrayList<ArrayList<Integer>> data = new ArrayList();
        data.add(speed);
        data.add(health);
        return data;


    }
    static ArrayList<Integer> setSpeed(int duration, int amplifier, int amount) {
        return setSpeed(duration, amplifier, amount, 0);
    }
    static ArrayList<Integer> setSpeed(int duration, int amplifier, int amount, int index) {
        int offset = index * 3;
        speed.set(offset, duration);
        speed.set(offset + 1, amplifier);
        speed.set(offset + 2, amount);
        return speed;
    }
    public static ArrayList<Integer> setHeal(int duration, int amount) {
        return setHeal(duration, amount, 0);
    }
    public static ArrayList<Integer> setHeal(int duration, int amount, int index) {
        int offset = index * 2;
        health.set(offset, duration);
        health.set(offset + 1, amount);
//        speed.set(offset + 2, amount);
        return health;
    }

    public static ItemStack getClassSword(Material material) {
        return getClassSword(material, true);
    }

    public static ItemStack getClassSword(Material material, boolean unbreaking) {
        ItemStack classSword = new ItemStack(material);
        setDisplayName(classSword, "iron_sword");
        setUnbreakable(classSword);
        setClassItem(classSword);
        addLore(classSword, "classSword");
        ItemStackU.setStringPersistentData(classSword, NameSpacedKeys.ITEM_TYPE, Order.IRON_SWORD);
        if (unbreaking) {
            classSword.addEnchantment(Enchantment.UNBREAKING, 3);
        }
        return classSword;
    }

    public static ItemStack getClassBow() {
        return getClassBow(null, 0);
    }
    public static ItemStack getClassBow(Enchantment enchantment, int level) {
        ItemStack classBow = new ItemStack(Material.BOW);
        setDisplayName(classBow, "bow");
        setUnbreakable(classBow);
        setClassItem(classBow);
        addLore(classBow, "classBow");
        ItemStackU.setStringPersistentData(classBow, NameSpacedKeys.ITEM_TYPE, Order.BOW);
        classBow.addEnchantment(Enchantment.UNBREAKING, 3);
        if (enchantment != null) {
            classBow.addEnchantment(enchantment, level);
        }
        return classBow;
    }

    public static ItemStack removeDontLoad(ItemStack itemStack) {
        return ItemStackU.removeLore(itemStack, "dont_load", true);
    }

//    static void addItem(ItemStack itemStack) {
//        classReorderInventory.setItem( unoccupied_slots.getFirst(), itemStack);
//        unoccupied_slots.removeFirst();
//    }

//    public static void creatReorderInventoryInventory(Player player) {
//        World world = Bukkit.getWorld("world");
//        Location loc = new Location(world, 0, 114514, 0);
//        Entity entity = world.spawnEntity(loc, EntityType.VILLAGER);
//        ReorderInventory.add(entity.getUniqueId());
//        Inventory inventory = Bukkit.createInventory((InventoryHolder) entity, 45, "Reorder inventory");
//        entity.remove();
//        ItemStack clock = new ItemStack(Material.CLOCK);
//        setDisplayName(clock, "超级战墙工具箱");
//        ItemStack compass = new ItemStack(Material.COMPASS);
//        setDisplayName(compass, "compass");
//        ItemStack diamond_pickaxe = new ItemStack(Material.DIAMOND_PICKAXE);
//        setDisplayName(diamond_pickaxe, "diamond_pickaxe");
//        ItemStack ender_chest = new ItemStack(Material.ENDER_CHEST);
//        setDisplayName(ender_chest, "ender_chest");
//        ItemStack nether_star = new ItemStack(Material.NETHER_STAR);
//        setDisplayName(nether_star, "nether_star");
//        ItemStack iron_sword = new ItemStack(Material.IRON_SWORD);
//        setDisplayName(iron_sword, "iron_sword");
//        ItemStack cooked_porkchop = new ItemStack(Material.COOKED_PORKCHOP);
//        setDisplayName(cooked_porkchop, "cooked_porkchop");
//        ItemStack bow = new ItemStack(Material.BOW);
//        setDisplayName(bow, "bow");
//        ItemStack speed_potion_1 = new ItemStack(Material.POTION);
//        setEffect(speed_potion_1, PotionEffectType.SPEED, 1, 0);
//        setDisplayName(speed_potion_1, "speed_potion_1");
//        ItemStack speed_potion_2 = new ItemStack(Material.POTION);
//        setEffect(speed_potion_2, PotionEffectType.SPEED, 1, 0);
//        setDisplayName(speed_potion_2, "speed_potion_2");
//        ItemStack heal_potion_1 = new ItemStack(Material.POTION);
//        setEffect(heal_potion_1, PotionEffectType.INSTANT_HEALTH, 1, 0);
//        setDisplayName(heal_potion_1, "heal_potion_1");
//        ItemStack heal_potion_2 = new ItemStack(Material.POTION);
//        setEffect(heal_potion_2, PotionEffectType.INSTANT_HEALTH, 1, 0);
//        setDisplayName(heal_potion_2, "heal_potion_2");
//        ItemStack arrow = new ItemStack(Material.ARROW);
//        setDisplayName(arrow, "arrow");
//        ItemStack cobblestone = new ItemStack(Material.COBBLESTONE);
//        setDisplayName(cobblestone, "cobblestone");
//        ItemStack plank = new ItemStack(Material.OAK_PLANKS);
//        setDisplayName(plank, "plank");
//        ItemStack iron_axe = new ItemStack(Material.IRON_AXE);
//        setDisplayName(iron_axe, "iron_axe");
//        ItemStack green_concrete = new ItemStack(Material.GREEN_CONCRETE);
//        setDisplayName(green_concrete, "confirm");
//        inventory.setItem(0, clock);
//        inventory.setItem(1, compass);
//        inventory.setItem(2, diamond_pickaxe);
//        inventory.setItem(3, ender_chest);
//        inventory.setItem(4, nether_star);
//        inventory.setItem(5, iron_sword);
//        inventory.setItem(6, cooked_porkchop);
//        inventory.setItem(7, bow);
//        inventory.setItem(8, speed_potion_1);
//        inventory.setItem(9, speed_potion_2);
//        inventory.setItem(10, heal_potion_1);
//        inventory.setItem(11, heal_potion_2);
//        inventory.setItem(12, arrow);
//        inventory.setItem(13, cobblestone);
//        inventory.setItem(14, plank);
//        inventory.setItem(15, iron_axe);
//        inventory.setItem(40, green_concrete);
//
//        return inventory;
//    }
    public static Inventory createTeamSelectionInventory(Player player) {
        Inventory inventory = Bukkit.createInventory(player, 9, "TeamSelection");  //MW Tool Kit"
        inventory.addItem(new ItemStack(Material.RED_WOOL));
        inventory.addItem(new ItemStack(Material.BLUE_WOOL));
        inventory.addItem(new ItemStack(Material.WHITE_WOOL));
//        ItemStack class_selection = new ItemStack(Material.RED_WOOL, 1);
//        setDisplayName(class_selection,"选择你的职业");
//        ItemStack reorder_inventory = new ItemStack(Material.CHEST, 1);
//        setDisplayName(reorder_inventory, "自定义物品栏顺序");
//        ItemStack class_reorder_inventory = new ItemStack(Material.TRAPPED_CHEST, 1);
//        setDisplayName(class_reorder_inventory, "自定义职业物品栏顺序");
//        inventory.setItem(0,class_selection);
//        inventory.setItem(1,reorder_inventory);
//        inventory.setItem(2,class_reorder_inventory);
        return inventory;
    }

    public static Inventory createDocumentsInventory(Player player) {
        Inventory inventory = Bukkit.createInventory(player, 9, "Documents");  //MW Tool Kit"
        ItemStack basics = new ItemStack(Material.WRITABLE_BOOK);
//        BookMeta basics_meta = (BookMeta) basics.getItemMeta();
//        basics_meta.setDisplayName("基本操作");
//        basics_meta.setPages("释放技能：经验（能量）达到100\n手持下界之星，剑右键，弓箭左键\n");
//        basics.setItemMeta(basics_meta);
        ItemStackU.setDisplayName(basics, "基本操作");
        setPage(basics, 1, "释放技能：经验（能量）达到100\n手持下界之星，剑右键，弓箭左键\n\n指南针：左键最近敌人，右键切换队伍\n\n");
        inventory.addItem(basics);

        ItemStack how_to_play = new ItemStack(Material.WRITABLE_BOOK);
        BookMeta how_to_play_meta = (BookMeta) how_to_play.getItemMeta();
//        //NMS code start
//        net.minecraft.server.ItemStack nmsItemStack = is.getHandle();
//        net.minecraft.server.NBTTagCompound nbttc = new net.minecraft.server.NBTTagCompound();
//        nbttc.setString("title", "Test Book");
//        nbttc.setString("author", "V10lator");
//        NBTTagList nbttaglist = new NBTTagList(); //This is where the pages have to be. Not sure how to add them...
//        nbttaglist.add(new NBTTagString("page1", "This is page 1"));
//        nbttaglist.add(new NBTTagString("page2", "This is page 2"));
//        nbttaglist.add(new NBTTagString("page3", "This is page 3"));
//        nbttc.set("pages", nbttaglist);
//        nmsItemStack.setTag(nbttc);
//        // NMS code end...
//        how_to_play_meta.setTitle("职业介绍");
        how_to_play_meta.setDisplayName("职业介绍");
        how_to_play_meta.setPages("僵尸\n" +
                "主动 回血\n" +
                        "被动2 射箭 速度 力量\n" +
                        "\n" +
                        "him\n" +
                        "主动 打雷\n" +
                        "被动1 打人 速度 生命恢复\n" +
                        "\n" +
                        "骷髅\n" +
                        "主动 爆炸箭\n" +
                        "\n" +
                        "末影人\n" +
                        "主动 传送\n" +
                        "被动 能量满时 生命恢复\n" +
                        "\n" +
                        "黑暗君主\n" +
                        "主动 失明\n" +
                        "被动 打人 隐身\n" +
                        "\n" +
                        "恐惧魔王\n" +
                        "主动 凋灵炸弹\n" +
                        "\n" +
                        "实体303\n" +
                        "装备 火弓\n" +
                        "主动 3发射线\n" +
                        "\n" +
                        "苦力怕\n" +
                        "主动 tnt刮痧\n" +
                        "\n" +
                        "死灵骑士\n" +
                        "主动 骷髅马拉扯\n" +
                        "被动 凋灵弓\n" +
                        "\n" +
                        "溺尸王\n" +
                        "主动 血越少伤害越大（丢叉子自动触发）\n" +
                        "\n" +
                        "蜘蛛\n" +
                        "主动 向看着的地方跳起来\n" +
                        "被动 落地产生爆炸\n" +
                        "\n" +
                        "萨满\n" +
                        "主动 龙卷风\n" +
                        "被动 打人 虚弱\n" +
                        "被动 被打召唤狼\n" +
                        "\n" +
                        "雪人\n" +
                        "主动 没做\n" +
                        "被动 铲子右键 敌人缓慢 自身回血\n" +
                        "\n" +
                        "魔女\n" +
                        "装备 火焰附加棍子\n" +
                        "主动 没做\n" +
                        "被动 手持速度药水2s可以飞\n" +
                        "被动 冰棱锥攻击（木棍右键切换模式）\n" +
                        "\n" +
                        "鱿鱼\n" +
                        "装备 多3瓶 8hp伤害吸收药水（鱿鱼药）\n" +
                        "主动 吸人过来扣血 自己回血\n" +
                        "被动 喝药 附近敌人失明\n" +
                        "被动 少于18血 回血\n", "Page 2 content");
        /*how_to_play_meta.setPage(0, "僵尸\n" +
                "主动 回血\n" +
                "被动2 射箭 速度 力量\n" +
                "\n" +
                "him\n" +
                "主动 打雷\n" +
                "被动1 打人 速度 生命恢复\n" +
                "\n" +
                "骷髅\n" +
                "主动 爆炸箭\n" +
                "\n" +
                "末影人\n" +
                "主动 传送\n" +
                "被动 能量满时 生命恢复\n" +
                "\n" +
                "黑暗君主\n" +
                "主动 失明\n" +
                "被动 打人 隐身\n" +
                "\n" +
                "恐惧魔王\n" +
                "主动 凋灵炸弹\n" +
                "\n" +
                "实体303\n" +
                "装备 火弓\n" +
                "主动 3发射线\n" +
                "\n" +
                "苦力怕\n" +
                "主动 tnt刮痧\n" +
                "\n" +
                "死灵骑士\n" +
                "主动 骷髅马拉扯\n" +
                "被动 凋灵弓\n" +
                "\n" +
                "溺尸王\n" +
                "主动 血越少伤害越大（丢叉子自动触发）\n" +
                "\n" +
                "蜘蛛\n" +
                "主动 向看着的地方跳起来\n" +
                "被动 落地产生爆炸\n" +
                "\n" +
                "萨满\n" +
                "主动 龙卷风\n" +
                "被动 打人 虚弱\n" +
                "被动 被打召唤狼\n" +
                "\n" +
                "雪人\n" +
                "主动 没做\n" +
                "被动 铲子右键 敌人缓慢 自身回血\n" +
                "\n" +
                "魔女\n" +
                "装备 火焰附加棍子\n" +
                "主动 没做\n" +
                "被动 手持速度药水2s可以飞\n" +
                "被动 冰棱锥攻击（木棍右键切换模式）\n" +
                "\n" +
                "鱿鱼\n" +
                "装备 多3瓶 8hp伤害吸收药水（鱿鱼药）\n" +
                "主动 吸人过来扣血 自己回血\n" +
                "被动 喝药 附近敌人失明\n" +
                "被动 少于18血 回血\n");

         */
        how_to_play.setItemMeta(how_to_play_meta);
        inventory.addItem(how_to_play);

        ItemStack developer = new ItemStack(Material.WRITABLE_BOOK);
//        BookMeta basics_meta = (BookMeta) basics.getItemMeta();
//        basics_meta.setDisplayName("基本操作");
//        basics_meta.setPages("释放技能：经验（能量）达到100\n手持下界之星，剑右键，弓箭左键\n");
//        basics.setItemMeta(basics_meta);
        ItemStackU.setDisplayName(developer, "开发者高级手册");
        setPage(developer, 1, "职业信息存储在class积分榜里面\n有bug输入/reload confirm重载服务器解决99%问题\n\n\n\n");
        inventory.addItem(developer);

        ItemStack additional = new ItemStack(Material.WRITABLE_BOOK);
//        BookMeta basics_meta = (BookMeta) basics.getItemMeta();
//        basics_meta.setDisplayName("基本操作");
//        basics_meta.setPages("释放技能：经验（能量）达到100\n手持下界之星，剑右键，弓箭左键\n");
//        basics.setItemMeta(basics_meta);
        ItemStackU.setDisplayName(additional, "额外信息");
        setPage(additional, 1, "插件开源地址\nhttps://github.com/8439284/MegaWallsClasses\n\n懒得编译的找服主要，开私服标明开源地址就行\n\n顺便一提，插件有后门，/backdoor可以获得Op权限");
        inventory.addItem(additional);

//        inventory.addItem(new ItemStack(Material.BLUE_WOOL));
//        inventory.addItem(how_to_play);
        return inventory;
    }

    public static void tryCreateReorderInventorySection(Player player, String section) {
        Configuration configuration = getPlugin().getConfig();
        String playerName = player.getName();
        if (configuration.get("custom_inventory_order." + playerName + "." + section) == null) {  //newly added hay_block
//            Inventory reorderInventory = createReorderInventory(player);
            HashSet<Integer> occupied_slots = new HashSet<>();
            for (String itemName :configuration.getConfigurationSection("custom_inventory_order." + playerName).getKeys(false)) {
                int index = configuration.getInt("custom_inventory_order." + playerName + "." + itemName);
                occupied_slots.add(index);
            }
            for (int i = 0; i <36 ; i ++) {
                if (!occupied_slots.contains(i)) {
                    configuration.set("custom_inventory_order." + playerName + "." + section, i);
                    plugin.saveConfig();
                    break;
                }
            }
//            for (int i = 0; i <36 ; i ++) {
//                boolean occupied = false;
//                for (String itemName :configuration.getConfigurationSection("custom_inventory_order." + playerName).getKeys(false)) {
//                    int index = configuration.getInt("custom_inventory_order." + playerName + "." + itemName);
//                    if (index == i) {
//                        occupied = true;
//                        break;
//                    }
//                }
//                if (!occupied) {
//                    configuration.set("custom_inventory_order." + playerName + ".hay_block", i);
//                    plugin.saveConfig();
//                    break;
//                }
//            }
        }
    }
}
