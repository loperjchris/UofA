package viewable.cards.abilityCards;
/**
 * IncreaseRewardCard.java
 * 
 * Doubles the reward for the round
 */
import java.io.File;
import java.io.IOException;

import viewable.cards.Card;
import viewable.gameObjects.Player;

public class IncreaseRewardCard extends AbilityCard {
	
	private static final int cardId = 11;
	private static final String cardName = "Plentiful Bounty";
	private static final int cardCost = 10;

	/**
	 *  Purpose - constructor for this class
	 */
	public IncreaseRewardCard() {
		super(cardName, cardId, cardCost);
	}

	/**
	 * Purpose - activates the ability for this card
	 * 
	 * @param p - Player object
	 */
	@Override
	public void ability(Player p) {
		// TODO Auto-generated method stub
		p.buffReward();
	}
	
	/**
	 * Purpose - gets the resource for the object
	 * 
	 * @return a String containing an image path
	 */
	@Override
	public String getResource() {
		try {
			return (new File("./resources/images/increaseRewardCard.png")).getCanonicalPath();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return "";
		}
	}

}
