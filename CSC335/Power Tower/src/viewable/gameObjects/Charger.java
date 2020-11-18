package viewable.gameObjects;

import java.io.File;
import java.io.IOException;

public class Charger extends Minion {
	
	private static final int defaultHealth = 15;
	private static final int defaultDamage = 5;
	private static final int defaultSpeed = 2;
	private static final int defaultReward = 10;

	/**
	 * purpose: constructor for charger minion
	 * 
	 * @param player Player object
	 */
	public Charger(Player player) {
		super(defaultHealth, defaultDamage, defaultSpeed, defaultReward, player);
	}

	/**
	 * purpose: loads the sprite for this minion
	 */
	@Override
	public String getResource() {
		try {
			return (new File("./resources/images/MonsterSprites/Charger.png")).getCanonicalPath();
		} catch (IOException e) {
			return "";
		}
	}

}
