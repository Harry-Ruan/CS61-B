package hashmap;

import java.util.*;

/**
 *  A hash table-backed Map implementation. Provides amortized constant time
 *  access to elements via get(), remove(), and put() in the best case.
 *
 *  Assumes null keys will never be inserted, and does not resize down upon remove().
 *  @author Harry
 */
public class MyHashMap<K, V> implements Map61B<K, V> {

    @Override
    public void clear() {
        this.size = 0;
        this.length = 16;
        this.buckets = createTable(length);
    }

    @Override
    public boolean containsKey(K key) {
        if (get(key) != null){
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public V get(K key) {
        int hash = hash(key);
        for (Node node : buckets[hash]){
            if (node.key.equals(key)){
                return node.value;
            }
        }
        return null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void put(K key, V value) {
        if (containsKey(key)){
            for (Node node : buckets[hash(key)]) {
                if (node.key.equals(key)) {
                    node.value = value;
                    return;
                }
            }
        }
        else{
            buckets[hash(key)].add(new Node(key, value));
            size += 1;
        }
        if ((double) size/length > loadFactor){
            length *= 2;
            resize(length);
        }
    }

    @Override
    public Set<K> keySet() {
        Set<K> keySet = new HashSet<>();
        for (Collection<Node> bucket : buckets){
            if (bucket != null){
                for (Node node : bucket){
                    keySet.add(node.key);
                }
            }
        }
        return keySet;
    }

    @Override
    public V remove(K key) {
        int index = hash(key);
        Collection<Node> bucket = buckets[index];

        if (bucket == null) {
            return null;
        }

        Iterator<Node> iterator = bucket.iterator();
        while (iterator.hasNext()) {
            Node node = iterator.next();
            if (node.key.equals(key)) {
                V val = node.value;
                iterator.remove(); // 从桶中安全移除该 Node
                size--;            // 记得 size 减 1！
                return val;
            }
        }
        return null;
    }

    @Override
    public V remove(K key, V value) {
        int index = hash(key);
        Collection<Node> bucket = buckets[index];

        if (bucket == null) {
            return null;
        }

        Iterator<Node> iterator = bucket.iterator();
        while (iterator.hasNext()) {
            Node node = iterator.next();
            if (node.key.equals(key) && node.value.equals(value)) {
                V val = node.value;
                iterator.remove(); // 从桶中安全移除该 Node
                size--;            // 记得 size 减 1！
                return val;
            }
        }
        return null;
    }

    @Override
    public Iterator<K> iterator() {
        return keySet().iterator();
    }

    private int hash(K key){
        return Math.floorMod(key.hashCode(), buckets.length);
    }

    private void resize(int newLength){
        Collection<Node>[] newBuckets = createTable(newLength);

        // 2. 遍历旧数组，把节点直接 add 到新桶里
        for (Collection<Node> bucket : buckets) {
            if (bucket != null) {
                for (Node node : bucket) {
                    // 手动计算在新数组中的下标
                    int newIndex = Math.floorMod(node.key.hashCode(), newLength);
                    newBuckets[newIndex].add(node);
                }
            }
        }

        // 3. 替换掉旧的数组和长度
        // 注意：size 完全不需要变动！因为搬运前后 key 的总数一模一样！
        this.buckets = newBuckets;
        this.length = newLength;
    }

    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    /* Instance Variables */
    private Collection<Node>[] buckets;
    // You should probably define some more!
    private int size;
    private int length;
    private double loadFactor;

    /** Constructors */
    public MyHashMap() {
        size = 0;
        loadFactor = 0.75;
        length = 16;
        buckets = createTable(length);
    }

    public MyHashMap(int initialSize) {
        size = 0;
        loadFactor = 0.75;
        length = initialSize;
        buckets = createTable(length);
    }

    /**
     * MyHashMap constructor that creates a backing array of initialSize.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialSize initial size of backing array
     * @param maxLoad maximum load factor
     */
    public MyHashMap(int initialSize, double maxLoad) {
        size = 0;
        loadFactor = maxLoad;
        length = initialSize;
        buckets = createTable(length);
    }

    /**
     * Returns a new node to be placed in a hash table bucket
     */
    private Node createNode(K key, V value) {
        return new Node(key, value);
    }

    /**
     * Returns a data structure to be a hash table bucket
     *
     * The only requirements of a hash table bucket are that we can:
     *  1. Insert items (`add` method)
     *  2. Remove items (`remove` method)
     *  3. Iterate through items (`iterator` method)
     *
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     *
     * Override this method to use different data structures as
     * the underlying bucket type
     *
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket() {
        return new LinkedList<>();
    }

    /**
     * Returns a table to back our hash table. As per the comment
     * above, this table can be an array of Collection objects
     *
     * BE SURE TO CALL THIS FACTORY METHOD WHEN CREATING A TABLE SO
     * THAT ALL BUCKET TYPES ARE OF JAVA.UTIL.COLLECTION
     *
     * @param tableSize the size of the table to create
     */
    private Collection<Node>[] createTable(int tableSize) {
            Collection<Node>[] table = (Collection<Node>[]) new Collection[tableSize];
            for (int i = 0; i < tableSize; i++) {
                table[i] = createBucket();
            }
            return table;
    }

    // TODO: Implement the methods of the Map61B Interface below
    // Your code won't compile until you do so!

}
