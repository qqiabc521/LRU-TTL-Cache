package com.ljj.lrucache;

public interface ILRUCache<K,V> {

    V get(K key);

    void put(K key,V value);

    V remove(K key);

    int size();

}
