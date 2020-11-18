/**
 * Animal.java
 * 
 * Represents an Animal object and determines how they will move about the
 * grid.
 * 
 * Usage instructions:
 * 
 * Construct an Animal
 * Animal animal = new Animal(row, col, type, gender);
 * 
 * Moving an animal
 * animal.move(number of rows in the grid, number of columns in the grid);
 * Determines where in the grid the animal will move to and sets its next
 * direction vector as well as handling grid wrap around.
 * 
 * Other useful methods:
 * animal.getX();
 * animal.getY();
 * animal.setCoordinates(x, y);
 * animal.getType();
 * animal.getGender();
 * animal.getNextDirection();
 * animal.setNextDirection(direction);
 */
public class Animal {
    // Variable fields used for Animal object construction.
    private int x;
    private int y;
    private String type;
    private String gender;
    private String nextDirection;

    /*
     * Purpose: Constructor method that creates an Animal object at coordinates
     * (x, y), and a type and gender.
     * 
     * @param x is the grid row. y it the grid column. type is the animal type.
     * gender is the animal gender.
     */
    public Animal(int x, int y, String type, String gender) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.gender = gender;
    }

    /*
     * Purpose: Determines where to move the animal and sets its next direction
     * and handles wrap around for grid placement.
     * 
     * @param xLimit is the amount of rows in the grid. yLimit is the amount of
     * columns in the grid.
     */
    public void move(int xLimit, int yLimit) {
        int xTemp = this.getX();
        int yTemp = this.getY();
        // Determines the coordinates to move to.
        if (this.getNextDirection().equals("down")) {
            xTemp++;
        } else if (this.getNextDirection().equals("right")) {
            yTemp++;
        } else if (this.getNextDirection().equals("up")) {
            xTemp--;
        } else if (this.getNextDirection().equals("left")) {
            yTemp--;
        }
        // Handling wrap around.
        if (xTemp < 0) {
            xTemp = xLimit - 1;
        } else if (xTemp >= xLimit) {
            xTemp = 0;
        }
        if (yTemp < 0) {
            yTemp = yLimit - 1;
        } else if (yTemp >= yLimit) {
            yTemp = 0;
        }
        this.setCoordinates(xTemp, yTemp);
    }

    // Getter method for x.
    public int getX() {
        return x;
    }

    // Getter method for y.
    public int getY() {
        return y;
    }

    // Sets new coordinates for an Animal object.
    public void setCoordinates(int row, int col) {
        x = row;
        y = col;
    }

    // Getter method for type.
    public String getType() {
        return type;
    }

    // Getter method for gender.
    public String getGender() {
        return gender;
    }

    // Getter method for nextDirection.
    public String getNextDirection() {
        return nextDirection;
    }

    // Sets nextDirection to the desired direction.
    public void setNextDirection(String direction) {
        nextDirection = direction;
    }

    // Purpose: Returns a string representation of an Animal object.
    public String toString() {
        return gender + " " + type + " " + "is at: (" + x + ", " + y
                + "), and is going to move " + nextDirection;
    }
}
