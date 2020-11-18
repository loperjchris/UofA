/** @author Chris Loper
 * 
 * Archer Tower Game Object
 * Contains default values for Archer Towers
 */

package viewable.gameObjects;

import java.io.File;
import java.io.IOException;

public class ArcherTower extends Tower {

	private static final int defaultAttack 			= 1;
	private static final int defaultRange 			= 1;
	private static final double defaultAttackSpeed	= 1.0;
	private static final String name				= "Archer Tower";

	/**
	 * constructor for the archer tower
	 */
	public ArcherTower() {
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
				return (new File("./resources/images/ArcherTower_Upgrade.png")).getCanonicalPath();
			} catch (IOException e) {
				return "";
			}
		} else {
			try {
				return (new File("./resources/images/ArcherTower_Default.png")).getCanonicalPath();
			} catch (IOException e) {
				return "";
			}
		}
	}
}
