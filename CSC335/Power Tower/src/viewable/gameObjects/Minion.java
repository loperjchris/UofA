/**
 * @purpose: the parent class for all the minions
 * 
 */

package viewable.gameObjects;

import viewable.Viewable;

public abstract class Minion extends Viewable {
	
	private Integer health;
	private Integer damage;
	private Integer speed;
	private Integer reward;
	private Integer currentHealth;
	private Integer currentStep;
	private Player player;
	
	
	/**
	 * purpose: parent constructor for each minion
	 * 
	 * @param health health of the minion
	 * 
	 * @param damage damage the minion does
	 * 
	 * @param speed speed the minion moves at
	 * 
	 * @param reward gold reward for killing the minion
	 * 
	 * @param player Player object
	 * 
	 */
	public Minion(Integer health, Integer damage, Integer speed, Integer reward, Player player) {
		this.health = health;
		currentHealth = health;
		currentStep = 0;
		this.damage = damage;
		this.speed = speed;
		this.reward = reward;
		this.player = player;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	/**
	 * purpose: setter for updating the minions health when it takes damage
	 * 
	 * @param newHealth: new value for health to be updated to
	 */
	public void setHealth(Integer newHealth) {
		this.health = newHealth;
	}
	
	/**
	 * purpose: setter for changing the amount of damage a minion does
	 * 
	 * @param newDamage: new value of the minions damage
	 */
	public void setDamage(Integer newDamage) {
		this.damage = newDamage;
	}
	
	/**
	 * purpose: setter for changing the speed of a minion
	 * 
	 * @param newSpeed: new speed value for the minion
	 */
	public void setSpeed(Integer newSpeed) {
		this.speed = newSpeed;
	}
	
	/**
	 * purpose: increases the step of the minion
	 */
	public void incrementStep() {
		currentStep++;
	}
	
	/**
	 * purpose: getter for the minions currentStep
	 * 
	 * @return: the minions currentStep value
	 */
	public int getStep() {
		return currentStep;
	}
	
	/**
	 * purpose: getter for the minions current health
	 * 
	 * @return: the minions health value
	 */
	public Integer getHealth() {
		return this.health;
	}
	
	/**
	 * purpose: getter for the minions damage value
	 * 
	 * @return: the minions damage value
	 */
	public Integer getDamage() {
		return this.damage;
	}
	
	/**
	 * purpose: getter for the minions speed value
	 * 
	 * @return: the minions speed value
	 */
	public Integer getSpeed() {
		return this.speed;
	}
	
	// Getter for reward.
	public Integer getReward() {
		return reward;
	}
	
	/**
	 * purpose: getter for the minions reward value
	 * 
	 * @param player the player to add the gold multiplier
	 * 
	 * @return: the minions reward value
	 */
	public Integer getReward(Player player) {
		return this.reward*player.getGoldMultiplier();
	}
	
	/**
	 * purpose: reduces the minions health when it is damaged
	 * 
	 * @param damage: amount of damage done to the minion
	 */
	public void takeDamage(int damage) {
		currentHealth-=damage;
	}
	
	/**
	 * purpose: getter for the minions current health
	 * 
	 * @return: the minions current health
	 */
	public int getCurrentHealth() {
		return currentHealth;
	}
	
	/**
	 * purpose: checks if the minion's health has dropped below zero to 
	 * determine if it has died
	 * 
	 * @return: true for dead and false for alive
	 */
	public boolean isDead() {
		return currentHealth<=0;
	}
	
}
