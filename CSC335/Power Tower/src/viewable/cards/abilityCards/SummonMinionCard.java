package viewable.cards.abilityCards;
/**
 * SummonMinionCard.java
 * 
 * Summons minionsToSummon minions
 */
import java.io.File;
import java.io.IOException;

import viewable.cards.Card;
import viewable.gameObjects.Player;

public class SummonMinionCard extends AbilityCard {
	private static final int cardId 			= 8;
	private static final String cardName 		= "Summon Minions";
	private static final int minionsToSummon	= 2;
	private static final int cardCost       = 10;
	
	/**
	 *  Purpose - constructor for this class
	 */
	public SummonMinionCard() {
		super(cardName, cardId, cardCost);
	}

	/**
	 * Purpose - activates the ability for this card
	 * 
	 * @param p - Player object
	 */
	@Override
	public void ability(Player p) {
		p.summonMinion(minionsToSummon);
	}
	
	/**
	 * Purpose - gets the resource for the object
	 * 
	 * @return a String containing an image path
	 */
	@Override
	public String getResource() {
		try {
			return (new File("./resources/images/summonMinionCard.png")).getCanonicalPath();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return "";
		}
	}
}
