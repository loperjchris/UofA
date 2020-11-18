/**
 * @author Ruben Tequida
 * 
 * Initial: Setup and creation of Player object
 */

package viewable.gameObjects;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collections;
import java.util.List;

import game.TowerDefenseController;
import handlers.CardObjectClickedHandler;
import handlers.GameObjectClickedHandler;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;
import viewable.Viewable;
import viewable.cards.Card;
import viewable.cards.abilityCards.PlunderCard;
import viewable.cards.towers.ArcherTowerCard;

public class Player implements Serializable{
	
	private IntegerProperty health;
	private ListProperty<ImageView> hand;
	private java.util.Map<Card, ImageView> mapCards;
	private Deck draw;
	private Deck discard;
	private IntegerProperty gold;
	private Card selectedCard;
	private int goldMultiplier;
	private transient TowerDefenseController controller;
	private volatile boolean turnComplete;
	private int extraMinions;
	
	/**
	 * constructor for the player
	 * 
	 * @param controller: the game controller
	 * 
	 * @throws FileNotFoundException throws exception if card resources can not be found
	 */
	public Player(TowerDefenseController controller) throws FileNotFoundException {
		this.controller = controller;
		turnComplete = false;
		extraMinions = 0;
		health = new SimpleIntegerProperty(20);
		mapCards = new HashMap<Card, ImageView>();
		ObservableList<ImageView> observableList = FXCollections.observableArrayList(new ArrayList<ImageView>());
		hand = new SimpleListProperty<ImageView>(observableList);
		draw = new Deck();
		discard = new Deck();
		gold = new SimpleIntegerProperty(20);
		goldMultiplier = 1;
		for (int i = 0; i < 6; i++) {
			draw.add(new ArcherTowerCard());
		}
		for (int i = 0; i < 4; i++) {
			draw.add(new PlunderCard());
		}
		draw.shuffle();
		for (int i = 0; i < 5; i++) {
			Card c = draw.drawCard();
			if(c==null) {
				continue;
			}
			ImageView view = getResource(c);
			hand.add(view);
			mapCards.put(c, view);
		}
		Collections.shuffle(hand);
	}
	
	/**
	 * Resets the single turn multipliers.
	 */
	public void reset() {
		goldMultiplier=1;
	}
	
	/**
	 * Resets the player.
	 * @throws FileNotFoundException when resources are missing
	 */
	public void resetAsNew() throws FileNotFoundException {
		goldMultiplier = 1;
		turnComplete = false;
		extraMinions = 0;
		health.setValue(20);;
		mapCards.clear();
		hand.clear();
		draw.reset();
		discard.reset();
		gold.setValue(20);;
		goldMultiplier = 1;
		for (int i = 0; i < 6; i++) {
			draw.add(new ArcherTowerCard());
		}
		for (int i = 0; i < 4; i++) {
			draw.add(new PlunderCard());
		}
		draw.shuffle();
		for (int i = 0; i < 5; i++) {
			Card c = draw.drawCard();
			if(c==null) {
				continue;
			}
			ImageView view = getResource(c);
			hand.add(view);
			mapCards.put(c, view);
		}
		Collections.shuffle(hand);
	}
	
	/**
	 * purpose: loads the resource image for the cards a player owns
	 * 
	 * @param obj: card object
	 * 
	 * @return: the art of the card
	 * 
	 * @throws FileNotFoundException throws exception if art can not be found
	 */
	private ImageView getResource(Card obj) throws FileNotFoundException {
		ImageView view;
		if(obj == null) {
			view = new ImageView(new Image(new FileInputStream(Viewable.getDefaultResource())));
		}else {
			view = new ImageView(new Image(new FileInputStream(obj.getResource())));
		}
		
		view.setFitHeight(196);
		view.setFitWidth(128);
		
		view.setOnMouseClicked(new CardObjectClickedHandler(obj, controller));
		
		return view;
	}
	
	/**
	 * purpose: getter for the players hand
	 * 
	 * @return the players hand
	 */
	public ObservableList<ImageView> getHand(){
		return hand;
	}
	
	/**
	 * purpose: add a card to the discard once it is played
	 * 
	 * @param card: the card
	 */
	public void addToDiscard(Card card) {
		hand.removeAll(mapCards.get(card));
		discard.add(card);
		hand.remove(card);
		mapCards.remove(card);
	}
	
	/**
	 * purpose: reshuffles cards into draw pile
	 */
	public void resetDraw() {
		for (Card card : discard.getDeck()) {
			draw.add(card);
		}
		draw.shuffle();
		discard.empty();
	}
	
	/**
	 * purpose: draws the cards, resets the draw pile if it is empty
	 * 
	 * @param x: number of cards
	 * 
	 * @throws FileNotFoundException throws exception if resource can not be acquired
	 */
	public void drawCards(int x) throws FileNotFoundException {
		for (int i = 0; i < x; i++) {
			if (draw.isEmpty()) {
				resetDraw();
			}
			Card c = draw.drawCard();
			if(c==null) {
				continue;
			}
			ImageView view = getResource(c);
			hand.add(view);
			mapCards.put(c, view);
		}
	}
	
	/**
	 * purpose: discards a card
	 */
	public void discardHand() {
		for (Card c : mapCards.keySet()) {
			discard.add(c);
			hand.remove(mapCards.get(c));
		}
		mapCards.clear();
	}
	
	/**
	 * purpose: check if the turn is completed
	 * 
	 * @return true if turn is complete
	 */
	public boolean isFinished() {
		return turnComplete;
	}
	
	/**
	 * purpose: sets whether the turn has been completed or not
	 * 
	 * @param b boolean of whether turn is done or not yet
	 */
	public void setComplete(boolean b) {
		turnComplete = b;
	}
	
	/**
	 * purpose: increases the players gold when they kill a minion
	 * 
	 * @param amount amount to increase gold by
	 */
	public void increaseGold(int amount) {
		gold.setValue((amount * goldMultiplier)+getGold());
	}
	
	/**
	 * purpose: gains life for the purpose of the gain life ability card
	 * 
	 * @param amount amount to increase life by
	 */
	public void gainLife(int amount) {
		health.setValue(amount+getHealth());
	}
	
	/**
	 * purpose: pays life for card that requires a life cost
	 * 
	 * @param amount: amount life to pay
	 */
	public void payLife(int amount) {
		health.setValue(getHealth()-amount);
	}
	
	/**
	 * purpose: increases the amount of gold to be received from killing minions
	 */
	public void buffReward() {
		goldMultiplier ++;
	}
	
	/**
	 * purpose: damages the other player
	 * 
	 * @param amount: amount to damage the other player
	 */
	public void damageOther(int amount) {
		controller.damageOther(amount);
	}
	
	/**
	 * purpose: getter for the amount of gold currently owned
	 * 
	 * @return: the amount of gold owned
	 */
	public int getGold() {
		return gold.intValue();
	}
 	
	/**
	 * purpose: takes damage
	 *  
	 * @param amount: amount of damage taken
	 */
	public void damageTaken(int amount) {
		health.setValue(getHealth()-amount);
	}
	
	/**
	 * purpose: getter for current player health
	 * 
	 * @return: amount of health
	 */
	public int getHealth() {
		return health.intValue();
	}

	/**
	 * purpose: selects the card that has been chosen
	 * 
	 * @param s: the card
	 */
	public void setSelectedCard(Card s) {
		selectedCard = s;
	}
	
	/**
	 * purpose: getter for the selected card
	 * 
	 * @return: the selected card
	 */
	public Card getSelectedCard() {
		return selectedCard;
	}
	
	/**
	 * purpose: gets health of player
	 * 
	 * @return: players health
	 */
	public IntegerProperty getViewableHealth() {
		return health;
	}
	
	/**
	 * purpose: gets player gold
	 * 
	 * @return: player gold
	 */
	public IntegerProperty getViewableGold() {
		return gold;
	}
	
	/**
	 * purpose: gets the players discard pile
	 * 
	 * @return: the discard pile
	 */
	public Deck getDiscard() {
		return discard;
	}
	
	/**
	 * purpose: getter for the players cards in hands
	 * 
	 * @return: the cards in hands
	 */
	public java.util.Map<Card, ImageView> getCardHand() {
		return mapCards;
	}
	
	/**
	 * purpose: prints out the cards the player has
	 * 
	 * @param deck: the deck of the cards
	 */
	public void printCards(Deck deck) {
		List<Card> cards = deck.getDeckAsList();
		System.out.println(cards);
	}

	// Setter for extra minions.
	public void summonMinion(int minionstosummon) {
		extraMinions = minionstosummon;
	}
	
	public int getExtraMinions() {
		int extra = extraMinions;
		extraMinions = 0;
		return extra;
	}
	
	// Getter for gold mult.
	public int getGoldMultiplier() {
		return goldMultiplier;
	}
}
