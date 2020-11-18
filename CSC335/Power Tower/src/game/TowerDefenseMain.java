/**
 * @author: Aramis Sennyey
 * @author: Chris Loper
 * @author: Duncan Rover
 * @author: Ruben Tequida
 * 
 * FILE: TowerDefenseMain.java
 * ASSIGNMENT: Team Project: Tower Defense Game
 * COURSE: CSC 335; Fall 2019
 * Purpose: This program is our version of a tower defense game with a deck
 * builder aspect added as well. The game functions as a multiplayer tower
 * defense game where each player can play all the cards within their hand 
 * and place any towers they may have on the field. A player may also buy any
 * number of cards from the market as long as they have the gold to do so. 
 * Once both players have completed their turns then the enemy wave is sent
 * and then players begin another turn.
 */

package game;
import javafx.application.Application;

public class TowerDefenseMain {

	/**
     * purpose: Launches the view which creates the GUI for the game.
     * 
     * @param args - takes in no additional command line arguments.
     */
	public static void main(String[] args) {
		Application.launch(TowerDefenseView.class, args);
	}

}
