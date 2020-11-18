
/**
 * Node.java
 * 
 * Builds nodes and holds their type, number, and position on the stage.
 * 
 * Usage instructions:
 * 
 * Construct a Node
 * Node node= new Node(type, nodeNum, xCoord, yCoord);
 * 
 * Other useful methods:
 * node.getType();
 * node.getNodeNum();
 * node.getXCoord();
 * node.getYCoord();
 * node.getRect();
 * node.getInput();
 * node.setInput(input);
 * node.getOutput();
 * node.toString();
 */

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Node implements Comparable<Node> {
    // Variable fields used for Node object construction.
    private String type;
    private int nodeNum;
    private int xCoord;
    private int yCoord;
    private Queue input;
    private Queue output;
    private Rectangle rect;
    private final int GRID_SQUARE_SIZE = 2;
    private final int RECT_HEIGHT = 40;
    private final int RECT_WIDTH = 30;

    /*
     * Purpose: Creates a new Node object with its type, its number, and its
     * coordinates. Also, creates new input and output queues for the node and
     * sets the nodes color and strokes.
     * 
     * @param types is a string that determines the type of node it is. nodeNum
     * is an int which is the nodes position number for the purpose of placing
     * the edges. xCoord and yCoord are ints that are the coordinates of the
     * node.
     * 
     * @return None.
     */
    public Node(String type, int nodeNum, int xCoord, int yCoord) {
        this.type = type;
        this.nodeNum = nodeNum;
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        input = new Queue();
        output = new Queue();
        // Creates a new Rectangle object to visually represent the node.
        rect = new Rectangle(xCoord * GRID_SQUARE_SIZE,
                yCoord * GRID_SQUARE_SIZE, RECT_WIDTH, RECT_HEIGHT);
        rect.setStroke(Color.BLACK);
        rect.setStrokeWidth(GRID_SQUARE_SIZE);
        rect.setFill(Color.WHITE);
    }

    // Getter method for type.
    public String getType() {
        return type;
    }

    // Getter method for nodeNum.
    public int getNodeNum() {
        return nodeNum;
    }

    // Getter method for xCoord.
    public int getXCoord() {
        return xCoord;
    }

    // Getter method for yCoord.
    public int getYCoord() {
        return yCoord;
    }

    // Getter method for rect.
    public Rectangle getRect() {
        return rect;
    }

    // Getter method for input.
    public Queue getInput() {
        return input;
    }

    // Setter method for input.
    public void setInput(Queue input) {
        this.input = input;
    }

    // Getter method for output.
    public Queue getOutput() {
        return output;
    }

    /*
     * Purpose: Modified compareTo method so when a node is added to a TreeSet
     * its order is preserved.
     * 
     * @param other is a Node object to be compared to.
     * 
     * @return a -1 if the node instance's nodeNum is lower than others. 0 if
     * they are the same and 1 if it is greater.
     */
    public int compareTo(Node other) {
        if (nodeNum < other.nodeNum) {
            return -1;
        } else if (nodeNum > other.nodeNum) {
            return 1;
        } else {
            return 0;
        }
    }

    // Returns a string representation of a Node object.
    public String toString() {
        return "Node: " + nodeNum + ", Type: " + type + ", x: " + xCoord
                + ", y: " + yCoord;
    }
}
