import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Mammal.java
 * 
 * Represents a Mammal object and determines how they will move about the
 * grid and how they will reproduce.
 * 
 * Usage instructions:
 * 
 * Construct a Mammal
 * Animal animal = new Mammal(row, col, type, gender, direction);
 * 
 * Moving a Mammal
 * mammal.takeStep();
 * Determines where in the grid the animal will move to depending on the
 * direction it is going.
 * 
 * Mammal reproduction
 * mammal.reproduce(parent, grid)
 * Ensures that the mammals reproduction limit hasn't been reached and creates
 * a new mammal object at the same location if it hasn't. Increases
 * reproduction count by 1.
 * 
 * Other useful methods:
 * mammal.getCurrentReproNum();
 * mammal.increaseReproNum();
 */
public class Mammal extends Animal {
    // Variable fields used for Mammal object construction.
    private int currentReproNum;
    private final int REPRO_LIMIT = 5;
    private final int MAX_MOVES = 25;
    private final int MAX_SINCE_EATEN = 10;

    /*
     * Purpose: Constructor method that creates a Mammal object at coordinates
     * (x, y), and a type and gender and direction.
     * 
     * @param x is the grid row. y it the grid column. type is the animal type.
     * gender is the animal gender. direction determines the direction it will
     * travel next when moving.
     */
    public Mammal(int x, int y, String type, String gender, String direction) {
        super(x, y, type, gender);
        currentReproNum = 0;
        if (direction.equals("right")) {
            this.setNextDirection("down");
        } else {
            this.setNextDirection("up");
        }
    }

    // Purpose: Determines the next direction the mammal will move when the
    // MOVE command is next called.
    public void takeStep() {
    	// Checks to see if the animal hasn't died from age or starving.
        if (this.getTotalMoves() <= MAX_MOVES
                && this.getMovesSinceEaten() <= MAX_SINCE_EATEN) {
            if (this.getNextDirection().equals("down")) {
                this.setNextDirection("right");
            } else if (this.getNextDirection().equals("right")) {
                this.setNextDirection("down");
            } else if (this.getNextDirection().equals("up")) {
                this.setNextDirection("left");
            } else {
                this.setNextDirection("up");
            }
        } else {
            this.setIsAlive();
        }

    }

    /*
     * Purpose: Determines if the mammal can still reproduce and creates a new
     * mammal object at its location and increases its reproduction count if it
     * can still reproduce.
     * 
     * @param parent is a Mammal object used to determine if both parents can
     * reproduce or not. grid is a Grid object.
     */
    public void reproduce(Mammal parent, Grid grid) {
        Random rand = new Random();
        List<String> genders = Arrays.asList("female", "male");
        List<String> directions = Arrays.asList("right", "left");
        int gender = rand.nextInt(2);
        int direction = rand.nextInt(2);
        if (this.getCurrentReproNum() < REPRO_LIMIT
                && parent.getCurrentReproNum() < REPRO_LIMIT) {
            this.increaseReproNum();
            parent.increaseReproNum();
            Animal baby = new Mammal(this.getX(), this.getY(), this.getType(),
                    genders.get(gender), directions.get(direction));
            grid.placeOnGrid(baby, baby.getX(), baby.getY());
        }
    }

    // Getter method for currentReproNum.
    public int getCurrentReproNum() {
        return currentReproNum;
    }

    // Increases currentReproNum by 1.
    public void increaseReproNum() {
        currentReproNum++;
    }

    // Purpose: Returns a string representation of a Mammal object.
    public String toString() {
        return this.getGender() + " " + this.getType() + " has reproduced "
                + currentReproNum + " times.";
    }
}
