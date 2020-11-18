/** @author Chris Loper
 * 
 * Cannon Tower Card
 * Contains default card ID and card name for Cannon Towers, also
 * contains an upgrade class to upgrade Cannon Towers
 * 
 */
package viewable.cards.towers;

import java.io.File;
import java.io.IOException;

import viewable.cards.Card;
import viewable.gameObjects.CannonTower;
import viewable.gameObjects.Player;
import viewable.gameObjects.Tower;

public class CannonTowerCard extends TowerCard {
	private static final int cardId 		= 1;
	private static final String cardName 	= "Cannon Tower";
	private static final int cardCost       = 30;
	
	/**
	 *  Purpose- Constructor for the class
	 */
	public CannonTowerCard() {
		super(cardName, cardId, cardCost);
	}
	
	/**
	 *  Purpose - Upgrades the tower
	 *  
	 *  @param c - the Tower object to be upgraded
	 */
	@Override
	public void Upgrade(Tower c) {
		c.setUpgraded(true);
		c.setAttack(c.getAttack() + 1000);
		c.setRange(c.getRange() + 1);
		c.setAttackSpeed(c.getAttackSpeed() + .1);
	}
	
	/**
	 *  Purpose - gets the resource for the object
	 *  
	 *  @return - returns an image path
	 */
	@Override
	public String getResource() {
		try {
			return (new File("./resources/images/cannonTowerCard.png")).getCanonicalPath();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return "";
		}
	}

	/**
	 *  Purpose - accessor for the Tower class
	 *  
	 *  @return an ArcherTower class
	 */
	@Override
	public Class<? extends Tower> getTower() {
		// TODO Auto-generated method stub
		return CannonTower.class;
	}

}
