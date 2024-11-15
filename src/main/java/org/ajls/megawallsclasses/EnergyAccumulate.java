package org.ajls.megawallsclasses;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.UUID;

import static org.ajls.megawallsclasses.MyListener.*;
import static org.ajls.megawallsclasses.PassiveSkills.*;
import static org.ajls.megawallsclasses.ScoreboardsAndTeams.getScore;
import static org.ajls.megawallsclasses.Utils.random;
import static org.bukkit.Bukkit.getServer;

public class EnergyAccumulate {
    static ArrayList<UUID> spiderAttacked = new ArrayList<>();
    //custom energy accumulate
    public static void addEnergy (Player player, int amount) {
        if (amount > 0) {
            MegaWallsClasses.addScore(player, "energy", amount);
//        levelEqualsEnergy(player);
            testSkillReady(player);
        }
        else if (amount < 0){
            BukkitTaskUtils.cancelTask(player, player_activeSkillReady);
            MegaWallsClasses.addScore(player, "energy", amount);
            if (GameManager.gameStage >= 0 && GameManager.gameStage <= 3) { // game stage >= 2 , 1 is preparation
                InitializeClass.initializeAutoEnergyAccumulation(player);
            }
            else if (GameManager.gameStage == 4) {
                InitializeClass.initializeDeathMatchAutoEnergyAccumulation(player);
            }
            levelEqualsEnergy(player);

        }
        else {
            // refresh
            if (getScore(player, "energy") < 100) {
                BukkitTaskUtils.cancelTask(player, player_activeSkillReady);
            }
            testSkillReady(player);
            if (GameManager.gameStage >= 0 && GameManager.gameStage <= 3) { // game stage >= 2 , 1 is preparation
                InitializeClass.initializeAutoEnergyAccumulation(player);
            }
            else if (GameManager.gameStage == 4) {
                InitializeClass.initializeDeathMatchAutoEnergyAccumulation(player);
            }
        }

    }
    //attack others
    public static void attackEnergyAccumulate(Player damager) {
        switch (MegaWallsClasses.getScore(damager, "class")) {
            case 1:
                MegaWallsClasses.addScore(damager, "energy", 12);
                break;
            case 2:
                MegaWallsClasses.addScore(damager, "energy", 25);
                break;
            case 4:
                MegaWallsClasses.addScore(damager, "energy", 20);
                break;
            case 5:
                MegaWallsClasses.addScore(damager, "energy", 15);
                break;
            case 6:
                MegaWallsClasses.addScore(damager, "energy", 17);
                break;
            case 7:
                MegaWallsClasses.addScore(damager, "energy", 4);
                break;
            case 8:
                MegaWallsClasses.addScore(damager, "energy", 15);
                break;
            case 9:
                MegaWallsClasses.addScore(damager, "energy", 10);
                break;
            case 10:
                MegaWallsClasses.addScore(damager, "energy", 12);
                break;
            case 11:
                UUID damagerUUID = damager.getUniqueId();
                if (!tasks.containsKey(damagerUUID) && getScore(damager, "energy") < 100) {
                    if (!spiderAttacked.contains(damagerUUID)) {
                        spiderAttacked.add(damagerUUID);
                    }
                    else if (spiderAttacked.contains(damagerUUID)) {
                        spiderAttacked.remove(damagerUUID);
                        autoEnergyAccumulation(damager, 10, 20);
                    }
                }
//                MegaWallsClasses.addScore(damager, "energy", 20);
                break;
            case 12:
                ScoreboardsAndTeams.addScore(damager, "energy", 10);
                break;
            case 13:
                ScoreboardsAndTeams.addScore(damager, "energy", 16);
                break;
            case 18:
                addEnergy(damager, 20);
            case 28:
                if (damager.getInventory().getItemInMainHand().getType() == Material.STICK) {
                    MegaWallsClasses.addScore(damager, "energy", 7);
                }
                break;
        }
        testSkillReady(damager);
    }

    //attacked by others
    public static void attackedEnergyAccumulate(Player player) {
        switch (MegaWallsClasses.getScore(player, "class")) {
            case 1:
                MegaWallsClasses.addScore(player, "energy", 1);
                break;
        }
        testSkillReady(player);
    }
    // shoot others
    public static void shootEnergyAccumulate(Player damager) {
        switch (MegaWallsClasses.getScore(damager, "class")) {
            case 1:
                MegaWallsClasses.addScore(damager, "energy", 15);
                break;
            case 2:
                MegaWallsClasses.addScore(damager, "energy", 25);
//                herobrine_passive_skill_1(damager);
                break;
            case 3:
                MegaWallsClasses.addScore(damager, "energy", 20);
                break;
            case 4:
                MegaWallsClasses.addScore(damager, "energy", 20);
                break;
            case 5:
                MegaWallsClasses.addScore(damager, "energy", 22);
                break;
            case 6:
                MegaWallsClasses.addScore(damager, "energy", 15);
                break;
            case 7:
                MegaWallsClasses.addScore(damager, "energy", 18);
                break;
            case 8:
                MegaWallsClasses.addScore(damager, "energy", 15);
                break;
            case 9:
                MegaWallsClasses.addScore(damager, "energy", 15);
                break;
            case 10:
                MegaWallsClasses.addScore(damager, "energy", 20);
                break;
            case 11:
                UUID damagerUUID = damager.getUniqueId();
                if (!tasks.containsKey(damagerUUID) && getScore(damager, "energy") < 100) {
                    if (!spiderAttacked.contains(damagerUUID)) {
                        spiderAttacked.add(damagerUUID);
                    }
                    else if (spiderAttacked.contains(damagerUUID)) {
                        spiderAttacked.remove(damagerUUID);
                        autoEnergyAccumulation(damager, 10, 20);
                    }
                }
//                MegaWallsClasses.addScore(damager, "energy", 20);
                break;
            case 12:
                ScoreboardsAndTeams.addScore(damager, "energy", 10);
                break;
            case 13:
                ScoreboardsAndTeams.addScore(damager, "energy", 15); //12 or 15
                break;
            case 18:
                addEnergy(damager, 20);
        }
        testSkillReady(damager);
    }

    //auto
    public static void autoEnergyAccumulation(Player player, int amount, int delay) {
        BukkitScheduler scheduler = getServer().getScheduler();
        if (!tasks.containsKey(player.getUniqueId())) {
            BukkitTask task = scheduler.runTaskTimer(MegaWallsClasses.getPlugin(), () -> {
                MegaWallsClasses.addScore(player, "energy", amount);
                testSkillReady(player);
            }, delay, delay);
            tasks.put(player.getUniqueId(), task);
        }
    }

    public static void disableAutoEnergyAccumulation(Player player) {
        BukkitTask task = tasks.remove(player.getUniqueId()); // remove from map if exist
        if(task != null) { // task found
            task.cancel();
        }
    }
}
