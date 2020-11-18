/*
 * Node.java
 * 
 * My own implementation of a Node object.
 * 
 * Usage instructions:
 * 
 * Construct a Node object
 * Node<key type, value type> node = new Node<key type, value type>();
 * 
 * Useful methods:
 * node.getKey();
 * node.setValue(value);
 * node.getValue();
 * node.getNext();
 * node.setNext(newNode);
 * node.toString();
 */
public class Node<K, V> {
    // Variable fields used for Node object construction.
    K key;
    V value;
    Node<K, V> next;

    // Constructor method for Node objects.
    public Node(K key, V value) {
        this.key = key;
        this.value = value;
        next = null;
    }

    // Getter method for key.
    public K getKey() {
        return key;
    }

    // Sets the value of the Node to value.
    public void setValue(V value) {
        this.value = value;
    }

    // Getter method for value
    public V getValue() {
        return value;
    }

    // Getter method for next.
    public Node<K, V> getNext() {
        return next;
    }

    // Sets the value of next to point to node.
    public void setNext(Node<K, V> node) {
        next = node;
    }

    // Prints a visual representation of a Node object.
    public String toString() {
        return "key: " + key + ", value: " + value + ", next: " + next;
    }
}
