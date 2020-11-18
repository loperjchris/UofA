/**
 * Mosquito.java
 * 
 * Represents a Mosquito object and determines how they will move about the
 * grid and how they will reproduce.
 * 
 * Usage instructions:
 * 
 * Construct a Mosquito
 * Animal animal = new Mosquito(row, col, type, gender, clockwise, parent1,
 * parent2);
 * 
 * Mosquito reproduction
 * mosquito.reproduce(parent, grid)
 * Creates a new Mosquito object at the given location if the parents meet the
 * reproduction requirements of mosquitos. If both have parent1 and parent2 set
 * to true then the mosquitos cannot reproduce.
 * 
 * Other useful methods:
 * mosquito.parent1();
 * mosquito.parent2();
 * 
 */
public class Mosquito extends Insect {
    // Variable fields used for Mosquito object construction.
    private Boolean parent1;
    private Boolean parent2;

    /*
     * Purpose: Constructor method that creates a Mosquito object at
     * coordinates (x, y), and a type and gender and clockwise and parent1 and
     * parent2.
     * 
     * @param x is the grid row. y it the grid column. type is the animal type.
     * gender is the animal gender. clockwise is a boolean that if true the
     * animal moves clockwise and if false the animal moves counter clockwise.
     * parent1 and parent2 are booleans that determine if the mosquito can
     * reproduce or not.
     */
    public Mosquito(int x, int y, String type, String gender, Boolean clockwise,
            Boolean parent1, Boolean parent2) {
        super(x, y, type, gender, clockwise);
        this.parent1 = parent1;
        this.parent2 = parent2;
    }

    // Purpose: Determines the next direction the bird will move when the MOVE
    // command is next called. Mosquitos cannot reproduce if either parent's
    // have both parent1 and parent2 set to true.
    public void reproduce(Mosquito parent, Grid grid) {
        Boolean genModParent1 = false;
        Boolean genModParent2 = false;
        if (!(this.getParent1() && this.getParent2())
                && !(parent.getParent1() && parent.getParent2())) {
            if (this.getParent1() || this.getParent2()) {
                genModParent1 = true;
            }
            if (parent.getParent1() || parent.getParent2()) {
                genModParent2 = true;
            }
            Animal baby = new Mosquito(this.getX(), this.getY(), this.getType(),
                    "female", false, genModParent1, genModParent2);
            grid.placeOnGrid(baby, baby.getX(), baby.getY());
        }
    }

    // Getter method for parent1.
    public Boolean getParent1() {
        return parent1;
    }

    // Getter method for parent2.
    public Boolean getParent2() {
        return parent2;
    }

    // Purpose: Returns a string representation of an Insect object.
    public String toString() {
        return this.getGender() + " " + this.getType() + " parent 1: " + parent1
                + ", parent2: " + parent2;
    }
}
