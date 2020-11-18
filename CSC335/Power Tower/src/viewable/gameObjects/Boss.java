package viewable.gameObjects;

import java.io.File;
import java.io.IOException;

public class Boss extends Minion {
	
	private static final int defaultHealth = 60;
	private static final int defaultDamage = 10;
	private static final int defaultSpeed = 1;
	private static final int defaultReward = 100;

	/**
	 * constructor for the boss minion
	 * 
	 * @param player Player object
	 * 
	 */
	public Boss(Player player) {
		super(defaultHealth, defaultDamage, defaultSpeed, defaultReward, player);
	}

	/**
	 * purpose: loads the sprite for this minion
	 */
	@Override
	public String getResource() {
		try {
			return (new File("./resources/images/MonsterSprites/Boss.png")).getCanonicalPath();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return "";
		}
	}

}
