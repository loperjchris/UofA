package model;

public abstract class MenuItem {
	private double price;
	
	public MenuItem(double price) {
		this.price = price;
	}
	
	public double getPrice() {
		return this.price;
	}
	
	public abstract String getQuip();
}
