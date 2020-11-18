
/**
 * Queue.java
 * 
 * Builds queues that hold circle objects for the inputs and outputs of each
 * node in the sculpture.
 * 
 * Usage instructions:
 * 
 * Construct a Queue
 * Queue queue= new Queue();
 * 
 * Adding to a queue:
 * queue.push(circle);
 * Adds a new circle object to the end of the queue.
 * 
 * Removing from the queue:
 * queue.pop();
 * Removed and returns the first circle object in the queue.
 * 
 * Other useful methods:
 * queue.size();
 * queue.peek();
 * queue.isEmpty();
 * queue.toString();
 */

import java.util.ArrayList;

import javafx.scene.shape.Circle;

public class Queue {
    // Variable fields used for Queue object construction.
    private ArrayList<Circle> circles;

    // Constructs a new queue with an empty ArrayList.
    public Queue() {
        circles = new ArrayList<>();
    }

    /*
     * Purpose: Adds a new circle object to the end of the queue.
     * 
     * @param circle is a Circle object.
     * 
     * @return None.
     */
    public void push(Circle circle) {
        circles.add(circle);
    }

    /*
     * Purpose: Removes a circle object from the front of the queue and returns
     * that circle.
     * 
     * @param None.
     * 
     * @return circle is a Circle object.
     */
    public Circle pop() {
        Circle circle = null;
        // Checks if the queue isn't empty
        if (!circles.isEmpty()) {
            circle = circles.remove(0);
        }
        return circle;
    }

    // Looks at the first circle in the queue and returns it.
    public Circle peek() {
        Circle circle = null;
        // Checks if the queue isn't empty
        if (!circles.isEmpty()) {
            circle = circles.get(0);
        }
        return circle;
    }

    // Returns how many objects are in the queue.
    public int size() {
        return circles.size();
    }

    // Returns whether the queue is empty or not.
    public boolean isEmpty() {
        return circles.isEmpty();
    }

    // Returns a string representation of a Queue object.
    public String toString() {
        String s = "";
        for (Circle circle : circles) {
            s += circle.getFill();
        }
        return s;
    }
}
