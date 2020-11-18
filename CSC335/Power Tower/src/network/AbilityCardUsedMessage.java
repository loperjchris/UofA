/**
 * AbilityCardUsedMessage.java
 * 
 * Creates a message to be sent over the network that tells the other
 * player that an ability card was used and which one it was.
 * 
 * Usage instructions:
 * 
 * Construct AbilityCardUsedMessage
 * AbilityCardUsedMessage acum = new AbilityCardUsedMessage(card)
 * 
 */

package network;

import viewable.cards.Card;

public class AbilityCardUsedMessage extends TowerDefenseMoveMessage{
	
	// Field variable for AbilityCardUsedMessage object
	private Card card;
	
	/**
	 * purpose: Initializes AbilityCardUsedMessage.
	 * 
	 * @param card - the Card object being used
	 */
	public AbilityCardUsedMessage(Card card) {
		this.card = card;
	}
	
	// Getter method for card
	public Card getCard() {
		return card;
	}
}
