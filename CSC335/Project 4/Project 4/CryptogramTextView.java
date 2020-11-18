/**
 * CryptogramTextView.java
 * 
 * Displays the command line text version of the cryptogram game and prints out
 * the result of the commands given by the user.
 * 
 * Usage instructions:
 * 
 * Construct CryptogramTextVIew
 * CryptogramTextView view = new CryptogramTextView();
 * 
 * run() - prints out all necessary strings and command responsesand sends any
 * updates to the model which in turn returns them to this view to be printed.
 */


import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

public class CryptogramTextView implements Observer{
	
	// field variable for the construction of CryptogramTextView objects.
	private CryptogramController controller;
	private CryptogramModel model;
	private List<String> formatted;
	private List<String> formattedUserString;
	private Boolean hasWon;
	
	public CryptogramTextView() {
		model = new CryptogramModel("text");
		controller = new CryptogramController(model);
		model.addObserver(this);
		controller.format();
	}
	
	/*
     * Purpose: Begins the program and waits for the user to supply input and
     * then sends the input to the model which returns a string for the view
     * to print.
     * 
     * @param None.
     * 
     * @return None.
     */
	public void run() {
		Command command = new Command();
		Scanner input = new Scanner(System.in);
		// Check for win
		while (!hasWon) {
			String com = "";
			for (int i = 0; i < formatted.size(); i++) {
				System.out.println(formattedUserString.get(i));
				System.out.println(formatted.get(i));
			}
			System.out.println("");
			System.out.print("Enter a command (help to see commands) ");
			com = input.nextLine().toUpperCase();
			// Check to see if given command is legal
			while (!command.isCommand(com)) {
				System.out.println("Unable to recognize command.\n"
						+ "Type 'help' in order to get a list of commands\n");
				System.out.print("Enter a command (help to see commands) ");
				com = input.nextLine().toUpperCase();
			}
			// Sending user input to controller to get response from model
			String response = command.processCommand(com, controller);
			if (response != null) {
				System.out.println(response);
			}
		}
		for (int i = 0; i < formatted.size(); i++) {
			System.out.println(formattedUserString.get(i));
			System.out.println(formatted.get(i));
		}
		System.out.println("You got it!\n");
		input.close();
	}

	/*
     * Purpose: Updates the view when a change is made by the model.
     * 
     * @param arg0 - the Observable class that notified its observers of any
     * changes to the model.
     * 
     * @param arg1 - the object that the observable is saying was changed.
     * 
     * @return None.
     */
	@SuppressWarnings("unchecked")
	@Override
	public void update(Observable o, Object arg) {
		// Prints out frequency or hint if user inputs those commands
		if (arg instanceof String) {
			System.out.println(arg);
			// Updates formatted strings when changes are made by model
		} else if (arg instanceof List) {
			if (((List<?>) arg).get(0) instanceof List) {
				formattedUserString = (List<String>) ((List<?>) arg).get(0);
			} else {
				formatted = (List<String>) arg;
			}
			// Keeps track of win state for the game
		} else if (arg instanceof Boolean) {
			hasWon = (Boolean) arg;
		}
	}
	
	// Inner class
	class Command {

		// Variable fields used for Commands object construction.
		private List<String> commands;
		
		/*
	     * Purpose: Creates a new Commands object and initializes the syntax of
	     * each command.
	     * 
	     * @param None.
	     * 
	     * @return None.
	     */
		public Command() {
			commands = new ArrayList<String>();
			commands.add("REPLACE (.*) BY (.*)");
			commands.add("(.*) = (.*)");
			commands.add("FREQ");
			commands.add("HINT");
			commands.add("EXIT");
			commands.add("HELP");
		}
		
		/*
	     * Purpose: Determines if the user provided input matches an actual 
	     * command.
	     * 
	     * Utilizes regex to determine if what the user provided matches accepted
	     * commands even if the command contains variables.
	     * 
	     * @param input - a string containing the user input.
	     * 
	     * @return found - a boolean that states if the command given matches any 
	     * of the program's commands.
	     */
		public Boolean isCommand(String input) {
			boolean found = false;
			for (int i = 0; i < commands.size(); i++) {
				if (input.matches(commands.get(i))) {
					found = true;
				}
			}
			return found;
		}
		
		/*
	     * Purpose: Determines what the program is going to do if a proper command
	     * is submitted by the user.
	     * 
	     * @param command - a String representing what the user submitted.
	     * 
	     * @param cypher - a TextManipulator object that calls the functions to be 
	     * run depending on the user provided input.
	     * 
	     * @return None.
	     */
		public String processCommand(String command, CryptogramController controller) {
			String temp[] = command.split(" ");
			// Determining if command matches replace X by Y format.
			if (command.matches(commands.get(0))) {
				if (temp[1].length() == 1 && temp[3].length() == 1) {
					controller.updateUserMap(temp[1].charAt(0), temp[3].charAt(0));
				}
			}
			// Determining if command matches X = Y format.
			if (command.matches(commands.get(1))) {
				if (temp[0].length() == 1 && temp[2].length() == 1)
				controller.updateUserMap(temp[0].charAt(0), temp[2].charAt(0));
			}
			// Determining if command matches freq.
			if (command.matches(commands.get(2))) {
				controller.requestFrequency();
			}
			// Determining if command matches hint.
			if (command.matches(commands.get(3))) {
				controller.requestHint();
			}
			// Determining if command matches exit.
			if (command.matches(commands.get(4))) {
				System.exit(0);
			}
			// Determining if command matches help.
			if (command.matches(commands.get(5))) {
				printCommands();
			}
			return null;
		}
		
		/*
	     * Purpose: Prints out all the acceptable commands if the user types help.
	     * 
	     * @param None.
	     * 
	     * @return None.
	     */
		public void printCommands() {
			System.out.println("replace X by Y - Replace letter the "
					+ "X with the letter Y in your attempted solution");
			System.out.println("X = Y - A shortcut for the 'replace X by Y' "
					+ "command");
			System.out.println("freq - Display the letter frequencies in the "
					+ "encrypted quotation");
			System.out.println("hint - Display one correct mapping that has "
					+ "not yet been guessed");
			System.out.println("exit - Ends the game early");
			System.out.println("help - List these commands\n");
		}
	}

}
