package org.ajls.megawallsclasses;

import org.ajls.lib.advanced.HaxhMap;
import org.ajls.megawallsclasses.rating.Rating;
import org.ajls.megawallsclasses.utils.VelocityU;
import org.bukkit.*;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
//import org.bukkit.material.MaterialData;
//import org.bukkit.material.Wool;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.UUID;

import static org.ajls.megawallsclasses.GameManager.*;
import static org.ajls.megawallsclasses.ItemStackModify.containsLore;
import static org.ajls.megawallsclasses.KillsManager.player_deaths;
import static org.ajls.megawallsclasses.KillsManager.registerPlayerDeath;
import static org.ajls.megawallsclasses.MegaWallsClasses.plugin;
import static org.ajls.megawallsclasses.MyListener.*;
import static org.ajls.megawallsclasses.ScoreboardsAndTeams.getPlayerTeamColor;
import static org.ajls.megawallsclasses.ScoreboardsAndTeams.getPlayerTeamName;

public class CustomEventsOld {
    public static HaxhMap<UUID, Item> getPlayer_deathItems() {
        return player_deathItems;
    }

    public static void setPlayer_deathItems(HaxhMap<UUID, Item> player_deathItems) {
        CustomEventsOld.player_deathItems = player_deathItems;
    }

    public static org.ajls.lib.advanced.HaxhMap<UUID, Item> player_deathItems = new HaxhMap<>();
    public static void playerDeathEvent(Player player) {
        UUID playerUUID = player.getUniqueId();
        String teamName = getPlayerTeamName(player);
        String playerName = player.getName();
        Vector velocity = player.getVelocity();
        ChatColor teamColor = getPlayerTeamColor(player, true);
        Location eyeLocation = player.getEyeLocation();
        Location location = player.getLocation();
        Location bodyLocation = eyeLocation.clone().add(location).multiply(0.5);
        World world = player.getWorld();
        registerPlayerDeath(player);
        Rating.loser_winnerMap.settle(player);
        player.setGameMode(GameMode.SPECTATOR);
        player.setHealth(player.getMaxHealth());
//        EnergyAccumulate.disableAutoEnergyAccumulation(player);
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_DEATH, 1, 1);  //ENTITY_PLAYER_HURT
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
        ItemStack player_head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta) player_head.getItemMeta();
        skullMeta.setOwningPlayer(player);
        skullMeta.setDisplayName(teamColor + playerName);
        player_head.setItemMeta(skullMeta);
        Item player_headItem = world.dropItemNaturally(eyeLocation, player_head);
        player_headItem.setCanPlayerPickup(false);
        player_headItem.setCustomNameVisible(true);
        player_headItem.setCustomName(teamColor + playerName);
        player_headItem.setVelocity(player_headItem.getVelocity().add(velocity));

        player_deathItems.put(playerUUID, player_headItem);


        for (int i = 0; i < 6; i++) {
            ItemStack boneItemStack;
            if (i <3) {
                boneItemStack =  new ItemStack(Material.BONE, 1);
            }
            else {
                boneItemStack = new ItemStack(Material.ROTTEN_FLESH, 1);
            }

            Item boneItem = world.dropItemNaturally(bodyLocation, boneItemStack);
            boneItem.setCanPlayerPickup(false);
            boneItem.setVelocity(boneItem.getVelocity().add(velocity));

            player_deathItems.put(playerUUID, boneItem);
        }


        world.dropItemNaturally(player.getLocation(), InitializeClass.elaina_potion());  //elaina potion for the ones still alive

        /*
        for (UUID uuid : MyListener.skeleton_lord_player.keySet()) {
//            UUID playerUUID = skeleton_lord_player.get(uuid);
            if (playerUUID.equals(skeleton_lord_player.get(uuid))) {
                skeleton_lord_player.remove(uuid);
                Bukkit.getPlayer(uuid).sendMessage("你的标记死了 已自动取消标记防止鞭尸");
            }
        }
         */

//        HashMapUtils.removeKeys(playerUUID, PassiveSkills.elaina_enemy);
        if (witherDeadTeams.contains(teamName)) {
            player.sendMessage("Too bad you died so young and early");
        }
        else {
            BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
            player.sendMessage("respawn in 5s");
            scheduler.scheduleSyncDelayedTask(plugin, () -> {
//                teamTeleportSpawn(player);
                Location teleportLocation = teleportNearPlayers(player);
                player.setGameMode(GameMode.SURVIVAL);
                if (player_nextClass.containsKey(player.getUniqueId())) {
                    ClassU.setClass(player, player_nextClass.remove(player.getUniqueId()));
                    InitializeClass.refreshClassOnChangeClass(player);  //add tf compatibility
                }
                else {
                    InitializeClass.resetPlayerCondition(player);
                    boolean isTF = ClassU.isTransformationMaster(player);
                    if (isTF) {
//                        InitializeClass.transformation_master_initialize_class(player);
                        initializeClass(player, true);
                        initializeClass(player, false);

                    }
                    else {
                        initializeClass(player, false);
                    }
                }


                HashSet<Item> deathItems = player_deathItems.getValues(playerUUID, true);
                BukkitTask fleshRecreateTask = scheduler.runTaskTimer(MegaWallsClasses.getPlugin(), () -> {
//                    for (Item item : deathItems) {
//                        if (item.isValid()) {
//                            item.setCanPlayerPickup(true);
//                            item.teleport(teleportLocation);
//                            item.setVelocity(new Vector(0, 0, 0));
//                        }
//                    }
                    for (Item item : deathItems) {
//                    item.setCanPlayerPickup(true);
                        Location itemLocation = item.getLocation();
                        Location playerCurrentEyeLocation = player.getEyeLocation();
                        Vector directionVector = playerCurrentEyeLocation.clone().subtract(itemLocation).toVector();
                        int hitWall = VelocityU.hitWall(item.getVelocity());
                        Vector newVelocity;
                        if (itemLocation.distance(playerCurrentEyeLocation) < 2) {
                            newVelocity = item.getVelocity().add(directionVector.multiply(0.1));  //f = -kx
                        }
                        else  {
                            newVelocity = directionVector.multiply(0.1);
                        }
                        item.setVelocity(newVelocity);
                        if (hitWall == 1) {
                            itemLocation.add(new Vector(item.getVelocity().getX(), 0, 0));
                        }
                        else if (hitWall == 2) {
                            itemLocation.add(new Vector(0, item.getVelocity().getY(), 0));
                        }
                        else if (hitWall == 3) {
                            itemLocation.add(new Vector(0, 0, item.getVelocity().getZ()));
                        }
                        if (hitWall > 0) {
                            item.teleport(itemLocation);
                            item.setVelocity(newVelocity);
                        }

//                        else if (hitWall == 4) {
//                            itemLocation.add(new Vector(-item.getVelocity().getX(), 0, 0));
//                        }
//                        else if (hitWall == 5) {
//                            itemLocation.add(new Vector(0, -item.getVelocity().getY(), 0));
//                        }
//                        else if (hitWall == 6) {
//                            itemLocation.add(new Vector(0, 0, -item.getVelocity().getZ()));
//                        }
//                        else {
//                            item.setCanPlayerPickup(true);
//                            item.teleport(teleportLocation);
//                            item.setVelocity(new Vector(0, 0, 0));
//                        }



                    }

                }, 0L, 1L);
                //                scheduler.runTaskLater(MegaWallsClasses.getPlugin(), fleshRecreateTask::cancel, 60L);
                scheduler.runTaskLater(MegaWallsClasses.getPlugin(), () -> {
                    fleshRecreateTask.cancel();
                    for (Item item : deathItems) {
                        item.remove();
                    }
                    world.spawnParticle(Particle.CLOUD, player.getLocation(), 10);  //0.5, 0.5, 0.5, 0.1
                    world.playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
                }, 60L);


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
        UUID playerUUID = player.getUniqueId();
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

            player_deathItems.put(playerUUID, item);
        }
    }
}
