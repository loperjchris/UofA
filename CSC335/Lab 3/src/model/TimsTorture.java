package model;

public class TimsTorture extends FoodItem {

	public TimsTorture() {
		super(4.0);
	}

	@Override
	public String getQuip() {
		return "Instead of bread, uses Hatch Chilis.";
	}
}
