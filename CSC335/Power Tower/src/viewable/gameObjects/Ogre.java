package viewable.gameObjects;

import java.io.File;
import java.io.IOException;

public class Ogre extends Minion {
	
	private static final int defaultHealth = 25;
	private static final int defaultDamage = 5;
	private static final int defaultSpeed = 1;
	private static final int defaultReward = 10;

	/**
	 * constructor for the ogre minion
	 * 
	 * @param player Player object
	 */
	public Ogre(Player player) {
		super(defaultHealth, defaultDamage, defaultSpeed, defaultReward, player);
	}

	/**
	 * purpose: loads the sprite for this minion
	 */
	@Override
	public String getResource() {
		try {
			return (new File("./resources/images/MonsterSprites/Ogre.png")).getCanonicalPath();
		} catch (IOException e) {
			return "";
		}
	}

}
