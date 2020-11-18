/**
 * Bird.java
 * 
 * Represents a Bird object and determines how they will move about the
 * grid and how they will reproduce.
 * 
 * Usage instructions:
 * 
 * Construct a Bird
 * Animal animal = new Bird(row, col, type, gender, steps);
 * 
 * Moving a Bird
 * bird.takeStep();
 * Determines where in the grid the animal will move to depending on the
 * direction it is going and the steps it has to take.
 * 
 * Bird reproduction
 * Bird.reproduce(parent, grid)
 * Creates a new Bird object at the given location.
 * 
 * Other useful methods:
 * bird.getSteps();
 * bird.getStepsTaken();
 * 
 */
public class Bird extends Animal {
    // Variable fields used for Bird object construction.
    private int steps;
    private int stepsTaken;

    /*
     * Purpose: Constructor method that creates a Bird object at coordinates
     * (x, y), and a type and gender and steps.
     * 
     * @param x is the grid row. y it the grid column. type is the animal type.
     * gender is the animal gender. steps determines how many moves the bird
     * will take before chaning directions.
     */
    public Bird(int x, int y, String type, String gender, int steps) {
        super(x, y, type, gender);
        this.steps = steps;
        stepsTaken = 0;
        this.setNextDirection("down");
    }

    // Purpose: Determines the next direction the bird will move when the MOVE
    // command is next called.
    public void takeStep() {
        stepsTaken++;
        // Changes direction when stepsTaken equals the number of steps the
        // bird was created to take.
        if (stepsTaken == steps) {
            stepsTaken = 0;
            if (this.getNextDirection().equals("down")) {
                this.setNextDirection("right");
            } else if (this.getNextDirection().equals("right")) {
                this.setNextDirection("up");
            } else {
                this.setNextDirection("down");
            }
        }
    }

    /*
     * Purpose: Creates a new Bird object at the parents location.
     * 
     * @param parent is a Bird object used to determine if both parents can
     * reproduce or not. grid is a Grid object.
     */
    public void reproduce(Bird parent, Grid grid) {
        Animal baby = new Bird(this.getX(), this.getY(), this.getType(),
                "female", 5);
        grid.placeOnGrid(baby, baby.getX(), baby.getY());
    }

    // Getter method for steps.
    public int getSteps() {
        return steps;
    }

    // Getter method for stepsTaken.
    public int getStepsTaken() {
        return stepsTaken;
    }

    // Purpose: Returns a string representation of a Bird object.
    public String toString() {
        return this.getGender() + " " + this.getType() + "has taken "
                + stepsTaken + " steps out of " + steps;
    }

}
