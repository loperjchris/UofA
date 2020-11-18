package controller;

import java.util.Scanner;

import model.Beverage;
import model.FoodItem;
import model.InvalidMenuItemException;
import model.Menu;
import model.MenuItem;
import model.PunchCard;
import model.SalesQuery;

public class CashRegister {

	public static void main(String[] args) {
		Menu menu = new Menu();
		LoyaltyProgram loyalty = new LoyaltyProgram();
		SalesLog salesLog = new SalesLog();

		Scanner input = new Scanner(System.in);

		// loop control variable for do/while
		boolean storeOpen = true;
		do {
			// display greeting messages and set up user's punch card
			System.out.println("Welcome to BeyondBrad, what's your name? ");

			String customerName = input.next();

			System.out.print(menu);

			PunchCard userCard = loyalty.getPunchCard(customerName);
			userCard.addVisit();

			// keeps track of the running total of orders for this transaction
			double currentTab = 0;
			
			String userInput = "";
			MenuItem itemPurchased = null;
			// Setting a variable to determine which phrase to display.
			boolean first = false;
			while (!userInput.equalsIgnoreCase("no")) {
				if (!first) {
					System.out.print("What would you like to order? ");
					first = true;
				} else {
					System.out.print("Anything else? (enter item name or "
							+ "\"no\" to ring out) ");
				}
				userInput = input.next().trim();
				if (!userInput.equalsIgnoreCase("no")) {
					// Determines if a given request is an item on the menu.
					while (itemPurchased == null && 
							!userInput.equalsIgnoreCase("no")) {
						try {
							itemPurchased = menu.getItem(userInput);
							System.out.println(itemPurchased.getQuip());
							currentTab += itemPurchased.getPrice();
							salesLog.addItem(itemPurchased);
							// Throws exception if item not on menu.
						} catch (InvalidMenuItemException ex) {
							System.out.println(ex.getMessage());
							userInput = input.next().trim();
						}
					}
					itemPurchased = null;
				}
			}
			
			// apply discount
			if(userCard.isNthVisit(LoyaltyProgram.VISITS_FOR_DISCOUNT)) {
				System.out.println("Congratulations! You get a discount for "
						+ "being a loyal customer.");
				currentTab *= (1 - LoyaltyProgram.DISCOUNT); 
			}

			// display goodbye messages
			System.out.printf("Cool, that will be $%.2f, take care!\n", 
					currentTab);
			
			System.out.println("Enter:\n\t\"exit\" to close down the store");
			System.out.println("\t\"next\" for the next customer");
			System.out.println("\t\"stats\" for the day's sales");	
			
			String command = input.next().trim().toLowerCase();	
			
			switch(command) {
			case "exit":
				storeOpen = false;
				break;
			case "stats":
				printStatistics(salesLog);
				break;
			}
		} while (storeOpen);
		
		input.close();
	}
	
	public static void printStatistics(SalesLog log) {
		System.out.print("Number of items sold today: ");
		
		// runQuery() will call the passed lambda function once per
		// item in the sales log. It will take the returned value
		// of the lambda, add it up and return the sum.
		//
		// If we pass a function that for each item simply returns 1,
		// the sum of 1 added N times is N, the total number of items 
		SalesQuery countQuery = item -> 1;
		System.out.println(log.runQuery(countQuery));
		
		System.out.print("Total income today: ");
		
		// We can also query the item passed and get a property of the
		// item like it's price.
		SalesQuery totalQuery = item -> item.getPrice();
		System.out.println(log.runQuery(totalQuery));
		
		// Displays how many items sold today were Beverages
		System.out.print("Number of beverages sold today: ");
		SalesQuery countBev = item -> {if (item instanceof Beverage) 
			return 1.0; return 0;};
		System.out.println(log.runQuery(countBev));
		
		// Displays the total money spent on Food items.
		System.out.print("Total incomr from food today: ");
		SalesQuery totalFood = item -> {if (item instanceof FoodItem) 
			return item.getPrice(); return 0;};
		System.out.println(log.runQuery(totalFood));
	}
}
