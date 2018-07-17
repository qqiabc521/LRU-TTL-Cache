package com.ljj.lrucache;

import java.util.*;

public class LRULinkCache<K, V> implements ILRUCache<K, V> {

    private int capacity;
    private LinkedHashMap<K, V> map;

    public LRULinkCache(int capacity) {
        this.capacity = capacity;
        map = new LinkedHashMap<>(10, 0.75f, true);
    }

    public V get(K key) {
        if (map.containsKey(key)) {
            return map.get(key);
        }
        return null;
    }

    public void put(K key, V value) {
        map.put(key, value);

        Iterator<K> iterator;
        while (capacity < map.size()) {
            iterator = map.keySet().iterator();
            if(iterator.hasNext()) {
                iterator.next();
                iterator.remove();
            }
        }
    }

    public V remove(K key) {
        if (map.containsKey(key)) {
            return map.remove(key);
        }
        return null;
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("map:").append("\n");
        Set<Map.Entry<K, V>> set = map.entrySet();
        Iterator<Map.Entry<K, V>> iterator = set.iterator();
        while (iterator.hasNext()) {
            Map.Entry<K,V> entry = iterator.next();
            sb.append(entry.getKey()).append(":").append(entry.getValue()).append("; ");
        }

        sb.append("\n");

        return sb.toString();
    }

}
