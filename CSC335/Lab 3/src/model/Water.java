package model;

public class Water extends Beverage {
	
	public Water() {
		super(0.5);
	}
	
	@Override
	public String getQuip() {
		return "The beverage of choice when you have to pay for yourself.";
	}
}
