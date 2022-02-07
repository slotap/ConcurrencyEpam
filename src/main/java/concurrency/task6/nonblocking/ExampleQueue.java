package concurrency.task6.nonblocking;

import java.util.Queue;
import java.util.Random;

public class ExampleQueue {
    Queue<String> queue = null;
    public boolean isEmpty;

    public ExampleQueue(Queue<String> queue) {
        this.queue = queue;
    }

    public synchronized void add() {
        Random random = new Random();
        this.queue.add("" + random.nextInt() + " " + "size: " + queue.size());
        isEmpty = false;
        notifyAll();
    }

    public synchronized void remove() {
        while (isEmpty) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }

        String message = this.queue.remove();

        if (queue.size() == 0) {
            isEmpty = true;
        }
    }

    public int size() {
        return queue.size();
    }
}
