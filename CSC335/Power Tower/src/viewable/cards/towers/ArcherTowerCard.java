/** @author Chris Loper
 * 
 * Contains card id and card name for Archer Towers, also
 * contains an upgrade class for Archer Towers
 * 
 */
package viewable.cards.towers;

import java.io.File;
import java.io.IOException;

import viewable.cards.Card;
import viewable.gameObjects.ArcherTower;
import viewable.gameObjects.Player;
import viewable.gameObjects.Tower;

public class ArcherTowerCard extends TowerCard {
	private static final int cardId 		= 0;
	private static final String cardName 	= "Archer Tower";
	private static final int cardCost       = 1;
	
	/**
	 *  Purpose- Constructor for the class
	 */
	public ArcherTowerCard() {
		super(cardName, cardId, cardCost);
	}
	
	/**
	 *  Purpose - gets the resource for the object
	 *  
	 *  @return - returns an image path
	 */
	@Override
	public String getResource() {
		try {
			return (new File("./resources/images/archerTowerCard.png")).getCanonicalPath();
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
		return ArcherTower.class;
	}

	/**
	 *  Purpose - Upgrades the tower
	 *  
	 *  @param t - the Tower object to be upgraded
	 */
	@Override
	public void Upgrade(Tower t) {
		t.setUpgraded(true);
		t.setAttack(t.getAttack() + 1);
		t.setRange(t.getRange() + 1);
		t.setAttackSpeed(t.getAttackSpeed() + .1);
	}
	
}
