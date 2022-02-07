package concurrency.task6.nonblocking;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Consumer implements Runnable {
    ExampleQueue queue;
    public AtomicBoolean isRun = new AtomicBoolean(true);
    public AtomicInteger consumerNoOps = new AtomicInteger(0);

    public Consumer(ExampleQueue queue) {
        this.queue = queue;
    }

    public AtomicInteger getNoOfOps() {
        return consumerNoOps;
    }

    @Override
    public void run() {
        while (isRun.get()) {
            queue.remove();
            consumerNoOps.getAndIncrement();
        }
    }
}
