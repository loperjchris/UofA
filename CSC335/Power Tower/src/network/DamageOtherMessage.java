package network;

/**
 * DamageOtherMessage.java
 * 
 * Creates a message that can be sent over the network and tells
 * the other player which type of damage is being received
 * 
 * Usage instructions:
 * 
 * Construct DamageOtherMessage
 * DamageOtherMessage dom = new DamageOtherMessage(amount)
 * 
 */

public class DamageOtherMessage extends TowerDefenseMoveMessage{
	
	// Field variable for DamageOtherMessage object
	private int amount;

	/**
	 * purpose: Initializes DamageOtherMessage.
	 * 
	 * @param amount - the amount of damage done
	 */
	public DamageOtherMessage(int amount) {
		this.amount = amount;
	}
	
	// Getter method for amount
	public int getAmount() {
		return amount;
	}
}
