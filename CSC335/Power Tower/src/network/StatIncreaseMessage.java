package network;

import viewable.cards.Card;

/**
 * StatIncreaseMessage.java
 * 
 * Creates a message to be sent over the network that tells the other
 * player that health or gold values were modified.
 * 
 * Usage instructions:
 * 
 * Construct StatIncreaseMessage
 * StatIncreaseMessage sim = new StatIncreaseMessage(card)
 * 
 */

public class StatIncreaseMessage extends TowerDefenseMoveMessage{
	
	// Field variables for StatIncreaseMessage object
		private Card card;
	private int healthIncrease;
	private int goldIncrease;
	
	/**
	 * purpose: Initializes StatIncreaseMessage.
	 * 
	 * @param h - amount to change health by
	 * 
	 * @param g - amount to change gold by
	 */
	public StatIncreaseMessage(int h, int g) {
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
