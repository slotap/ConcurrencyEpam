package concurrency.task2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CollectionsThread {
    volatile boolean isRun;

    public CollectionsThread(boolean isRun) {
        this.isRun = isRun;
    }

    /**
     * First thread adds elements to a collection , 100 elements at a time, then either sum or square methods are executed,
     * then writeToCollection is invoked again - the process is repeated infinitely
     */
    public static void main(String[] args) {
        List<Integer> listCollection = new ArrayList<>();
        CollectionsThread collectionsThread = new CollectionsThread(true);

        new Thread(new Runnable() {
            public void run() {
                collectionsThread.writeToCollection(listCollection);
            }
        }).start();

        new Thread(new Runnable() {
            public void run() {
                collectionsThread.sumAll(listCollection);
            }
        }).start();

        new Thread(new Runnable() {
            public void run() {
                collectionsThread.square(listCollection);
            }
        }).start();
    }

    void writeToCollection(List<Integer> list) {
        Random random = new Random();
        synchronized (this) {
            while (true) {
                System.out.println("Started adding to collection");
                while (!isRun) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                for (int i = 0; i < 100; i++) {
                    list.add(random.nextInt(9));
                }
                isRun = false;
                notifyAll();
            }
        }
    }

    void sumAll(List<Integer> list) {
        synchronized (this) {
            while (true) {
                while (isRun) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                int sum = list.stream()
                        .mapToInt(Integer::intValue)
                        .sum();
                System.out.println(sum);
                isRun = true;
                notifyAll();
            }
        }

    }

    void square(List<Integer> list) {
        synchronized (this) {
            while (true) {
                while (isRun) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                int sum = list.stream()
                        .mapToInt(Integer::intValue)
                        .map(number -> number * number)
                        .sum();
                System.out.println(Math.sqrt(sum));
                isRun = true;
                notifyAll();
            }
        }
    }
}
