package concurrency.task6.blocking;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Consumer implements Runnable {
    BlockingQueue<String> queue = null;
    public AtomicInteger consumerNoOps = new AtomicInteger(0);

    public Consumer(BlockingQueue<String> queue) {
        this.queue = queue;
    }

    public AtomicInteger getNoOfOps() {
        return consumerNoOps;
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {
                queue.take();
                consumerNoOps.getAndIncrement();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
