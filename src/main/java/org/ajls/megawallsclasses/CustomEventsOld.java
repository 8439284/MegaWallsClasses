package org.ajls.megawallsclasses;

import org.bukkit.*;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Colorable;
//import org.bukkit.material.MaterialData;
//import org.bukkit.material.Wool;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

import static org.ajls.megawallsclasses.GameManager.teamTeleportSpawn;
import static org.ajls.megawallsclasses.GameManager.witherDeadTeams;
import static org.ajls.megawallsclasses.ItemStackModify.containsLore;
import static org.ajls.megawallsclasses.KillsManager.registerPlayerDeath;
import static org.ajls.megawallsclasses.MegaWallsClasses.plugin;
import static org.ajls.megawallsclasses.MyListener.*;
import static org.ajls.megawallsclasses.ScoreboardsAndTeams.getPlayerTeamName;

public class CustomEventsOld {
    public static void playerDeathEvent(Player player) {
        UUID playerUUID = player.getUniqueId();
        String teamName = getPlayerTeamName(player);
        registerPlayerDeath(player);
        player.setGameMode(GameMode.SPECTATOR);
        player.setHealth(player.getMaxHealth());
//        EnergyAccumulate.disableAutoEnergyAccumulation(player);
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_HURT, 1, 1);
//        player.setHealth(player.getMaxHealth()); health potion exp saturation
        Inventory inventory = player.getInventory();
        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack itemStack = inventory.getItem(i);
            if (itemStack != null) {
                inventory.clear(i);
                if (!containsLore(itemStack, "classItem")) {
                    player.getWorld().dropItemNaturally(player.getLocation(), itemStack);
                }
            }
        }
        woolOnDeath(player);
        for (UUID uuid : MyListener.skeleton_lord_player.keySet()) {
//            UUID playerUUID = skeleton_lord_player.get(uuid);
            if (playerUUID.equals(player.getUniqueId())) {
                skeleton_lord_player.remove(uuid);
                Bukkit.getPlayer(uuid).sendMessage("你的标记死了 已自动取消标记防止鞭尸");
            }
        }
        HashMapUtils.removeKeys(playerUUID, PassiveSkills.elaina_enemy);
        if (witherDeadTeams.contains(teamName)) {
            player.sendMessage("Too bad you died so young and early");
        }
        else {
            BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
            player.sendMessage("respawn in 5s");
            scheduler.scheduleSyncDelayedTask(plugin, () -> {
                teamTeleportSpawn(player);
                player.setGameMode(GameMode.SURVIVAL);
                if (player_nextClass.containsKey(player.getUniqueId())) {
                    ClassU.setClass(player, player_nextClass.remove(player.getUniqueId()));
                    InitializeClass.refreshClass(player);
                }
                else {
                    initializeClass(player);
                }


//                initializeClass(player);
            }, 100L);
        }
    }

    private static final Material[] WOOL_COLORS = {
            Material.WHITE_WOOL, Material.ORANGE_WOOL, Material.MAGENTA_WOOL,
            Material.LIGHT_BLUE_WOOL, Material.YELLOW_WOOL, Material.LIME_WOOL,
            Material.PINK_WOOL, Material.GRAY_WOOL, Material.LIGHT_GRAY_WOOL,
            Material.CYAN_WOOL, Material.PURPLE_WOOL, Material.BLUE_WOOL,
            Material.BROWN_WOOL, Material.GREEN_WOOL, Material.RED_WOOL,
            Material.BLACK_WOOL
    };
    static void woolOnDeath(Player player) {
//        Material randomWool = WOOL_COLORS[new Random().nextInt(WOOL_COLORS.length)];

        // Create an ItemStack of the selected wool color
        for (Material material: WOOL_COLORS) { //int i = 0; i < WOOL_COLORS.length; i++  //DyeColor dyeColor : DyeColor.values()
            ItemStack wool = new ItemStack(material, 1);
            // Create a wool item with the random color
//            DyeColor randomColor = DyeColor.values()[new Random().nextInt(DyeColor.values().length)];
//            ItemStack woolItem = new ItemStack(Material.LEGACY_WOOL, 1);
//            Wool woolItem = new Wool(dyeColor);
//            woolItem.setData(wool);
//            ItemStack wool = new ItemStack(Material.WHITE_WOOL);
            double randomPitch = Math.random();
            randomPitch = Math.asin(randomPitch);
            double randomYaw = Math.random() * Math.PI;
            Random random = new Random();
            int negative = random.nextInt(2);
            if (negative == 1) {
                randomYaw = - randomYaw;
            }
            Vector vector = new Vector(Math.cos(randomPitch) * Math.cos(randomYaw), Math.sin(randomPitch), Math.cos(randomPitch) * Math.sin(randomYaw));
            Location location = player.getLocation();
            World world = player.getWorld();
            Item item = world.dropItem(location, wool); //woolItem.toItemStack()
            item.setCanPlayerPickup(false);
            item.setVelocity(vector.add(player.getVelocity()));
        }
    }
}
