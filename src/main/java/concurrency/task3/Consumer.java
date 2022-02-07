package concurrency.task3;

import java.util.concurrent.atomic.AtomicBoolean;

public class Consumer implements Runnable {
    ExampleQueue queue;
    public AtomicBoolean isRun = new AtomicBoolean(true);
    public String name;

    public Consumer(ExampleQueue queue, String name) {
        this.name = name;
        this.queue = queue;
    }

    @Override
    public void run() {
        while (isRun.get()) {
            queue.remove();
        }
    }
}
