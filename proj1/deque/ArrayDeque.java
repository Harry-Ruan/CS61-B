package deque;

public class ArrayDeque<Item> implements Deque<Item> {
    private int size;
    private int head;
    private int tail;
    private Item[] items;
    private int FACTOR = 2;

    public ArrayDeque(){
        size = 0;
        items = (Item[]) new Object[8];
        head = 0;
        tail = 0;
    }

    public void addFirst(Item i){
        if (size == items.length - 1){
            resize(items.length * FACTOR);
        }
        if (items[head] == null){
            items[head] = i;
            size += 1;
            return;
        }

            head = (head - 1 + items.length) % items.length;
            items[head] = i;
            size += 1;

    }
    public void addLast(Item i){
        if (size == items.length - 1){
            resize(items.length * FACTOR);
        }
        if (items[tail] == null){
            items[tail] = i;
            size += 1;
            return;
        }
            tail = (tail + 1) % items.length;
            items[tail] = i;
            size += 1;
    }
    public int size(){
        return size;
    }
    public void printDeque(){
        for (int i = head; i < items.length; i++){
            System.out.print(items[i] + " ");
        }
        for (int i = 0; i <= tail; i++){
            System.out.print(items[i] + " ");
        }
    }
    public Item removeFirst(){
        Item temp = items[head];
        if (size < items.length / 4){
            int newLength = items.length / FACTOR;
            resize(newLength);
        }
        else{
            head = (head + 1) % items.length;
        }
        size -= 1;
        return temp;
    }
    public Item removeLast(){
        Item temp = items[tail];
        if (size < items.length / 4){
            int newLength = items.length / FACTOR;
            resize(newLength);
        }
        else{
            tail = (tail - 1 + items.length) % items.length;
        }
        size -= 1;
        return temp;
    }
    public Item get(int index){
        if (index >= size || index < 0){
            return null;
        }
        else if (index < items.length - head){
            return items[head + index];
        }
        else{
            return items[index + head - items.length];
        }
    }
    private void resize(int capacity){
        if (capacity < size) {
            throw new IllegalArgumentException("not sufficient capacity");
        }
        Item[] newItems = (Item[]) new Object[capacity];
        if (head > tail){
            System.arraycopy(items, head, newItems, 0, items.length - head);
            System.arraycopy(items, 0, newItems, items.length - head, tail + 1);
        }
        else{
            System.arraycopy(items, head, newItems, 0, tail - head + 1);
        }
        head = 0;
        tail = size -1;
        items = newItems;
    }
}
