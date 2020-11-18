package game;

/**
 * MapEditor.java
 * 
 * Creates a map editor window where players can create, save, load, and edit
 * maps.
 * 
 * Usage instructions:
 * 
 * Construct MapEditor
 * MapEditor editor = new MapEditor()
 * 
 * Other useful methods:
 * editor.create()
 * editor.repaintGrid()
 * editor.createTowerMenu()
 * editor.createMenuBar()
 * editor.createFileMenu()
 * editor.createOptionMenu()
 * editor.createGrid()
 * editor.getResource()
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import handlers.MapEditorImageClickedHandler;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import viewable.Viewable;
import viewable.gameObjects.Map;
import viewable.mapObjects.Path;
import viewable.mapObjects.Placeable;

public class MapEditor {
	private Map map;
	private GridPane grid;
	private int[] currentLocation = new int[2];
	private Viewable[] currentObject = new Viewable[1];	
	
	/**
     * purpose: Builds the initial window that the map editor opens up in.
     * 
     * @return stage - returns the stage that was created for the GUI
     * 
     * @throws FileNotFoundException exception
     * 
     * @return Stage object
     * 
     */
	public Stage create() throws FileNotFoundException {
		Stage stage = new Stage();
		BorderPane root = new BorderPane();
		
		grid = createGrid();
		
		root.setCenter(grid);
		root.setTop(createMenuBar(stage));
		root.setBottom(createTowerMenu());
		
		stage.setScene(new Scene(root, 1920, 1080));
		
		return stage;
	}
	
	/**
     * purpose: Redraws the grid after the user has made changes.
     * 
     * @throws - FileNotFoundException - thrown if the image that is to be set
     * is not found.
     */
	private void repaintGrid() throws FileNotFoundException {
		grid.getChildren().clear();
		// Adding image to each square of the grid
		Viewable[][][] board = map.getBoard();
		for(int i =0;i<board.length;i++) {
			for(int j = 0;j<board[i].length;j++) {
				int col = j;
				int row = i;
				ImageView view = getResource(board[i][j][0], 1);
				// Handler for clicking on a square in the grid
				view.setOnMouseClicked(new MapEditorImageClickedHandler(row, col, currentLocation, currentObject, grid, map));
				grid.add(view , i, j);
			}
		}
	}
	
	/**
     * purpose: Creates a menu of towers.
     * 
     * @return pane - The pane that holds all the viewables that can be seen on the grid
     * 
     * @throws - FileNotFoundException - thrown if the image that is to be set
     * is not found.
     */
	private TilePane createTowerMenu() throws FileNotFoundException {
		TilePane pane = new TilePane();
		Viewable[] towers = new Viewable[] {new Path(), new Placeable()};
		// Going throw each tower as a viewable object
		for(Viewable v : towers) {
			ImageView view = getResource(v,2);
			view.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent e) {
					currentObject[0] = v;
				}
			});
			// Adding back to the pane
			pane.getChildren().add(view);
		}
		return pane;
	}
	
	/**
     * purpose: Create the top menu bar.
     * 
     * @return bar - the menu bar that displays at the top right of the GUI
     * 
     * @param stage - is the window the map editor is inside.
     */
	private MenuBar createMenuBar(Stage stage) {
		// Create the menu bar.
		MenuBar bar = new MenuBar();
		
		bar.getMenus().add(createFileMenu(stage));
		int menuHeight = 30;
		bar.setMinHeight(menuHeight);
		bar.setPrefHeight(menuHeight);
		bar.setMaxHeight(menuHeight);
		return bar;
	}
	
	/**
     * purpose: Creates the menus that are in the menu bar.
     * 
     * @return file - the dropdown menu list that appears when menu is clicked
     * 
     * @param stage - is the window the map editor is inside.
     */
	private Menu createFileMenu(Stage stage) {
		// Create the file menu option, will hold save and exit commands.
		Menu file = new Menu();
		
		MenuItem newGame = new MenuItem();
		newGame.setText("Save");
		// Event handler for when the save menu option is selected
		newGame.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				try {
					FileChooser fileChooser = new FileChooser();
					
					// Directing towards the packages inner save location
					File initDir = new File("./saves");
					initDir.mkdir();
					
					fileChooser.setInitialDirectory(initDir);
					  
					//Set extension filter
					FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
					fileChooser.getExtensionFilters().add(extFilter);
  
					//Show save file dialog
					File file = fileChooser.showSaveDialog(stage);
					if(file!=null) {
						map.save(file.getCanonicalPath());
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		// Creating dropdown menu selection for loading maps
		MenuItem load = new MenuItem();
		load.setText("Load");
		// Event handler for when the load menu option is selected
		load.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				try {
					FileChooser fileChooser = new FileChooser();
					
					File initDir = new File("./saves");
					initDir.mkdir();
					
					fileChooser.setInitialDirectory(initDir);
					
					fileChooser.setTitle("Open Resource File");
					File path = fileChooser.showOpenDialog(stage);
					if(path!=null) {
						map.load(path.getCanonicalPath());
						repaintGrid();
					}
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		MenuItem exit = new MenuItem();
		exit.setText("Exit");
		// Event handler for when the exit menu option is selected
		exit.setOnAction((e)->{
			Platform.exit();
		});
		
		file.getItems().addAll(newGame, exit, load);
		file.setText("File");
		return file;
	}
	
	/**
     * purpose: Creates the grid that acts as the play area for the game.
     * 
     * @return grid - the grid with all the images for each object the gridpane holds
     * 
     * @throws - FileNotFoundException - thrown if the image that is to be set
     * is not found.
     */
	private GridPane createGrid() throws FileNotFoundException {
		GridPane grid = new GridPane();
		map = new Map();
		Viewable[][][] board = map.getBoard();
		for(int i =0;i<board.length;i++) {
			for(int j = 0;j<board[i].length;j++) {
				int col = j;
				int row = i;
				ImageView view = getResource(board[i][j][0], 1);
				view.setOnMouseClicked(new MapEditorImageClickedHandler(row, col, currentLocation, currentObject, grid, map));
				grid.add(view , i, j);
			}
		}
		
		return grid;
	}
	
	/**
     * purpose: Gets the image resource for any square the was clicked on and
     * changed.
     * 
     * @param obj - a viewable game object that can be modified for the intent
     * of creating a map.
     * 
     * @param use - determines which dimensions to use. Used to change the size
     * of the squares in the grid.
     * 
     * @return view - the image for a specific object on the grid
     * 
     * @throws - FileNotFoundException - thrown if the image that is to be set
     * is not found.
     */
	private ImageView getResource(Viewable obj, int use) throws FileNotFoundException {
		ImageView view;
		if(obj == null) {
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
