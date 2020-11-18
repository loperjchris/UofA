package model;

public class PunchCard {
	private int visits;
	
	public void addVisit() {
		visits++;
	}
	
	public boolean isNthVisit(int n) {
		return (visits%n) == 0;
	}
}