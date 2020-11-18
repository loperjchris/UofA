
/**
 * Grid.java
 * 
 * Represents the animal ecosystem where the animals will be placed and the
 * commands MOVE and REPRODUCE will be evaluated.
 * 
 * Usage instructions:
 * 
 * Construct a Grid
 * Grid grid = new Grid(numRows, numColumns);
 * 
 * Add an animal
 * grid.placeOnGrid(animal, row, col);
 * 
 * Moving animals
 * The move command can be called to move all animals on the grid, animals on 
 * specific grid locations, or only animals the specified class or type.
 * grid.findAnimalsToMove();
 * grid.findAnimalsToMove(row, col);
 * grid.findAnimalsToMove(type or class);
 * All of these methods call the moveAnimals(animal, temp) method which
 * actually handles moving the specific animal.
 * 
 * Reproducing animals
 * The reproduce command can be called to the first two animals on each
 * location and determine if they can reproduce. The command can be further 
 * specified to include a specific grid location or a specific animal type
 * which will be the only type able to reproduce.
 * grid.findAnimalsToReproduce(grid);
 * grid.findAnimalsToReproduce(row, col, grid);
 * grid.findAnimalsToReproduce(type, grid);
 * All of these methods call the reproduce(column, grid) method which handles
 * creating a new animal object and placing it on the grid. 
 * 
 * Eating animals
 * The eat command will be called on the first two animals at each location and
 * can be further specified to one grid location or one specific animal type.
 * If an animal is eaten it is removed from the ecosystem.
 * grid.eat();
 * grid.eat(row, col);
 * grid.eat(type);
 * grid.eatAnimal(list);
 * 
 * Drawing the ecosystem for javafx
 * grid.drawEcosystem
 * Tells the canvas where to place each rectangle and what color it is
 * depending on if the grid location is empty or has animals in it. If there
 * are animals there a random color is assigned to each animal.
 * 
 * Other Useful methods:
 * grid.evaluateBoard();
 * grid.toString();
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Grid {
    // Variable fields used for Grid object construction.
    private List<List<List<Animal>>> grid;
    private Map<Animal, Color> animalColors;
    private Set<List<Float>> colorValues;
    final private float RGB_MAX = 255;

    /*
     * Purpose: Constructor method that creates a grid of size row x columns.
     * 
     * @param rows, number of rows. cols, number of columns.
     */
    public Grid(int rows, int cols) {
        grid = new ArrayList<List<List<Animal>>>();
        for (int i = 0; i < rows; i++) {
            grid.add(new ArrayList<List<Animal>>());
            for (int j = 0; j < cols; j++) {
                grid.get(i).add(new ArrayList<Animal>());
            }
        }
        colorValues = new HashSet<List<Float>>();
        animalColors = new HashMap<Animal, Color>();
    }

    /*
     * Purpose: places an animal object onto the grid.
     * 
     * @param animal is an animal object, row and col are integers to correlate
     * to grid coordinates.
     */
    public void placeOnGrid(Animal animal, int row, int col) {
        grid.get(row).get(col).add(animal);
    }

    // Purpose: finds all animals on the grid and calls a method to move them.
    public void findAnimalsToMove() {
        Grid temp = new Grid(grid.size(), grid.get(0).size());
        for (List<List<Animal>> row : grid) {
            for (List<Animal> col : row) {
                for (Animal animal : col) {
                    moveAnimals(animal, temp);
                }
            }
        }
        grid = temp.grid;
        evaluateBoard();
    }

    /*
     * Purpose: Finds the animals at a specific grid coordinate and calls the
     * function to move them.
     * 
     * @param row and col are integers that are grid coordinates.
     */
    public void findAnimalsToMove(int row, int col) {
        Grid temp = new Grid(grid.size(), grid.get(0).size());
        for (int x = 0; x < grid.size(); x++) {
            for (int y = 0; y < grid.get(x).size(); y++) {
                for (int a = 0; a < grid.get(x).get(y).size(); a++) {
                    Animal animal = grid.get(x).get(y).get(a);
                    // Moves the animal if they are on the given coordinates.
                    if (x == row && y == col) {
                        moveAnimals(animal, temp);
                    } else {
                        // Places the unmoved animals on the new grid.
                        temp.placeOnGrid(animal, animal.getX(), animal.getY());
                    }
                }
            }
        }
        grid = temp.grid;
        evaluateBoard();
    }

    /*
     * Purpose: Finds animals that fit the class or type given and calls the
     * method to move the ones that match.
     * 
     * @param word is either an animal subclass or animal type.
     */
    public void findAnimalsToMove(String word) {
        Grid temp = new Grid(grid.size(), grid.get(0).size());
        List<String> classes = Arrays.asList("mammal", "bird", "insect");
        for (List<List<Animal>> row : grid) {
            for (List<Animal> col : row) {
                for (Animal animal : col) {
                    if (classes.contains(word)) {
                        if ((word.equals("mammal") && animal instanceof Mammal)
                                || (word.equals("bird")
                                        && animal instanceof Bird)
                                || (word.equals("insect")
                                        && animal instanceof Insect)) {
                            moveAnimals(animal, temp);
                        } else {
                            temp.placeOnGrid(animal, animal.getX(),
                                    animal.getY());
                        }
                    } else {
                        if (animal.getType().equals(word)) {
                            moveAnimals(animal, temp);
                        } else {
                            temp.placeOnGrid(animal, animal.getX(),
                                    animal.getY());
                        }
                    }
                }
            }
        }
        grid = temp.grid;
        evaluateBoard();
    }

    /*
     * Purpose: Takes the animals that are passed to it and moves them to a new
     * position the takeStep method in the animals respective class.
     * 
     * @param animal is an Animal object and temp is a Grid object.
     */
    public void moveAnimals(Animal animal, Grid temp) {
        animal.move(grid.size(), grid.get(0).size());
        temp.placeOnGrid(animal, animal.getX(), animal.getY());
        // Casts each animal to its proper subclass.
        if (animal instanceof Mammal) {
            Mammal mammal = (Mammal) animal;
            mammal.takeStep();
        } else if (animal instanceof Bird) {
            Bird bird = (Bird) animal;
            bird.takeStep();
        } else {
            Insect insect = (Insect) animal;
            insect.takeStep();
        }
    }

    /*
     * Purpose: Finds every pair of animals on the grid and calls the reproduce
     * method to determine if they can reproduce.
     * 
     * @param theGrid is a Grid object.
     */
    public void findAnimalsToReproduce(Grid theGrid) {
        for (List<List<Animal>> row : grid) {
            for (List<Animal> col : row) {
                reproduce(col, theGrid);
            }
        }
    }

    /*
     * Purpose: Finds the first pair of animals at the given coordinates and
     * calls the reproduce method to determine if they can reproduce.
     * 
     * @param row and col are integers that represent grid coordinates. theGrid
     * is a Grid object.
     */
    public void findAnimalsToReproduce(int row, int col, Grid theGrid) {
        reproduce(grid.get(row).get(col), theGrid);
    }

    /*
     * Purpose: Finds animal pairs that fit the given type and calls the
     * reproduce method to determine if they can reproduce.
     * 
     * @param type is a string that references an animal type and theGrid is a
     * Grid object.
     */
    public void findAnimalsToReproduce(String type, Grid theGrid) {
        for (List<List<Animal>> row : grid) {
            for (List<Animal> col : row) {
                if (col.size() > 1 && col.get(0).getType().equals(type)) {
                    reproduce(col, theGrid);
                }
            }
        }
    }

    /*
     * Purpose: Determines if the animals at a given location can reproduce
     * with one another and creates a new animal object at the some location.
     * 
     * @param col is a list of animal objects and theGrid is a Grid object.
     */
    public void reproduce(List<Animal> col, Grid theGrid) {
        // Determines if the first two animals share the same type and are of
        // Opposite genders.
        if (col.size() > 1 && col.get(0).getType().equals(col.get(1).getType())
                && !col.get(0).getGender().equals(col.get(1).getGender())) {
            if (col.get(0) instanceof Mammal) {
                Mammal mammal1 = (Mammal) col.get(0);
                Mammal mammal2 = (Mammal) col.get(1);
                mammal1.reproduce(mammal2, theGrid);
            } else if (col.get(0) instanceof Bird) {
                Bird bird1 = (Bird) col.get(0);
                Bird bird2 = (Bird) col.get(1);
                bird1.reproduce(bird2, theGrid);
            } else if (col.get(0) instanceof Insect) {
                Insect insect1 = (Insect) col.get(0);
                Insect insect2 = (Insect) col.get(1);
                insect1.reproduce(insect2, theGrid);
            }
        }
    }

    /*
     * Purpose: Finds the first pair of animals at each grid location and 
     * calls the eatAnimal method to determine if they will eat each other.
     * 
     * @param None.
     */
    public void eat() {
        for (List<List<Animal>> row : grid) {
            for (List<Animal> col : row) {
                if (col.size() > 1) {
                    eatAnimal(col);
                }
            }
        }
    }

    /*
     * Purpose: Finds the first pair of animals at the given coordinates and
     * calls the eatAnimal method to determine if they will eat each other.
     * 
     * @param row and col are integers that represent grid coordinates.
     */
    public void eat(int row, int col) {
        List<Animal> list = grid.get(row).get(col);
        if (list.size() > 1) {
            eatAnimal(list);
        }
    }

    /*
     * Purpose: Finds the first pair of animals at each grid location and
     * determines if they match the type given and if one animal can eat
     * the other.
     * 
     * @param type is a string that represents an animal type.
     */
    public void eat(String type) {
        for (List<List<Animal>> row : grid) {
            for (List<Animal> col : row) {
                if (col.size() > 1) {
                	// Determines if the first animal can eat the second.
                    if (col.get(0).getType().equals(type)) {
                        if ((col.get(0) instanceof Mammal
                                && col.get(1) instanceof Mammal)
                                || (col.get(0) instanceof Bird
                                        && col.get(1) instanceof Insect)) {
                            col.get(1).setIsAlive();
                            col.get(0).resetMovesSinceEaten();
                        }
                        // Determines if the second animal can eat the first.
                    } else if (col.get(1).getType().equals(type)) {
                        if ((col.get(1) instanceof Mammal
                                && col.get(0) instanceof Mammal)
                                || (col.get(1) instanceof Bird
                                        && col.get(0) instanceof Insect)) {
                            col.get(0).setIsAlive();
                            col.get(1).resetMovesSinceEaten();
                        }
                    }
                }
            }
        }
        evaluateBoard();
    }

    /*
     * Purpose: Determines if the first two animals in the list can eat each
     * other and removes the eat one and resets the sinceEaten counter on the
     * animal that ate.
     * 
     * @param col is a List<Animal> and is the list at a specific row and col
     * in the grid.
     */
    public void eatAnimal(List<Animal> col) {
        if (col.get(0) instanceof Mammal && col.get(1) instanceof Mammal) {
            col.get(1).setIsAlive();
            col.get(0).resetMovesSinceEaten();

        } else if ((col.get(0) instanceof Bird && col.get(1) instanceof Insect)
                || (col.get(0) instanceof Insect
                        && col.get(1) instanceof Bird)) {
            if (col.get(0) instanceof Bird) {
                col.get(1).setIsAlive();
                col.get(0).resetMovesSinceEaten();
            } else {
                col.get(0).setIsAlive();
                col.get(1).resetMovesSinceEaten();
            }
        }
        evaluateBoard();
    }

    // Checks if animals have been moved, eaten, or died of old age or starved
    public void evaluateBoard() {
        Grid temp = new Grid(grid.size(), grid.get(0).size());
        for (List<List<Animal>> row : grid) {
            for (List<Animal> coll : row) {
                for (Animal animal : coll) {
                    if (animal.getIsAlive()) {
                        temp.placeOnGrid(animal, animal.getX(), animal.getY());
                    }
                }
            }
        }
        grid = temp.grid;
    }

    /*
     * Purpose: Fills the canvas with a light brown if no animals are in that
     * location and calls a function to assign a random color to each animal 
     * and makes a rectangle at the location the animal is at.
     * 
     * @param gc is the GraphicsContext that is the graphic area of the GUI,
     * rectSize is the size that each rectangle should be.
     */
    public void drawEcosystem(GraphicsContext gc, int rectSize) {
    	final Integer[] BROWN_RGB_VALUES = {222, 184, 135};
        colorValues.add(Arrays.asList(BROWN_RGB_VALUES[0] / RGB_MAX,
        		BROWN_RGB_VALUES[1] / RGB_MAX, BROWN_RGB_VALUES[2] / RGB_MAX));
        for (int y = 0; y < grid.size(); y++) {
            for (int x = 0; x < grid.get(0).size(); x++) {
                if (grid.get(y).get(x).size() > 0) {
                	// Checks if the animals already has a color assigned.
                    if (!animalColors.containsKey(grid.get(y).get(x).get(0))) {
                        assignColor(y, x);
                    }
                    // Sets color to animal's color.
                    gc.setFill(animalColors.get(grid.get(y).get(x).get(0)));
                } else {
                	// Sets color to light brown for empty locations.
                	gc.setFill(new Color(BROWN_RGB_VALUES[0] / RGB_MAX, 
                			BROWN_RGB_VALUES[1] / RGB_MAX, 
                			BROWN_RGB_VALUES[2] / RGB_MAX, 1));
                }
                gc.fillRect(x * rectSize, y * rectSize, rectSize, rectSize);
            }
        }
    }
    
    /*
     * Purpose: Randomly generates a color and compares it to a set of
     * previously used color values. If it's not in the set then uses that
     * set of rgb values.
     * 
     * @param y and x are the current grid locations for the animal being 
     * assigned a color.
     */
    public void assignColor(int y, int x) {
    	Random rand = new Random();
    	boolean found = false;
        while (!found) {
            float r = rand.nextFloat();
            float g = rand.nextFloat();
            float b = rand.nextFloat();
            List<Float> values = Arrays.asList(r, g, b);
            if (!colorValues.contains(values)) {
                colorValues.add(values);
                Color c = new Color(r, g, b, 1);
                found = true;
                animalColors.put(grid.get(y).get(x).get(0), c);
            }
        }
    }

    // Purpose: Returns a string representation of a Grid object.
    public String toString() {
        String s = "";
        for (int x = 0; x < grid.size(); x++) {
            for (int y = 0; y < grid.get(0).size(); y++) {
                if (grid.get(x).get(y).isEmpty()) {
                    s += ".";
                } else {
                    s += grid.get(x).get(y).get(0).getType().substring(0, 1);
                }
            }
            s += "\n";
        }
        return s;
    }
}
