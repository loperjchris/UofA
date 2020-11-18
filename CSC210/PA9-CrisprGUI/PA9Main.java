
/* AUTHOR: Ruben Tequida
 * FILE: PA9Main.java
 * ASSIGNMENT: Programming Assignment 9 - CrisprGUI
 * COURSE: CSc 210; Section C; Spring 2019
 * Purpose: This program takes in as as arguments two numbers. The first number
 * represents the number of rows in the ecosystem (N) and the second number
 * represents the number of columns in the ecosystem (M).The program will then
 * create a grid of NxM size. Then the program will wait for a command to be
 * typed into the command field and the 'Process Command' button to be pressed.
 * The possible commands are CREATE, MOVE, REPRODUCE, and EAT. Given the CREATE
 * command the program will create an animal object of the type of animal given
 * and place it on the grid at the given location. There are also buttons that
 * can be pressed that fill in the command line with Animal templates that need
 * to be modified in the command line. There is also a 'Create Random' button 
 * that will populate the ecosystem with a random animal and display the animal
 * that was created in the output text area. The MOVE command will move the
 * animals on the grid according to their move instructions. This command can
 * be modified to specify a grid coordinate and only move animals on that 
 * specific coordinate or it can be modified with a specific animal class or 
 * animal type and only the animals that fit that class or type will be moved. 
 * REPRODUCE will cause the first two animals on all locations of the grid to 
 * attempt to reproduce. The animals will only reproduce if they are of the 
 * same type and of opposite genders. REPRODUCE can be further modified to 
 * include specific coordinates which will only cause animals of a specific 
 * coordinate location to attempt to reproduce or the command can be modified 
 * with an animal type which would cause only that specific animal type to 
 * attempt to reproduce. The command EAT will cause the first two animals at 
 * all locations to attempt to eat each other. Mammals can eat mammals and 
 * birds can eat insects. If an animal is eaten it is removed from the 
 * ecosystem. EAT can be modified with grid locations which will cause only the
 * animals at that location to attempt to eat each other. If can also be 
 * modified with a type so that only animals of that type will attempt to eat
 * other animals at their location. There are also buttons that submit the 
 * commands MOVE, REPRODUCE, and EAT without additional modifiers. There's a 
 * 'Random Event' button which will call MOVE, REPRODUCE, or EAT randomly and 
 * there is a final button, 'Clear' which clears the command line and output 
 * text area of all lines and clears the ecosystem of any animals. After every
 * command that is given a GUI will be presented that is a visual 
 * representation of the current state of the grid. Any changes made to the 
 * grid will also be reflected in the GUI. Each command will be delayed by a 
 * specific time given as the third integer input of the file.
 * 
 * example input arguments:
 * 10 10
 */

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class PA9Main extends Application {
    // Class variable
    private static Grid grid;
    private final static int TEXT_SIZE = 120;
    private final static int RECT_SIZE = 40;
    private final static int CMD_LENGTH = 400;
    private static int SIZE_ACROSS;
    private static int SIZE_DOWN;
    private static TextField cmd_in;
    private static TextArea commandArea;
    private static GraphicsContext gc;
    private static Button button;
    private static Button moveButton;
    private static Button reproduceButton;
    private static Button eatButton;
    private static Button randomEvent;
    private static Button createMammalButton;
    private static Button createBirdButton;
    private static Button createInsectButton;
    private static Button createMosquitoButton;
    private static Button createRandom;
    private static Button clear;
    private static BorderPane p;

    /*
     * Purpose: Initializes the size of the grid and the grid itself. Then,
     * launches the javafx GUI.
     * 
     * @param args, which takes in arguments from the command line which will
     * be the number of rows and cols for the grid.
     * 
     * @return None.
     */
    public static void main(String[] args) {
        SIZE_ACROSS = Integer.valueOf(args[1]) * RECT_SIZE;
        SIZE_DOWN = Integer.valueOf(args[0]) * RECT_SIZE;
        grid = new Grid(Integer.valueOf(args[0]), Integer.valueOf(args[1]));
        launch(args);
    }

    /*
     * Purpose: Calls setupStage method, sets all the actions for the buttons
     * are pressed and displays the GUI.
     * 
     * @param primaryStage which is the main window of the GUI.
     * 
     * @return None.
     */
    @Override
    public void start(Stage primaryStage) {
        setupStage();
        button.setOnAction(new HandleTextInput());
        moveButton.setOnAction(new HandleEvents(0));
        reproduceButton.setOnAction(new HandleEvents(1));
        eatButton.setOnAction(new HandleEvents(2));
        randomEvent.setOnAction(new HandleEvents(3));
        clear.setOnAction(new HandleEvents(4));
        createMammalButton
                .setOnAction(new HandleCreate());
        createBirdButton
                .setOnAction(new HandleCreate());
        createInsectButton
                .setOnAction(new HandleCreate());
        createMosquitoButton.setOnAction(new HandleCreate());
        createRandom.setOnAction(new HandleCreate());
        primaryStage.setTitle("Ecosystem");
        primaryStage.setScene(new Scene(p));
        primaryStage.show();
    }

    /*
     * Purpose: Creates the main components of the stage such as the canvas,
     * the GraphicsContext, the text areas and the BorderPane. Also, places the
     * canvas, all the buttons, and the text areas in the proper locations of
     * the BorderPane.
     * 
     * @param None.
     * 
     * @return None.
     */
    public static void setupStage() {
        commandArea = new TextArea();
        commandArea.setPrefHeight(TEXT_SIZE);
        Canvas canvas = new Canvas(SIZE_ACROSS, SIZE_DOWN);
        gc = canvas.getGraphicsContext2D();
        p = new BorderPane();
        cmd_in = new TextField();
        cmd_in.setPrefWidth(CMD_LENGTH);
        createButtons();
        // Adding padding to each box.
        final int COMMANDS_H_SPACE = 15;
        final int L_BUTTONS_SPACE = 25;
        final int R_BUTTONS_SPACE = 25;
        // These boxes will be used for placement of objects onto the
        // BorderPane.
        HBox commandsBoxH = new HBox(COMMANDS_H_SPACE);
        commandsBoxH.setAlignment(Pos.CENTER);
        VBox commandsBoxV = new VBox();
        VBox rightButtons = new VBox(R_BUTTONS_SPACE);
        VBox leftButtons = new VBox(L_BUTTONS_SPACE);
        setPlaces(commandsBoxH, commandsBoxV, rightButtons, leftButtons);
        p.setCenter(canvas);
        p.setLeft(leftButtons);
        p.setRight(rightButtons);
        p.setBottom(commandsBoxV);
        grid.drawEcosystem(gc, RECT_SIZE);
    }

    // Creates all the buttons for the GUI as well as setting their width.
    public static void createButtons() {
        final int MAX_WIDTH = 200;
        button = new Button("Process Command");
        moveButton = new Button("MOVE");
        moveButton.setMaxWidth(MAX_WIDTH);
        reproduceButton = new Button("REPRODUCE");
        reproduceButton.setMaxWidth(MAX_WIDTH);
        eatButton = new Button("EAT");
        eatButton.setMaxWidth(MAX_WIDTH);
        randomEvent = new Button("Random Event");
        clear = new Button("Clear");
        clear.setMaxWidth(MAX_WIDTH);
        createMammalButton = new Button("CREATE Mammal");
        createMammalButton.setMaxWidth(MAX_WIDTH);
        createBirdButton = new Button("CREATE Bird");
        createBirdButton.setMaxWidth(MAX_WIDTH);
        createInsectButton = new Button("CREATE Insect");
        createInsectButton.setMaxWidth(MAX_WIDTH);
        createMosquitoButton = new Button("CREATE Mosquito");
        createRandom = new Button("Random Animal");
        createRandom.setMaxWidth(MAX_WIDTH);
    }

    /*
     * Purpose: Places all the buttons and text areas into their proper VBoxes
     * and HBoxes which will be used for placement on the BorderPane.
     * 
     * @param commandsBoxH is a horizontal field that holds the command line
     * and process button. commandsBoxV is a vertical field that adds the
     * output text area with the command line and submit button. rightButtons
     * is a vertical field that holds all the buttons to go on the right side
     * of the BorderPane. leftButtons is a vertical field that holds all the
     * buttons to go on the left side of the BorderPane.
     * 
     * @return None.
     */
    public static void setPlaces(HBox commandsBoxH, VBox commandsBoxV,
            VBox rightButtons, VBox leftButtons) {
        Text instructions = new Text("Enter Command Here -->");
        commandsBoxH.setPadding(new Insets(10));
        commandsBoxH.getChildren().add(instructions);
        commandsBoxH.getChildren().add(cmd_in);
        commandsBoxH.getChildren().add(button);
        commandsBoxV.getChildren().add(commandsBoxH);
        commandsBoxV.getChildren().add(commandArea);
        rightButtons.setPadding(new Insets(0, 45, 0, 42));
        rightButtons.setAlignment(Pos.CENTER);
        rightButtons.getChildren().add(moveButton);
        rightButtons.getChildren().add(reproduceButton);
        rightButtons.getChildren().add(eatButton);
        rightButtons.getChildren().add(randomEvent);
        rightButtons.getChildren().add(clear);
        leftButtons.setPadding(new Insets(0, 27, 0, 27));
        leftButtons.setAlignment(Pos.CENTER);
        leftButtons.getChildren().add(createMammalButton);
        leftButtons.getChildren().add(createBirdButton);
        leftButtons.getChildren().add(createInsectButton);
        leftButtons.getChildren().add(createMosquitoButton);
        leftButtons.getChildren().add(createRandom);
    }

    // Purpose: Handles the pressing of the 'Process Command' button.
    class HandleTextInput implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            String command = cmd_in.getText();
            String[] temp = command.split(" ");
            // Processes the input based on the command type given.
            if (temp[0].equals("CREATE")) {
                createAnimal(temp, grid);
            } else if (temp[0].equals("MOVE")) {
                moveAnimal(temp, grid);
            } else if (temp[0].equals("REPRODUCE")) {
                reproduce(temp, grid);
            } else if (temp[0].equals("EAT")) {
                eat(temp, grid);
            } else if (!temp[0].equals("PRINT")) {
                command = "Invalid Command";
            }
            grid.drawEcosystem(gc, RECT_SIZE);
            // Appends the given command to the output text area.
            commandArea.appendText(command + "\n");
        }
    }

    /*
     * Purpose: Determines which event to process depending on the integer
     * passed to it. 0 = MOVE, 1 = REPRODUCE, 2 = EAT, 3 randomly generates a
     * number from 0 to 2 and processes the respective command as described
     * above, and 4 = CLEAR which clears the text areas of input and the
     * ecosystem of animals.
     * 
     * @param option is an integer that indicates which event to run.
     * 
     * @return None.
     */
    class HandleEvents implements EventHandler<ActionEvent> {
        private int option;

        public HandleEvents(int option) {
            this.option = option;
        }

        @Override
        public void handle(ActionEvent event) {
            cmd_in.clear();
            Random rand = new Random();
            int choice = this.option;
            // Creates a random int to choose MOVE, REPRODUCE, or EAT if
            // 'Random Event' is chosen.
            if (this.option == 3) {
                choice = rand.nextInt(3);
            }
            if (choice == 0) {
                moveAnimal(new String[] { "MOVE" }, grid);
                grid.drawEcosystem(gc, RECT_SIZE);
                commandArea.appendText("MOVE\n");
            } else if (choice == 1) {
                reproduce(new String[] { "REPRODUCE" }, grid);
                grid.drawEcosystem(gc, RECT_SIZE);
                commandArea.appendText("REPRODUCE\n");
            } else if (choice == 2) {
                eat(new String[] { "EAT" }, grid);
                grid.drawEcosystem(gc, RECT_SIZE);
                commandArea.appendText("EAT\n");
            } else {
                commandArea.clear();
                cmd_in.clear();
                grid = new Grid(SIZE_DOWN / RECT_SIZE, SIZE_ACROSS / RECT_SIZE);
                grid.drawEcosystem(gc, RECT_SIZE);
            }
        }
    }

    /*
     * Purpose: Handles when the create buttons are pressed. If a specific
     * animal create button is pressed it will fill the command line with an
     * animal template that can be modified for the desired animal. If 'Random
     * Animal' is pressed it will call a method to generate a random animal,
     * adds the animal to the ecosystem and prints the command to the output
     * text area.
     * 
     * @param None.
     * 
     * @return None.
     */
    class HandleCreate implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent arg0) {
            cmd_in.clear();
            // Grabs which button was pressed.
            String buttonPressed = arg0.getSource().toString();
            String[] temp = buttonPressed.split("'");
            // Creating specific animal.
            if (!temp[1].equals("Random Animal")) {
                if (temp[1].equals("CREATE Mammal")) {
                    cmd_in.appendText("CREATE (X,Y) TYPE GENDER DIRECTION");
                } else if (temp[1].equals("CREATE Bird")) {
                    cmd_in.appendText("CREATE (X,Y) TYPE GENDER STEPS");
                } else if (temp[1].equals("CREATE Insect")) {
                    cmd_in.appendText("CREATE (X,Y) TYPE GENDER CLOCKWISE");
                } else {
                    cmd_in.appendText(
                            "CREATE (X,Y) mosquito GENDER CLOCKWISE PARENT1 PARENT2");
                }
                commandArea.appendText(
                        "Modify the command placed in the input area to your desired inputs\n");
                // Creating Random Animal.
            } else {
                String command = createRandomAnimal();
                String[] randAnimal = command.split(" ");
                createAnimal(randAnimal, grid);
                grid.drawEcosystem(gc, RECT_SIZE);
                commandArea.appendText(command + "\n");
            }
        }
    }

    /*
     * Purpose: Generates a properly formatted create animal input string.
     * 
     * @param None.
     * 
     * @return s is a String that is a properly formatted create animal input.
     */
    public static String createRandomAnimal() {
        String s = "CREATE (";
        Random rand = new Random();
        int x = rand.nextInt(SIZE_DOWN / RECT_SIZE);
        int y = rand.nextInt(SIZE_ACROSS / RECT_SIZE);
        int gender = rand.nextInt(2);
        List<String> types = Arrays.asList("elephant", "rhinoceros", "lion",
                "giraffe", "zebra", "thrush", "owl", "warbler", "shrike",
                "mosquito", "bee", "fly", "ant");
        List<String> genders = Arrays.asList("male", "female");
        List<String> directions = Arrays.asList("left", "right");
        int type = rand.nextInt(13);
        s += x + "," + y + ") " + types.get(type) + " " + genders.get(gender) + " "; 
        if (type < 5) {
            int direction = rand.nextInt(2);
            s += directions.get(direction);
        } else if (type < 9) {
            int steps = rand.nextInt(SIZE_DOWN/RECT_SIZE);
            s += steps;
        } else {
            boolean clockwise = rand.nextBoolean();
            if (!types.get(type).equals("mosquito")) {
                s += clockwise;
            } else {
                boolean parent1 = rand.nextBoolean();
                boolean parent2 = rand.nextBoolean();
                s += clockwise + " " + parent1 + " " + parent2;
            }
        }
        return s;
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

}
