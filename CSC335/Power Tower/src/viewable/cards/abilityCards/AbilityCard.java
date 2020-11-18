package viewable.cards.abilityCards;
/**
 * AbilityCard.java
 * 
 * Super class for the ability cards
 */
import viewable.Viewable;
import viewable.cards.Card;
import viewable.gameObjects.Player;

public abstract class AbilityCard extends Card {
	
	/**
	 *  Purpose - constructor for this class
	 *  
	 *  @param name - name of card
	 *  
	 *  @param id - id of card
	 *  
	 *  @param cost - cost of card
	 *  
	 */
	public AbilityCard(String name, int id, int cost) {
		super(name, id, cost);
	}
	
	/**
	 * Purpose - gets the resource for the object
	 * 
	 * @param p - Player object
	 */
	public abstract void ability(Player p);
}
