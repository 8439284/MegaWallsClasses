package org.ajls.megawallsclasses;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

public class BukkitTaskUtils {
    public static void cancelTask(UUID uuid, HashMap<UUID, BukkitTask> hashMap) {
        BukkitTask task = hashMap.remove(uuid);
        if (task != null) {
            task.cancel();
        }
    }

    public static void cancelTask(Entity entity, HashMap<UUID, BukkitTask> hashMap) {
        BukkitTask task = hashMap.remove(entity.getUniqueId());
        if (task != null) {
            task.cancel();
        }
//        if (entity instanceof Player) {
//            Player player = (Player) entity;
//            player.sendMessage("canceled");
//        }
    }

    public static void cancelTask(Player player, String metaData) {
        BukkitTask task = (BukkitTask) player.getMetadata(metaData).getFirst();
        if (task != null) {
            task.cancel();
        }
    }

    public static void cancelTask(UUID uuid, HashSet<HashMap<UUID, BukkitTask>> tasks) {
        for (HashMap<UUID, BukkitTask> task : tasks) {
            cancelTask(uuid, task);
        }
    }

    public static void cancelTask(Entity entity, HashSet<HashMap<UUID, BukkitTask>> tasks) {
        UUID uuid = entity.getUniqueId();
        cancelTask(uuid, tasks);
    }
}
