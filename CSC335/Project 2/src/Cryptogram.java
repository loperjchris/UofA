/* AUTHOR: Ruben Tequida
 * FILE: Cryptogram.java
 * ASSIGNMENT: Project 2: More Cryptograms
 * COURSE: CSC 335; Fall 2019
 * Purpose: This program takes a file as an input that contains quotations or
 * sayings. One of the contained quotations is chosen at random and every
 * letter in the quote is replaced with another unique letter. Then, the user
 * is able to input a letter from the encrypted quote and choose which letter
 * to replace it with. The user is also able to utilize other commands to
 * include freq, which displays the number of times a letter appears in the 
 * encrypted quote, hint, which gives the user an unguessed letter pairing,
 * exit, which ends the program before the quote is decrypted, and help which
 * displays the list of usable commands and what they do. The program ends 
 * when the user correctly guesses the original quote.
 * 
 * Example input file:
 * 
 * Computers are useless. They can only give you answers. - Pablo Picasso
 * Never trust a computer you can't throw out a window. - Steve Wozniak
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Cryptogram {
	
	/*
     * Purpose: Opens the file, encrypts the quote, and runs the game for the
     * user.
     * 
     * @param args - which takes in no additional command line arguments.
     * 
     * @return None.
     */
	public static void main(String[] args) {
		String quote = openFile();
		TextManipulator cypher = new TextManipulator(quote);
		run(cypher);
	}
	
	/*
     * Purpose: Opens the file containing the quotes and selects one at random.
     * 
     * Uses a scanner to open the quote file, the random class to generate a
     * random integer to select a quote from the quote file at random, and a 
     * try/catch block to catch FileNotFoundExceptions. Also, catches whether
     * the given file has no quotes in it.
     * 
     * @param None.
     * 
     * @return quote - is a string that contains the selected quote.
     */
	public static String openFile() {
		Scanner file = null;
		Random rand = new Random();
		List<String> quotes = new ArrayList<String>();
		// Attempting to open the provided file.
		try {
			file = new Scanner(new File ("quotes.txt"));
		// Exits the program with an exception if the file can't be found.
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		}
		while (file.hasNextLine()) {
			quotes.add(file.nextLine());
		}
		file.close();
		if (quotes.isEmpty()) {
			System.out.println("The file provided contains no data.");
			System.exit(0);
		}
		// Selecting a random quote
		int i = rand.nextInt(quotes.size());
		String quote = quotes.get(i);
		return quote;
	}
	
	/*
     * Purpose: Enables the user to enter commands.
     * 
     * Uses a while loop to determine if the user provided input matches the
     * syntax of one of the accepted commands and prompts the user to try again
     * if it doesn't. Sends command handling to the Commands class.
     * 
     * @param cypher - a TextManipulator object that holds the origianl quote,
     * the encrypted quote, and the user's guess.
     * 
     * @return None.
     */
	public static void run(TextManipulator cypher) {
		Commands commands = new Commands();
		Scanner input = new Scanner(System.in);
		while (!cypher.checkAnswer(cypher.getUserString())) {
			String command = "";
			cypher.printCrypts();
			System.out.println("");
			System.out.print("Enter a command (help to see commands) ");
			command = input.nextLine().toUpperCase();
			while (!commands.isCommand(command)) {
				System.out.println("Unable to recognize command.\n"
						+ "Type 'help' in order to get a list of commands\n");
				System.out.print("Enter a command (help to see commands) ");
				command = input.nextLine().toUpperCase();
			}
			commands.processCommand(command, cypher);
		}
		cypher.printCrypts();
		System.out.println("You got it!\n");
		input.close();
	}

}
