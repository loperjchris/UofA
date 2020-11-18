/** @author Chris Loper
 * 
 * Cannon Tower Game Object
 * Contains default values for Cannon Towers
 */
package viewable.gameObjects;

import java.io.File;
import java.io.IOException;

public class CannonTower extends Tower {
	private static final int defaultAttack 			= 50;
	private static final int defaultRange 			= 1;
	private static final double defaultAttackSpeed	= 0.5;
	private static final String name				= "Cannon Tower";
	
	/**
	 * purpose: constructor for the cannon tower
	 */
	public CannonTower() {
		super(defaultAttack, defaultRange, defaultAttackSpeed, name);
	}
	
	/**
	 * purpose: loads the image for the archer tower if placed on an unused grid
	 * and updates the tower with upgraded art if the tower is upgraded
	 */
	@Override
	public String getResource() {
		if (this.getUpgraded()) {
			try {
				return (new File("./resources/images/CannonTower_Upgrade.png")).getCanonicalPath();
			} catch (IOException e) {
				return "";
			}
		} else {
			try {
				return (new File("./resources/images/CannonTower_Default.png")).getCanonicalPath();
			} catch (IOException e) {
				return "";
			}
		}
	}
}
