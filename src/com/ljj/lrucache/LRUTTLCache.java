package com.ljj.lrucache;

import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class LRUTTLCache<K, V> implements ILRUCache<K, V> {
    private static long period = -1;

    private int capacity;
    private LinkedHashMap<K, TimeNode> map;

    public LRUTTLCache(int capacity, long period) {
        if(period <= 0){
            throw new RuntimeException("The period cannot be less than zero");
        }

        this.capacity = capacity;
        this.period = period;
        map = new LinkedHashMap<>(10, 0.75f, true);
    }

    public V get(K key) {
        if (map.containsKey(key)) {
            TimeNode node =  map.get(key);
            return node == null?null: node.value;
        }
        return null;
    }

    public void put(K key, V value) {
        if(map.containsKey(key)){
            TimeNode node = map.get(key);
            node.value = value;
            node.time = System.currentTimeMillis();
            System.out.println("产生碰撞："+key);
        }else{
            map.put(key, new TimeNode(value,System.currentTimeMillis()));
            System.out.println("新Add："+key);
        }


        Iterator<K> iterator;
        while (capacity < map.size()) {
            iterator = map.keySet().iterator();
            if(iterator.hasNext()) {
                K keyNext = iterator.next();
                System.out.println(keyNext+ "被移除，【容量已满】");
                iterator.remove();
            }
        }

        Iterator<Map.Entry<K,TimeNode>> entryIterator = map.entrySet().iterator();
        if(entryIterator.hasNext()){
            Map.Entry<K,TimeNode> entry = entryIterator.next();
            TimeNode node = entry.getValue();
            if(node != null && getExpirationTime(node.time) > period ){
                System.out.println(node.toString() + "被移除，【过期】");
                entryIterator.remove();
            }
        }
    }

    private long getExpirationTime(long time){
        return Math.abs(System.currentTimeMillis() - time);
    }

    public V remove(K key) {
        if (map.containsKey(key)) {
            TimeNode node =  map.remove(key);
            return node == null?null:node.value;
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
        Set<Map.Entry<K, TimeNode>> set = map.entrySet();
        Iterator<Map.Entry<K, TimeNode>> iterator = set.iterator();
        while (iterator.hasNext()) {
            Map.Entry<K,TimeNode> entry = iterator.next();
            sb.append(entry.getKey()).append(":").append(entry.getValue().toString()).append("; ");
        }

        sb.append("\n");

        return sb.toString();
    }

    private class TimeNode{
        private V value;
        private long time;

        public TimeNode(V value,long time){
            this.value = value;
            this.time = time;
        }

        @Override
        public String toString() {
            return value+" ["+getExpirationTime(time)+"]";
        }
    }

}
