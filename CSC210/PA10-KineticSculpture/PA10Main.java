
/* AUTHOR: Ruben Tequida
 * FILE: PA10Main.java
 * ASSIGNMENT: Programming Assignment 10 - KineticSculpture
 * COURSE: CSc 210; Section C; Spring 2019
 * Purpose: This program builds a JavaFX scene and stage and prompts the user
 * to supply a filename in a text box and press the button labeled "Process"
 * in order to read in the file and display its contents. The file will have
 * the delay on the first line which is how long the program will wait before 
 * moving marbles and how long marbles will take to transition between nodes.
 * The second line will contain the marbles to be added to the sculpture and
 * in what order. The next section of the file will detail how many nodes there
 * are, what type of node they are, and their position on the canvas. The 
 * remaining lines with detail which nodes are connected together by edges.
 * Once the input file is completely read in the program will display all the
 * and edges and after the initial delay will begin running marbles through
 * the sculpture until they reach the sink node.
 * 
 * example input file:
 * delay: 1
 * input: RED, BLUE, YELLOW, GREEN, PURPLE, PINK, BLACK
 * 0: input, (20,20)
 * 1: passthrough, (70,40)
 * 2: passthrough, (80,100)
 * 3: passthrough, (80, 200)
 * 4: passthrough, (140, 100)
 * 5: passthrough, (140, 200)
 * 6: sink, (600,150)
 * 0 -> 1
 * 0 -> 2
 * 0 -> 3
 * 1 -> 5
 * 2 -> 4
 * 3 -> 4
 * 4 -> 6
 * 5 -> 6
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import javafx.animation.KeyFrame;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class PA10Main extends Application {
    // Class variables.
    private final static int INPUT_LENGTH = 400;
    private final static int GRID_SQUARE_SIZE = 2;
    private final static int MARBLE_RADIUS = 10;
    private final static int SIZE_ACROSS = 650;
    private final static int SIZE_DOWN = 250;
    private final static int SPACING = 20;
    private final static int PADDING = 10;
    private final static int RECT_HEIGHT = 40;
    private final static int RECT_WIDTH = 30;
    private final static int KF_DELAY = 2;
    private static TextField input;
    private static double delay;
    private static Group root;
    private static String[] colors;
    private static Queue initialInput;
    private static Sculpture sculpture;

    // Launches the JavaFX stage.
    public static void main(String[] args) {
        launch(args);
    }

    /*
     * Purpose: Sets up the stage with a text box, button and group. Sends the
     * program to the event handler once the process button is pressed.
     * 
     * @param primaryStage which is the main window of the GUI.
     * 
     * @return None.
     */
    public void start(Stage primaryStage) {
        Text instructions = new Text("Enter Filename Here -->");
        sculpture = new Sculpture();
        input = new TextField();
        input.setPrefWidth(INPUT_LENGTH);
        Rectangle background = new Rectangle(0, 0,
                SIZE_ACROSS * GRID_SQUARE_SIZE, SIZE_DOWN * GRID_SQUARE_SIZE);
        background.setFill(Color.WHITE);
        Button process = new Button("Process");
        // Adding the text, textfield, and button to an HBox for placement.
        HBox commandLine = new HBox();
        commandLine.setAlignment(Pos.CENTER);
        commandLine.setPadding(new Insets(PADDING));
        commandLine.setSpacing(SPACING);
        commandLine.getChildren().add(instructions);
        commandLine.getChildren().add(input);
        commandLine.getChildren().add(process);
        root = new Group();
        root.getChildren().add(background);
        BorderPane p = new BorderPane();
        p.setBottom(commandLine);
        p.setCenter(root);
        process.setOnAction(new HandleProcess());
        primaryStage.setTitle("Kinetic Sculpture");
        primaryStage.setScene(new Scene(p));
        primaryStage.show();
    }

    /*
     * Purpose: Initializes the setup of the stage and places every node on it.
     * 
     * @param None.
     * 
     * @return None.
     */
    public static void drawNodes() {
        Set<Node> nodes = sculpture.getNodes();
        for (Node node : nodes) {
            if (node.getType().equals("input")) {
                // Copies the initial marble queue from the input file to the
                // input queue of the input node.
                node.setInput(initialInput);
            }
            Rectangle newRect = node.getRect();
            root.getChildren().add(newRect);
        }
    }

    /*
     * Purpose: Draws all the lines between the nodes and adds the line to its
     * proper edge object.
     * 
     * @param None.
     * 
     * @return None.
     */
    public static void drawLines() {
        Set<Edge> edges = sculpture.getEdges();
        for (Edge edge : edges) {
            Node node1 = sculpture.getNode(edge.getNode1());
            Node node2 = sculpture.getNode(edge.getNode2());
            Line newLine = new Line(
                    (node1.getXCoord() * GRID_SQUARE_SIZE) + RECT_WIDTH,
                    (node1.getYCoord() * GRID_SQUARE_SIZE) + (RECT_HEIGHT / 2),
                    (node2.getXCoord() * GRID_SQUARE_SIZE),
                    (node2.getYCoord() * GRID_SQUARE_SIZE) + (RECT_HEIGHT / 2));
            edge.setLine(newLine);
            root.getChildren().add(newLine);
        }
    }

    /*
     * Purpose: Begins the process of animating the flow of marbles through the
     * sculpture once it is completely setup. Structure taken from PA10 demo.
     * 
     * @param None.
     * 
     * @return None.
     */
    public static void runSculpture() {
        Set<Node> nodes = sculpture.getNodes();
        Set<Edge> edges = sculpture.getEdges();
        List<Queue> toRemove = new ArrayList<Queue>();
        // Creating a timeline to process motions at the desired timing.
        final Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        // Using a lambda function to determine what happens after every
        // keyframe.
        final KeyFrame kf = new KeyFrame(Duration.seconds(KF_DELAY),
                (ActionEvent e) -> {
                    // Moving marbles from queues within nodes.
                    for (Node node : nodes) {
                        processNodes(node);
                    }
                    // Moving marbles between edges.
                    for (Edge edge : edges) {
                        processEdges(edge, toRemove);
                    }
                    // Clearing output queues once the marbles have been moved.
                    for (Queue queue : toRemove) {
                        queue.pop();
                    }
                    toRemove.clear();
                });
        timeline.getKeyFrames().add(kf);
        timeline.play();
    }

    /*
     * Purpose: Moves marbles from a node's input queue to their output queue.
     * 
     * @param node which is a Node object and holds the node's marble queues.
     * 
     * @return None.
     */
    public static void processNodes(Node node) {
        if (!node.getInput().isEmpty()) {
            Circle marble = node.getInput().pop();
            node.getOutput().push(marble);
            root.getChildren().remove(marble);
        }
        if (node.getType().equals("sink")) {
            while (!node.getOutput().isEmpty()) {
                Circle curr = node.getOutput().pop();
                System.out.println(curr.getFill());
            }
        }
    }

    /*
     * Purpose: Moves marbles from a node's output queue across the edge and
     * into the connected node's input queue.
     * 
     * @param edge is an Edge object that connects two nodes. toRemove is a
     * list of queues that marbles are added to which will later be removed
     * once all marbles have moved during one phase.
     * 
     * @return None.
     */
    public static void processEdges(Edge edge, List<Queue> toRemove) {
        Node node1 = sculpture.getNode(edge.getNode1());
        Node node2 = sculpture.getNode(edge.getNode2());
        if (!node1.getOutput().isEmpty()) {
            Circle marbleToMove = marbleClone(node1.getOutput().peek());
            toRemove.add(node1.getOutput());
            node2.getInput().push(marbleToMove);
            root.getChildren().add(marbleToMove);
            PathTransition newTrans = new PathTransition(
                    Duration.seconds(delay), edge.getLine(), marbleToMove);
            newTrans.play();
        }
    }

    /*
     * Purpose: Duplicates a marbles so they can be added to the input queues
     * of each connected node. Taken from PA10 demo.
     * 
     * @param toClone is a Circle object that is to be duplicated.
     * 
     * @return clone .
     */
    private static Circle marbleClone(Circle toClone) {
        Circle clone = new Circle();
        clone.setFill(toClone.getFill());
        clone.setRadius(toClone.getRadius());
        return clone;
    }

    /*
     * Purpose: Event handler for when the process button is pressed. Opens the
     * provided file and calls methods to create the sculpture, display the
     * nodes and edges, and run the sculpture.
     * 
     * @param None.
     * 
     * @return None.
     */
    class HandleProcess implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent arg0) {
            String fileName = input.getText();
            Scanner file = null;
            // Attempts to open the given filename.
            try {
                file = new Scanner(new File(fileName));
                // Throws an exception and ends the program if the file isn't
                // found.
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
            createSculpture(file);
            drawNodes();
            drawLines();
            runSculpture();
        }

        /*
         * Purpose: Creates the sculpture based on the information given in the
         * input file. Reads in the delay, marble queue, types of nodes and
         * their positions, and how the nodes are connected.
         * 
         * @param file is a Scanner object that holds all the contents of the
         * input file..
         * 
         * @return None.
         */
        public void createSculpture(Scanner file) {
            String[] temp;
            initialInput = new Queue();
            delay = Double.valueOf((file.nextLine().split(" "))[1]);
            colors = file.nextLine().split(", ");
            colors[0] = (colors[0].split(": ")[1]);
            // Creating the marble queue.
            for (String color : colors) {
                insertMarbles(color);
            }
            while (file.hasNextLine()) {
                String command = file.nextLine();
                // Getting node types and positions
                if (!command.equals("")
                        && command.substring(1, 2).equals(":")) {
                    temp = command.split(",");
                    Node newNode = new Node(temp[0].substring(3),
                            Integer.valueOf(temp[0].substring(0, 1)),
                            Integer.valueOf((temp[1].split("\\("))[1]),
                            Integer.valueOf((temp[2].trim().split("\\)"))[0]));
                    sculpture.addNode(newNode);
                    // Getting edges.
                } else if (!command.equals("")) {
                    temp = command.split(" -> ");
                    Edge newEdge = new Edge(Integer.valueOf(temp[0]),
                            Integer.valueOf(temp[1]));
                    sculpture.addEdge(newEdge);
                }
            }
        }

        /*
         * Purpose: Takes of string of marble colors, creates a marble of each
         * color and adds that marble to the initial input queue.
         * 
         * @param color is a string of a color.
         * 
         * @return None.
         */
        public void insertMarbles(String color) {
            Circle marble = new Circle();
            marble.setFill(Color.valueOf(color));
            marble.setRadius(MARBLE_RADIUS);
            initialInput.push(marble);
        }
    }
}

