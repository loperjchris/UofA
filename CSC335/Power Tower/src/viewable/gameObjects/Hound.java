package viewable.gameObjects;

import java.io.File;
import java.io.IOException;

public class Hound extends Minion {
	
	private static final int defaultHealth = 5;
	private static final int defaultDamage = 1;
	private static final int defaultSpeed = 2;
	private static final int defaultReward = 2;

	/**
	 * constructor for the hound minion
	 * 
	 * @param player Player object
	 */
	public Hound(Player player) {
		super(defaultHealth, defaultDamage, defaultSpeed, defaultReward, player);
	}

	/**
	 * purpose: loads the sprite for this minion
	 */
	@Override
	public String getResource() {
		try {
			return (new File("./resources/images/MonsterSprites/Hound.png")).getCanonicalPath();
		} catch (IOException e) {
			return "";
		}
	}

}
