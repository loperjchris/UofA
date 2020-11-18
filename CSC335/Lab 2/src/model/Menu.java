package model;

import java.util.HashMap;

public class Menu {
	
	private HashMap<String, MenuItem> items;
	
	public Menu() {
		items = new HashMap<>();
		items.put("JamessJicama", new JamesJicama());
		items.put("AustensAustere", new AustensAustere());
		items.put("TimsTorture", new TimsTorture());
		items.put("RyansRoast", new RyansRoast());
	}
	
	public double getPrice(String in) {
		return items.get(in).getPrice();
	}
	
	public String getQuip(String in) {
		return items.get(in).toString();
	}
	
	public String toString() {
		String ret = "Menu:\n";

		for(String item: items.keySet()) {
			ret += "  " + item + "\n";
		}
		return ret;
	}
}
