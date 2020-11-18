
/*
 * MyHashMap.java
 * 
 * My own implementation of a hashmap object.
 * 
 * Usage instructions:
 * 
 * Construct a MyHashMap object
 * MyHashMap<key type, value type> myMap = new MyHashMap<key type, value 
 *      type>();
 * 
 * Adding to the hashmap:
 * myMap.put(key, value)
 * put will get the hashcode for the key and use that to find the index where 
 * the key value pair will be added. Also, handles collisions by adding the new
 * pair to the front of the linked list within the given array index.
 * 
 * Other useful methods:
 * myMap.get(key);
 * myMap.hash(key);
 * myMap.containsKey(key);
 * myMap.keySet();
 * myMap.printTable();
 * myMap.toString();
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class MyHashMap<K, V> {
    // Variable fields used for MyHashMap object construction.
    List<LinkedList<K, V>> hashMap;
    final int BUCKETS = 8;

    /*
     * Purpose: Constructor method that creates a MyHashMap object which is an
     * array list with linked lists in each index. The hashmap is created using
     * generics so the key is of parameter type K and the value is of parameter
     * type V.
     */
    public MyHashMap() {
        hashMap = new ArrayList<LinkedList<K, V>>(BUCKETS);
        // Adds a LinkedList object in every index.
        for (int i = 0; i < 9; i++) {
            LinkedList<K, V> temp = new LinkedList<K, V>();
            hashMap.add(temp);
        }
    }

    /*
     * Purpose: Adds a new key/value pair to the hashmap. Does so by finding
     * the hash for the key and placing the key/value pair in that index of
     * the array list and adding it as the first element of the inside Linked
     * List.
     * 
     * @param key, is of parameter type K and is the key in the hashmap.
     * value, is of parameter type V and is the value in the hashmap.
     */
    public void put(K key, V value) {
        int hash = hash(key);
        // If the key is already in the hashmap then only the value is updated.
        if (this.containsKey(key)) {
            LinkedList<K, V> llist = hashMap.get(hash);
            Node<K, V> current = llist.getHead();
            while (!(current == null)) {
                if (current.getKey().equals(key)) {
                    current.setValue(value);
                    break;
                }
                current = current.getNext();
            }
            // Adds a new key/value pair to the hashmap.
        } else {
            Node<K, V> node = new Node<K, V>(key, value);
            hashMap.get(hash).add(node);
        }
    }

    /*
     * Purpose: Returns the value that is associated with the given key.
     * 
     * @param key, is of parameter type K and is the key in the hashmap.
     * 
     * @Return value, is the value that is associated with the given key.
     */
    public V get(K key) {
        int hash = hash(key);
        V value = null;
        LinkedList<K, V> llist = hashMap.get(hash);
        Node<K, V> current = llist.getHead();
        while (!(current == null)) {
            if (current.getKey().equals(key)) {
                value = current.getValue();
                break;
            }
            current = current.getNext();
        }
        return value;
    }

    // Creates the hash for the given key by utilizing the hashCode of the key
    // object and finding the remainder after dividing by the number of buckets
    private int hash(K key) {
        int hashCode = key.hashCode();
        int index = hashCode % BUCKETS;
        return Math.abs(index);
    }

    // Returns the boolean result of whether the given key is in the map or not
    public boolean containsKey(K key) {
        for (LinkedList<K, V> llist : hashMap) {
            Node<K, V> current = llist.getHead();
            while (!(current == null)) {
                if (current.getKey().equals(key)) {
                    return true;
                }
                current = current.getNext();
            }
        }
        return false;
    }

    // Returns a set of all the keys that are currently in the map
    public Set<K> keySet() {
        Set<K> keys = new TreeSet<K>();
        for (LinkedList<K, V> llist : hashMap) {
            Node<K, V> current = llist.getHead();
            while (!(current == null)) {
                keys.add(current.getKey());
                current = current.getNext();
            }
        }
        return keys;
    }

    // Prints every bucket in the hashmap and all of its keys as well as
    // indicating the number of collisions per bucket and throughout the map.
    public void printTable() {
        int conflicts = 0;
        for (int i = 0; i < BUCKETS; i++) {
            LinkedList<K, V> llist = hashMap.get(i);
            // Gets the number of conflicts in a bucket by taking the length of
            // LinkedList inside and subtracting 1 as long as it's not empty.
            int listLength = llist.length();
            if (listLength > 0) {
                listLength--;
            }
            conflicts += listLength;
            System.out.println("Index " + String.valueOf(i) + ": ("
                    + String.valueOf(listLength) + " conflicts), "
                    + llist.toString());
        }
        System.out
                .println("Total # of conflicts: " + String.valueOf(conflicts));
    }

    // Prints a visual representation of a MyHashMap object.
    public String toString() {
        String s = "";
        // Iterates through every index in the arraylist.
        for (LinkedList<K, V> llist : hashMap) {
            s += "[";
            Node<K, V> current = llist.getHead();
            // Iterates through every node in the linkedlist.
            while (!(current == null)) {
                s += "key: " + current.getKey() + ", value: "
                        + current.getValue();
                current = current.getNext();
                if (!(current == null)) {
                    s += " -> ";
                }
            }
            s += "],";
        }
        s = s.substring(0, s.length() - 1);
        return s;
    }
}
