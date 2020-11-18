package network;

/**
 * OtherStatIncreaseMessage.java
 * 
 * Creates a message to be sent over the network that tells the other
 * player that health or gold values where modified.
 * 
 * Usage instructions:
 * 
 * Construct OtherStatIncreaseMessage
 * OtherStatIncreaseMessage osim = new OtherStatIncreaseMessage(card)
 * 
 */

public class OtherStatIncreaseMessage extends TowerDefenseMoveMessage{
	
	// Field variables for OtherStatIncreaseMessage object
	private int healthIncrease;
	private int goldIncrease;
	
	/**
	 * purpose: Initializes OtherStatIncreaseMessage.
	 * 
	 * @param h - amount to change health by
	 * 
	 * @param g - amount to change gold by
	 */
	public OtherStatIncreaseMessage(int h, int g) {
		this.healthIncrease = h;
		this.goldIncrease = g;
	}
	
	// Getter method for healthIncrease
	public int getHealth() {
		return healthIncrease;
	}
	
	// Getter method for goldIncrease
	public int getGold() {
		return goldIncrease;
	}
}
