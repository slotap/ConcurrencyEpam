package concurrency.task3;

import java.util.concurrent.atomic.AtomicBoolean;

public class Producer implements Runnable {
    ExampleQueue queue;
    public AtomicBoolean isRun = new AtomicBoolean(true);
    public String name;

    public Producer(ExampleQueue queue, String name) {
        this.name = name;
        this.queue = queue;
    }

    public String getName() {
        return name;
    }

    @Override
    public void run() {
        while (isRun.get()) queue.add();
    }
}
