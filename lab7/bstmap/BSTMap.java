package bstmap;

import java.util.Iterator;
import java.util.Set;

public class BSTMap<K extends Comparable<K>, V>  implements Map61B<K, V> {
    private int size = 0;
    private Node root = null;

    private class Node{
        private K key;
        private V value;
        private Node left, right;

        Node(K k, V v, Node l, Node r){
            key = k;
            value = v;
            left = l;
            right = r;
        }

        Node get(K k){
            if (k.compareTo(this.key) == 0){
                return this;
            }
            else{
                if (this.left != null && k.compareTo(this.key) < 0){
                    return this.left.get(k);
                }
                else if (this.right != null && k.compareTo(this.key) > 0){
                    return this.right.get(k);
                }
                return null;
            }
        }
    }


    @Override
    public void clear() {
        size = 0;
        root = null;
    }

    @Override
    public boolean containsKey(K key) {
        if (root == null) {
            return false;
        }
        return root.get(key) != null;
    }

    @Override
    public V get(K key) {
        if (root == null){
            return null;
        }
        Node result = root.get(key);
        if (result == null){
            return null;
        }
        else{
            return result.value;
        }

    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void put(K key, V value) {
        if (root == null){
            root = new Node(key, value, null, null);
            size += 1;
        }
        else{
            put(key,value, root);
        }
    }

    private void put(K k, V v, Node n){
        if (k.compareTo(n.key) == 0){
            n.value = v;
        }
        else if (k.compareTo(n.key) < 0)
        {
            if (n.left == null){
                n.left = new Node(k, v, null, null);
                size += 1;
            }
            else{
                put(k, v, n.left);
            }
        }
        else{
            if (n.right == null){
                n.right = new Node(k, v, null, null);
                size += 1;
            }
            else{
                put(k, v, n.right);
            }
        }
    }

    public void printInOrder(){
        printInOrder(root);
    }

    private void printInOrder(Node n){
        if (n != null){
            printInOrder(n.left);
            System.out.print(n.value + " ");
            printInOrder(n.right);
        }
    }

    @Override
    public Set<K> keySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<K> iterator() {
        throw new UnsupportedOperationException();
    }
}
