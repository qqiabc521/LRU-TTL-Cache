package com.ljj.lrucache;

public class LRUCacheTest {

    public static void main(String[] args){
        LRUCache<Integer,String> lruCache = new LRUCache<Integer, String>(8);

//        LinkHashMapLRUCache<Integer,String> lruCache = new LinkHashMapLRUCache<>(8);

        for(int i=1;i<=10;i++){
            lruCache.put(i,String.valueOf(i));
        }

        println("初始化",lruCache);

        lruCache.get(4);

        println("get 4",lruCache);

        lruCache.remove(7);

        println("remove 7",lruCache);

        for(int i=11;i<=13;i++){
            lruCache.put(i,String.valueOf(i));
        }

        println("put 10-12",lruCache);
    }

    private static void println(String tag, LRUCache lruCache){
        System.out.println("================ "+tag+" =====================");
        System.out.println(lruCache.toString());
    }
}
