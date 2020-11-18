/** @author Chris Loper
 * 
 * Contains card id and card name for Currency Towers, also
 * contains an upgrade class for Currency Towers
 * 
 */
package viewable.cards.towers;

import java.io.File;
import java.io.IOException;

import viewable.cards.Card;
import viewable.gameObjects.CurrencyTower;
import viewable.gameObjects.Player;
import viewable.gameObjects.Tower;

public class CurrencyTowerCard extends TowerCard {
	private static final int cardId 		= 4;
	private static final String cardName 	= "Currency Tower";
	private static final int cardCost       = 10;
	
	/**
	 *  Purpose- Constructor for the class
	 */
	public CurrencyTowerCard() {
		super(cardName, cardId, cardCost);
	}
	
	/**
	 *  Purpose - Upgrades the tower
	 *  
	 *  @param m - the Tower object to be upgraded
	 */
	@Override
	public void Upgrade(Tower m) {
		m.setUpgraded(true);
		m.setAttack(m.getAttack() + 1);
		m.setRange(m.getRange() + 0);
		m.setAttackSpeed(m.getAttackSpeed() + .1);
	}
	
	/**
	 *  Purpose - gets the resource for the object
	 *  
	 *  @return - returns an image path
	 */
	@Override
	public String getResource() {
		try {
			return (new File("./resources/images/currencyTowerCard.png")).getCanonicalPath();
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
		return CurrencyTower.class;
	}
}
