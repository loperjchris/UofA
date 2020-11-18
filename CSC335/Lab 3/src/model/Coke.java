package model;

public class Coke extends Beverage {
	
	public Coke() {
		super(0.99);
	}
	
	@Override
	public String getQuip() {
		return "Is Pepsi okay?";
	}
}
