package org.ajls.megawallsclasses.advanced;

import org.ajls.lib.advanced.BukkitTaskMap;
import org.ajls.lib.advanced.HashMapInteger;
import org.ajls.megawallsclasses.EnergyAccumulate;
import org.ajls.megawallsclasses.container.SpiderEnergyD;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class SpiderEnergy {
    public static HashMapInteger<UUID> player_spiderEnergyTimes = new HashMapInteger<>();
//    HashMapInteger<UUID> player_activeTimes = new HashMapInteger<>();
//    public static HashMap<UUID, SpiderEnergyD> player_spiderEnergyData = new HashMap<>();
//    public static BukkitTaskMap<UUID> energyTask = new BukkitTaskMap<>() {};
//


//    public void put(Player player, int activeTimes, int amount, int delay) {
////        player_activeTimes.put(player.getUniqueId(), activeTimes);
////        player_times.put(player.getUniqueId(), 0);
//        SpiderEnergyD container = new SpiderEnergyD(activeTimes, amount, delay);
//        player_spiderEnergyData.put(player.getUniqueId(), container);
//
//    }
//    public boolean increment(Player player) {
////        if (player_times.containsKey(player.getUniqueId())) {
////            if (player_times.increment(player.getUniqueId()) + 1 >= player_activeTimes.get(player.getUniqueId())) {
////                player_times.put(player.getUniqueId(), 0);
////                return true;
////            }
////        }
////        return false;
//        boolean incremented = player_spiderEnergyData.get(player.getUniqueId()).increment();
//        if (incremented) {
//
//        }
//    }

    public static void increment(Player player, int activeTimes, int amount, int delay) {
        if (EnergyAccumulate.energyTask.contains(player.getUniqueId())) {
            return;
        }
        if (EnergyAccumulate.getEnergy(player) >= 100) {
            return; // Already at max energy
        }
        int times = player_spiderEnergyTimes.increment(player.getUniqueId()) + 1;
        if (times >= activeTimes) {
            player_spiderEnergyTimes.put(player.getUniqueId(), 0);
            EnergyAccumulate.autoEnergyAccumulation(player, amount, delay, false);
        }
    }
}
