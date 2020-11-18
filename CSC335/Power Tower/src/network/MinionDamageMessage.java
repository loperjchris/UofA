package network;

/**
 * MinionDamageMessage.java
 * 
 * Creates a message to be sent over the network that tells the other
 * player how much damage is being done to a minion.
 * 
 * Usage instructions:
 * 
 * Construct MinionDamageMessage
 * MinionDamageMessage mdm = new MinionDamageMessage(card)
 * 
 */

public class MinionDamageMessage extends TowerDefenseMoveMessage{
	
	// Field variable for MinionDamageMessage object
	private int amount;
	
	/**
	 * purpose: Initializes MinionDamageMessage.
	 * 
	 * @param amount - the amount of damage being dealt
	 */
	public MinionDamageMessage(int amount) {
		this.amount = amount;
	}
	
	// Getter method for amount
	public int getAmount() {
		return amount;
	}
}
