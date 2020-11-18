package viewable.cards.abilityCards;
/**
 *  DrawCard.java
 *  
 *  Draws 1 card for the user
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import viewable.cards.Card;
import viewable.gameObjects.Player;

public class DrawCard extends AbilityCard {
	private static final int cardId 		= 7;
	private static final String cardName 	= "Draw A Card";
	private static final int cardsToDraw	= 1;
	private static final int cardCost       = 10;
	
	/**
	 *  Purpose - constructor for this class
	 */
	public DrawCard() {
		super(cardName, cardId, cardCost);
	}
	
	/**
	 * Purpose - activates the ability for this card
	 * 
	 * @param p - Player object
	 */
	@Override
	public void ability(Player p){
		try {
			p.drawCards(cardsToDraw);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose - gets the resource for the object
	 * 
	 * @return a String containing an image path
	 */
	@Override
	public String getResource() {
		try {
			return (new File("./resources/images/drawACard.png")).getCanonicalPath();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return "";
		}
	}
}
