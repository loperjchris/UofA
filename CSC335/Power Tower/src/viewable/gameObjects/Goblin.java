package viewable.gameObjects;

import java.io.File;
import java.io.IOException;

public class Goblin extends Minion {

	private static final int defaultHealth = 10;
	private static final int defaultDamage = 1;
	private static final int defaultSpeed = 1;
	private static final int defaultReward = 2;

	/**
	 * constructor for the goblin minion
	 * 
	 * @param player Player object
	 */
	public Goblin(Player player) {
		super(defaultHealth, defaultDamage, defaultSpeed, defaultReward, player);
	}

	/**
	 * purpose: loads the sprite for this minion
	 */
	@Override
	public String getResource() {
		try {
			return (new File("./resources/images/MonsterSprites/Goblin.png")).getCanonicalPath();
		} catch (IOException e) {
			return "";
		}
	}
}
