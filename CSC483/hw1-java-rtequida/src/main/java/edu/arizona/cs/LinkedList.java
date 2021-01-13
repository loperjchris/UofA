package edu.arizona.cs;

/*
 * LinkedList class that will hold the documents that contain a specified
 * token
 */
public class LinkedList<V> {

    Node<V> head, tail;

    /*
     * Purpose: Constructor method that creates a linked list object with
     * no nodes.
     */
    public LinkedList() {
        head = tail = null;
    }

    /*
     * Purpose: Adds the passed in Node to the end of the linked list.
     *
     * @param: node, a Node object to be added to the end of the linked
     * list.
     */
    public void add(Node<V> node) {
        // No nodes currently in linked list
        if (head == null) {
            head = tail = node;
            // Add to tail of linked list
        } else {
            tail.setNext(node);
            tail = tail.getNext();
        }
    }

    // Getter method for head
    public Node<V> getHead() {
        return head;
    }

    // Getter method for tail
    public Node<V> getTail() {
        return tail;
    }

    /*
     * Purpose: Checks to see if the linked list already contains the given doc.
     *
     * @param: doc, a String representing a document name
     */
    public boolean containsDoc(String doc) {
        Node<V> current = this.getHead();
        while (!(current == null)) {
            if (current.getValue().equals(doc)) {
                return true;
            }
            current = current.getNext();
        }
        return false;
    }

    // Creates a visual representation of the linkedlist
    public String toString() {
        String s = "";
        if (head == null) {
            return "null";
        } else {
            s = "[";
            Node<V> current = this.getHead();
            while (!(current == null)) {
                s += current.getValue() + ", ";
                current = current.getNext();
            }
            s = s.substring(0, s.length() - 2);
            s += "]";
            return s;
        }
    }
}

