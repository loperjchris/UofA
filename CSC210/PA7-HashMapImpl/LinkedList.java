/*
 * LinkedList.java
 * 
 * My own implementation of a LinkedList object.
 * 
 * Usage instructions:
 * 
 * Construct a LinkedList object
 * LinkedList<key type, value type> llist = new LinkedList<key type, value 
 *      type>();
 * 
 * Useful methods:
 * llist.add(node);
 * llist.getHead();
 * llist.length();
 * llist.toString();
 */
public class LinkedList<K, V> {
    // Variable fields used for LinkedList object construction.
    Node<K, V> head;

    public LinkedList() {
        // Initially sets the head to null within the constructor.
        this.head = null;
    }

    /*
     * Purpose: Adds a new node to the front of the LinkedList.
     * 
     * @param node, is a Node object.
     */
    public void add(Node<K, V> node) {
        if (head == null) {
            head = node;
            // Adds the front of the list and has the new node point to the
            // the rest of the linkedlist.
        } else {
            Node<K, V> temp = head;
            head = node;
            head.setNext(temp);
        }
    }

    // Getter method for head.
    public Node<K, V> getHead() {
        return head;
    }

    // Returns the number of nodes within the linkedlist.
    public int length() {
        int numNodes = 0;
        Node<K, V> current = this.getHead();
        while (!(current == null)) {
            numNodes++;
            current = current.getNext();
        }
        return numNodes;
    }

    // Creates a visual representation of the linkedlist.
    public String toString() {
        String s = "[";
        Node<K, V> current = this.getHead();
        while (!(current == null)) {
            s += current.getKey() + ", ";
            current = current.getNext();
        }
        s += "]";
        return s;
    }
}
