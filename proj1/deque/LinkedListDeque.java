package deque;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;


public class LinkedListDeque<Item> implements Iterable<Item>{
    private class ListNode {
        private Item value;
        private ListNode next;
        private ListNode prev;

        public ListNode(Item i, ListNode m, ListNode n){
            value = i;
            next = m;
            prev = n;
        }
    }

    private ListNode sentinel;
    private int size;

    LinkedListDeque(){
        sentinel = new ListNode(null, null, null);
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
        size = 0;
    }
    public void addFirst(Item i){
        if (this.isEmpty()){
            ListNode first = new ListNode(i, sentinel, sentinel);
            sentinel.next = first;
            sentinel.prev = first;
        }
        else{
            ListNode first = new ListNode(i, sentinel.next, sentinel);
            sentinel.next.prev = first;
            sentinel.next = first;
        }
        size += 1;
    }
    public void addLast(Item i){
        if (this.isEmpty()){
            ListNode first = new ListNode(i, sentinel, sentinel);
            sentinel.next = first;
            sentinel.prev = first;
        }
        else{
            ListNode last = new ListNode(i, sentinel, sentinel.prev);
            sentinel.prev.next = last;
            sentinel.prev = last;
        }
        size += 1;
    }
    public boolean isEmpty() {
        return (sentinel.next == sentinel);
    }

    public void printDeque(){
        ListNode cursor  = sentinel.next;
        while (cursor != sentinel){
            cursor = cursor.next;
            System.out.print(cursor.value + " ");
        }
        System.out.println();
    }
    public Item removeFirst(){
        if (size > 1){
            Item temp = sentinel.next.value;
            sentinel.next = sentinel.next.next;
            sentinel.next.prev = sentinel;
            size -= 1;
            return temp;
        }
        else if (size == 1) {
            Item temp = sentinel.next.value;
            sentinel.next = sentinel;
            sentinel.prev = sentinel;
            size -= 1;
            return temp;
        }
        else {
            return null;
        }
    }
    public Item removeLast(){
        if (size > 1) {
            Item temp = sentinel.prev.value;
            sentinel.prev = sentinel.prev.prev;
            sentinel.prev.next = sentinel;
            size -= 1;
            return temp;
        }
        else if (size == 1){
            Item temp = sentinel.prev.value;
            sentinel.next = sentinel;
            sentinel.prev = sentinel;
            size -= 1;
            return temp;
        }
        else{
            return null;
        }
    }
    public Item get(int index){
        ListNode cursor = sentinel;
        for (int i = index; i >= 0; i -= 1){
            cursor = cursor.next;
        }
        Item value = cursor.value;
        return value;
    }
    public int size(){
        return size;
    }

    @Override
    public Iterator<Item> iterator(){
        return new LLDIterator();
    }

    private class LLDIterator implements Iterator<Item>{
        private ListNode cursor = sentinel.next;

        @Override
        public boolean hasNext() {
            return (cursor != sentinel);
        }

        @Override
        public Item next() {
            Item value = cursor.value;
            cursor = cursor.next;
            return value;
        }
    }

    public boolean equals(Object o){
        if (this == o) {
            return true;
        }
        else if (!(o instanceof LinkedListDeque)){
            return false;
        }
        else{
            LinkedListDeque<Item> other = (LinkedListDeque<Item>) o;
            if (this.size == other.size){
                return false;
            }
            else{
                ListNode otherCursor = other.sentinel.next;
                for (Item curr : this) {
                    if (curr != otherCursor.value){
                        return false;
                    }
                }
                return true;
            }


            }
        }
    public Item getRecursive(int index){
        if (index >= size || index < 0){
            return null;
        }
        else{
            return getRecursiveHelper(sentinel.next, index);
        }

    }
    private Item getRecursiveHelper(ListNode curr, int index){
        if (index == 0){
            return curr.value;
        }
        else{
            return getRecursiveHelper(curr.next, index -= 1);
        }
    }
}
