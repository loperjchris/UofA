/** @author Chris Loper
 * 
 * Super class for all Tower Game Objects
 * Contains all accessors and mutators for tower game objects
 * 
 */
package viewable.gameObjects;
import viewable.Viewable;

public abstract class Tower extends Viewable{
	private Integer attack;
	private Integer range;
	private double attackSpeed;
	private String name;
	private boolean onCooldown;
	private boolean upgraded;
	
	public Tower(Integer attack, Integer range, double attackSpeed, String name) {
		this.attack 		= attack;
		this.range 			= range;
		this.attackSpeed 	= attackSpeed;
		this.name			= name;
		upgraded = false;
	}
	/** 
	 * purpose Mutator for attack
	 * 
	 * @param newAttack: the new attack value
	 */
	public void setAttack(Integer newAttack) {
		this.attack = newAttack;
	}
	/**
	 * purpose Mutator for range
	 * 
	 * @param newRange: the new range
	 */
	public void setRange(Integer newRange) {
		this.range = newRange;
	}
	/**
	 * purpose Mutator for attack speed
	 * 
	 * @param newAttackSpeed: new attack speed
	 */
	public void setAttackSpeed(double newAttackSpeed) {
		this.attackSpeed = newAttackSpeed;
	}
	/**
	 * purpose Accessor for attack
	 * 
	 * @return this.attack: new attack
	 */
	public Integer getAttack() {
		return this.attack;
	}
	/**
	 * purpose Accessor for range
	 * 
	 * @return this.range: new range
	 */
	public Integer getRange() {
		return this.range;
	}
	/**
	 * purpose Accessor for attack speed
	 * 
	 * @return this.attackSpeed: new attack speed
	 */
	public double getAttackSpeed() {
		return this.attackSpeed;
	}
	/**
	 * purpose Accessor for name
	 * 
	 * @return this.name: the name
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * purpose: starts the cooldown for the tower
	 */
	public void startCooldown() {
		onCooldown = true;
	}
	
	/**
	 * purpose: ends the cooldown for a tower
	 */
	public void endCooldown() {
		onCooldown = false;
	}
	
	/**
	 * purpose: checks if a tower is on cooldown
	 * 
	 * @return: true if on cooldown
	 */
	public boolean canAttack() {
		return onCooldown == false;
	}
	
	/**
	 * purpose: set how many upgrades the tower has
	 * 
	 * @param val true if tower being set to upgraded
	 */
	public void setUpgraded(boolean val) {
		upgraded = val;
	}
	
	/**
	 * purpose: checks if a tower is upgraded
	 * 
	 * @return: true if upgraded
	 */
	public boolean getUpgraded() {
		return upgraded;
	}
}
