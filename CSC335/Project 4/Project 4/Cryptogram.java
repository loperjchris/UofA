/* AUTHOR: Ruben Tequida
 * FILE: Cryptogram.java
 * ASSIGNMENT: Project 4: Cryptogram GUI
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
 * when the user correctly guesses the original quote. This program also
 * utilizes an interactive GUI to display the game state as well as the text
 * based version of the game and implements an MVC concept.
 * 
 * Example input file:
 * 
 * Computers are useless. They can only give you answers. - Pablo Picasso
 * Never trust a computer you can't throw out a window. - Steve Wozniak
 */

import javafx.application.Application;

public class Cryptogram {
	public static void main(String[] args) {
		// Launching GUI version
		if (args.length == 0 || args[0].equals("-window")) {
			Application.launch(CryptogramGUIView.class, args);
			// Running text based version
		} else {
			CryptogramTextView view = new CryptogramTextView();
			view.run();
		}
	}
}
