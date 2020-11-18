package edu.spr19cs210.drill05;

import java.util.List;

public class Drill05 {

    public static Shape getShapeFromGrid(List<List<Shape>> grid, int row,
            int col) {
        return grid.get(row).get(col);
    }

    public static boolean isTriangle(List<List<Shape>> grid, int row, int col) {
        return grid.get(row).get(col) instanceof Triangle;
    }

    public static boolean isSquare(List<List<Shape>> grid, int row, int col) {
        return grid.get(row).get(col) instanceof Square;
    }

    // The amount of nesting you are going to use will probably be painful.
    // Do you have ideas on how to encapsulate some of it for PA6?
    public static String byRow(List<List<String>> grid) {
        String s = "";
        grid.remove(null);
        for (List<String> row : grid) {
            row.remove(null);
            for (String col : row) {
                if (!col.equals(null)) {
                    s += col;
                }
            }
        }
        return s;
    }

    public static void setArrayElem(char[][] screen, int row, int col, char c) {
        screen[row][col] = c;
    }

}
