package concurrency.task4;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Pool that block when it has not any items or it full
 */
public class BlockingObjectPool {
    private final ReentrantLock lock = new ReentrantLock();
    private int size;
    private final Queue<Object> pool;
    private final Condition isEmpty;
    private final Condition isFull;

    /**
     * Creates filled pool of passed size
     * * * @param size of pool
     */
    public BlockingObjectPool(int size) {
        this.size = size;
        pool = new ArrayDeque(size);
        this.lock.unlock();
        isEmpty = lock.newCondition();
        isFull = lock.newCondition();
    }

    /**
     * Gets object from pool or blocks if pool is empty
     * * * @return object from pool
     */
    public Object get() throws InterruptedException {
        lock.lock();
        while (pool.size() == 0)
            isEmpty.await();

        Object object = pool.poll();
        isFull.signal();
        lock.unlock();

        return object;
    }

    /**
     * Puts object to pool or blocks if pool is full
     * * * @param object to be taken back to pool
     */
    public void take(Object object) throws InterruptedException {
        lock.lock();
        while (size <= pool.size())
            isFull.await();

        pool.offer(object);
        isEmpty.signal();
        lock.unlock();
    }
}
