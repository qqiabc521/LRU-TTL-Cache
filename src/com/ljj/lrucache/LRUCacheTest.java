package com.ljj.lrucache;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class LRUCacheTest {

    private static CountDownLatch main = new CountDownLatch(1);

    public static void main(String[] args) {
//        ILRUCache<Integer,String> lruCache = new LRUCache<>(10);
//        ILRUCache<Integer,String> lruCache = new LRULinkCache<>(10);
        ILRUCache<Integer, String> lruCache = new LRUTTLCache<>(10, 10 * 1000L);

        for (int i = 1; i <= 5; i++) {
            lruCache.put(i, String.valueOf(i));
        }

        ExecutorService executorService = Executors.newFixedThreadPool(3);
        executorService.execute(new AddTask(lruCache));
        executorService.execute(new GetTask(lruCache));

        try {
            main.await(10L,TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            executorService.shutdownNow();
        }
    }

    private static class AddTask implements Runnable{

        private ILRUCache<Integer, String> lruCache;
        private Random random = new Random();

        public AddTask(ILRUCache<Integer, String> lruCache ){
            this.lruCache = lruCache;
        }

        @Override
        public void run() {
            while (true){
                try {
                    int time = random.nextInt(2000);
                    Thread.sleep(time);
                    int temp = time % 10;
                    lruCache.put(temp,String.valueOf(temp));

                    println("AddTask key="+temp+" size="+ lruCache.size(), lruCache);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static class GetTask implements Runnable{

        private ILRUCache<Integer, String> lruCache;
        private Random random = new Random();

        public GetTask(ILRUCache<Integer, String> lruCache ){
            this.lruCache = lruCache;
        }

        @Override
        public void run() {
            while (true){
                try {
                    int time = random.nextInt(10000);
                    Thread.sleep(time);
                    int temp = time % 10;
                    lruCache.get(temp);
                    println("GetTask key="+ temp +" size="+lruCache.size(), lruCache);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void println(String tag, ILRUCache lruCache) {
        System.out.println("================ " + tag + " =====================");
        System.out.println(lruCache.toString());
    }
}
