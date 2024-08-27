package org.ajls.megawallsclasses;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import static org.ajls.megawallsclasses.ItemStackModify.setDisplayName;
import static org.ajls.megawallsclasses.ItemStackModify.setEffect;

public class InventoryManager {

    public static Inventory createLobbyMenuInventory(Player player) {
        Inventory inventory = Bukkit.createInventory(player, 9, "MW Menu");  //MW Tool Kit"
        ItemStack class_selection = new ItemStack(Material.IRON_SWORD, 1);
        setDisplayName(class_selection,"选择你的职业");
        ItemStack reorder_inventory = new ItemStack(Material.CHEST, 1);
        setDisplayName(reorder_inventory, "自定义物品栏顺序");
        inventory.setItem(0,class_selection);
        inventory.setItem(1,reorder_inventory);
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
        ItemStack elaina = new ItemStack(Material.DEBUG_STICK, 1);
        setDisplayName(elaina, "伊雷娜");
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
        inventory.setItem(14,elaina);
        inventory.setItem(27,skeleton_lord);
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
}
