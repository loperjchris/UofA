/** @author: Chris Loper
 * 
 * Super class for all cards
 */
package viewable.cards;

import viewable.Viewable;
import viewable.gameObjects.Player;
import viewable.gameObjects.Tower;

public abstract class Card extends Viewable{
	private String name;
	private int id;
	private int cost;
	
	/**
	 * Purpose - constructor for the class
	 * @param name - String containing the name of the card
	 * @param id - integer containing the ID of the card
	 * @param cost - integer containing the cost of the card
	 */
	public Card(String name, int id, int cost) {
		this.name = name;
		this.id = id;
		this.cost = cost;
	}
	
	/**
	 * Purpose - accessor for the name of the card
	 * 
	 * @return the name of the card as a String
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Purpose - accessor for the id of the card
	 * 
	 * @return the id of the card as an int
	 */
	public int getId() {
		return this.id;
	}
	
	/**
	 *  Purpose - accessor for the cost of the card
	 *  
	 *  @return the cost of the card as an int
	 */
	public int getCost() {
		return this.cost;
	}
}
