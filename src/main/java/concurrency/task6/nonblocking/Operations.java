package concurrency.task6.nonblocking;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicReference;

public class Operations {
    public static void main(String[] args) throws InterruptedException {
        AtomicReference<Double> noOfOpsPerSecond = new AtomicReference<>((double) 0);

        Thread printingHook = new Thread(() -> {
            System.out.println("Number of operations/second : " + noOfOpsPerSecond);
        });
        Runtime.getRuntime().addShutdownHook(printingHook);

        ExampleQueue messages = new ExampleQueue(new LinkedList<>());

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
            producer.isRun.set(false);
            consumer.isRun.set(false);
            noOfOpsPerSecond.set((double) ((consumer.getNoOfOps().intValue() + producer.getProducerNoOps().intValue()) / 5));

        }).start();
    }
}
