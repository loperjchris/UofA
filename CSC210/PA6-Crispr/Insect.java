/**
 * Insect.java
 * 
 * Represents an Insect object and determines how they will move about the
 * grid and how they will reproduce.
 * 
 * Usage instructions:
 * 
 * Construct an Insect
 * Animal animal = new Insect(row, col, type, gender, clockwise);
 * 
 * Moving an Insect
 * insect.takeStep();
 * Determines where in the grid the animal will move to depending on the
 * direction it is going and the steps it has to take.
 * 
 * Insect reproduction
 * insect.reproduce(parent, grid)
 * Creates a new insect object at the given location.
 * 
 * Other useful methods:
 * insect.getClockwise();
 * insect.getStepsTaken();
 * insect.getTotalSteps();
 * 
 */
public class Insect extends Animal {
    // Variable fields used for Insect object construction.
    private Boolean clockwise;
    private int stepsTaken;
    private int totalSteps;

    /*
     * Purpose: Constructor method that creates an Insect object at coordinates
     * (x, y), and a type and gender and clockwise.
     * 
     * @param x is the grid row. y it the grid column. type is the animal type.
     * gender is the animal gender. clockwise is a boolean that if true the
     * animal moves clockwise and if false the animal moves counter clockwise.
     */
    public Insect(int x, int y, String type, String gender, Boolean clockwise) {
        super(x, y, type, gender);
        this.clockwise = clockwise;
        stepsTaken = 0;
        totalSteps = 1;
        this.setNextDirection("left");
    }

    // Purpose: Determines the next direction the bird will move when the MOVE
    // command is next called.
    public void takeStep() {
        stepsTaken++;
        // Next direction if the insect is moving clockwise.
        if (stepsTaken == totalSteps) {
            stepsTaken = 0;
            if (this.getClockwise()) {
                if (this.getNextDirection().equals("left")) {
                    this.setNextDirection("up");
                } else if (this.getNextDirection().equals("up")) {
                    this.setNextDirection("right");
                } else if (this.getNextDirection().equals("right")) {
                    this.setNextDirection("down");
                } else {
                    this.setNextDirection("left");
                    totalSteps++;
                }
                // Next direction if the insect is moving counter clockwise.
            } else {
                if (this.getNextDirection().equals("left")) {
                    this.setNextDirection("down");
                } else if (this.getNextDirection().equals("down")) {
                    this.setNextDirection("right");
                } else if (this.getNextDirection().equals("right")) {
                    this.setNextDirection("up");
                } else {
                    this.setNextDirection("left");
                    totalSteps++;
                }
            }
        }
    }

    /*
     * Purpose: Creates a new Insect object at the parents location.
     * 
     * @param parent is an Insect object used to determine if both parents can
     * reproduce or not. grid is a Grid object.
     */
    public void reproduce(Insect parent, Grid grid) {
        // Reproduce like normal if the insects aren't mosquitos.
        if (!this.getType().equals("mosquito")) {
            Animal baby = new Insect(this.getX(), this.getY(), this.getType(),
                    "female", false);
            grid.placeOnGrid(baby, baby.getX(), baby.getY());
            // If they are mosquitos call the reproduce method in Mosquito
            // class.
        } else {
            Mosquito mosquito1 = (Mosquito) this;
            Mosquito mosquito2 = (Mosquito) parent;
            mosquito1.reproduce(mosquito2, grid);
        }
    }

    // Getter method for clockwise.
    public Boolean getClockwise() {
        return clockwise;
    }

    // Getter method for stepsTaken.
    public int getStepsTaken() {
        return stepsTaken;
    }

    // Getter method for totalSteps.
    public int getTotalSteps() {
        return totalSteps;
    }

    // Purpose: Returns a string representation of an Insect object.
    public String toString() {
        String direction = "";
        if (clockwise) {
            direction = "clockwise";
        } else {
            direction = "counter clockwise";
        }
        return this.getGender() + " " + this.getType() + " is moving "
                + direction + " and has taken " + stepsTaken + " out of "
                + totalSteps;
    }

}
