/**
 * TextManipulator.java
 * 
 * Builds a TextManipulator object and stores the original quote, the encrypted
 * quote, the user's guess, and all mappings of encrypted letters to actual 
 * letters and user guesses to encrypted letters.
 * 
 * Usage instructions:
 * 
 * Construct a TextManipulator
 * TextManipulator cypher= new TextManipulator(quote);
 * 
 * Create the letter cypher used to encrypt a qutoe
 * cypher.createCypher();
 * Maps all letters of the alphabet with another random letter (but not with 
 * itself) 
 * 
 * Format the quote to print properly
 * cypher.formatQuote();
 * Manipulates the encrypted string so it prints out with a max of 80 
 * characters per line.
 * 
 * Other useful methods:
 * cypher.encryptQuote();
 * cypher.createEmptyUserMap();
 * cypher.formatUserString();
 * cypher.getFrequency();
 * cypher.getHint();
 * cypher.updateUserString;
 * cypher.updateUserMap(key, value);
 * cypher.checkAnswer();
 * cypher.getUserString();
 * cypher.printCrypts();
 * cypher.getFrequency();
 * cypher.getHint();
 * cypher.updateUserString();
 * cypher.updateUserMap(key, value);
 * cypher.checkAnswer(guess);
 * cypher.getUserString();
 * cypher.getUserMap();
 * cypher.getLetterMap();
 * 
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.TreeMap;

public class TextManipulator {
	
	// Variable fields used for TextManipulator object construction.
	private String quote;
	private String encryptedQuote;
	private String userString;
	private List<String> formatted;
	private List<String> formattedUserString;
	private ArrayMap<Character, Character> letterMap;
	private ArrayMap<Character, Character> userMap;
	private final int LINE_MAX = 80;
	private final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	
	/*
     * Purpose: Setups up a TextManipulator object to have all the right
     * strings and maps necessary to process the program.
     * 
     * @param quote - a sting of the original quote that was chosen for the 
     * cryptogram.
     * 
     * @return None.
     */
	public TextManipulator(String quote) {
		this.quote = quote.toUpperCase();
		userString = quote;
		formatted = new ArrayList<String>();
		formattedUserString = new ArrayList<String>();
		letterMap = new ArrayMap<Character, Character>();
		userMap = new ArrayMap<Character, Character>();
		createCypher();
		encryptQuote();
		formatQuote();
		createEmptyUserMap();
		updateUserString();
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
     * Purpose: Encrypts the original quote using the encrypted letter mapping.
     * 
     * @param None.
     * 
     * @return None.
     */
	private void encryptQuote() {
		encryptedQuote = quote.toLowerCase();
		for (Character key : letterMap.keySet()) {
			encryptedQuote = encryptedQuote.replace(Character.toLowerCase(key), letterMap.get(key));
		}
	}
	
	/*
     * Purpose: Breaks apart the encrypted quote into lines <= 80 characters.
     * 
     * This is done by splitting at spaces to keep words intact and new lines
     * are inserted if adding a new word would make the line go over 80
     * characters.
     * 
     * @param None.
     * 
     * @return None.
     */
	private void formatQuote() {
		StringTokenizer words = new StringTokenizer(encryptedQuote, " ");
		String section = "";
		int lineCount = 0;
		while (words.hasMoreTokens()) {
			String word = words.nextToken();
			// If adding the new word makes the line go over 80 characters a
			// new line token is inserted first.
			if (lineCount + word.length() > LINE_MAX) {
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
		for (int i = 0; i < ALPHABET.length(); i++) {
			userMap.put(ALPHABET.charAt(i), ' ');
		}
	}
	
	/*
     * Purpose: Formats the users guess so that each line only displays up to 
     * 80 characters
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
	}
	
	/*
     * Purpose: Prints out the user guess and encrypted quote as desired.
     * 
     * @param None.
     * 
     * @return None.
     */
	public void printCrypts() {
		formatUserString();
		for (int i = 0; i < formatted.size(); i++) {
			System.out.println(formattedUserString.get(i));
			System.out.println(formatted.get(i));
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
     * @return None.
     */
	public void getFrequency() {
		Map<String, Integer> freq = new TreeMap<String, Integer>();
		char[] alpha = ALPHABET.toCharArray();
		for (char letter : alpha) {
			freq.put(Character.toString(letter).toUpperCase(), 0);
		}
		char[] onlyLetters = encryptedQuote.replaceAll("[^a-z^A-Z]","").toCharArray();
		for (char letter: onlyLetters) {
			String newLetter = Character.toString(letter).toUpperCase();
			freq.put(newLetter, freq.get(newLetter) + 1);
		}
		int i = 0;
		for (String key : freq.keySet()) {
			if (i == 6) {
				System.out.print(key + ": " + freq.get(key) + "\n");
				i = 0;
			} else {
				System.out.print(key + ": " + freq.get(key) + " ");
				i++;
			}
		}
		System.out.println("\n");
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
     * @return None.
     */
	public void getHint() {
		Random rand = new Random();
		while (true) {
			int r = rand.nextInt(25);
			Character letter = ALPHABET.charAt(r);
			Character correctAnswer = letterMap.get(letter);
			Character userGuess = userMap.get(correctAnswer);
			// Checks if a user already correctly guessed the pairing or if the
			// pairing even exists in the encrypted quote.
			if (!letter.equals(userGuess) && encryptedQuote.contains(Character.toString(correctAnswer))) {
				System.out.println(correctAnswer + " = " + letter);
				break;
			}
		}
		System.out.println("");
	}
	
	/*
     * Purpose: Updates the user string with the guesses that have input so
     * far.
     * 
     * @param None.
     * 
     * @return None.
     */
	public void updateUserString() {
		userString = encryptedQuote;
		for (Character key : userMap.keySet()) {
			userString = userString.replace(key, Character.toLowerCase(userMap.get(key)));
		}
		userString = userString.toUpperCase();
	}
	
	/*
     * Purpose: Puts the users guess into the user guess map.
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
		if (ALPHABET.indexOf(key) >= 0 && ALPHABET.indexOf(value) >= 0) {
			userMap.put(key, value);
			updateUserString();
		}
	}
	
	/*
     * Purpose: Checks whether the user's guess is equal to the original quote.
     * 
     * @param guess - a String that represents the user's guess.
     * 
     * @return A boolean result if the two strings match.
     */
	public Boolean checkAnswer(String guess) {
		return guess.equals(quote);
	}
	
	// Getter method for userString.
	public String getUserString() {
		return userString;
	}
	
	// Getter method for userMap.
	public Map<Character, Character> getUserMap() {
		return userMap;
	}
	
	// Getter method for letterMap.
	public Map<Character, Character> getLetterMap() {
		return letterMap;
	}

}
