package concurrency.task6.nonblocking;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Producer implements Runnable {
    ExampleQueue queue;
    public AtomicBoolean isRun = new AtomicBoolean(true);
    public AtomicInteger producerNoOps = new AtomicInteger(0);

    public Producer(ExampleQueue queue) {
        this.queue = queue;
    }

    public AtomicInteger getProducerNoOps(){
        return producerNoOps;
    }

    @Override
    public void run() {
        while(isRun.get()) {
            queue.add();
            producerNoOps.getAndIncrement();
        }
    }
}
