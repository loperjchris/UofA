/** @author Chris Loper
 * 
 * Minion Tower Game Object
 * Contains default values for Minion Towers
 */
package viewable.gameObjects;

import java.io.File;
import java.io.IOException;

public class MinionTower extends Tower {
	private static final int defaultAttack 			= 1;
	private static final int defaultRange 			= 0;
	private static final double defaultAttackSpeed	= 1;
	private static final String name				= "Minion Tower";
	
	/**
	 * constructor for the minion tower
	 */
	public MinionTower() {
		super(defaultAttack, defaultRange, defaultAttackSpeed, name);
	}
	
	/**
	 * purpose: loads the image for the archer tower if placed on an unused grid
	 * 
	 * @return string path of the resource to be loaded
	 * 
	 */
	@Override
	public String getResource() {
		try {
			return (new File("./resources/images/MinionTower_Default.png")).getCanonicalPath();
		} catch (IOException e) {
			return "";
		}
	}
}
