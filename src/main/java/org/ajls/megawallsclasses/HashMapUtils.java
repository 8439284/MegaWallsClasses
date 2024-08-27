package org.ajls.megawallsclasses;

import org.bukkit.block.Block;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;

public class HashMapUtils {
    public static int hashMapIncrease(UUID uuid, HashMap<UUID, Integer> hashMap) {
        if (hashMap.containsKey(uuid)) {
            int value = hashMap.get(uuid);
            value++;
            hashMap.put(uuid, value);
            return value;
        }
        else {
            hashMap.put(uuid, 1);
            return 1;
        }
    }
    public static int hashMapIncrease(Block block, HashMap<Block, Integer> hashMap) {
        if (hashMap.containsKey(block)) {
            int value = hashMap.get(block);
            value++;
            hashMap.put(block, value);
            return value;
        }
        else {
            hashMap.put(block, 1);
            return 1;
        }
    }

    public static <K> int hashMapIncrease(K key, HashMap<K, Integer> hashMap, int amount, int cap) {
        int value = 0;
        if (hashMap.containsKey(key)) {
            value = hashMap.get(key);
        }
        value += amount;
        if (value > cap) {
            value = cap;
        }
        hashMap.put(key, value);
        return value;

//        else {
//            hashMap.put(key, 1);
//            return 1;
//        }
    }

    public static <K> int hashMapMenuIncrease(K key, HashMap<K, Integer> hashMap, int amount, int cap) {
        int value = 0;
        if (hashMap.containsKey(key)) {
            value = hashMap.get(key);
        }
        value += amount;
        value %= (cap + 0);
        hashMap.put(key, value);
        return value;
    }

    public static <K> int hashMapDecrease(K key, HashMap<K, Integer> hashMap) {
        if (hashMap.containsKey(key)) {
            int value = hashMap.get(key);
            value--;
            if (value == 0) {
                hashMap.remove(key);
                return 0;
            }
            else {
                hashMap.put(key, value);
                return value;
            }

        }
        else {
            return 0;
        }
    }

    public static <K, V> K getFirstKey(V value, HashMap<K, V> hashMap) {
//        for (HashMap.Entry<K, V> entry : hashMap.entrySet()) {
//            if (entry.getValue() == value) {
//                return entry.getKey();
//            }
//        }
        for (K key : hashMap.keySet()) {
            if (value.equals(hashMap.get(key))) {
                return key;
            }
        }
//        return hashMap.get(va);
        return null;
    }

    public static <K, V> K removeFirstKey(V value, HashMap<K, V> hashMap) {
//        for (HashMap.Entry<K, V> entry : hashMap.entrySet()) {
//            if (entry.getValue() == value) {
//                return entry.getKey();
//            }
//        }
        for (K key : hashMap.keySet()) {
            if (value.equals(hashMap.get(key))) {
                hashMap.remove(key);
                return key;
            }
        }
//        return hashMap.get(va);
        return null;
    }

    public static <K, V> HashSet<K> getKeys(V value, HashMap<K, V> hashMap) {
//        for (HashMap.Entry<K, V> entry : hashMap.entrySet()) {
//            if (entry.getValue() == value) {
//                return entry.getKey();
//            }
//        }
        HashSet<K> keys = new HashSet<>();
        for (K key : hashMap.keySet()) {
            if (value.equals(hashMap.get(key))) {
                keys.add(key);
//                hashMap.remove(key);
            }
        }
        if (!keys.isEmpty()) {
            return keys;
        }
        else {
            return null;
        }
//        return hashMap.get(va);
//        return null;
    }

    public static <K, V> HashSet<K> removeKeys(V value, HashMap<K, V> hashMap) {
//        for (HashMap.Entry<K, V> entry : hashMap.entrySet()) {
//            if (entry.getValue() == value) {
//                return entry.getKey();
//            }
//        }
        HashSet<K> keys = new HashSet<>();
        for (K key : hashMap.keySet()) {
            if (value.equals(hashMap.get(key))) {
                keys.add(key);
                hashMap.remove(key);
            }
        }
        if (!keys.isEmpty()) {
            return keys;
        }
        else {
            return null;
        }
//        return hashMap.get(va);
//        return null;
    }
}
