
/* AUTHOR: Ruben Tequida
 * FILE: Project1.java
 * ASSIGNMENT: Project 1: Cryptograms
 * COURSE: CSC 335; Fall 2019
 * Purpose: This program takes a file as an input that contains quotations or
 * sayings. One of the contained quotations is chosen at random and every
 * letter in the quote is replaced with another unique letter. Then, the user
 * is able to input a letter from the encrypted quote and choose which letter
 * to replace it with. The program ends when the user correctly guesses the
 * original quote.
 * 
 * Example input file:
 * 
 * Computers are useless. They can only give you answers. - Pablo Picasso
 * Never trust a computer you can't throw out a window. - Steve Wozniak
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Project1 {
	/*
     * Purpose: Calls functions to read in the file and select one quote at
     * random, create the cypher of shuffled letters, scramble the quote with
     * the cypher, and call the function to allow the user to solve the 
     * cryptogram.
     * 
     * @param args, which takes in an argument from the command line which is 
     * the file name containing the quotes.
     * 
     * @return None.
     */
	public static void main(String[] args) {
		String quote = getQuote(args[0]);
		String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		// Creates a list with one letter per element.
		List<String> letters = new ArrayList<String>
				(Arrays.asList(alphabet.split("")));
		Collections.shuffle(letters);
		// Maps out each letter to a new letter in the shuffled list.
		HashMap<String, String> letterMap = mapOut(letters, alphabet);
		String scrambledQuote = scramble(quote, letterMap);
		runGuesses(quote, scrambledQuote, letterMap);
	}
	
	/*
     * Purpose: Opens the given file and selects a random quote from it.
     * 
     * @param fileName, which is a string that denotes the file containing the
     * quotes.
     * 
     * @return quotes.get(i).toUpperCase(), which is one of the quotes in the
     * file but all uppercase.
     */
	public static String getQuote(String fileName) {
		Scanner file = null;
		List<String> quotes = new ArrayList<String>();
        // Attempts to open the given file.
        try {
            file = new Scanner(new File(fileName));
            // Throws an exception if the file can't be found.
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        while (file.hasNextLine()) {
        	quotes.add(file.nextLine());
        }
        // Selecting a random quote from the file.
        Random rand = new Random();
        int i = rand.nextInt(quotes.size());
        return quotes.get(i).toUpperCase();
	}
	
	/*
     * Purpose: Maps out the alphabet to a new letter in the corresponding
     * position in the shuffled alphabet list.
     * 
     * @param letters is a list of strings that contains the shuffled alphabet.
     * alphabet is a string of the each alphabet letter in order.
     * 
     * @return letterMap is a hashmap that contains the mapping of alphabet
     * letters to shuffled letters.
     */
	public static HashMap<String, String> mapOut(List<String> letters, 
			String alphabet) {
		HashMap<String, String> letterMap = new HashMap<String, String>();
		for (int i = 0; i < letters.size(); i++) {
			letterMap.put(Character.toString(alphabet.charAt(i)), 
					letters.get(i));
		}
		return letterMap;
	}
	
	/*
     * Purpose: Scrambles up the origianl quote utilizing the letterMap 
     * hashmap.
     * 
     * @param quote is a string which is the original, unscrambled quote. 
     * letterMap is the hashmap that contains the map out of alphabet letters
     * to scrambled letters.
     * 
     * @return scrambledQuote is a string that is the scrambled version of the
     * original quote.
     */
	public static String scramble(String quote, 
			HashMap<String, String> letterMap) {
		String scrambledQuote = quote.toLowerCase();
		for (String key : letterMap.keySet()) {
			scrambledQuote = scrambledQuote.replace(key.toLowerCase(), 
					letterMap.get(key));
		}
		return scrambledQuote;
	}
	
	/*
     * Purpose: Allows the user to replace letters in the scrambled quote with
     * the letters they believe to be the solution to the cryptogram.
     * 
     * @param quote is the original quote. scrambledQuote is the scrambled
     * version of the original quote. letterMap is the hashmap containing the
     * mapping of alphabet letters to scrambled letters.
     * 
     * @return None.
     */
	public static void runGuesses(String quote, String scrambledQuote, 
			HashMap<String, String> letterMap) {
		// Maps every key to a blank space value.
		HashMap<String, String> userGuesses = emptyMap(letterMap);
		Scanner input = new Scanner(System.in);
		String userOut = "";
		// Repeats until the cryptogram is solved.
		while (!userOut.toUpperCase().equals(quote)) {
			userOut = scrambledQuote;
			System.out.println(scrambledQuote);
			System.out.print("Enter the letter to replace: ");
			String letter = input.next().toUpperCase();
			System.out.print("Enter its replacement: ");
			String replacement = input.next().toUpperCase();
			// Adds the users guess to the new mapping.
			userGuesses.put(letter, replacement);
			for (String key : userGuesses.keySet()) {
				userOut = userOut.replace(key, 
						userGuesses.get(key).toLowerCase());
			}
			System.out.println("\n" + userOut.toUpperCase());
		}
		System.out.println(scrambledQuote + "\n\nYou got it!");
		input.close();
	}
	
	/*
     * Purpose: Replaces every value in a hashmap with blank spaces.
     * 
     * @param letterMap is a map of alphabet letters to scrambled letters.
     * 
     * @return letterMap is a hashmap that now contains empty blank spaces for
     * every value.
     */
	public static HashMap<String, String> emptyMap(
			HashMap<String, String> letterMap) {
		for (String key : letterMap.keySet()) {
			letterMap.put(key, " ");
		}
		return letterMap;
	}

}
