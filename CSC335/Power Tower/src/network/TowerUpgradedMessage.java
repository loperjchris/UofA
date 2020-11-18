package network;

/**
 * TowerUpgradeMessage.java
 * 
 * Creates a message to be sent over the network that tells the other
 * player that a tower was upgraded by the other palyer.
 * 
 * Usage instructions:
 * 
 * Construct TowerUpgradeMessage
 * TowerUpgradeMessage tum = new TowerUpgradeMessage(card)
 * 
 */

public class TowerUpgradedMessage extends TowerDefenseMoveMessage{
	
	// Field variable for TowerUpgradeMessage object
	private int row;
	private int col;

	/**
	 * purpose: Initializes TowerUpgradeMessage.
	 * 
	 * @param row - the row in the grid of the tower that was upgraded
	 * 
	 * @param col - the column in the grid of the tower that was upgraded
	 */
	public TowerUpgradedMessage(int row, int col) {
		this.row = row;
		this.col = col;
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
