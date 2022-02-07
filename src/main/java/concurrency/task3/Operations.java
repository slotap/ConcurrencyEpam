package concurrency.task3;

import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Operations {
    public static void main(String[] args) {
        ExampleQueue messages = new ExampleQueue(new LinkedList<>());

/*        Producer producer = new Producer(messages, "P1");
        Consumer consumer = new Consumer(messages, "P2");*/

/*        new Thread(producer).start();
        new Thread(consumer).start();*/


        ExecutorService executorService = Executors.newFixedThreadPool(10);

        executorService.execute(new Producer(messages, "P1"));
        executorService.execute(new Producer(messages, "P2"));
        executorService.execute(new Producer(messages, "P3"));
        executorService.execute(new Consumer(messages, "C1"));
        executorService.execute(new Consumer(messages, "C2"));
        executorService.execute(new Consumer(messages, "C3"));

        executorService.shutdown();
    }
}
