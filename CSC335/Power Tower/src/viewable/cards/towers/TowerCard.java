package viewable.cards.towers;

import viewable.cards.Card;
import viewable.gameObjects.ArcherTower;
import viewable.gameObjects.Tower;

public abstract class TowerCard extends Card{

	/**
	 *  Purpose- Constructor for the class
	 *  @param name name of tower
	 *  @param id id of tower
	 *  @param cost cost of tower
	 */
	public TowerCard(String name, int id, int cost) {
		super(name, id, cost);
		// TODO Auto-generated constructor stub
	}

	/**
	 *  Purpose - accessor for the Tower class
	 *  
	 *  @return an ArcherTower class
	 */
	public abstract Class<? extends Tower> getTower();
	
	/**
	 *  Purpose - Upgrades the tower
	 *  
	 *  @param t - the Tower object to be upgraded
	 */
	public abstract void Upgrade(Tower t);
}
