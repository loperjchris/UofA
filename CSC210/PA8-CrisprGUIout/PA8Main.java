
/* AUTHOR: Ruben Tequida
 * FILE: PA8Main.java
 * ASSIGNMENT: Programming Assignment 6 - CrisprGUIout
 * COURSE: CSc 210; Section C; Spring 2019
 * Purpose: This program takes in an array of strings where the only argument
 * is a file name. After opening the file the program will create a grid of
 * NxM size which are given as the first integers in the file. Then the
 * program will process each command line by line. The possible commands are
 * CREATE, PRINT, MOVE, and REPRODUCE. Given the CREATE command the program
 * will create an animal object of the type of animal given and place it on
 * the grid at the given location. The PRINT command will print the current
 * state of the grid. The MOVE command will move the animals on the grid
 * according to their move instructions. This command can be modified to
 * specify a grid coordinate and only move animals on that specific coordinate
 * or it can be modified with a specific animal class or animal type and only
 * the animals that fit that class or type will be moved. The final command,
 * REPRODUCE, will cause the first two animals on all locations of the grid to
 * attempt to reproduce. The animals will reproduce if they are of the same
 * type and of opposite genders. REPRODUCE can be further modified to include
 * specific coordinates which will only cause animals of a specific coordinate
 * location to attempt to reproduce or the command can be modified with an
 * animal type which would cause only that specific animal type to attempt to 
 * reproduce. After every command that is given a GUI will be presented that
 * is a visual representation of the current state of the grid. Any changes
 * made to the grid will also be reflected in the GUI. Each command will be
 * delayed by a specific time given as the third integer input of the file.
 * 
 * example input file:
 * 
 * rows: 10
 * cols: 10
 * delay: .5
 *
 * CREATE (6,1) bee female true
 * CREATE (1,1) lion female left
 * CREATE (1,1) lion male left
 * CREATE (3,3) zebra male right
 * CREATE (3,3) warbler male 4
 * REPRODUCE
 * PRINT
 * MOVE (1,1)
 * PRINT
 * MOVE
 * PRINT
 * EAT
 * MOVE lion
 * PRINT
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class PA8Main extends Application {

    // variables that will be read in from file
    private static double delay = 2;
    private static List<Integer> gridSize;
    private static List<String> commands;
    private static Grid grid;

    // constants for the program
    private final static int TEXT_SIZE = 120;
    private final static int RECT_SIZE = 40;

    // temporary constants for starter code
    private static int SIZE_ACROSS;
    private static int SIZE_DOWN;

    /*
     * Purpose: Initializes gridSize, calls functions to read in the file and
     * creates the ecosystem grid, then launches the javafx GUI.
     * 
     * @param args, which takes in arguments from the command line which will
     * only be the filename.
     * 
     * @return None.
     */
    public static void main(String[] args) {
        gridSize = new ArrayList<Integer>();
        commands = readFile(args[0], gridSize);
        grid = new Grid(gridSize.get(0), gridSize.get(1));
        launch(args);
    }

    /*
     * Purpose: Opens the given file, pulls the size of the rows and columns
     * for the grid, and creates a list of all the commands that follow.
     * 
     * @param fileName, which is the name of the file given. gridSize is an
     * empty list that will be populates with the number of rows and columns
     * of the grid that needs to be created.
     * 
     * @return commands, which is a list of strings of every command after the
     * grid size requirements in the given file.
     */
    public static ArrayList<String> readFile(String fileName,
            List<Integer> gridSize) {
        List<String> commands = new ArrayList<String>();
        Scanner file = null;
        // Attempts to open the given filename.
        try {
            file = new Scanner(new File(fileName));
            // Throws an exception and ends the program if the file isn't found.
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        // Gets the size of the grid and the delay.
        String[] rows = file.nextLine().split(" ");
        String[] cols = file.nextLine().split(" ");
        String[] delayNum = file.nextLine().split(" ");
        gridSize.add(Integer.valueOf(rows[1]));
        gridSize.add(Integer.valueOf(cols[1]));
        delay = Double.valueOf(delayNum[1]);
        // Populates commands list with all the commands to process.
        while (file.hasNextLine()) {
            String line = file.nextLine();
            line = line.toLowerCase();
            String[] temp = line.split(" ");
            temp[0] = temp[0].toUpperCase();
            if (!line.equals("")) {
                commands.add(String.join(" ", temp));
            }
        }
        file.close();
        return (ArrayList<String>) commands;
    }

    /*
     * Purpose: Gets the size of the grid that will be shown, sets up the text
     * box and viewing are, launches the GUI window and calls to the functions
     * that create the visual representation of the ecosystem.
     * 
     * @param primaryStage which is the main window of the GUI.
     * 
     * @return None.
     */
    @Override
    public void start(Stage primaryStage) {
        SIZE_ACROSS = gridSize.get(0) * RECT_SIZE;
        SIZE_DOWN = gridSize.get(1) * RECT_SIZE;
        // PA8 TODO: change SIZE_ACROSS to something like
        // num_cols * CELLSIZE and SIZE_DOWN to num_rows * CELLSIZE
        TextArea command = new TextArea();
        GraphicsContext gc = setupStage(primaryStage, SIZE_ACROSS, SIZE_DOWN,
                command);
        primaryStage.show();
        simulateEcosystem(gc, command);
    }

    /**
     * Purpose: Creates a pause transition that determines the pace of the
     * program and calls the function to create the visual representation of
     * the ecosystem.
     * 
     * @param gc
     *            GraphicsContext for drawing ecosystem to.
     * @param command
     *            Reference to text area to show simulation commands.
     */
    private void simulateEcosystem(GraphicsContext gc, TextArea command) {
        // Update GUI based on value of delay(seconds to wait)
        // The below code constructs a PauseTransition and then
        // passes a lambda function into the setOnFinished method.
        // A lambda function is an unnamed function.  Here, the
        // unnamed function takes a parameter it names "e"
        // and then parses the next command out of the file,
        // applies the command to the Ecosystem, and then tells the
        // wait thread to pause for the delay again.
        PauseTransition wait = new PauseTransition(Duration.seconds(delay));
        wait.setOnFinished(new CommandHandler(commands, gc, command, wait));

        // Now that the PauseTransition thread is setup, get it going.
        wait.play();
    }

    /*
     * Purpose: Creates and initializes CommandHandler objects.
     * 
     * @param commands is a list of all commands in the given file, gc is the
     * GraphicsContext which holds the canvas viewed in the GUI, command is the
     * TextArea used to display the commands passed to it, wait is a
     * PauseTransition and controls the delay between displaying command inputs
     * and ecosystem changes.
     * 
     * @return None.
     */
    class CommandHandler implements EventHandler<ActionEvent> {
        private List<String> commandList;
        private GraphicsContext gc;
        private TextArea command;
        private PauseTransition wait;
        private int commandLines = 0;

        // CommandHandler constructor.
        CommandHandler(List<String> commands, GraphicsContext gc,
                TextArea command,
                PauseTransition wait) {
            this.commandList = commands;
            this.gc = gc;
            this.command = command;
            this.wait = wait;
        }

        /*
         * Purpose: Reads in every command line in the given file, executes the
         * command to the grid, prints the command in the TextArea and draws 
         * out the grid in the GUI.
         * 
         * @param e is an ActionEvent which triggers when the program is
         * started.
         * 
         * @return None.
         */
        @Override
        public void handle(ActionEvent e) {
            if (commandLines < commandList.size()) {
                String[] temp = commandList.get(commandLines).split(" ");
                if (temp[0].equals("CREATE")) {
                    createAnimal(temp, grid);
                } else if (temp[0].equals("MOVE")) {
                    moveAnimal(temp, grid);
                } else if (temp[0].equals("REPRODUCE")) {
                    reproduce(temp, grid);
                } else if (temp[0].equals("EAT")) {
                    eat(temp, grid);
                } else if (!temp[0].equals("PRINT")){
                	commandList.set(commandLines, "Invalid Command");
                }
                grid.drawEcosystem(gc, RECT_SIZE);
                command.appendText(commandList.get(commandLines) + "\n");
                commandLines++;
                wait.playFromStart();
            } else {
                wait.stop();
            }
        }
    }

    /*
     * Purpose: Determines how to evaluate the parameters of the command EAT.
     * 
     * @param command, which is an array of strings and holds the command that
     * specifies the parameters of the EAT command. grid, which is a
     * Grid object and used to place the created animals on itself.
     * 
     * @return None.
     */
    public static void eat(String[] command, Grid grid) {
    	// Process command with no parameters.
        if (command.length == 1) {
            grid.eat();
        } else {
        	// Process command with grid coordinates.
            if (command[1].startsWith("(")) {
                String[] temp = command[1].substring(1, command[1].length() - 1)
                        .split(",");
                int[] coords = { Integer.valueOf(temp[0]),
                        Integer.valueOf(temp[1]) };
                grid.eat(coords[0], coords[1]);
                // Process command with a specific animal type given.
            } else {
                grid.eat(command[1]);
            }
        }
    }

    /*
     * Purpose: Determines which type of object to create, creates that object
     * and places it on the grid.
     * 
     * @param command, which is an array of strings and holds the command that
     * specifies the parameters of the animal to be creates. grid, which is a
     * Grid object and used to place the created animals on itself.
     * 
     * @return None.
     */
    public static void createAnimal(String[] command, Grid grid) {
        Animal animal = null;
        List<String> mammals = Arrays.asList("elephant", "rhinoceros", "lion",
                "giraffe", "zebra");
        List<String> birds = Arrays.asList("thrush", "owl", "warbler",
                "shrike");
        // Splits the command into each of its parameters.
        String[] temp = command[1].substring(1, command[1].length() - 1)
                .split(",");
        int[] coords = { Integer.valueOf(temp[0]), Integer.valueOf(temp[1]) };
        if (mammals.contains(command[2])) {
            animal = new Mammal(coords[0], coords[1], command[2], command[3],
                    command[4]);
        } else if (birds.contains(command[2])) {
            animal = new Bird(coords[0], coords[1], command[2], command[3],
                    Integer.valueOf(command[4]));
        } else {
            if (!command[2].equals("mosquito")) {
                animal = new Insect(coords[0], coords[1], command[2],
                        command[3], Boolean.valueOf(command[4]));
            } else {
                animal = new Mosquito(coords[0], coords[1], command[2],
                        command[3], Boolean.valueOf(command[4]),
                        Boolean.valueOf(command[5]),
                        Boolean.valueOf(command[6]));
            }
        }
        grid.placeOnGrid(animal, animal.getX(), animal.getY());
    }

    /*
     * Purpose: Processes the MOVE command and determines which animals on the
     * grid need to be moved based on the command given.
     * 
     * @param command, which is an array of strings and holds the command that
     * specifies the parameters of the MOVE command. grid, which is a Grid
     * object and used to determine where animals move.
     * 
     * @return None.
     */
    public static void moveAnimal(String[] command, Grid grid) {
        // Moves all animals on the grid as no additional parameters are given.
        if (command.length == 1) {
            grid.findAnimalsToMove();
        } else {
            // Processes MOVE if specific coordinates are given.
            if (command[1].startsWith("(")) {
                String[] temp = command[1].substring(1, command[1].length() - 1)
                        .split(",");
                int[] coords = { Integer.valueOf(temp[0]),
                        Integer.valueOf(temp[1]) };
                grid.findAnimalsToMove(coords[0], coords[1]);
                // Processes MOVE if an animal class or type is given.
            } else {
                grid.findAnimalsToMove(command[1]);
            }
        }
    }

    /*
     * Purpose: Processes the REPRODUCE command and finds the first two animals
     * at each grid location and sends them to the grids reproduce methods.
     * 
     * @param command, which is an array of strings and holds the command that
     * specifies the parameters of the REPRODUCE command. grid, which is a Grid
     * object and used to determine if animals reproduce.
     * 
     * @return None.
     */
    public static void reproduce(String[] command, Grid grid) {
        // Calls the reproduce method for grid to evaluate every grid location.
        if (command.length == 1) {
            grid.findAnimalsToReproduce(grid);
        } else {
            // Finds the first two animals at the given location.
            if (command[1].startsWith("(")) {
                String[] temp = command[1].substring(1, command[1].length() - 1)
                        .split(",");
                int[] coords = { Integer.valueOf(temp[0]),
                        Integer.valueOf(temp[1]) };
                grid.findAnimalsToReproduce(coords[0], coords[1], grid);
            } else {
                // Calls the reproduce method for grid with a specific animal
                // type to evaluate.
                grid.findAnimalsToReproduce(command[1], grid);
            }
        }
    }

    /**
     * Sets up the whole application window and returns the GraphicsContext from
     * the canvas to enable later drawing. Also sets up the TextArea, which
     * should be originally be passed in empty.
     * 
     * @param primaryStage
     *            Reference to the stage passed to start().
     * @param canvas_width
     *            Width to draw the canvas.
     * @param canvas_height
     *            Height to draw the canvas.
     * @param command
     *            Reference to a TextArea that will be setup.
     * @return Reference to a GraphicsContext for drawing on.
     */
    public GraphicsContext setupStage(Stage primaryStage, int canvas_width,
            int canvas_height, TextArea command) {
        // Border pane will contain canvas for drawing and text area underneath
        BorderPane p = new BorderPane();

        // Canvas(pixels across, pixels down)
        // Note this is opposite order of parameters of the Ecosystem in PA6.
        Canvas canvas = new Canvas(canvas_width, canvas_height);

        // Command TextArea will hold the commands from the file
        command.setPrefHeight(TEXT_SIZE);
        command.setEditable(false);

        // Place the canvas and command output areas in pane.
        p.setCenter(canvas);
        p.setBottom(command);

        // Title the stage and place the pane into the scene into the stage.
        primaryStage.setTitle("Ecosystem");
        primaryStage.setScene(new Scene(p));

        return canvas.getGraphicsContext2D();
    }
}
