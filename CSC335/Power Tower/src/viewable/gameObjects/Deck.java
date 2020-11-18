/**
 * @author Aramis Sennyey
 * @author Ruben Tequida
 * 
 * rt update - adding drawCard and shuffle function
 */

package viewable.gameObjects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import viewable.cards.Card;

public class Deck implements Serializable{
	private List<Card> deck;
	
	/**
	 * zero argument constructor for deck
	 */
	public Deck() {
		deck = new ArrayList<Card>();
	}
	
	/**
	 * constructor for deck with param size
	 * 
	 * @param size: the size of the deck
	 */
	public Deck(int size) {
		deck = new ArrayList<Card>();
		
	}
	
	/**
	 * Resets the deck to the pre game state.
	 */
	public void reset() {
		deck.clear();
	}
	
	/**
	 * purpose: draws a card and removes it from the deck
	 * 
	 * @return: the card that was drawn
	 */
	public Card drawCard() {
		if(deck.size()==0) {
			return null;
		}
		Card card = deck.get(0);
		deck.remove(0);
		return card;
	}
	
	/**
	 * purpose: shuffles the collection of cards in the deck
	 */
	public void shuffle() {
		Collections.shuffle(deck);
	}
	
	/**
	 * purpose: adds a card to the deck when it is purchased from the market
	 * 
	 * @param card: card to be added to the deck
	 */
	public void add(Card card) {
		deck.add(card);
	}
  
	/**
	 * purpose: checks if the deck is empty
	 * 
	 * @return: true if the deck is empty and false otherwise
	 */
	public Boolean isEmpty() {
		return deck.isEmpty();
	}
	
	/**
	 * purpose: getter for the deck object
	 * 
	 * @return: the deck
	 */
	public List<Card> getDeck() {
		return deck;
	}
	
	/**
	 * purpose: empties the deck
	 */
	public void empty() {
		deck.removeAll(deck);
	}
	
	/**
	 * purpose: getter for the size of the deck
	 * 
	 * @return: the number of cards in the deck
	 */
	public int getSize() {
		return deck.size();
	}
	
	/**
	 * purpose: gets the deck as a list
	 * 
	 * @return: deck as a list
	 */
	public List<Card> getDeckAsList() {
		return deck;
	}
	
	/**
	 * purpose: prints the names of the cards as strings
	 */
	public String toString() {
		String s = "";
		for (Card c : deck) {
			s += c.getName() + ", ";
		}
		s = s.substring(0, s.length() - 2);
		return s;
	}
	
}