package handlers;
/**
 * MapEditorImageClickedHandler.java
 * 
 * Handles all clicks done in the Map Editor
 * 
 *  * Usage instructions:
 * 
 * Construct MapEditorClickedHandler:
 * MapEditorClickedHandler h = new MapEditorClickedHandler(row, col, currentlocation, viewable object, gridpane, map);
 */
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import viewable.Viewable;
import viewable.gameObjects.Map;
import viewable.mapObjects.Path;
import viewable.mapObjects.Placeable;

public class MapEditorImageClickedHandler implements EventHandler<MouseEvent>{
	private int col;
	private int row;
	private int[] currentLocation;
	private Viewable[] currentObject;
	private GridPane grid;
	private Map map;
	
	/**
	 *  Purpose: constructor for the class
	 * @param row an integer containing a row
	 * @param col an integer containing a col
	 * @param c an array of integers containing the current location of the object
	 * @param cO an array of the current viewable object
	 * @param g a GridPane
	 * @param m a Map
	 */
	public MapEditorImageClickedHandler(int row, int col, int[] c, Viewable[] cO, GridPane g, Map m) {
		this.col = col;
		this.row = row;
		currentLocation = c;
		currentObject = cO;
		grid = g;
		map = m;
	}
	
	/**
	 *  Purpose: Handles the mouse event to update the Map Editor's view
	 *  
	 *  @param e the MouseEvent being handles
	 */
	@Override
	public void handle(MouseEvent e) {
		currentLocation[0] = col; // obtain current column
		currentLocation[1] = row; // obtain current row
		if(currentObject[0]!=null) { // ensure no null objects
			try {
				grid.getChildren().removeIf(node -> GridPane.getRowIndex(node) == col && GridPane.getColumnIndex(node)== row);
				ImageView view = getResource(currentObject[0], 1);
				view.setOnMouseClicked(new MapEditorImageClickedHandler(row, col, currentLocation, currentObject, grid, map));
				if(currentObject[0] instanceof Placeable) {	// checks to see if placing a valid tower location or a path
					map.getBoard()[row][col][0] = new Placeable();
				}else if(currentObject[0] instanceof Path) { // if placing a path
					map.getBoard()[row][col][0] = new Path();
				}
				grid.add(view, row, col); // adds the new object to the grid
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	
	/**
	 * Purpose: gets a resource for each object that is being placed
	 * @param obj - the object that needs the resource
	 * @param use - an integer containing a 1 or 0 to indicate whether or not a tile is being placed
	 * @return view - the new ImageView object
	 * @throws FileNotFoundException
	 */
	private ImageView getResource(Viewable obj, int use) throws FileNotFoundException {
		ImageView view;
		if(obj == null) { // ensures object is not null
			view = new ImageView(new Image(new FileInputStream(Viewable.getDefaultResource())));
			view.setUserData(obj);
		}else {
			view = new ImageView(new Image(new FileInputStream(obj.getResource())));
			view.setUserData(obj);
		}
		if(use==1) {
			view.setFitHeight(48);
			view.setFitWidth(48);
		}else {
			view.setFitHeight(128);
			view.setFitWidth(128);
		}
		return view;
	}
}
	
