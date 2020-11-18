package viewable.cards.abilityCards;
/**
 * DamageCard.java
 * 
 * Deals 1 damage to opposing player
 */
import java.io.File;
import java.io.IOException;

import viewable.cards.Card;
import viewable.gameObjects.Player;
import viewable.gameObjects.Tower;


public class DamageCard extends AbilityCard {
	
	private static final int cardId = 10;
	private static final String cardName = "Bombing Pigeon";
	private static final int cardCost = 10;
	private static final int damageAmount = 2;

	/**
	 *  Purpose - constructor for this class
	 */
	public DamageCard() {
		super(cardName, cardId, cardCost);
	}

	/**
	 * Purpose - activates the ability for this card
	 * 
	 * @param p - Player object
	 */
	@Override
	public void ability(Player p) {
		p.damageOther(damageAmount);
	}
	
	public int getDamage() {
		return damageAmount;
	}
	
	/**
	 * Purpose - gets the resource for the object
	 * 
	 * @return a String containing an image path
	 */
	@Override
	public String getResource() {
		try {
			return (new File("./resources/images/damageCard.png")).getCanonicalPath();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return "";
		}
	}

}
