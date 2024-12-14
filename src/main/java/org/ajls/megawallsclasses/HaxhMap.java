package org.ajls.megawallsclasses;

import java.util.HashMap;
import java.util.HashSet;

public class HaxhMap<K, V> {
    HashMap<K, HashSet<V>> hashMap;
    public HaxhMap() {
        hashMap = new HashMap<>();
    }

    boolean put(K key, V value) {
        HashSet<V> hashSet = new HashSet<>();
        if (hashMap.containsKey(key)) {
            hashSet = hashMap.get(key);
        }
        boolean contains = hashSet.add(value);
        hashMap.put(key, hashSet);
        return contains;
    }

    HashSet<V> getValues(K key) {
        return hashMap.get(key);
    }

    HashSet<K> getKeys(V value) {
        HashSet<K> keys = new HashSet<>();
        for (K key : hashMap.keySet()) {
            HashSet<V> hashSet = hashMap.get(key);
            if (hashSet.contains(value)) {
                keys.add(key);
            }
        }
        if (keys.isEmpty()) {
            return null;
        }
        return keys;
    }

    HashSet<V> removeValues(K key) {
        return hashMap.remove(key);
    }

    HashSet<K> removeKeys(V value) {
        HashSet<K> keys = new HashSet<>();
        for (K key : hashMap.keySet()) {
            HashSet<V> hashSet = hashMap.get(key);
            if (hashSet.contains(value)) {
                keys.add(key);
                hashSet.remove(value);
                if (hashSet.isEmpty()) {
                    hashMap.remove(key);
                }
            }
        }
        if (keys.isEmpty()) {
            return null;
        }
        return keys;
    }
}
