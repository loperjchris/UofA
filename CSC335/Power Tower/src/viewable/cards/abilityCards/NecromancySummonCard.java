package viewable.cards.abilityCards;
/**
 * NecromancySummonCard.java
 * 
 * Summons minionsToSummon minions and costs lifeCost life to play. 
 */
import java.io.File;
import java.io.IOException;

import viewable.cards.Card;
import viewable.gameObjects.Player;

// player pays life to summon minion
// 2 life for 5 normal minions
// maybe 2 life for 1 powerful minion

public class NecromancySummonCard extends AbilityCard {
	
	private static final int cardId = 12;
	private static final String cardName = "Necromancy";
	private static final int cardCost = 10;
	private static final int lifeCost = 2;
	private static final int minionsToSummon = 5;

	/**
	 *  Purpose - constructor for this class
	 */
	public NecromancySummonCard() {
		super(cardName, cardId, cardCost);
	}

	/**
	 * Purpose - activates the ability for this card
	 * 
	 * @param p - Player object
	 */
	@Override
	public void ability(Player p) {
		p.payLife(lifeCost);
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
			return (new File("./resources/images/necromancyCard.png")).getCanonicalPath();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return "";
		}
	}

}
