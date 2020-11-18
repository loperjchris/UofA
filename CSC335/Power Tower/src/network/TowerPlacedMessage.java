package network;

/**
 * TowerPlacedMessage.java
 * 
 * Creates a message to be sent over the network that tells the other
 * player that a tower was placed on the grid by the other player.
 * 
 * Usage instructions:
 * 
 * Construct TowerPlacedMessage
 * TowerPlacedMessage tpm = new TowerPlacedMessage(card)
 * 
 */

import viewable.gameObjects.TowerType;

public class TowerPlacedMessage extends TowerDefenseMoveMessage{
	
	// Field variable for TowerPlacedMessage object
	private TowerType tower;
	private int row;
	private int col;
	
	/**
	 * purpose: Initializes TowerPlacedMessage.
	 * 
	 * @param tower - a TowerType object that tells what kind of tower
	 * was placed
	 * 
	 * @param row - the row in the grid where the tower was placed
	 * 
	 * @param col - the column in the grid where the tower was placed
	 */
	public TowerPlacedMessage(TowerType tower, int row, int col) {
		this.tower = tower;
		this.row = row;
		this.col = col;
	}
	
	// Getter method for tower
	public TowerType getTower() {
		return tower;
	}
	
	// Getter method for row
	public int getRow() {
		return row;
	}
	
	// Getter method for col
	public int getCol() {
		return col;
	}
}
