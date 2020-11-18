package handlers;
/**
 * CardObjectClickedHandler.java
 * 
 * Handles all clicks on cards in the game scene
 * 
 *  * Usage instructions:
 * 
 * Construct CardObjectClickedHandler:
 * CardObjectClickedHandler h = new CardObjectClickedHandler(card, controller);
 */
import java.util.Map;

import game.TowerDefenseController;
import javafx.event.EventHandler;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import viewable.cards.Card;
import viewable.cards.abilityCards.AbilityCard;
import viewable.gameObjects.Player;

public class CardObjectClickedHandler implements EventHandler<MouseEvent>{
	private Card card;
	private Player player;
	private TowerDefenseController controller;
	
	/**
	 * Purpose: constructor for the class
	 * 
	 * @param card is a Card object
	 * 
	 * @param controller is a TowerDefenseController object
	 */
	public CardObjectClickedHandler(Card card, TowerDefenseController controller) {
		this.card = card;
		this.controller = controller;
	}

	/**
	 *  Purpose: handles a MouseEvent for cards. Either selects
	 *  a card or uses the ability of a card
	 *  
	 *  @param arg0 - the event being passed in
	 */
	@Override
	public void handle(MouseEvent arg0) {
		if(!controller.hasConnected()||controller.getPlayer().isFinished()||controller.isPaused()) {

			return;
		}
		if (!arg0.getButton().equals(MouseButton.PRIMARY)) {
			arg0.consume();
			return;
		} 
		if (arg0.getClickCount() == 1) {
			controller.setSelectedCard(card);
		}
		if (arg0.getClickCount() == 2) {
			if (card instanceof AbilityCard) {
				controller.useAbilityCard((AbilityCard)card);
			}
		}
	}
}
