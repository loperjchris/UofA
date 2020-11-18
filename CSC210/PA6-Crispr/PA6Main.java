
/* AUTHOR: Ruben Tequida
 * FILE: PA6Main.java
 * ASSIGNMENT: Programming Assignment 6 - Crispr
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
 * reproduce.
 * 
 * example input file:
 * 
 * rows: 10
 * cols: 10
 *
 * CREATE (6,1) bee female true
 * CREATE (1,1) lion female left
 * CREATE (1,1) lion male left
 * CREATE (3,3) warbler male 4
 * REPRODUCE
 * PRINT
 * MOVE (1,1)
 * PRINT
 * MOVE
 * PRINT
 * MOVE lion
 * PRINT
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class PA6Main {
    /*
     * Purpose: Calls functions to read in the file and creates the ecosystem
     * grid, then calls the method associated with the given command.
     * 
     * @param args, which takes in arguments from the command line which will
     * only be the filename.
     * 
     * @return None.
     */
    public static void main(String[] args) {
        List<Integer> gridSize = new ArrayList<Integer>();
        List<String> commands = readFile(args[0], gridSize);
        Grid grid = new Grid(gridSize.get(0), gridSize.get(1));
        for (String command : commands) {
            String[] temp = command.split(" ");
            if (temp[0].equals("CREATE")) {
                createAnimal(temp, grid);
            } else if (temp[0].equals("PRINT")) {
                System.out.println("> PRINT\n" + grid);
            } else if (temp[0].equals("MOVE")) {
                System.out.println("> " + command + "\n");
                moveAnimal(temp, grid);
            } else if (temp[0].equals("REPRODUCE")) {
                System.out.println("> " + command + "\n");
                reproduce(temp, grid);
            }
        }
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
        // Gets the size of the grid.
        String[] rows = file.nextLine().split(" ");
        String[] cols = file.nextLine().split(" ");
        gridSize.add(Integer.valueOf(rows[1]));
        gridSize.add(Integer.valueOf(cols[1]));
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