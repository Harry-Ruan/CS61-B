package deque;

public class ArrayDeque<Item> {
    private int size;
    private int head;
    private int tail;
    private Item[] items;
    private int FACTOR = 2;

    public ArrayDeque(){
        size = 0;
        items = (Item[]) new Object[8];
        head = items.length - 1;
        tail = 0;
    }

    public void addFirst(Item i){
        if (tail + 1 == head){
            resize(items.length * FACTOR);
        }
        else if (items[head] == null){
            items[head] = i;
            size += 1;
        }
        else{
            head = (head - 1 + items.length) % items.length;
            items[head] = i;
            size += 1;
        }
    }
    public void addLast(Item i){
        if (tail + 1 == head){
            resize(items.length * FACTOR);
        }
        else if (items[tail] == null){
            items[tail] = i;
            size += 1;
        }
        else{
            tail = (tail + 1) % items.length;
            items[tail] = i;
            size += 1;
        }

    }
    public boolean isEmpty(){
        return size == 0;
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
        if (size < items.length / 4){
            int newLength = items.length / FACTOR;
            head = newLength - items.length + head;
            resize(newLength);

        }
        Item temp = items[head];
        head = (head + 1) % items.length;
        return temp;
    }
    public Item removeLast(){
        if (size < items.length / 4){
            resize(items.length / FACTOR);
        }
        Item temp = items[tail];
        tail = (tail - 1 + items.length) % items.length;
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
        Item[] newItems = (Item[]) new Object[capacity];
        System.arraycopy(items, head, newItems, 0, items.length - head);
        System.arraycopy(items, 0, newItems, items.length - head, tail + 1);
        head = items.length -1;
        tail = size -1;
        items = newItems;
    }
}
