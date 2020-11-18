package network;

/**
 * MarketCardRemovedMessage.java
 * 
 * Creates a message to be sent over the network that tells the other
 * player that a card was purchased from the market.
 * 
 * Usage instructions:
 * 
 * Construct MarketCardRemovedMessage
 * MarketCardRemovedMessage mcrm = new MarketCardRemovedMessage(index)
 * 
 */

import viewable.cards.Card;

public class MarketCardRemovedMessage extends TowerDefenseMoveMessage{
	
	// Field variable for MarketCardRemovedMessage object
	private int index;
	
	/**
	 * purpose: Initializes AbilityCardUsedMessage.
	 * 
	 * @param index - the index within the market list of the card that
	 * was purchased
	 */
	public MarketCardRemovedMessage(int index) {
		this.index = index;
	}
	
	// Getter method for index
	public int getIndex() {
		return index;
	}
}
