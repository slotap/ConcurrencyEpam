package concurrency.task1;

public class CustomMapOperations {
    public static void main(String[] args) {
        CustomMap<Integer, Integer> customMap = new CustomMap<>(1_000_000);

        long start = System.currentTimeMillis();

        new Thread(new Runnable() {
            public void run() {
                addRandom(customMap);
            }
        }).start();
        System.out.println("First thread started");

        new Thread(new Runnable() {
            public void run() {
                sumAll(customMap);
            }
        }).start();
        System.out.println("Second thread started");
        System.out.println("Program done" + " Time: " + (System.currentTimeMillis() - start));
    }

    static void addRandom(CustomMap<Integer, Integer> map) {
        synchronized (map) {
            for (int i = 0; i < 1_000_000; i++) {
                map.put(i, i);
            }
        }
    }

    static void sumAll(CustomMap<Integer, Integer> map) {
        synchronized (map) {
            int sum = 0;
            for (Entry<Integer, Integer> entry : map) {
                sum += entry.getValue();
            }
        }
    }
}
