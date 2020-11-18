
/**
 * Edge.java
 * 
 * Builds an Edge object that will hold the connections between nodes as well
 * as the line between them.
 * 
 * Usage instructions:
 * 
 * Construct an Edge
 * Edge edge = new Edge(node1, node2);
 * 
 * Other useful methods:
 * edge.setLine(line);
 * edge.getLine();
 * edge.getNode1();
 * edge.getNode2();
 */

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class Edge implements Comparable<Edge> {
    // Variable fields used for Sculpture object construction.
    private int node1;
    private int node2;
    private Line line;

    /*
     * Purpose: Creates an Edge object that will keep track of which two nodes
     * are connected to each other.
     * 
     * @param node1 and node2 are ints that represent node numbers.
     * 
     * @return None.
     */
    public Edge(int node1, int node2) {
        this.node1 = node1;
        this.node2 = node2;
    }

    // Setter method for line.
    public void setLine(Line line) {
        this.line = line;
        line.setStroke(Color.BLACK);
    }

    // Getter method for line.
    public Line getLine() {
        return line;
    }

    // Getter method for node1.
    public int getNode1() {
        return node1;
    }

    // Getter method for node2.
    public int getNode2() {
        return node2;
    }

    /*
     * Purpose: Modified compareTo method so when an edge is added to a TreeSet
     * its order is preserved.
     * 
     * @param other is an Edge object to be compared to.
     * 
     * @return a -1 if the edge instance's nodeNum is lower than others. 0 if
     * they are the same and 1 if it is greater.
     */
    public int compareTo(Edge other) {
        if (node1 < other.node1
                || (node1 == other.node1 && node2 < other.node2)) {
            return -1;
        } else if (node1 > other.node1
                || (node1 == other.node1 && node2 > other.node2)) {
            return 1;
        } else {
            return 0;
        }
    }

    // Returns a string representation of a Node object.
    public String toString() {
        return node1 + " -> " + node2;
    }
}
