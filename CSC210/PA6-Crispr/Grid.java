
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
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Grid {
    // Variable fields used for Grid object construction.
    private List<List<List<Animal>>> grid;

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
     * Purpose: Finds the first pair of animals that the given coordinates and
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
