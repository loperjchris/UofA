package edu.spr19cs210.drill07;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Drill07 {

    /**
     * Increased red component of a color
     * 
     * @param oldColor
     *            Color to adapt
     * @return A color with the red component increased by 0.1
     */
    public static Color increaseRed(Color oldColor) {
        Color newColor = new Color(oldColor.getRed() + 0.1, oldColor.getGreen(),
                oldColor.getBlue(), oldColor.getOpacity());
        return newColor;

    }

    /**
     * Swaps the green and blue components of a color
     * 
     * @param oldColor
     *            Color to adapt
     * @return A color with the blue and green components swapped
     */
    public static Color swapGreenBlue(Color oldColor) {
        Color newColor = new Color(oldColor.getRed(), oldColor.getBlue(),
                oldColor.getGreen(), oldColor.getOpacity());
        return newColor;

    }

    /**
     * Sets the fill color of a canvas to RGB(255,0,0)
     * 
     * @param canvas
     *            Canvas to set the fill color on
     */
    public static void setFillColor(Canvas canvas) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.rgb(255, 0, 0));
    }

    /**
     * Sets the stroke color of a canvas to RGB(0,255,0)
     * 
     * @param canvas
     *            Canvas to set the stroke color on
     */
    public static void setStrokeColor(Canvas canvas) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setStroke(Color.rgb(0, 255, 0));
    }

    /**
     * Draws an oval on the canvas. Oval outline has a color
     * of RGB(0,0,255). Its upper-left corner is at (20,20)
     * and it should be 40 wide and 40 tall.
     * 
     * @param canvas
     */
    public static void drawOval(Canvas canvas) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setStroke(Color.rgb(0, 0, 255));
        gc.strokeOval(20, 20, 40, 40);

    }

}
