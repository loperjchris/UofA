package model;

import java.util.HashMap;

public class LoyaltyProgram {

	public static final int VISITS_FOR_DISCOUNT = 5;
	public static final float DISCOUNT = 0.1f;

	private HashMap<String, PunchCard> customerPunchCards = new HashMap<>();

	/**
	 * Returns the PunchCard associated with the given customer. If the customer
	 * does not have a PunchCard, this will create a new PunchCard for the
	 * customer and return it.
	 * 
	 * @param customerName the name of the customer.
	 * @return the PunchCard of the customer.
	 */
	public PunchCard getPunchCard(String customerName) {
		
		PunchCard card = customerPunchCards.get(customerName);
		
		// create a new card for the customer if it's not found
		if (card == null) {
			card = new PunchCard();
			customerPunchCards.put(customerName, card);
		}
		
		return card;
	}
}
