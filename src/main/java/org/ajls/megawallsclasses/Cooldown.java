package org.ajls.megawallsclasses;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.ajls.lib.advanced.BukkitTaskMap;
import org.ajls.lib.advanced.HashMapInteger;
import org.ajls.lib.utils.ItemStackU;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import java.text.DecimalFormat;
import java.util.*;

import static org.ajls.megawallsclasses.MyListener.n5ll_invisibility;
import static org.ajls.megawallsclasses.MyListener.warden_sensorAmount;
import static org.bukkit.ChatColor.*;

public class Cooldown {
    public static HashMap<UUID, Integer> player_passiveSkill1Cooldown = new HashMap<>();
    public static HashMap<UUID, Integer> player_passiveSkill2Cooldown = new HashMap<>();
    public static ArrayList<HashMap<UUID, Integer>> playerCooldowns = new ArrayList<>();//Arrays.asList(player_passiveSkill1Cooldown, player_passiveSkill2Cooldown) // {{
//        add(player_passiveSkill1Cooldown);
//        add(player_passiveSkill2Cooldown);
//    }};

    public static BukkitTaskMap<UUID> player_cooldownTask = new BukkitTaskMap<>();
    public static boolean isNotInCooldown(int time, int cooldown) {
        return (time - org.ajls.megawallsclasses.MegaWallsClasses.time) >= cooldown;
    }

    public static void registerCooldowns() {
        BukkitScheduler scheduler = MegaWallsClasses.getPlugin().getServer().getScheduler();
        scheduler.runTaskLater(MegaWallsClasses.getPlugin(), () -> {
            Cooldown.playerCooldowns.add(Cooldown.player_passiveSkill1Cooldown);
            Cooldown.playerCooldowns.add(Cooldown.player_passiveSkill2Cooldown);
        }, 1);
    }

    public static void removeCooldown(HashMap<UUID, Integer> hashMap, int amount, int index) {
//        registerCooldowns();
//        if (!playerCooldowns.contains(player_passiveSkill1Cooldown)) {
//            registerCooldowns();
//        }

//        for (HashMap<UUID, Integer> skillCooldown : playerCooldowns) {
//
//        }
        for (UUID uuid : hashMap.keySet()) {

            int cooldown = hashMap.get(uuid);
//            if (classIndex == 5) {
//
//            }
            cooldown = cooldown - amount;
            if (cooldown <= 0) {
                hashMap.remove(uuid);
                Player player = Bukkit.getPlayer(uuid);
                if (player != null) {
                    int classIndex = ClassU.getClass(player);
                    if (index == 0) {  //passive1
                        switch (classIndex) {
                            case 5: //null
                                int null_mode = PassiveSkills.null_invisibility_mode.get(uuid);
                                PassiveSkills.null_invisibility_mode.pageUp(uuid); //switch mode
                                if (null_mode == 0) {
                                    PassiveSkills.null_passive_skill_1(player);
                                    n5ll_invisibility.decrement(uuid);
                                    hashMap.put(uuid, 70);
                                }
                                else if (null_mode == 1) {
                                    PassiveSkills.null_passive_skill_disable(player);
                                    if (MyListener.n5ll_invisibility.get(uuid) == 0) {
//                                        hashMap.remove(uuid); //dont need because default is remove
                                    }
                                    else {
                                        hashMap.put(uuid, 130);
                                    }
                                }
//                                if (player.getHealth() < 16) {
//                                    PassiveSkills.null_passive_skill_1(player);
//                                }
                                break;
                        }
                    }
                    else if (index == 1) { //passive 2
                        switch (classIndex) {
//                            case 5: //null
//                                if (PassiveSkills.null_invisibility_mode.get(uuid) == 0) {
//
//                                }
//                                if (player.getHealth() < 16) {
//                                    PassiveSkills.null_passive_skill_1(player);
//                                }
//                                break;
                            case 18:  //squid
                                if (player.getHealth() < 18) {
                                    PassiveSkills.squid_passive_skill_2(player);
                                }
                                break;
                            case 30:
                                if (MyListener.warden_sensorAmount.get(uuid) <= 1) {
                                    warden_sensorAmount.increment(uuid);
//                                    player.sendMessage(ChatColor.GREEN + "你获得了一个感应器");
                                    boolean found = false;
                                    for (int i = 0; i < player.getInventory().getSize(); i++) {
                                        ItemStack playerItem = player.getInventory().getItem(i);
                                        if (playerItem != null) {
                                            String itemType = ItemStackU.getStringPersistentData(playerItem, NameSpacedKeys.ITEM_TYPE);
                                            if (itemType != null){
                                                if (itemType.equals("warden_sensor")) {
                                                    if (!found) {
                                                        found = true;
//                                                        playerItem.setType(Material.SCULK_SENSOR);
//                                                        playerItem.setAmount(MyListener.warden_sensorAmount.get(uuid));
                                                        ItemStack item = new ItemStack(Material.SCULK_SENSOR);
                                                        ItemStackU.setStringPersistentData(item, NameSpacedKeys.ITEM_TYPE, "warden_sensor");
                                                        item.setAmount(MyListener.warden_sensorAmount.get(uuid));
                                                        player.getInventory().setItem(i, item);

                                                    }
                                                    else {
                                                        playerItem.setAmount(0);// = new ItemStack(Material.AIR);
                                                    }
                                                }
                                            }

                                        }
                                    }
                                    if (!found) {
                                        ItemStack item = new ItemStack(Material.SCULK_SENSOR);
                                        ItemStackU.setStringPersistentData(item, NameSpacedKeys.ITEM_TYPE, "warden_sensor");
                                        item.setAmount(MyListener.warden_sensorAmount.get(uuid));
                                        player.getInventory().addItem(item);
                                    }
                                }
                                if (warden_sensorAmount.get(uuid) < 2) {
                                    hashMap.put(uuid, 20*30);
                                }
//                                else {
//                                    MyListener.warden_sensorAmount.decrement(uuid);
//                                    hashMap.put(uuid, 20);
//                                }

                        }
                    }
                }
            }
            else {
                hashMap.put(uuid, cooldown);
            }
        }
    }

    public static void displayCooldown(Player player) {
        BukkitScheduler scheduler = Bukkit.getScheduler();
        UUID uuid = player.getUniqueId();
        String p1c = "";//passive skill 1 class prefix
        String p2c = "";
        HashSet<HashMap<UUID, Integer>> class_cooldowns = new HashSet<>();
        switch (ScoreboardsAndTeams.getScore(player, "class")) {
            case 5:
                p1c = GRAY + "间隐";
                break;
            case 9:
                p1c = DARK_GRAY + "凋零箭矢";
//                class_cooldowns.add(player_passiveSkill1Cooldown);
                break;
            case 12:
                p1c = ChatColor.GOLD + "英勇";
                p2c = ChatColor.GRAY + " 汪汪队";
                break;
            case 13:
                p1c = ChatColor.WHITE + "暴风雪";
                break;
            case 14:
                p1c = ChatColor.GREEN + "地表领域";
                break;
            case 15:
                p2c = ChatColor.BLUE + "魔女之帚";
                break;
            case 18:
                p2c = ChatColor.DARK_RED + "鱿鱼复生";
                break;
            case 30:
                p2c = ChatColor.DARK_RED + "ヘブンバーンズレッド";
                break;
        }
        String finalP1c = p1c;
        String finalP2c = p2c;
        BukkitTask task = scheduler.runTaskTimer(MegaWallsClasses.plugin, () -> {
            HashSet<UUID> isHoldingTrackerCompass = org.ajls.tractorcompass.MyListener.getIsHoldingTrackerCompass();
            if (!isHoldingTrackerCompass.contains(player.getUniqueId())) {
//                        String p1n = (number1 > 0) ? "positive" : (number1 < 0) ? "negative" : "zero";
                String p11 = "";
                String p21 = "";
                switch (ScoreboardsAndTeams.getScore(player, "class")) {
                    case 5:
                        int null_mode = PassiveSkills.null_invisibility_mode.get(uuid);
                        ChatColor defaultColor = RED;
                        if (null_mode == 1) {
                            defaultColor = GREEN;
                        }
                        p11 = getCooldownTime(player, player_passiveSkill1Cooldown, false, defaultColor)+ChatColor.DARK_GRAY+"/"+ChatColor.AQUA+ n5ll_invisibility.get(player.getUniqueId());
                        break;
                    case 9:
                        p11 = getCooldownTime(player, player_passiveSkill1Cooldown);// passive skill 1 first word
//                        p11 = DARK_GRAY + "凋零箭矢 ";
//                class_cooldowns.add(player_passiveSkill1Cooldown);
                        break;
                    case 12:
                        p11 = getCooldownAmount(player, PassiveSkills.shaman_heroism, 5);
                        p21 = getCooldownAmount(player, PassiveSkills.shaman_wolfpack, 6);
                        break;
                    case 13:
                        p11 = getCooldownTime(player, player_passiveSkill1Cooldown);
                        break;
                    case 14:
                        p11 = getCooldownAmount(player, PassiveSkills.mole_mineCount);
                        break;
                    case 15:
                        p21 = getCooldownTime(player, player_passiveSkill2Cooldown);
                        break;
                    case 18:
                        p21 = getCooldownTime(player, player_passiveSkill2Cooldown);
                        break;
                    case 30:
                        p21 = getCooldownTime(player, player_passiveSkill2Cooldown);
                }
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacy(finalP1c + p11 + finalP2c + p21));
            }
        },0 , 1);
        player_cooldownTask.put(uuid, task);
    }

    public static void notDisplayCooldown(Player player) {
        UUID playerUUID = player.getUniqueId();
        player_cooldownTask.remove(playerUUID);
    }

    public static void disableDisplayCooldown(Player player) {
        BukkitTask task = player_cooldownTask.remove(player.getUniqueId());
        if (task != null) {
            task.cancel();
        }
    }

    public static String getCooldownTime(Player player, HashMap<UUID, Integer> player_cooldown, boolean tick_or_cross, ChatColor defaultColor) {
        UUID uuid = player.getUniqueId();
        String p1n;
        if (tick_or_cross) {
            p1n = ChatColor.GREEN + " ✔";
        }
        else {
            p1n = ChatColor.RED + " ✘";
        }
//        String
        if (player_cooldown.containsKey(uuid)) {
            int cooldown = player_cooldown.get(uuid);
            double cooldownShow = ((double) cooldown / 20);
            if (cooldownShow >= 1) {
                cooldownShow = (int) Math.ceil(cooldownShow);
                DecimalFormat format = new DecimalFormat("0.#");
                p1n = " " + defaultColor + format.format(cooldownShow) + "s";
            }
            else {
                cooldownShow = (Math.ceil(cooldownShow * 10) / 10);
                p1n = " " + ChatColor.YELLOW + cooldownShow + "s";
            }
        }
        return p1n;
    }

    public static String getCooldownTime(Player player, HashMap<UUID, Integer> player_cooldown) {
        return getCooldownTime(player, player_cooldown, true, RED);
    }

//    public static String getCooldownTime(Player player, HashMap<UUID, Integer> player_cooldown) {
//        return getCooldownTime(player, player_cooldown, true, RED);
//    }

    public static String getCooldownAmount(Player player, HashMap<UUID, Integer> player_cooldown, int cap) {
        UUID uuid = player.getUniqueId();
        String p1n = ChatColor.GREEN + " ✔";
        int amount = 0;
        if (player_cooldown.containsKey(uuid)) {
            amount = player_cooldown.get(uuid);
        }
        if (amount != cap - 1) {
            p1n = " " + ChatColor.GRAY + String.valueOf(amount) + ChatColor.DARK_GRAY + "/" + ChatColor.GREEN + cap;
        }
        return p1n;
    }

    public static String getCooldownAmount(Player player, HashMapInteger<UUID> player_cooldown) {
        return getCooldownAmount(player, player_cooldown, player_cooldown.getUpperBound() + 1);
    }
}
