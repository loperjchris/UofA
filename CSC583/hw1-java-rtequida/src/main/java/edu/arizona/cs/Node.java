package edu.arizona.cs;

/*
 * Node class that will hold a document id
 */
public class Node<V> {

    // Field variables for Node object
    private V value;
    private Node<V> next;

    // Constructor
    public Node(V value) {
        this.value = value;
        next = null;
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
    public Node<V> getNext() {
        return next;
    }

    // Sets the value of next to point to node.
    public void setNext(Node<V> node) {
        next = node;
    }

    // Prints a visual representation of a Node object.
    public String toString() {
        return "value: " + value + ", next: " + next;
    }

}
