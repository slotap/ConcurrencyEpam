package concurrency.task6.blocking;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Producer implements Runnable {

    BlockingQueue<String> queue = null;
    public AtomicInteger producerNoOps = new AtomicInteger(0);

    public Producer(BlockingQueue<String> queue) {
        this.queue = queue;
    }

    public AtomicInteger getProducerNoOps() {
        return producerNoOps;
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            Random random = new Random();
            try {
                this.queue.put("" + random.nextInt() + " " + "size: " + queue.size());
                producerNoOps.getAndIncrement();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
