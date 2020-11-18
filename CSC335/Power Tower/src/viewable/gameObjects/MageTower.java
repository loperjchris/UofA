/** @author Chris Loper
 * 
 * Mage Tower Game Object
 * Contains default values for Mage Towers
 */
package viewable.gameObjects;

import java.io.File;
import java.io.IOException;

public class MageTower extends Tower {
	private static final int defaultAttack 			= 1;
	private static final int defaultRange 			= 2;
	private static final double defaultAttackSpeed	= 0.75;
	private static final String name				= "Mage Tower";
	
	/**
	 * constructor for the mage tower
	 */
	public MageTower() {
		super(defaultAttack, defaultRange, defaultAttackSpeed, name);
	}
	
	/**
	 * purpose: loads the image for the archer tower if placed on an unused grid
	 * and updates the tower with upgraded art if the tower is upgraded
	 * 
	 * @return string path of the resource to be loaded
	 * 
	 */
	@Override
	public String getResource() {
		if (this.getUpgraded()) {
			try {
				return (new File("./resources/images/MageTower_Upgrade.png")).getCanonicalPath();
			} catch (IOException e) {
				return "";
			}
		} else {
			try {
				return (new File("./resources/images/MageTower_Default.png")).getCanonicalPath();
			} catch (IOException e) {
				return "";
			}
		}
	}
}
