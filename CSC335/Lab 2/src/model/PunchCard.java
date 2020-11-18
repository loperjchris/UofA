package model;

public class PunchCard {

	private int numVisits;
	
	public PunchCard() {
		numVisits = 0;
	}
	
	public void addVisit() {
		numVisits++;
	}
	
	public boolean isNthVisit(int n) {
		return numVisits % n == 0;
	}
}
