/**
 * CryptogramModel.java
 * 
 * Handles all the data associated with maintaining the instruction set and 
 * gameplay of cryptograms. Creates the maps that hold character pairings, 
 * formats strings for each view style, generates hints and frequency strings
 * and determines if the user's guesses have solved the cryptogram
 * 
 * Usage instructions:
 * 
 * Construct CryptogramModel
 * CryptogramModel model = new CryptogramMdoel(type);
 *  
 * Other Useful Methods:
 * model.openFile();
 * model.createCypher();
 * model.createEmptyUserMap();
 * model.encryptQuote();
 * model.formatQuote();
 * model.updateUserString();
 * model.formatUserString();
 * model.updateUserMap(key, value);
 * model.getFrequency();
 * model.getHint();
 * model.checkAnswer();
 * model.getUserString(guess);
 * model.getFormatted();
 * model.getFormattedUserString();
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Random;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.TreeMap;

import javafx.util.Pair;

public class CryptogramModel extends Observable{
	
	private String type;
	private String originalQuote;
	private String userString;
	private List<String> formatted;
	private List<String> formattedUserString;
	private Map<Character, Character> letterMap;
	private Map<Character, Character> userMap;
	
	/*
     * Purpose: create all the maps and strings that hold encrypted/user data.
     * Then opens the file containing the quotes, randomly picks a quote, and 
     * initially populates the objects with the encrypted version of the quote.
     * 
     * @param type - a string that tells the model what kind of view it is.
     * 
     * @return None.
     */
	public CryptogramModel(String type) {
		this.type = type;
		originalQuote = "";
		userString = "";
		formatted = new ArrayList<String>();
		formattedUserString = new ArrayList<String>();
		letterMap = new HashMap<Character, Character>();
		userMap = new HashMap<Character, Character>();
		openFile();
		createCypher();
		createEmptyUserMap();
		updateUserString();
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
     * @return None.
     */
	public void openFile() {
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
		originalQuote = quotes.get(i).toUpperCase();
	}
	
	/*
     * Purpose: Scrambles the letters to create a unique mapping so every
     * letter is mapped to a new letter.
     * 
     * Shuffles the letters of the alphabet until the last entry is not equal
     * to 'Z'. This is done so that when a letter maps to itself it goes to the
     * next letter and 'Z' is not left mapping to 'Z'.
     * 
     * @param None.
     * 
     * @return None.
     */
	private void createCypher() {
		// Creates a list with one letter per element.
		String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		List<String> letters = new ArrayList<String>
				(Arrays.asList(ALPHABET.split("")));
		// Continues to shuffle until the last element doesn't equal 'Z'.
		while (letters.get(25).equals("Z")) {
			Collections.shuffle(letters);
		}
		int i = 0;
		// Mapping each letter to another letter and utilizing the next
		// character if the two letters match.
		while (!letters.isEmpty()) {
			// Goes to next letter if the two letters match.
			if (letters.get(0).equals(Character.toString(ALPHABET.charAt(i)))) {
				letterMap.put(ALPHABET.charAt(i), letters.get(1).charAt(0));
				letters.remove(1);
			} else {
				letterMap.put(ALPHABET.charAt(i), letters.get(0).charAt(0));
				letters.remove(0);
			}
			i++;
		}
	}
	
	/*
     * Purpose: Initializes the user letter map mapping every letter to a blank
     * space.
     * 
     * @param None.
     * 
     * @return None.
     */
	private void createEmptyUserMap() {
		String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		for (int i = 0; i < ALPHABET.length(); i++) {
			userMap.put(ALPHABET.charAt(i), ' ');
		}
	}
	
	/*
     * Purpose: Encrypts the original quote using the encrypted letter mapping.
     * 
     * @param None.
     * 
     * @return encryptedQuote - a string representation of the encrypted quote.
     */
	private String encryptQuote() {
		String encryptedQuote = originalQuote.toLowerCase();
		for (Character key : letterMap.keySet()) {
			encryptedQuote = encryptedQuote.replace(Character.toLowerCase(key),
					letterMap.get(key));
		}
		return encryptedQuote;
	}
	
	/*
     * Purpose: Breaks apart the encrypted quote into lines <= the line max
     * which depends on the type of view that created the model.
     * 
     * This is done by splitting at spaces to keep words intact and new lines
     * are inserted if adding a new word would make the line go over the line
     * max characters.
     * 
     * @param None.
     * 
     * @return None.
     */
	public void formatQuote() {
		String encryptedQuote = encryptQuote();
		StringTokenizer words = new StringTokenizer(encryptedQuote, " ");
		String section = "";
		int lineMax = 0;
		// Determining the line max depending on the view.
		if (type.equals("text")) {
			lineMax = 80;
		} else {
			lineMax = 30;
		}
		int lineCount = 0;
		while (words.hasMoreTokens()) {
			String word = words.nextToken();
			// If adding the new word makes the line go over line max a
			// new line token is inserted first.
			if (lineCount + word.length() > lineMax) {
				section = section.trim();
				section += "\n";
				lineCount = 0;
			}
			section += word + " ";
			// counting the word plus the space after it.
			lineCount += word.length() + 1;
		}
		// Removing the last space from the quote.
		section = section.trim();
		formatted = Arrays.asList(section.split("\n"));
		updateUserString();
		setChanged();
		notifyObservers(formatted);
	}
	
	/*
     * Purpose: Updates the user string with the guesses that have been input 
     * so far.
     * 
     * @param None.
     * 
     * @return None.
     */
	public void updateUserString() {
		userString = encryptQuote();
		for (Character key : userMap.keySet()) {
			userString = userString.replace(key, Character.toLowerCase(userMap.get(key)));
		}
		userString = userString.toUpperCase();
		formatUserString();
		checkAnswer(userString);
	}
	
	/*
     * Purpose: Formats the users guess so that each line only displays up to 
     * the same amount of characters as the encrypted quote formatted version.
     * 
     * This is done using the formatted encrypted quote as a template and
     * splitting the user string where the encrypted quote is split.
     * 
     * @param None.
     * 
     * @return None.
     */
	private void formatUserString() {
		int i = 0;
		formattedUserString = new ArrayList<String>();
		for (String item : formatted) {
			formattedUserString.add(userString.substring(i, i + item.length()));
			i += item.length() + 1;
		}
		List<List<String>> list = new ArrayList<List<String>>();
		list.add(formattedUserString);
		setChanged();
		notifyObservers(list);
	}
	
	/*
     * Purpose: Puts the users guess into the user guess map, updates the
     * userString, and notifies all observers that a change has been made.
     * 
     * @param key - a Character that represents a letter in the encrypted form
     * of the quote.
     * 
     * @param value - a Character that represents the user's guess for a letter
     * within the encrypted form of the quote.
     * 
     * @return None.
     */
	public void updateUserMap(Character key, Character value) {
		String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		if (ALPHABET.indexOf(key) >= 0 && ALPHABET.indexOf(value) >= 0) {
			userMap.put(key, value);
			Pair<Character, Character> pair = new Pair<>(key, value);
			updateUserString();
			setChanged();
			notifyObservers(pair);
		}
	}
	
	/*
     * Purpose: Prints out the number of times a letter appears in the
     * encrypted quote.
     * 
     * Creates a map of all letters in the alphabet mapped to 0. Then, removes
     * all non-letter characters and adds 1 to the letters map if it appears in
     * the encrypted string.
     * 
     * @param None.
     * 
     * @return output - a string that shows how many times a letter appears in
     * the encrypted quote and is formatted properly for each veiw.
     */
	public void getFrequency() {
		String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		String encryptedQuote = encryptQuote();
		int limit = 0;
		// Setting the line limit depending on which view called this method
		if (type.equals("window")) {
			limit = 27;
		} else {
			limit = 6;
		}
		Map<String, Integer> freq = new TreeMap<String, Integer>();
		char[] alpha = ALPHABET.toCharArray();
		// Creating a map entry for each letter mapped to 0.
		for (char letter : alpha) {
			freq.put(Character.toString(letter).toUpperCase(), 0);
		}
		// Getting only alphabet characters from encrypted quote.
		char[] onlyLetters = encryptedQuote.replaceAll("[^a-z^A-Z]","")
				.toCharArray();
		// Updating each letter by one when it appears in the encrypted quote.
		for (char letter: onlyLetters) {
			String newLetter = Character.toString(letter).toUpperCase();
			freq.put(newLetter, freq.get(newLetter) + 1);
		}
		String output = "";
		int i = 0;
		// Splitting the frequency string based on the limit
		for (String key : freq.keySet()) {
			if (i == limit) {
				output += key + ": " + freq.get(key) + "\n";
				i = 0;
			} else {
				if (type.equals("window")) {
					output += key + " " + freq.get(key) + "\n";
				} else {
					output += key + ": " + freq.get(key) + " ";
				}
				i++;
			}
		}
		output += "\n";
		setChanged();
		notifyObservers(output);
	}
	
	/*
     * Purpose: Prints out a letter mapping of a correct letter to an unguessed
     * or incorrectly guessed letter.
     * 
     * Does not display letter pairing that the user already guessed and are 
     * correct nor pairings that aren't a part of the given quote.
     * 
     * @param None.
     * 
     * @return output - a string that shows an encrypted letter mapped to its 
     * unencrypted character.
     */
	public void getHint() {
		String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		String encryptedQuote = encryptQuote();
		Random rand = new Random();
		String output = "";
		while (true) {
			int r = rand.nextInt(25);
			Character letter = ALPHABET.charAt(r);
			Character correctAnswer = letterMap.get(letter);
			Character userGuess = userMap.get(correctAnswer);
			// Checks if a user already correctly guessed the pairing or if the
			// pairing even exists in the encrypted quote.
			if (!letter.equals(userGuess) && encryptedQuote.contains(Character.toString(correctAnswer))) {
				output += correctAnswer + " = " + letter;
				break;
			}
		}
		output += "\n";
		setChanged();
		notifyObservers(output);
	}
	
	/*
     * Purpose: Checks whether the user's guess is equal to the original quote.
     * 
     * @param guess - a String that represents the user's guess.
     * 
     * @return A boolean result if the two strings match.
     */
	public void checkAnswer(String guess) {
		Boolean answer = false;
		if (guess.equals(originalQuote)) {
			answer = true;
		}
		setChanged();
		notifyObservers(answer);
	}
}
