package game;

/**
 * ViewModel.java
 * 
 * Determines the dimensions for the view.
 * 
 * Usage instructions:
 * 
 * Construct ViewModel
 * ViewModel vModel = new ViewModel(height, width)
 * 
 * Other useful methods:
 * vModel.getHeight()
 * vModel.setHeigh(h)
 * vModel.getWidth()
 * vModel.getEffectiveWidth()
 * vModel.addSubWidth(m)
 * vModel.setWidth(w)
 * vModel.getSubtractedHeight()
 * vModel.addSubHeight(m)
 * vModel.getEffectiveBoardHeight()
 * vModel.getCurrentRow()
 * vModel.setCurrentRow(c)
 * vModel.getCurrentCol()
 * vModel.setCurrentCol(c)
 */

import java.util.Observable;

public class ViewModel extends Observable {
	
	// Field variables for ViewModel Objects
	private int screenHeight;
	private int screenWidth;
	private int subtractedHeight;
	private int subtractedWidth;
	private int currentRow;
	private int currentCol;
	
	/**
	 * purpose: Initializes a ViewModel object with all of its attributes.
	 * 
	 * @param height - the height of the window to be created by this view
	 * 
	 * @param width - the width of the window to be created by this view
	 */
	public ViewModel(int height, int width) {
		screenHeight = height;
		screenWidth = width;
		subtractedHeight = 0;
		subtractedWidth = 0;
	}

	// Getter method for the height attribute
	public int getHeight() {
		return screenHeight;
	}
	
	// Setter method for the height attribute
	public void setHeight(int h) {
		screenHeight = h;
	}
	
	// Getter method for the width attribute
	public int getWidth() {
		return screenWidth;
	}
	
	// Getter method for the width of playable area
	public int getEffectiveWidth() {
		return screenWidth-subtractedWidth;
	}
	
	/**
	 * purpose: increases how much of the window is subtracted from view.
	 * 
	 * @param m - the amount to be subtracted
	 * 
	 */
	public void addSubWidth(int m) {
		subtractedWidth += m;
	}
	
	// Setter method for width attribute
	public void setWidth(int w) {
		screenWidth = w;
	}
	
	// Getter method for the height of playable area
	public int getSubtractedHeight() {
		return subtractedHeight;
	}
	
	/**
	 * purpose: increases how much of the window is subtracted from view.
	 * 
	 * @param m - the amount to be subtracted
	 * 
	 */
	public void addSubHeight(int m) {
		subtractedHeight += m;
	}
	
	// Getter method for height of the playable area
	public int getEffectiveBoardHeight() {
		return screenHeight-subtractedHeight;
	}
	
	// Getter method for currentRow
	public int getCurrentRow() {
		return currentRow;
	}
	
	// Setter method for CurrentRow
	public void setCurrentRow(int c) {
		currentRow = c;
	}
	
	// Getter method for currentCol
	public int getCurrentCol() {
		return currentCol;
	}
	
	// Setter method for currentCol
	public void setCurrentCol(int c) {
		currentCol = c;
	}
}
