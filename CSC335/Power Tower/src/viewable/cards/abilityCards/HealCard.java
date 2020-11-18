package viewable.cards.abilityCards;
/**
 * HealCard.java
 * 
 * Heals the user for healAmount health
 */
import java.io.File;
import java.io.IOException;

import viewable.cards.Card;
import viewable.gameObjects.Player;

// heals 1 life

public class HealCard extends AbilityCard {
	
	private static final int cardId = 9;
	private static final String cardName = "Heal";
	private static final int cardCost = 10;
	private static final int healAmount = 5;

	/**
	 *  Purpose - constructor for this class
	 */
	public HealCard() {
		super(cardName, cardId, cardCost);
	}

	/**
	 * Purpose - activates the ability for this card
	 * 
	 * @param p - Player object
	 */
	@Override
	public void ability(Player p) {
		p.gainLife(healAmount);
	}
	
	/**
	 * Purpose - gets the resource for the object
	 * 
	 * @return a String containing an image path
	 */
	@Override
	public String getResource() {
		try {
			return (new File("./resources/images/healCard.png")).getCanonicalPath();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return "";
		}
	}

}
