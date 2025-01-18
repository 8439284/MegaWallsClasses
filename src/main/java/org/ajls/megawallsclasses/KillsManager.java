package org.ajls.megawallsclasses;

import org.ajls.lib.utils.ScoreboardU;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.*;

import static org.ajls.megawallsclasses.GameManager.witherDeadTeams;
import static org.ajls.megawallsclasses.GamemodeUtils.isPlayerPlayable;
import static org.ajls.megawallsclasses.GamemodeUtils.isPlayerUnplayable;
import static org.ajls.megawallsclasses.MegaWallsClasses.plugin;
import static org.ajls.megawallsclasses.ScoreboardsAndTeams.*;
import static org.ajls.megawallsclasses.SidebarManager.updateFinalAssists;
import static org.ajls.megawallsclasses.SidebarManager.updateFinalKills;
import static org.bukkit.Bukkit.getPlayer;
import static org.bukkit.Bukkit.getScheduler;

public class KillsManager {
//    public static HashMap<UUID, Integer> player_attackedTimes = new HashMap<>(); //check if the player haven't receive new attacks
    public static HashMap<UUID, Integer> player_deaths = new HashMap<>();// record player deaths
    public static HashMap<UUID, ArrayList<UUID>> player_damagers = new HashMap<>(); //record the damager of the player
    public static HashMap<UUID, Integer> player_finalKills = new HashMap<>();//record the final kills of a player
    public static HashMap<UUID, Integer> player_finalAssists = new HashMap<>();
    public static HashMap<UUID, Integer> player_kills = new HashMap<>();//record the final kills of a player
    public static HashMap<UUID, Integer> player_assists = new HashMap<>();
    public static ArrayList<UUID> finalDeathPlayers = new ArrayList<>(); //record the players that have died finally
    public static void registerPlayerHit(Player player, Player damager) {
        if (isPlayerPlayable(damager)) {
            ArrayList<UUID> damagers = new ArrayList<>();
            UUID playerUUID = player.getUniqueId();
            UUID damagerUUID = damager.getUniqueId();
            if (player_damagers.containsKey(player.getUniqueId())) {
                damagers = player_damagers.get(playerUUID);
            }
//            int newAttackedTime = 0;
//            if (player_attackedTimes.containsKey(playerUUID)) { //attacked before
//
//                newAttackedTime = player_attackedTimes.get(playerUUID) + 1;
//                player_attackedTimes.put(playerUUID, newAttackedTime);
//            }
//            else {//haven't attacked before
//                newAttackedTime = 1;
//                player_attackedTimes.put(player.getUniqueId(), 1);
//            }
            damagers.add(damagerUUID);
            player_damagers.put(playerUUID, damagers);
            int deaths;
            if (player_deaths.containsKey(playerUUID)) {
                deaths = player_deaths.get(playerUUID);
            } else {
                deaths = 0;
            }
//            player.sendMessage(damager.getName());
            BukkitScheduler bukkitScheduler = getScheduler();
//            int finalNewAttackedTime = newAttackedTime;
            bukkitScheduler.scheduleSyncDelayedTask(plugin, () -> {
//                if (player_attackedTimes.get(playerUUID).equals(finalNewAttackedTime)) {
//                player_damager.remove(playerUUID);
                int newDeaths = 0;
                if (player_deaths.containsKey(playerUUID)) {
                    newDeaths = player_deaths.get(playerUUID);
                }
                if (newDeaths == deaths) { //player haven't died during 15 s
                    ArrayList<UUID> latestDamagers = player_damagers.get(playerUUID);
                    latestDamagers.removeFirst();
                    player_damagers.put(playerUUID, latestDamagers);
//                    player.sendMessage("15s passed");
                }
//                }
            }, 300L);
        }
    }

    public static void registerPlayerDeath(Player player) {
        UUID playerUUID = player.getUniqueId();
        String teamName = ScoreboardU.getPlayerTeamName(player, true);
        if (!finalDeathPlayers.contains(playerUUID) && witherDeadTeams.contains(teamName)) {
            finalDeathPlayers.add(playerUUID);
            Inventory enderChest = player.getEnderChest();
            for (int i = 0; i < enderChest.getSize(); i++) {
                ItemStack item = enderChest.getItem(i);
                if (item != null) {
                    enderChest.clear(i);
                    player.getWorld().dropItemNaturally(player.getLocation(), item);
                }
            }
        }
        if (player_deaths.containsKey(playerUUID)) {
            player_deaths.put(playerUUID, player_deaths.get(playerUUID) + 1);
        }
        else {
            player_deaths.put(playerUUID, 1);
        }
        ArrayList<UUID> damagers = new ArrayList<>();
        if (player_damagers.containsKey(playerUUID)) {
            damagers = player_damagers.get(playerUUID);
        }
//        ArrayList<Integer> unplayableDamagersIndex = new ArrayList<>();
        for (int i = damagers.size() - 1; i >= 0; i--) {
            //check if damager playable, else remove
            UUID damagerUUID = damagers.get(i);
            Player damager = Bukkit.getPlayer(damagerUUID);
            if (finalDeathPlayers.contains(damagerUUID)) {
//                unplayableDamagersIndex.add()
                damagers.remove(i);
            }
        }
        if (damagers.size() > 0) {
            UUID damagerUUId = damagers.getLast(); // the last alive player to attack him
            Player damager = getPlayer(damagerUUId);
            //strength
            switch (ClassU.getClassEnum(damager)) {
                case HEROBRINE -> {
                    damager.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 120, 0, true, true));
                }
                case DREAD_LORD -> {
                    damager.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 140, 0, true, true));
                    damager.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 120, 0, true, true));
                }

            }
            //strength end
            if (witherDeadTeams.contains(teamName)) {
                addFinalKill(damager);
            }
            else {
                addKill(damager);
            }
            HashSet<UUID> damagersSet = new HashSet<>(damagers);
            damagersSet.remove(damagerUUId);  //damager don't count as assist
            if (damagersSet.size() > 0) {
                if (witherDeadTeams.contains(teamName)) {
                    for (UUID uuid : damagersSet) {
                        addFinalAssist(getPlayer(uuid));
                    }
                }
                else {
                    for (UUID uuid : damagersSet) {
                        addAssist(getPlayer(uuid));
                    }
                }
            }
//            ArrayList<UUID> nullDamagers = new ArrayList<>();
//            for (int i = 0 ; i < damagers.size() ; i++) {
//                nullDamagers.add(null);
//            }
            if (witherDeadTeams.contains(teamName)) {
                Bukkit.broadcastMessage(getPlayerTeamColor(player)+player.getDisplayName() + ChatColor.DARK_AQUA + " has been killed by " + getPlayerTeamColor(damager) + damager.getDisplayName() + ChatColor.RED + " FINAL KILL!");
            }
            else {
                Bukkit.broadcastMessage(getPlayerTeamColor(player)+player.getDisplayName() + ChatColor.DARK_AQUA + " has been killed by " + getPlayerTeamColor(damager) + damager.getDisplayName());
            }
        }
        else {
            if (witherDeadTeams.contains(teamName)) {
                Bukkit.broadcastMessage(getPlayerTeamColor(player)+player.getDisplayName() + ChatColor.DARK_AQUA + " has died " + ChatColor.RED + "FINAL DEATH!");
            }
            else {
                Bukkit.broadcastMessage(getPlayerTeamColor(player)+player.getDisplayName() + ChatColor.DARK_AQUA + " has died");
            }
        }


        player_damagers.remove(playerUUID);
    }

    static void addFinalKill(Player player) {
        UUID playerUUID = player.getUniqueId();
        if (player_finalKills.containsKey(playerUUID)) {
            player_finalKills.put(playerUUID, player_finalKills.get(playerUUID) + 1);
//            updateFinalKills(player);
        }
        else {
            player_finalKills.put(playerUUID, 1);
//            updateFinalKills(player);
        }
    }

    static void addFinalAssist(Player player) {
        UUID playerUUID = player.getUniqueId();
        if (player_finalAssists.containsKey(playerUUID)) {
            player_finalAssists.put(playerUUID, player_finalAssists.get(playerUUID) + 1);
//            updateFinalAssists(player);
        }
        else {
            player_finalAssists.put(playerUUID, 1);
//            updateFinalAssists(player);
        }
    }

    static void addKill(Player player) {
        UUID playerUUID = player.getUniqueId();
        if (player_kills.containsKey(playerUUID)) {
            player_kills.put(playerUUID, player_kills.get(playerUUID) + 1);
//            updateFinalKills(player);
        }
        else {
            player_kills.put(playerUUID, 1);
//            updateFinalKills(player);
        }
    }

    static void addAssist(Player player) {
        UUID playerUUID = player.getUniqueId();
        if (player_assists.containsKey(playerUUID)) {
            player_assists.put(playerUUID, player_assists.get(playerUUID) + 1);
//            updateFinalAssists(player);
        }
        else {
            player_assists.put(playerUUID, 1);
//            updateFinalAssists(player);
        }
    }


    public static void showEveryTeamFinalKill(Player player) {
        int blue_fk = 0;
        int red_fk = 0;
        for (UUID playerUUID : player_finalKills.keySet()) {
            Player player_final = getPlayer(playerUUID);
            if (isPlayerPlayable(player_final)) {
                String teamName = getPlayerTeamName(getPlayer(playerUUID));
                if (teamName.equals("blue_team")) {
                    blue_fk = blue_fk + player_finalKills.get(playerUUID);
                }
                else if (teamName.equals("red_team")) {
                    red_fk = red_fk + player_finalKills.get(playerUUID);
                }
            }
        }
        UUID playerUUID = player.getUniqueId();
        int your_fk = 0;
        if (player_finalKills.containsKey(playerUUID)) {
            your_fk = player_finalKills.get(playerUUID);
        }
        player.sendMessage("fk:");
        player.sendMessage(getPlayerTeamColor(player) + "You: " + your_fk);
        player.sendMessage(ChatColor.BLUE + "Blue: " + blue_fk);
        player.sendMessage(ChatColor.RED + "Red: " + red_fk);
    }
}
