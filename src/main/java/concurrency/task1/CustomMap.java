package concurrency.task1;

import java.util.Iterator;

public class CustomMap<K, V> implements Iterable<Entry<K, V>> {
    private int capacity = 16;

    private Entry<K, V>[] table;

    public CustomMap() {
        table = new Entry[capacity];
    }

    public CustomMap(int capacity) {
        this.capacity = capacity;
        table = new Entry[capacity];
    }

    public void put(K key, V value) {
        int index = index(key);
        Entry newEntry = new Entry(key, value, null);
        if (table[index] == null) {
            table[index] = newEntry;
        } else {
            Entry<K, V> previousNode = null;
            Entry<K, V> currentNode = table[index];
            while (currentNode != null) {
                if (currentNode.getKey().equals(key)) {
                    currentNode.setValue(value);
                    break;
                }
                previousNode = currentNode;
                currentNode = currentNode.getNext();
            }
            if (previousNode != null)
                previousNode.setNext(newEntry);
        }
    }

    public V get(K key) {
        V value = null;
        int index = index(key);
        Entry<K, V> entry = table[index];
        while (entry != null) {
            if (entry.getKey().equals(key)) {
                value = entry.getValue();
                break;
            }
            entry = entry.getNext();
        }
        return value;
    }

    public void remove(K key) {

    }

    public int size() {
        int size = 0;
        for (Entry<K, V> entry : table) {
            size++;
        }
        return size;
    }


    private int index(K key) {
        if (key == null) {
            return 0;
        }
        return Math.abs(key.hashCode() % capacity);
    }

    @Override
    public Iterator<Entry<K, V>> iterator() {
        Iterator<Entry<K, V>> it = new Iterator<Entry<K, V>>() {

            private int currentIndex = 0;

            @Override
            public boolean hasNext() {
                return currentIndex < table.length && table[currentIndex] != null;
            }

            @Override
            public Entry<K, V> next() {
                return table[currentIndex++];
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
        return it;
    }
}
