package org.ajls.megawallsclasses;

import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;

public class BukkitTaskMap<K> {
    HashMap<K, BukkitTask> tasks;
    public BukkitTaskMap() {
        tasks = new HashMap<>();
    }

    BukkitTask put(K key, BukkitTask task, boolean override) {
        if (tasks.containsKey(key)) {
            BukkitTask removedTask = tasks.get(key);
            if (override) {
                if (removedTask != null) {
                    removedTask.cancel();
                }
                tasks.put(key, task);
            }
            return removedTask;
        }
        else {
            tasks.put(key, task);
            return null;
        }
    }

    BukkitTask put(K key, BukkitTask task) {
        return put(key, task, true);
    }

    BukkitTask remove(K key) {
        BukkitTask removedTask = tasks.remove(key);
        if (removedTask != null) {
            removedTask.cancel();
        }
        return removedTask;
    }

    boolean contains(K key) {
        return tasks.containsKey(key);
    }

}
