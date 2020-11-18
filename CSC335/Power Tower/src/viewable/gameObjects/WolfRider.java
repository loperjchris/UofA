/**
 * 
 */

package viewable.gameObjects;

import java.io.File;
import java.io.IOException;

public class WolfRider extends Minion {
	
	private static final int defaultHealth = 15;
	private static final int defaultDamage = 2;
	private static final int defaultSpeed = 2;
	private static final int defaultReward = 5;

	/**
	 * constructor for the wolf rider minion
	 * 
	 * @param player Player object
	 */
	public WolfRider(Player player) {
		super(defaultHealth, defaultDamage, defaultSpeed, defaultReward, player);
	}

	/**
	 * purpose: loads the sprite for this minion
	 */
	@Override
	public String getResource() {
		try {
			return (new File("./resources/images/MonsterSprites/WolfRider.png")).getCanonicalPath();
		} catch (IOException e) {
			return "";
		}
	}

}
