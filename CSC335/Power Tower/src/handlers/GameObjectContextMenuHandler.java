package handlers;
/**
 * GameObjectClickedHandler.java
 * 
 * Handles all GameObjects click events. 
 * 
 *  * Usage instructions:
 * 
 * Construct GameObjectClickedHandler:
 * GameObjectClickedHandler g = new GameObjectClickedHandler(view, col, row, controller);
 */
import java.io.FileNotFoundException;

import game.TowerDefenseController;
import javafx.event.EventHandler;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import viewable.Viewable;
import viewable.gameObjects.Tower;
import viewable.gameObjects.TowerType;

public class GameObjectContextMenuHandler implements EventHandler<ContextMenuEvent>{
	
	private int row;
	private int col;
	private TowerDefenseController controller;
	private Viewable view;
	
	/**
	 *  Purpose: Constructor for the class
	 *  
	 * @param view the main Viewable object
	 * 
	 * @param col an integer containing the column
	 * 
	 * @param row an integer containing the row
	 * 
	 * @param controller the main TowerDefenseController object
	 */
	public GameObjectContextMenuHandler(Viewable view, int col, int row, TowerDefenseController controller) {
		this.view = view;
		this.row = row;
		this.col = col;
		this.controller = controller;
	}
	
	/**
	 *  Purpose: handler for MouseEvents regarding towers and upgrades
	 *  
	 *  @param e is the MouseEvent being handled
	 */
	@Override
	public void handle(ContextMenuEvent e) {
		if(!controller.hasConnected()||controller.getPlayer().isFinished()||controller.isPaused()) {
			return;
		}
		if(col>controller.getMapArray().length/2) {
			return;
		}
		controller.addTower(row, col, TowerType.Deleted);
		try {
			controller.getPlayer().drawCards(1);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
