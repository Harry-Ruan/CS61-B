package deque;

import java.util.Iterator;
import java.util.List;


public class LinkedListDeque<T> implements Iterable<T>, Deque<T>{
    private class ListNode {
        private T value;
        private ListNode next;
        private ListNode prev;

        public ListNode(T i, ListNode m, ListNode n){
            value = i;
            next = m;
            prev = n;
        }
    }

    private ListNode sentinel;
    private int size;

    public LinkedListDeque(){
        sentinel = new ListNode(null, null, null);
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
        size = 0;
    }
    public void addFirst(T i){
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
    public void addLast(T i){
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

    public void printDeque(){
        ListNode cursor  = sentinel.next;
        while (cursor != sentinel){
            cursor = cursor.next;
            System.out.print(cursor.value + " ");
        }
        System.out.println();
    }
    public T removeFirst(){
        if (size > 1){
            T temp = sentinel.next.value;
            sentinel.next = sentinel.next.next;
            sentinel.next.prev = sentinel;
            size -= 1;
            return temp;
        }
        else if (size == 1) {
            T temp = sentinel.next.value;
            sentinel.next = sentinel;
            sentinel.prev = sentinel;
            size -= 1;
            return temp;
        }
        else {
            return null;
        }
    }
    public T removeLast(){
        if (size > 1) {
            T temp = sentinel.prev.value;
            sentinel.prev = sentinel.prev.prev;
            sentinel.prev.next = sentinel;
            size -= 1;
            return temp;
        }
        else if (size == 1){
            T temp = sentinel.prev.value;
            sentinel.next = sentinel;
            sentinel.prev = sentinel;
            size -= 1;
            return temp;
        }
        else{
            return null;
        }
    }
    public T get(int index){
        ListNode cursor = sentinel;
        for (int i = index; i >= 0; i -= 1){
            cursor = cursor.next;
        }
        T value = cursor.value;
        return value;
    }
    public int size(){
        return size;
    }

    @Override
    public Iterator<T> iterator(){
        return new LLDIterator();
    }

    private class LLDIterator implements Iterator<T>{
        private ListNode cursor = sentinel.next;

        @Override
        public boolean hasNext() {
            return (cursor != sentinel);
        }

        @Override
        public T next() {
            T value = cursor.value;
            cursor = cursor.next;
            return value;
        }
    }

    public boolean equals(Object o){
        if (this == o) {
            return true;
        }
        else if (!(o instanceof Deque)){
            return false;
        }
        else{
            Deque<T> other = (Deque<T>) o;
            if (this.size() != other.size()){
                return false;
            }
            else{
                int bothSize = this.size();
                for (int i = 0; i < bothSize; i++){
                    if (!(this.get(i).equals(other.get(i)))){
                        return false;
                    }
                }
                return true;
            }


            }
        }
    public T getRecursive(int index){
        if (index >= size || index < 0){
            return null;
        }
        else{
            return getRecursiveHelper(sentinel.next, index);
        }

    }
    private T getRecursiveHelper(ListNode curr, int index){
        if (index == 0){
            return curr.value;
        }
        else{
            return getRecursiveHelper(curr.next, index -= 1);
        }
    }
}
