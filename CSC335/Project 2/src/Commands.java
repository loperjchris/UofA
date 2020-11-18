/**
 * Commands.java
 * 
 * Stores all the acceptable commands and processes them if they match.
 * Prints a list of all the commands as well.
 * 
 * Usage instructions:
 * 
 * Construct Commands
 * Commands commands = new Commands();
 * 
 * Process a command
 * commands.processCommand(command);
 * Determines which function to call based on the user's input.
 * 
 * Other useful methods:
 * commands.isCommand(command);
 * commands.printCommands();
 */

import java.util.ArrayList;
import java.util.List;

public class Commands {

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
	public Commands() {
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
	public void processCommand(String command, TextManipulator cypher) {
		String temp[] = command.split(" ");
		// Determining if command matches replace X by Y format.
		if (command.matches(commands.get(0))) {
			if (temp[1].length() == 1 && temp[3].length() == 1) {
				cypher.updateUserMap(temp[1].charAt(0), temp[3].charAt(0));
			}
		}
		// Determining if command matches X = Y format.
		if (command.matches(commands.get(1))) {
			if (temp[0].length() == 1 && temp[2].length() == 1)
			cypher.updateUserMap(temp[0].charAt(0), temp[2].charAt(0));
		}
		// Determining if command matches freq.
		if (command.matches(commands.get(2))) {
			cypher.getFrequency();
		}
		// Determining if command matches hint.
		if (command.matches(commands.get(3))) {
			cypher.getHint();
		}
		// Determining if command matches exit.
		if (command.matches(commands.get(4))) {
			System.exit(0);
		}
		// Determining if command matches help.
		if (command.matches(commands.get(5))) {
			printCommands();
		}
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
		System.out.println("help - List these commands");
	}
}
