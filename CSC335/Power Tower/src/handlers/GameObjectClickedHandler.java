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
import javafx.scene.input.MouseEvent;
import viewable.Viewable;
import viewable.cards.towers.TowerCard;
import viewable.gameObjects.Tower;
import viewable.mapObjects.Path;

public class GameObjectClickedHandler implements EventHandler<MouseEvent>{
	
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
	public GameObjectClickedHandler(Viewable view, int col, int row, TowerDefenseController controller) {
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
	public void handle(MouseEvent e) {
		if(!controller.hasConnected()||controller.getPlayer().isFinished()||controller.isPaused()) {
			return;
		}
		if(col>controller.getMapArray().length/2) {
			return;
		}
		if(view instanceof Path||!(controller.getPlayer().getSelectedCard() instanceof TowerCard)) {
			return;
		}
		if(view!=null&&((Tower)view).getUpgraded()) {
			return;
		}
		if(controller.canUpgrade(row, col)) {
			controller.upgradeTower((Tower)view, row, col);
			try {
				((ImageView) e.getTarget()).setImage(ImageResourceLoadingHandler.getResource(view).getImage());
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}else {
			controller.useTowerCard(row, col);
		}
	}
}
