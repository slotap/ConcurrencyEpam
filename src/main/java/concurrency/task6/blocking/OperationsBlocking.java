package concurrency.task6.blocking;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicReference;

public class OperationsBlocking {
    public static void main(String[] args) {
        AtomicReference<Double> noOfOpsPerSecond = new AtomicReference<>((double) 0);

        Thread printingHook = new Thread(() -> {
            System.out.println("Number of operations/second : " + noOfOpsPerSecond);
        });

        Runtime.getRuntime().addShutdownHook(printingHook);

        BlockingQueue<String> messages = new ArrayBlockingQueue<String>(100);

        Producer producer = new Producer(messages);
        Consumer consumer = new Consumer(messages);

        Thread t1 = new Thread(producer);
        Thread t2 = new Thread(consumer);

        t1.start();
        t2.start();

        new Thread(() -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            t1.interrupt();
            t2.interrupt();
            noOfOpsPerSecond.set((double) ((consumer.getNoOfOps().intValue() + producer.getProducerNoOps().intValue()) / 5));
        }).start();
    }
}
