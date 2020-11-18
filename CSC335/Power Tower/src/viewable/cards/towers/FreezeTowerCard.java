/** @author Chris Loper
 * 
 * Contains card id and card name for Freeze Towers, also
 * contains an upgrade class for Freeze Towers
 * 
 */
package viewable.cards.towers;

import java.io.File;
import java.io.IOException;

import viewable.cards.Card;
import viewable.gameObjects.FreezeTower;
import viewable.gameObjects.Player;
import viewable.gameObjects.Tower;

public class FreezeTowerCard extends TowerCard {
	private static final int cardId 		= 3;
	private static final String cardName 	= "Freeze Tower";
	private static final int cardCost       = 25;
	
	/**
	 *  Purpose- Constructor for the class
	 */
	public FreezeTowerCard() {
		super(cardName, cardId, cardCost);
	}
	
	/**
	 *  Purpose - Upgrades the tower
	 *  
	 *  @param f - the Tower object to be upgraded
	 */
	@Override
	public void Upgrade(Tower f) {
		f.setUpgraded(true);
		f.setAttack(f.getAttack() + 0);
		f.setRange(f.getRange() + 1);
		f.setAttackSpeed(f.getAttackSpeed() + .1);
	}
	
	/**
	 *  Purpose - gets the resource for the object
	 *  
	 *  @return - returns an image path
	 */
	@Override
	public String getResource() {
		try {
			return (new File("./resources/images/freezeTowerCard.png")).getCanonicalPath();
		} catch (IOException e) {
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
		return FreezeTower.class;
	}
}
