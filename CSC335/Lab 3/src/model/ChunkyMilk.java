package model;

public class ChunkyMilk extends Beverage {

	public ChunkyMilk() {
		super(1.25);
	}
	
	@Override
	public String getQuip() {
		return "It has the wholesome chunks that growing kids need, unlike smooth-style milk.";
	}
}
