
/**
 * Sculpture.java
 * 
 * Builds a graph consisting of nodes and edges.
 * 
 * Usage instructions:
 * 
 * Construct a Sculpture
 * Sculpture sculpture= new Sculpture();
 * 
 * Other useful methods:
 * sculpture.addNode(nod);
 * sculpture.addEdge(edge);
 * sculpture.getNodes();
 * sculpture.getEdges();
 * sculpture.getNode(nodeNum);
 */

import java.util.TreeSet;

public class Sculpture {
    // Variable fields used for Sculpture object construction.
    private TreeSet<Edge> edges;
    private TreeSet<Node> nodes;

    /*
     * Purpose: Creates a new TreeSet for the nodes and the edges that will be
     * a part of the sculpture graph.
     * 
     * @param None.
     * 
     * @return None.
     */
    public Sculpture() {
        edges = new TreeSet<>();
        nodes = new TreeSet<>();
    }

    // Adds a node to the nodes TreeSet.
    public void addNode(Node node) {
        nodes.add(node);
    }

    // Adds an edge to the Edges TreeSet.
    public void addEdge(Edge edge) {
        edges.add(edge);
    }

    // Getter method for the nodes TreeSet.
    public TreeSet<Node> getNodes() {
        return nodes;
    }

    // Getter method a node within the nodes TreeSet.
    public Node getNode(int nodeNum) {
        for (Node node : nodes) {
            if (node.getNodeNum() == nodeNum) {
                return node;
            }
        }
        return null;
    }

    // Getter method for the edges TreeSet
    public TreeSet<Edge> getEdges() {
        return edges;
    }
}
