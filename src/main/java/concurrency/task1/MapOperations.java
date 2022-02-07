package concurrency.task1;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class MapOperations {
    public static void main(String[] args) {
        Map<Integer, Integer> testMap = new ConcurrentHashMap<>();

        long start = System.currentTimeMillis();

        new Thread(new Runnable() {
            public void run() {
                addRandom(testMap);
            }
        }).start();
        System.out.println("First thread started");

        new Thread(new Runnable() {
            public void run() {
                sumAll(testMap);
            }
        }).start();
        System.out.println("Second thread started");

        System.out.println("Program done" + " Time: " + (System.currentTimeMillis() - start));
    }

    static void addRandom(Map<Integer, Integer> map) {
        Random random = new Random();
        for (int i = 0; i < 10_000_000; i++) {
            map.put(i, random.nextInt(9));
        }
    }

    static void sumAll(Map<Integer, Integer> map) {
        int sum = 0;
        for (int value : map.values()) {
            sum += value;
        }
    }
}
