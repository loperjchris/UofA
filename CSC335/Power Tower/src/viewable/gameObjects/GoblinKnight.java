package viewable.gameObjects;

import java.io.File;
import java.io.IOException;

public class GoblinKnight extends Minion {

	private static final int defaultHealth = 15;
	private static final int defaultDamage = 2;
	private static final int defaultSpeed = 1;
	private static final int defaultReward = 5;
	
	/**
	 * constructor for the goblin knight minion
	 * 
	 * @param player Player object
	 */
	public GoblinKnight(Player player) {
		super(defaultHealth, defaultDamage, defaultSpeed, defaultReward, player);
	}

	/**
	 * purpose: loads the sprite for this minion
	 */
	@Override
	public String getResource() {
		try {
			return (new File("./resources/images/MonsterSprites/GoblinKnight.png")).getCanonicalPath();
		} catch (IOException e) {
			return "";
		}
	}

}
