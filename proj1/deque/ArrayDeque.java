package deque;

import java.util.Iterator;

public class ArrayDeque<T> implements Iterable<T>, Deque<T> {
    private int size;
    private int head;
    private int tail;
    private T[] items;
    private int FACTOR = 2;

    public ArrayDeque() {
        size = 0;
        items = (T[]) new Object[8];
        head = 0;
        tail = 0;
    }

    public void addFirst(T i) {
        if (size == items.length) {
            resize(items.length * FACTOR);
        }
        if (size > 0) {
            head = (head - 1 + items.length) % items.length;
        }
        items[head] = i;
        size += 1;

    }

    public void addLast(T i) {
        if (size == items.length) {
            resize(items.length * FACTOR);
        }
        if (size > 0) {
            tail = (tail + 1) % items.length;
        }
        items[tail] = i;
        size += 1;
    }

    public int size() {
        return size;
    }

    public void printDeque() {
        for (int i = 0; i < size; i++) {
            System.out.print(get(i) + " ");
        }
        System.out.println();
    }

    public T removeFirst() {
        if (size == 0){
            return null;
        }
        T temp = items[head];
        items[head] = null;
        size -= 1;
        if (size > 0){
            head = (head + 1) % items.length;
        }
        if (size > 8 && size < items.length / 4) {
            int newLength = items.length / FACTOR;
            resize(newLength);
        }

        return temp;
    }

    public T removeLast() {
        if (size == 0) {
            return null;
        }
        T temp = items[tail];
        items[tail] = null; // 释放内存
        size -= 1;

        if (size > 0) {
            tail = (tail - 1 + items.length) % items.length;
        }


        // 缩容判断
        if (items.length > 8 && size < items.length / 4) {
            resize(items.length / FACTOR);
        }
        return temp;

    }

    public T get(int index) {
        if (index >= size || index < 0) {
            return null;
        } else if (index < items.length - head) {
            return items[head + index];
        } else {
            return items[index + head - items.length];
        }
    }

    private void resize(int capacity) {
        if (capacity < size) {
            throw new IllegalArgumentException("not sufficient capacity");
        }
        T[] newTs = (T[]) new Object[capacity];
        if (head > tail) {
            System.arraycopy(items, head, newTs, 0, items.length - head);
            System.arraycopy(items, 0, newTs, items.length - head, tail + 1);
        } else {
            System.arraycopy(items, head, newTs, 0, tail - head + 1);
        }
        head = 0;
        tail = size - 1;
        items = newTs;
    }

    @Override
    public Iterator<T> iterator() {
        return new ArrayDeque.ADIterator();
    }

    private class ADIterator implements Iterator<T> {
        private int cnt = 0;

        @Override
        public boolean hasNext() {
            return (cnt < size);
        }

        @Override
        public T next() {
            int index = (cnt + head) % items.length;
            T value = items[index];
            cnt += 1;
            return value;
        }
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (!(o instanceof Deque)) {
            return false;
        } else {
            Deque<T> other = (Deque<T>) o;
            if (this.size() != other.size()) {
                return false;
            } else {
                int bothSize = this.size();
                for (int i = 0; i < bothSize; i++) {
                    if (!this.get(i).equals(other.get(i))) {
                        return false;
                    }
                }
                return true;
            }

        }
    }
}
