package org.ajls.megawallsclasses;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.Pair;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import it.unimi.dsi.fastutil.Hash;
import net.kyori.adventure.util.TriState;
import org.ajls.lib.Lib;
import org.ajls.lib.advanced.HashMapInteger;
import org.ajls.megawallsclasses.commands.PlayerUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Objective;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

import static org.ajls.megawallsclasses.BlocksModify.isInBounds;
import static org.ajls.megawallsclasses.ColorAndChatColor.translateChatColorToColor;
import static org.ajls.megawallsclasses.MegaWallsClasses.plugin;
import static org.ajls.megawallsclasses.MyListener.*;
import static org.ajls.megawallsclasses.ScoreboardsAndTeams.getPlayerTeam;
import static org.ajls.megawallsclasses.ScoreboardsAndTeams.getPlayerTeamColor;
import static org.ajls.megawallsclasses.Utils.random;
import static org.bukkit.Bukkit.broadcastMessage;
import static org.bukkit.Bukkit.getServer;

public class PassiveSkills {
    static HashMap<UUID, BukkitTask> nullInvisibility_tasks = new HashMap<>();
//    public static ArrayList<UUID> nullPassiveSkillDisable = new ArrayList<>(); //disable modifying packets when sender modified it when send
    static HashSet<UUID> nullInvisibilityPlayers = new HashSet<>();

    //zombie
    public static void zombie_passive_skill_1(Player player) {
        int randomInt = org.ajls.lib.utils.JavaU.random(1, 100);
        if (randomInt <= 38) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 30, 0));
//            player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 60, 0));
            player.sendMessage("僵尸抗性1.5s");
        }
    }
    public static void zombie_passive_skill_2(Player player) {
        int randomInt = org.ajls.lib.utils.JavaU.random(1, 10);
        if (randomInt <= 6) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 120, 1));
            player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 60, 0));
            player.sendMessage("僵尸被射生气了，冲啊！！！");
        }
    }

    //herobrine

    public static void herobrine_passive_skill_1(Player damager) {
        if (random(1,10) <= 3) {
            damager.addPotionEffect((new PotionEffect(PotionEffectType.SPEED, 60, 1, true, true)));
            damager.addPotionEffect((new PotionEffect(PotionEffectType.REGENERATION, 120, 0, true, true)));
            damager.sendMessage(ChatColor.BLUE + "飓风 " + ChatColor.WHITE + "速度 " + ChatColor.YELLOW + "II " + ChatColor.AQUA + "3s");
            damager.sendMessage(ChatColor.BLUE + "飓风 " + ChatColor.RED + "生命恢复 " + ChatColor.YELLOW + "I " + ChatColor.AQUA + "6s");
        }
    }

    //null
    public static HashMapInteger<UUID> null_invisibility_mode = new HashMapInteger<>(1);  //0 wait till invisible 1 invisible end timer  //0 default 1 wait till invisible 2 invisible end timer
    public static void null_passive_skill_1_increase(Player damager) {
        UUID damagerUUID = damager.getUniqueId();
        n5ll_invisibility.increment(damagerUUID, 3);
        if (!Cooldown.player_passiveSkill1Cooldown.containsKey(damagerUUID)) {
            Cooldown.player_passiveSkill1Cooldown.put(damagerUUID, 130);
        }
//        else {
//
//        }
    }
    public static void null_passive_skill_1(Player damager) {  //改进 任务开始等6.5s给3.5s隐身并且减少计数 然后任务循环10s，延迟0s，循环结束判断是否还有计数，如果为0那么任务取消
        damager.addPotionEffect((new PotionEffect(PotionEffectType.INVISIBILITY, 70, 0, true, true)));
        damager.addPotionEffect((new PotionEffect(PotionEffectType.SPEED, 70, 1, true, true)));
        damager.sendMessage(ChatColor.GRAY + "间隐 " + ChatColor.GRAY + "隐身 " + ChatColor.YELLOW + "I " + ChatColor.AQUA + "3.5s");
        damager.sendMessage(ChatColor.GRAY + "间隐 " + ChatColor.WHITE + "速度 " + ChatColor.YELLOW + "II " + ChatColor.AQUA + "3.5s");
        null_hide_armor(damager);
        /*
        if (n5ll_invisibility.containsKey(damager.getUniqueId())) {
            int invisibility_count = n5ll_invisibility.get(damager.getUniqueId());
            if ( invisibility_count < 3 ) {
                n5ll_invisibility.put(damager.getUniqueId(), invisibility_count + 1);
            }
        }
        else {
            BukkitScheduler scheduler = getServer().getScheduler();
            n5ll_invisibility.put(damager.getUniqueId(), 1);
            BukkitTask task = scheduler.runTaskTimer(MegaWallsClasses.getPlugin(), () -> {
                null_passive_skill_disable(damager); //隐身结束如果计数为0那么取消
//                scheduler.scheduleSyncDelayedTask(MegaWallsClasses.getPlugin(), () -> {
//                    broadcastMessage("20");
//                }, 20L);
//                scheduler.scheduleSyncDelayedTask(MegaWallsClasses.getPlugin(), () -> {
//                    broadcastMessage("40");
//                }, 40L);
//                scheduler.scheduleSyncDelayedTask(MegaWallsClasses.getPlugin(), () -> {
//                    broadcastMessage("60");
//                }, 60L);
//                scheduler.scheduleSyncDelayedTask(MegaWallsClasses.getPlugin(), () -> {
//                    broadcastMessage("80");
//                }, 80L);
//                scheduler.scheduleSyncDelayedTask(MegaWallsClasses.getPlugin(), () -> {
//                    broadcastMessage("100");
//                }, 100L);
//                scheduler.scheduleSyncDelayedTask(MegaWallsClasses.getPlugin(), () -> {
//                    broadcastMessage("120");
//                }, 120L);
//                scheduler.scheduleSyncDelayedTask(MegaWallsClasses.getPlugin(), () -> {
//                    broadcastMessage("140");
//                }, 140L);
//                scheduler.scheduleSyncDelayedTask(MegaWallsClasses.getPlugin(), () -> {
//                    broadcastMessage("160");
//                }, 160L);
//                scheduler.scheduleSyncDelayedTask(MegaWallsClasses.getPlugin(), () -> {
//                    broadcastMessage("180");
//                }, 180L);
                if (n5ll_invisibility.get(damager.getUniqueId()) <= 0) {
                    BukkitTask task1 = nullInvisibility_tasks.remove(damager.getUniqueId()); // remove from map if exist
                    n5ll_invisibility.remove(damager.getUniqueId());
                    if(task1 != null) { // task found
                        task1.cancel();
                    }
                }
                else {

                    scheduler.scheduleSyncDelayedTask(MegaWallsClasses.getPlugin(), () -> {
                        damager.addPotionEffect((new PotionEffect(PotionEffectType.INVISIBILITY, 70, 0, true, true)));
                        damager.addPotionEffect((new PotionEffect(PotionEffectType.SPEED, 70, 1, true, true)));
                        damager.sendMessage(ChatColor.GRAY + "间隐 " + ChatColor.GRAY + "隐身 " + ChatColor.YELLOW + "I " + ChatColor.AQUA + "3.5s");
                        damager.sendMessage(ChatColor.GRAY + "间隐 " + ChatColor.WHITE + "速度 " + ChatColor.YELLOW + "II " + ChatColor.AQUA + "3.5s");
                        null_hide_armor(damager);
                        int invisibility_count = n5ll_invisibility.get(damager.getUniqueId());
                        n5ll_invisibility.put(damager.getUniqueId(), invisibility_count - 1);
                    }, 130L);
                }

            }, 0L, 200);
            nullInvisibility_tasks.put(damager.getUniqueId(), task);
        }

         */
//        BukkitScheduler scheduler = getServer().getScheduler();
//        n5ll_invisibility.put(damager.getUniqueId(), 1);
//        BukkitTask task = scheduler.runTaskTimer(MegaWallsClasses.getPlugin(), () -> {
//            damager.addPotionEffect((new PotionEffect(PotionEffectType.INVISIBILITY, 70, 0, false, false)));
//            damager.addPotionEffect((new PotionEffect(PotionEffectType.SPEED, 70, 1, false, false)));
//            damager.sendMessage(ChatColor.GRAY + "间隐 " + ChatColor.GRAY + "隐身 " + ChatColor.YELLOW + "I " + ChatColor.AQUA + "3.5s");
//            damager.sendMessage(ChatColor.GRAY + "间隐 " + ChatColor.WHITE + "速度 " + ChatColor.YELLOW + "II " + ChatColor.AQUA + "3.5s");
//            null_hide_armor(damager);
//            scheduler.scheduleSyncDelayedTask(MegaWallsClasses.getPlugin(), () -> {
//                null_passive_skill_disable(damager);
//            }, 70L);
//            int invisibility_count = n5ll_invisibility.get(damager.getUniqueId());
//            n5ll_invisibility.put(damager.getUniqueId(), invisibility_count - 1);
//            if (invisibility_count <= 1) {
//                BukkitTask task1 = tasks.remove(damager.getUniqueId()); // remove from map if exist
//                if(task1 != null) { // task found
//                    task1.cancel();
//                }
//                n5ll_invisibility.remove(damager.getUniqueId());
//            }
//
//        }, 130L, 200);
//        tasks.put(damager.getUniqueId(), task);
    }

    static void null_hide_armor(Player damager) {
        for (Player online : Bukkit.getOnlinePlayers()) {
            if (online != damager) {
                ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
                PacketContainer hide_armor_packet = protocolManager.createPacket(PacketType.Play.Server.ENTITY_EQUIPMENT);
                hide_armor_packet.getIntegers().write(0, damager.getEntityId());
                List<Pair<EnumWrappers.ItemSlot, ItemStack>> list = new ArrayList<>();
                list.add(new Pair<>(EnumWrappers.ItemSlot.HEAD, new ItemStack(Material.AIR)));
                list.add(new Pair<>(EnumWrappers.ItemSlot.CHEST, new ItemStack(Material.AIR)));
                list.add(new Pair<>(EnumWrappers.ItemSlot.LEGS, new ItemStack(Material.AIR)));
                list.add(new Pair<>(EnumWrappers.ItemSlot.FEET, new ItemStack(Material.AIR)));
                list.add(new Pair<>(EnumWrappers.ItemSlot.MAINHAND, new ItemStack(Material.AIR)));
                list.add(new Pair<>(EnumWrappers.ItemSlot.OFFHAND, new ItemStack(Material.AIR)));
                hide_armor_packet.getSlotStackPairLists().write(0, list);
                protocolManager.sendServerPacket(online, hide_armor_packet);

//                try {
//                    protocolManager.sendServerPacket(online, hide_armor_packet);
//                } catch (InvocationTargetException e) {
//                    throw new RuntimeException(e);
//                }


                //                        PacketContainer hide_arrow_packet = new PacketContainer(PacketType.Play.Server.ENTITY_METADATA);
//                        hide_arrow_packet.getIntegers().write(0, damager.getEntityId());
//
//                        WrappedDataWatcher watcher = new WrappedDataWatcher();
//                        watcher.setEntity(damager);
//                        watcher.setObject(10, WrappedDataWatcher.Registry.get(Integer.class), 0); //10 is the dw_arrow_index (Arrow field index in a DataWatcher, 0 is arrow count
//                        hide_arrow_packet.getWatchableCollectionModifier().write(0, watcher.getWatchableObjects());
//                        try {
//                            protocolManager.sendServerPacket(online, hide_arrow_packet);
//                        } catch (InvocationTargetException e) {
//                            throw new RuntimeException(e);
//                        }
            }
        }
    }

    public static void null_passive_skill_disable(Player player) {  //add disable notification where number turn gold when switch hotbar, turn dark red when got hit
        for (Player online : Bukkit.getOnlinePlayers()) {
            if (online != player) {
                ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
                PacketContainer hide_armor_packet = protocolManager.createPacket(PacketType.Play.Server.ENTITY_EQUIPMENT);
                hide_armor_packet.getIntegers().write(0, player.getEntityId());
                List<Pair<EnumWrappers.ItemSlot, ItemStack>> list = new ArrayList<>();
                list.add(new Pair<>(EnumWrappers.ItemSlot.HEAD, player.getInventory().getHelmet()));
                list.add(new Pair<>(EnumWrappers.ItemSlot.CHEST, player.getInventory().getChestplate()));
                list.add(new Pair<>(EnumWrappers.ItemSlot.LEGS, player.getInventory().getLeggings()));
                list.add(new Pair<>(EnumWrappers.ItemSlot.FEET, player.getInventory().getBoots()));
                list.add(new Pair<>(EnumWrappers.ItemSlot.MAINHAND, player.getInventory().getItemInMainHand()));
                list.add(new Pair<>(EnumWrappers.ItemSlot.OFFHAND, player.getInventory().getItemInOffHand()));
                hide_armor_packet.getSlotStackPairLists().write(0, list);
//                //copy start
//                PacketContainer packet = hide_armor_packet;
////                List<Pair<EnumWrappers.ItemSlot, ItemStack>> list = new ArrayList<>();
////                list = packet.getSlotStackPairLists().read(0);
//                ItemStack stackget = null;
//                for (int i = 0; i < list.size(); i++) {
//                    Pair pair = list.get(i);
//                    stackget = (ItemStack) pair.getSecond();
//                    if (stackget == null) continue;
//                    if (stackget.equals(new ItemStack(Material.AIR))) continue;
//                    if (stackget.getItemMeta() == null) continue;
//                    ItemStack stack = stackget.clone();
//                    boolean not_armor = false;
//                    // Only modify leather armor
//
//                    if (stack != null && Objects.requireNonNull(stack.getItemMeta()).getEnchants().isEmpty()) { //&& stack.getType().name().contains("IRON")
//                        // The problem turned out to be that certain Minecraft functions update
//                        // every player with the same packet for an equipment, whereas other
//                        // methods update the equipment with a different packet per player.
//                        // To fix this, we'll simply clone the packet before we modify it
////                                event.setPacket(packet = packet.deepClone());
////                                stack = packet.getItemModifier().read(0);
//                        switch (stack.getType().name()) {
//                            case "IRON_HELMET":
//                                stack.setType(Material.LEATHER_HELMET);
//                                break;
//                            case "IRON_CHESTPLATE":
//                                stack.setType(Material.LEATHER_CHESTPLATE);
//                                break;
//                            case "IRON_LEGGINGS":
//                                stack.setType(Material.LEATHER_LEGGINGS);
//                                break;
//                            case "IRON_BOOTS":
//                                stack.setType(Material.LEATHER_BOOTS);
//                                break;
//                            default:
//                                not_armor = true;
//                                break;
//
//                        }
//                        if (not_armor) {
//                            break;
//                        }
//                        Player player1 = null;
//                        int entityID = packet.getIntegers().read(0);
//                        for (Player p : Bukkit.getOnlinePlayers()) {
//                            if (p.getEntityId() == entityID) {
//                                player1 = p;
//                                break;
//                            }
//                        }
////                                Player player = event.getPlayer();
////                            Entity entity = world.getEnti
//                        // Color that depends on the player's name
////                                String recieverName = event.getPlayer().getName();
////                            int color = recieverName.hashCode() & 0xFFFFFF;
//
//                        // Change the color
//                        if (player != null) {
//                            LeatherArmorMeta meta = (LeatherArmorMeta) stack.getItemMeta();
//                            meta.setColor(translateChatColorToColor(getPlayerTeamColor(player)));  //Color.fromBGR(color)
//                            stack.setItemMeta(meta);
//                            pair.setSecond(stack);
//                            list.set(i, pair);
//
//                        }
//                    }
//                }
//                packet.getSlotStackPairLists().write(0, list);
//                nullPassiveSkillDisable.add(player.getUniqueId());
////                BukkitScheduler scheduler = Bukkit.getScheduler();
////                scheduler.scheduleSyncDelayedTask(plugin, () -> {
////
////                } )
//                //copy end
                protocolManager.sendServerPacket(online, hide_armor_packet);
//                try {
//                    protocolManager.sendServerPacket(online, hide_armor_packet);
//                } catch (InvocationTargetException e) {
//                    throw new RuntimeException(e);
//                }
            }
        }
    }
//    static HashMap<UUID, BukkitTask> nullTelepathy_tasks = new HashMap<>();
    static BukkitTaskMap<UUID> nullTelepathy_tasks = new BukkitTaskMap<>();
    public static void null_passive_skill_2(Player player) {
        BukkitScheduler scheduler = Bukkit.getScheduler();
        BukkitTask task = scheduler.runTaskTimer(MegaWallsClasses.getPlugin(), () -> {
            if (player.getHealth() >= 16) {
                nullTelepathy_tasks.remove(player.getUniqueId());
            }
            else {
                player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 0, true, true));
                player.sendMessage("心灵感应回血");
            }
        }, 0, 100);
        nullTelepathy_tasks.put(player.getUniqueId(), task);
    }

    //shaman
    static HashMap<UUID, Integer> shaman_heroism = new HashMap<>();
    static HashMap<UUID, Integer> shaman_wolfpack = new HashMap<>();

    public static int shaman_passive_skill_1_increase(Player damager, Player player) {
        int amount = HashMapUtils.hashMapIncrease(damager.getUniqueId(), shaman_heroism);
        if (amount == 5) {
            shaman_heroism.remove(damager.getUniqueId());
            shaman_passive_skill_1(damager, player);
        }
        return amount;
    }

    static void shaman_passive_skill_1(Player damager, Player player) {
        if (damager != null) {
            damager.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 120, 1, true, true));
        }
        if (player != null) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 100, 0, true, true));
        }
        damager.sendMessage("对面5秒虚弱，冲啊！！！");
    }

    public static int shaman_passive_skill_2_increase(Player damager, Player player) {
        int amount = HashMapUtils.hashMapIncrease(player.getUniqueId(), shaman_wolfpack);
        if (amount == 6) {
            shaman_wolfpack.remove(player.getUniqueId());
            shaman_passive_skill_2(damager, player);
        }
        return amount;
    }

    static void shaman_passive_skill_2(Player damager, Player player) {
        World world = player.getWorld();
        Location location = player.getLocation();
        Wolf wolf = world.spawn(location, Wolf.class);
        wolf.setOwner(player);
        wolf.setMaxHealth(14);
        wolf.setHealth(14);
        wolf.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1, false, false));
        wolf.setCustomName(getPlayerTeam(player).getColor() + player.getName() + "'s Wolf");
        BukkitScheduler scheduler = Bukkit.getScheduler();
        scheduler.runTaskLater(MegaWallsClasses.getPlugin(), () -> {
            wolf.remove();
        }, 300);
        damager.sendMessage("汪汪队闯大祸");


    }


//    static void shaman_passive_skill_1(Player damager) {
//        damager.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 120, 1, true, true));
//    }


    //mole
    static HashMapInteger<UUID> mole_mineCount = new HashMapInteger<>(3);

    public static void mole_passive_skill_1(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 60, 1));
        player.addPotionEffect(new PotionEffect(PotionEffectType.HASTE, 60, 1));
        player.sendMessage("地表战神就是我");
    }

    //Elaina

    public static BukkitTaskMap<UUID> elainaFly = new BukkitTaskMap<>();
    public static HashMap<UUID, UUID> elaina_slime = new HashMap<>();
    public static void elaina_passive_skill_2(Player player) {
        World world = player.getWorld();
        Location location = player.getLocation();
        UUID playerUUID = player.getUniqueId();
        Slime slime = world.spawn(location, Slime.class);
        slime.setSize(1); //original 4
        slime.setPassenger(player);
        slime.setAggressive(false);
        slime.setWander(false);
        slime.setMaxHealth(0.1);
        slime.setHealth(0.1);
        slime.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, Integer.MAX_VALUE, 255, false, false));
        slime.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, Integer.MAX_VALUE, 255, false, false));
        slime.setGravity(false);
//        slime.setVisibleByDefault(false);
        slime.setFrictionState(TriState.FALSE);
        UUID slimeUUID = slime.getUniqueId();
//        slime.setMetadata("elainaSlimeOwner" , new FixedMetadataValue(MegaWallsClasses.getPlugin(), playerUUID));
//        slime.getMetadata("elainaSlimeOwner").getFirst();

        BukkitScheduler scheduler = Bukkit.getScheduler();
        BukkitTask task = new BukkitRunnable() {
            public void run() {
//                if (!player.isOnline() || slime.getPassengers().isEmpty() || slime.isDead()) {
//                    PassiveSkills.elaina_passive_skill_1_disable(player);
//                    slime.remove();
//                    cancel();
//                    return;
//                }

                slime.setVelocity(player.getLocation().getDirection().multiply(0.5));
            }
        }.runTaskTimer(MegaWallsClasses.plugin, 0, 1);
        elainaFly.put(playerUUID, task);
        elaina_slime.put(playerUUID, slimeUUID);
        scheduler.runTaskLater(MegaWallsClasses.getPlugin(), () -> {
            elaina_passive_skill_2_disable(player);
        }, 130);
    }

    public static void elaina_passive_skill_2_disable(Player player) {
        UUID playerUUID = player.getUniqueId();
        if (elainaFly.contains(playerUUID)) {
            elainaFly.remove(playerUUID);
            Slime slime1 = (Slime) Bukkit.getEntity(elaina_slime.get(playerUUID));
            if (slime1 != null) {
                slime1.remove();
            }
            player.setVelocity(player.getLocation().getDirection().multiply(0.5));
        }
    }

    public static void elaina_slime_death(Slime slime) { // see listener entity death

    }

    static HashMap<UUID, Integer> elaina_icicle = new HashMap<>();
    static HashMap<UUID, Integer> elaina_mode = new HashMap<>();
    static HashMap<UUID, UUID> elaina_enemy = new HashMap<>();
    static HashMap<UUID, Integer> elainaIcicle_degree = new HashMap<>();
    static HashMap<UUID, UUID> elainaIcicle_elaina = new HashMap<>();
    static int baseDegree = 0;
    static BukkitTaskMap<UUID> elainaShootTask = new BukkitTaskMap<>();
    static BukkitTaskMap<UUID> elainaIcicleAccumulateTask = new BukkitTaskMap<>();
    static BukkitTaskMap<UUID> elainaIcicleSpinTask = new BukkitTaskMap<>();
//    static BukkitTaskMap<UUID> icicleFly = new BukkitTaskMap<>();

    public static BukkitTask elaina_passive_skill_1_task(Player player) {
        UUID playerUUID  = player.getUniqueId();
        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                if (GamemodeUtils.isPlayerPlayable(player) && elaina_icicle.containsKey(playerUUID)) {
                    int mode = elaina_mode.get(playerUUID);
                    if (mode == 0 || mode == 2) {
                        UUID enemyUUID = elaina_enemy.get(playerUUID);
                        if (enemyUUID != null) {
                            Player enemy = Bukkit.getPlayer(enemyUUID);
                            if (enemy != null) {
                                Location enemyLocation = enemy.getLocation();
                                Location playerLocation = player.getLocation();
                                if (playerLocation.distance(enemyLocation) <= 13) {
                                    ArrayList<Player> enemies = new ArrayList<>();
                                    enemies.add(enemy);
                                    elaina_passive_skill_1_try(player, enemies);
                                }
                            }
                        }
                    }
                }
//                else if(mode == 3) {
//
//                }
            }
        }.runTaskTimer(MegaWallsClasses.getPlugin(), 0, 16);
        return task;
    }

    public static BukkitTask elaina_accumulate_icicle_task(Player player) {

        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                elaina_spawn_icicle(player);
            }
        }.runTaskTimer(MegaWallsClasses.getPlugin(), 120, 120);
        return task;
    }

    static void elaina_spawn_icicle(Player player) {
        UUID playerUUID = player.getUniqueId();
        World world = player.getWorld();
        Location eyeLocation = player.getEyeLocation();
        eyeLocation.setY(eyeLocation.getY() + 2); ///original 1
        if (elaina_icicle.containsKey(playerUUID)) {
            if (elaina_icicle.get(playerUUID) < 8) {
                int degree = HashMapUtils.hashMapIncrease(playerUUID, elaina_icicle, 1, 8) ;
                ItemDisplay itemDisplay = world.spawn(eyeLocation, ItemDisplay.class);
                itemDisplay.setItemStack(new ItemStack(Material.END_ROD));
                UUID itemDisplayUUID = itemDisplay.getUniqueId();
                elainaIcicle_degree.put(itemDisplayUUID, degree);
                elainaIcicle_elaina.put(itemDisplayUUID, playerUUID);
                icicleTeleport(player, itemDisplay);
            }
        }
        else {
            int degree = HashMapUtils.hashMapIncrease(playerUUID, elaina_icicle, 1, 8) ;
            ItemDisplay itemDisplay = world.spawn(eyeLocation, ItemDisplay.class);
            itemDisplay.setItemStack(new ItemStack(Material.END_ROD));
            UUID itemDisplayUUID = itemDisplay.getUniqueId();
            elainaIcicle_degree.put(itemDisplayUUID, degree);
            elainaIcicle_elaina.put(itemDisplayUUID, playerUUID);
            icicleTeleport(player, itemDisplay);
        }
    }

    public static BukkitTask elaina_icicleSpinTask(Player player) {
        UUID playerUUID = player.getUniqueId();
        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
//                World world = player.getWorld();
//                Location eyeLocation = player.getEyeLocation();
//                eyeLocation.setY(eyeLocation.getY() + 1);
//                HashMapUtils.hashMapIncrease(playerUUID, elaina_icicle, 1, 8);
//                ItemDisplay itemDisplay = world.spawn(eyeLocation, ItemDisplay.class);
//                itemDisplay.setItemStack(new ItemStack(Material.END_ROD));
//                icicleTeleport(player, itemDisplay);
//                for (elainaIcicle_degree.keySet().iterator().next() ;)
//                for (UUID itemDisplayUUID : elainaIcicle_degree.keySet()) {
//                    icicleTeleport(player, (ItemDisplay) Bukkit.getEntity(itemDisplayUUID));
//                }
                HashSet<UUID> itemDisplayUUIDs = new HashSet<>();
                if (elainaIcicle_elaina.containsValue(playerUUID)) {
                    itemDisplayUUIDs = HashMapUtils.getKeys(playerUUID, elainaIcicle_elaina);
                }
                for (UUID itemDisplayUUID : itemDisplayUUIDs) {
                    ItemDisplay itemDisplay = (ItemDisplay) Bukkit.getEntity(itemDisplayUUID);
                    icicleTeleport(player, itemDisplay);
                }
            }
        }.runTaskTimer(MegaWallsClasses.getPlugin(), 0, 1);// change to player per task //and key is player uuid value is icicle uuid
//        for (Player player : Bukkit.getOnlinePlayers()) {
//            if (ScoreboardsAndTeams.getScore(player, "class") == 15) {
//
////                return task;
//            }
//        }
        return task;
    }

    static void icicleTeleport(Player player, ItemDisplay itemDisplay) {
        World world = player.getWorld();
        Location eyeLocation = player.getEyeLocation();
        eyeLocation.setY(eyeLocation.getY() + 2);
        eyeLocation.setYaw(0);
        eyeLocation.setPitch(0);
        UUID itemDisplayUUID = itemDisplay.getUniqueId();
        int degree = (elainaIcicle_degree.get(itemDisplayUUID) - 1) * 45 + baseDegree;
        eyeLocation.setX(eyeLocation.getX() + Math.sin(Math.toRadians(degree)));
        eyeLocation.setZ(eyeLocation.getZ() + Math.cos(Math.toRadians(degree)));
        itemDisplay.teleport(eyeLocation);
    }

    public static void elaina_switch_mode(Player player) {
        UUID playerUUID = player.getUniqueId();
        elaina_enemy.remove(playerUUID);
        int mode = HashMapUtils.hashMapMenuIncrease(playerUUID, elaina_mode, 1, 3);
        if (mode == 0) {
            player.sendMessage("锁定");
        }
        else if (mode == 1) {
            player.sendMessage("存储");
        }
        else if (mode == 2) {
            player.sendMessage("守卫");
        }
    }

    public static void elaina_passive_skill_1_try(Player player, ArrayList<Player> enemies) {
        for (int i = 0; i < enemies.size(); i++) {
            Player enemy = enemies.get(i);
            if (!elaina_passive_skill_1(player, enemy)) {
                break;
            }
        }
    }

    public static boolean elaina_passive_skill_1(Player player, Player enemy) {
        Location first = null;
        boolean blocked = false;
        org.bukkit.util.Vector vector = PlayerUtils.getMiddleLocation(enemy).toVector().subtract(player.getEyeLocation().toVector()).normalize();
        vector = vector.multiply(0.1); // vector  step
        org.bukkit.util.Vector start = new Vector(player.getEyeLocation().getX(), player.getEyeLocation().getY(), player.getEyeLocation().getZ()); // start vector from origin to player
        Configuration configuration = plugin.getConfig();
        for (int i = 0; i <= 150; i++) {
            Location loc = new Location(player.getWorld(), start.getX(), start.getY(), start.getZ());
            if (isInBounds(start.getX(), start.getY(), start.getZ(), configuration.getInt("locations.loc_map_min.x"), configuration.getInt("locations.loc_map_min.y"), configuration.getInt("locations.loc_map_min.z"), configuration.getInt("locations.loc_map_max.x"), configuration.getInt("locations.loc_map_max.y"), configuration.getInt("locations.loc_map_max.z"))) {
//                    spawnParticles(loc);
//                if ((i %= 10) == 0) {
//                    spawnParticles(loc);
//                }
                Block block = loc.getBlock();
                BoundingBox blockBoundingBox = block.getBoundingBox();
                if (blockBoundingBox.contains(start)) {  //!block.getType().equals(Material.AIR)
                    first = loc;
//                    if (i != 0) {
//                        start.subtract(vector);
//                    }
////                else {
////                    Location pro_loc = loc.clone();
////                }
//                    Location pre_loc = new Location(player.getWorld(), start.getX(), start.getY(), start.getZ());
//                    World world = player.getWorld();
//                    Entity marker = world.spawnEntity(new Location(world, 0, 114514,0), EntityType.MARKER);
//                    marker_player.put(marker.getUniqueId(), player.getUniqueId());
//                    marker.remove();
//                    BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
//                    scheduler.scheduleSyncDelayedTask(MegaWallsClasses.getPlugin(), () -> {
//                        marker_player.remove(marker.getUniqueId());
//                    }, 2L);
//                    world.createExplosion(pre_loc, 1.5F, false, false, marker); // damage and kb
//                    world.createExplosion(pre_loc, 2F, false, true, player); // damage blocks
                    blocked = true;
                    return blocked;
//                    break;
                }
                if (enemy.getBoundingBox().contains(start)) {
                    elaina_passive_skill_1_activate(player, enemy);
                    return false;
                }
//                for (Player p : player.getWorld().getPlayers()) {
//                    if (!p.equals(player)) {
//                        if (p.getBoundingBox().contains(start)) {
////                        Bukkit.broadcastMessage(start.toString());
////                        Bukkit.broadcastMessage(p.getBoundingBox().toString());
//                            addHealth(p, -5);
//                            p.damage(0.000000000000000000000000000000000000000000000700649232162408535461864791644958065640135, player);
//                            p.setFireTicks(120);
//                            first = loc;
//                            break;
//                        }
//                    }
//                }
//                if (first != null) {
//                    break;
//                }
                start = start.add(vector);
            }
            else {
                break;
            }
        }
        return true;
    }

    public static void elaina_passive_skill_1_activate(Player player, Player enemy) {

        World world = player.getWorld();
        Location eyeLocation = player.getEyeLocation();
//        Location eyeLocation = player.getEyeLocation();
        Location enemyLocation = PlayerUtils.getMiddleLocation(enemy);
        Vector vector = PlayerUtils.getMiddleLocation(enemy).toVector().subtract(player.getEyeLocation().toVector()).normalize();
        ItemDisplay itemDisplay = world.spawn(eyeLocation, ItemDisplay.class);
        itemDisplay.setItemStack(new ItemStack(Material.END_ROD));
        Location itemDisplayLocation = eyeLocation.clone(); //itemDisplay.getLocation();
        float pitch = (float) -Math.toDegrees(Math.asin(vector.getY())) + 90;
        float yaw = (float) -Math.toDegrees(Math.atan2(vector.getX(), vector.getZ()));

        itemDisplayLocation.setPitch(pitch);  //itemDisplayLocation.getPitch() + 90
        itemDisplayLocation.setYaw(yaw);

        itemDisplay.teleport(itemDisplayLocation);
//        itemDisplay.setRotation();


        UUID playerUUID = player.getUniqueId();
        int degree = HashMapUtils.hashMapDecrease(playerUUID, elaina_icicle) + 1;
        HashSet<UUID> icicles = new HashSet<>();
        icicles = HashMapUtils.getKeys(playerUUID, elainaIcicle_elaina);
//        if (icicles.contains(playerUUID)) {}
        for (UUID icicleUUID: icicles) {
            int icicleDegree = elainaIcicle_degree.get(icicleUUID);
            if (degree == icicleDegree) {
                elainaIcicle_elaina.remove(icicleUUID);
                elainaIcicle_degree.remove(icicleUUID);

                elainaIcicleSpinTask.remove(icicleUUID);

                Bukkit.getEntity(icicleUUID).remove();
                break;
            }
        }
//        Bukkit.getEntity(HashMapUtils.removeFirstKey(degree, elainaIcicle_degree)).remove();
        Slime slime = world.spawn(eyeLocation, Slime.class);
        slime.setSize(1); //original 4
//            slime.setPassenger(player);
        slime.setAggressive(false);
        slime.setWander(false);
//            slime.setMaxHealth(0.1);
//            slime.setHealth(0.1);
//            slime.setInvulnerable(true);
        slime.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, Integer.MAX_VALUE, 255, false, false));
        slime.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, Integer.MAX_VALUE, 255, false, false));
        slime.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, Integer.MAX_VALUE, 255, false, false));
        slime.setGravity(false);
        slime.setVisibleByDefault(true);
        slime.setFrictionState(TriState.FALSE);
        UUID slimeUUID = slime.getUniqueId();
        Vector velocity = vector.multiply(1); //2
        slime.setVelocity(velocity);
        BukkitScheduler scheduler = Bukkit.getScheduler();
        BukkitTask task = new BukkitRunnable() {
            public void run() {
//                if (!player.isOnline() || slime.getPassengers().isEmpty() || slime.isDead()) {
//                    PassiveSkills.elaina_passive_skill_1_disable(player);
//                    slime.remove();
//                    cancel();
//                    return;
//                }
                boolean onPeople = false;
                Location slimeLocation = slime.getLocation();
                Location teleportLocation = slimeLocation.clone();
                teleportLocation.setYaw(yaw);
                teleportLocation.setPitch(pitch);
                itemDisplay.teleport(teleportLocation);
//                    itemDisplay.setVelocity(velocity);
                for (Player p : world.getPlayers()) {
                    if (GamemodeUtils.isPlayer2PlayableEnemy(player, p))
                    if (slime.getBoundingBox().overlaps(p.getBoundingBox())) {
                        p.damage(0.00001, slime);
                        slime.remove();
                        itemDisplay.remove();
                        addHealth(enemy, -2);
//                        tntPrimed.setMetadata("onPeople", new FixedMetadataValue(plugin, true));
                        onPeople = true;
//                        scheduler.scheduleSyncDelayedTask(plugin, () -> {
//                            BukkitTask task1 = tnt_explode_task.remove(tntPrimedUUID);
//                            if (task1 != null) {
//                                task1.cancel();
//                            }
//                        }, 2L);
                        cancel();
                    }
                }
                if (!onPeople) {
                    if (slime.getVelocity().getX() == 0 || slime.getVelocity().getY() == 0 || slime.getVelocity().getZ() == 0) {
//                        tntPrimed.setFuseTicks(0);
                        slime.remove();
                        itemDisplay.remove();
//                        scheduler.scheduleSyncDelayedTask(plugin, () -> {
////                            BukkitTask task1 = tnt_explode_task.remove(tntPrimedUUID);
////                            if (task1 != null) {
////                                task1.cancel();
////                            }
//                        }, 2L);
                        cancel();  // use full expression so that it can be canceled
                    }
                }
                slime.setVelocity(velocity);

            }
        }.runTaskTimer(MegaWallsClasses.plugin, 0, 1);
    }

    //squid
    public static void squid_passive_skill_2(Player player) {
        if (!Cooldown.player_passiveSkill2Cooldown.containsKey(player.getUniqueId())) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 30, 4, true, true));
            player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 300, 0, true, true));
            player.sendMessage("鱿鱼超强再生能力");
            Cooldown.player_passiveSkill2Cooldown.put(player.getUniqueId(), 40 * 20);
        }
    }

    //skeleton_lord
    public static void skeleton_lord_passive_skill_2(Player damager, Player player) {
        if (random(1,100) <= 12) {
            player.addPotionEffect((new PotionEffect(PotionEffectType.NAUSEA, 60, 0, false, false)));
            player.addPotionEffect((new PotionEffect(PotionEffectType.SLOWNESS, 100, 0, false, false)));
            player.addPotionEffect((new PotionEffect(PotionEffectType.WEAKNESS, 100 , 0, false, false)));
        }
    }
}
