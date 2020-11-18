package game;

/**
 * TowerDefenseView.java
 * 
 * Creates the window the game is being played in.
 * Determines the actions to be taken when the player interacts with
 * any interactable object in the view.
 * 
 * Usage instructions:
 * 
 * Construct TowerDefenseView
 * TowerDefenseView view = new TowerDefenseView(primaryStage)
 * 
 * Other useful methods:
 * view.start(primaryStage)
 * view.mainMenu()
 * view.connectOrHost()
 * view.newGame()
 * view.createClickThrough()
 * view.createClearGrid()
 * view.createGrid()
 * view.createGridResource(obj, row, col)
 * view.update()
 * view.move(index, minion, minions, minionsL, offset)
 * view.checkMinionsFinished(minions)
 * view.checkTowers(minion, x, y)
 * view.findNode(col, row)
 * view.generatePath()
 * view.generatePath(callback)
 * view.createBottom()
 * view.createTop()
 * view.createMarket()
 * view.createMenuBar()
 * view.createFileMenu()
 * view.createOptionMenu()
 * view.loadMusic()
 * view.findRandomMusic()
 * view.update(o, e)
 * view.setBoard(row, col)
 * view.getPrimaryStage()
 * 
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import chat.ChatView;
import handlers.GameObjectClickedHandler;
import handlers.GameObjectContextMenuHandler;
import handlers.ImageResourceLoadingHandler;
import handlers.MapEditorHandler;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;
import viewable.Viewable;
import viewable.gameObjects.WaveGenerator;
import viewable.mapObjects.Path;
import viewable.gameObjects.Map;
import viewable.gameObjects.Market;
import viewable.gameObjects.Minion;
import viewable.gameObjects.Player;
import viewable.gameObjects.Tower;

public class TowerDefenseView extends Application implements Observer{
	
	// Field variables for TowerDefenseView Objects
	private static final int SIZE_IMAGE = 47;
	private static final int MINION_MAX_SPEED = 500;
	private static final int TOWER_MAX_ATTACK_SPEED = 5;
	private Stage stage;
	private TowerDefenseController controller;
	private ViewModel model;
	private GridPane grid;
	private GridPane animationGrid;
	private Pane attackGrid;
	private WaveGenerator wave;
	private WaveGenerator otherWave;
	private List<ImageView> lsPath;
	private List<Integer> direction;
	private java.util.Map<Minion, Timeline> transitions;
	private MediaPlayer player;
	private volatile int currentYVal;
	private Market m;
	private Button endTurn;
	private MenuItem pause;
	private MenuItem fastForward;
	private int portNum;
	
	/**
	 * Factory for creating simple notification modals.
	 * @param message
	 */
	private void createModal(String message) {
		Platform.runLater(()->{
			Stage s = new Stage();
			BorderPane textPane = new BorderPane();
			Text label = new Text();
			label.setText("Alert");
			Text reason = new Text();
			reason.setText(message);
			portNum = 5000;
	
			textPane.setCenter(reason);
			s.setScene(new Scene(textPane, 600,200));
			s.setTitle("Alert");
			s.initModality(Modality.WINDOW_MODAL);
			s.initOwner(stage);
			s.showAndWait();
		});
	}
	
	/**
	 * purpose: Launches the GUI for the tower defense game.
	 * 
	 * @param primaryStage - the main stage created by JavaFX and used to display
	 * all game variables
	 * 
	 * @throws Exception - throws an exception if the view isn't properly passed
	 * in to create a new controller.
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		stage = primaryStage;
		
		transitions = new HashMap<Minion,Timeline>();

		controller = new TowerDefenseController(this);
		// Closing threads when exit is selected
		Runtime.getRuntime().addShutdownHook(new Thread(()->{
			controller.setRunning(false);
		}));

		loadMusic();
		
		mainMenu();
	}
	
	/**
	 * purpose: creates the opening main menu for the game
	 * 
	 * @throws IOException - an exception is thrown if any resources for the
	 * main menu aren't able to be found.
	 */
	private void mainMenu() throws IOException {
		// Setting up main menu window
		VBox vbox = new VBox(25);
		vbox.setPadding(new Insets(20));
		FileInputStream in = new FileInputStream("./resources/images/splashScreen.gif");
		Image logo = new Image(in);
		ImageView logoView = new ImageView(logo);
		in.close();
		HBox buttons = new HBox(15);
		
		Button hostGame = new Button("Host a Game");
		hostGame.setOnAction((e)->{
			FileChooser fileChooser = new FileChooser();
			
			File initDir = new File("./saves");
			initDir.mkdir();
			
			fileChooser.setInitialDirectory(initDir);
			
			fileChooser.setTitle("Open Resource File");
			File path = fileChooser.showOpenDialog(stage);
			if (path != null) {
				try {
					controller.getBoard().getBoard().load(path.getCanonicalPath());

					controller.startServer();
					hostGame.setDisable(true);
					newGame();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					createModal(e1.getMessage());
					fileChooser.showOpenDialog(stage);
				}
				
			}
		});
		Button newGame = new Button("Connect");
		Button mapEditor = new Button("Map Editor");
		Button exit = new Button("Exit");
		buttons.getChildren().addAll(newGame, hostGame, mapEditor, exit);
		

		buttons.setAlignment(Pos.CENTER);
		vbox.getChildren().add(logoView);
		vbox.getChildren().add(buttons);
		
		newGame.setOnAction((e) -> {
			connectOrHost();
		});
		
		mapEditor.setOnAction(new MapEditorHandler());
		exit.setOnAction((e)->{
			controller.setRunning(false);
			Platform.exit();
		});
		Scene scene = new Scene(vbox);
		stage.setTitle("Power Tower");
		stage.setScene(scene);
		stage.show();
	}
	
	/**
     * purpose: Creates the window to be displayed when the user clicks
     * connect. Allows the user to scan for games or manually input hostname
     * and port number of host.
     * 
     */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void connectOrHost() {
		GridPane pane = new GridPane();
		BorderPane main = new BorderPane();
		
		Button timer = new Button("Scan for Games");
		timer.setOnAction((e)->{
			try {
				controller.scanPorts();
			} catch (IOException ex) {
				// TODO Auto-generated catch block
				createModal("Failed to run the correct command. Please restart the game.");
			}
		});
		
		ListView view = new ListView();
		view.setItems(controller.getPossibleConnections());
		view.setCellFactory(new Callback() {
			@Override
			public Object call(Object arg0) {
				// TODO Create the server view.
	            return new PossibleConnectionCell();
			}
		});
		view.setOnMouseClicked((e)->{
			// Do nothing for right clicks
			if(!e.getButton().equals(MouseButton.PRIMARY)) {
				e.consume();
				return;
			}else if(e.getClickCount()<2) {
				e.consume();
				return;
			}
			InetSocketAddress address = (InetSocketAddress)view.getSelectionModel().getSelectedItem();
			if(address==null) {
				e.consume();
				return;
			}
			controller.startClient(address.getHostString(), address.getPort());
		});

		// Display objects
		TextField host = new TextField();
		Label hostText = new Label("Host");
		
		TextField port = new TextField();
		Label portText = new Label("Port");
		
		Button client = new Button("Connect to Host");
		client.setOnAction((e)->{
			if(host.getText().length()==0||port.getText().length()==0) {
				Stage error = new Stage();
				error.showAndWait();
				e.consume();
			}
			try {
				Integer.parseInt(port.getText());
			}catch(Exception ex) {
				createModal("Port is not an integer.");
				e.consume();
			}
			controller.startClient(host.getText(), Integer.parseInt(port.getText()));
			client.setDisable(true);
		});

		pane.add(host, 0, 1);
		pane.add(hostText, 0, 0);
		pane.add(port, 1, 1);
		pane.add(portText, 1, 0);
		pane.add(client, 0, 2);
		pane.add(timer, 3, 2);
		
		main.setCenter(view);
		main.setBottom(pane);
		stage.setScene(new Scene(main, 1920, 1080));
		stage.setFullScreen(true);
		stage.show();
	}
	
	/**
     * purpose: Resets all values to begin a new game.
     * 
     * @throws IOException - throws an error if newGame was called before the 
     * very first game was completely instantiated.
     */
	public void newGame() throws IOException {
		// Initial Set Up
		currentYVal = 0;
		wave = new WaveGenerator(controller.getPlayer());
		otherWave = new WaveGenerator(controller.getOtherPlayer());
		controller.reset();
		model = new ViewModel(1080,1920);
		lsPath = new ArrayList<ImageView>();
		direction = new ArrayList<Integer>();
		// Set Up Other Player Area
		HBox top = createTop();
		
		// Set Up Primary Player
		HBox bottom = createBottom();
		// Set Up Market
		VBox market = createMarket();
		grid = createGrid();
		animationGrid = createClearGrid();
		attackGrid = createClickThrough();

		// Set Up Menu Bar
		MenuBar menu = createMenuBar();
		
		StackPane stack = new StackPane();
		stack.getChildren().add(grid);
		stack.getChildren().add(animationGrid);
		stack.getChildren().add(attackGrid);
		
		BorderPane root = new BorderPane();
		BorderPane pane = new BorderPane();
		root.setCenter(pane);
		root.setLeft(market);
		root.setTop(menu);
		pane.setCenter(stack);
		pane.setTop(top);
		pane.setBottom(bottom);

		stage.setScene(new Scene(root, model.getWidth(), model.getHeight()));
		stage.getScene().getStylesheets().add(getClass().getResource("mainView.css").toExternalForm());
		stage.setResizable(false);
		stage.setFullScreen(true);
		stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);

		stage.sizeToScene();
		stage.show();
	}
	
	/**
     * purpose: Creates a pane for ammunition to be displayed and still
     * allowing the user to click on the board through this pane.
     * 
     * @return box - is a Pane that holds tower fire animations
     * 
     */
	private Pane createClickThrough() {
		Pane box = new Pane();
		box.setPrefHeight(model.getEffectiveBoardHeight());
		box.setPrefWidth(model.getEffectiveWidth());
		box.setMouseTransparent(true);
		box.setPickOnBounds(false);
		return box;
	}
	
	/**
     * purpose: Creates the bottom layer of the board that displays either the
     * empty grass squares where the user can place towers or the dirt path
     * squares where the enemy waves walk on.
     * 
     * @return grid - the grid that holds all the objects to be places on the playable area
     * 
     * @throws FileNotFoundException - throws an exception if the image resources
     * cannot be found.
     */
	private GridPane createClearGrid() throws FileNotFoundException {
		GridPane grid = new GridPane();
		Viewable[][][] board = controller.getMapArray();
		// Initializing board to grass squares
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				ImageView view = new ImageView();
				view.setImage(new Image(new FileInputStream("./resources/images/Grass.png")));
				view.setFitHeight(SIZE_IMAGE+2);
				view.setFitWidth(SIZE_IMAGE+2);
				view.setOpacity(0);
				grid.add(view, i, j);
			}
		}
		grid.setMouseTransparent(true);
		grid.setPickOnBounds(false);
		return grid;
	}
	
	/**
     * purpose: Applies a grid of squares to the playing field so the players can
     * see where towers can be placed.
     * 
     * @return grid - the GridPane that separates the grid into squares to the players
     * can know where they can place towers
     * 
     * @throws FileNotFoundException - throws an error if any grid resources cannot
     * be found.
     */
	public GridPane createGrid() throws FileNotFoundException {
		GridPane grid = new GridPane();
		Viewable[][][] board = controller.getMapArray();
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				
				HBox view = createGridResource(board[i][j][0], i, j);
				
				grid.add(view, i, j);
			}
		}
		return grid;	
	}
	
	/**
     * purpose: Iterates through the entire board and applies an image for 
     * each square that is associated to the object at that location.
     * 
     * @param obj - the Viewable object that is currently at that grid location
     * 
     * @param row - the row number of the object
     * 
     * @param col - the column number of the object
     * 
     * @return x - the image of the object currently being looked at
     * 
     * @throws FileNotFoundException - throws an error if the objects image resource
     * cannot be located.
     * 
     */
	private HBox createGridResource(Viewable obj, int row, int col) throws FileNotFoundException {
		HBox box = new HBox();
		ImageView x = ImageResourceLoadingHandler.getResource(obj);
		x.setFitHeight(SIZE_IMAGE);
		x.setFitWidth(SIZE_IMAGE);
		x.setOnMouseClicked(new GameObjectClickedHandler(obj, row, col, controller));
		x.setOnContextMenuRequested(new GameObjectContextMenuHandler(obj, row, col, controller));
		box.getChildren().add(x);
		box.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1,1,1,1), Insets.EMPTY)));
		return box;
	}

	/**
     * purpose: Handles the game animation for minions and how they traverse the path.
     * 
     */
	public void update() {
		transitions.clear();
		Thread thread = new Thread(()-> {
			// Generating new wave
			List<Minion> currentWave = wave.generateRandom();
			List<ImageView> minions = new ArrayList<ImageView>();
			// Placing and animating the minions within the wave
			for(Minion m: currentWave) {
				try {
					// Getting image for the object
					ImageView view = ImageResourceLoadingHandler.getResource(m);
					view.setViewport(new Rectangle2D(0, 0, SIZE_IMAGE, SIZE_IMAGE));
					Timeline t = new Timeline(new KeyFrame(Duration.millis(100), (e) -> {
						Rectangle2D viewport = view.getViewport();
						view.setViewport(
								new Rectangle2D(
										viewport.getMinX() + 47 > SIZE_IMAGE * 2 ? 0 : viewport.getMinX() + 47,
										viewport.getMinY(),
										SIZE_IMAGE + 2, SIZE_IMAGE + 2
										)
								);
					}));
					t.setCycleCount(Timeline.INDEFINITE);
					// Playing animation
					t.play();
					minions.add(view);
				} catch (FileNotFoundException e) {
					createModal("You're missing resource files, please check the github page again...");
				}
			}
			// Adding minions to the grid
			for(Node n: grid.getChildrenUnmodifiable()) {
				if(GridPane.getColumnIndex(n)==0&&GridPane.getRowIndex(n)==currentYVal) {
					int finY = currentYVal;
					Platform.runLater(()->{
						for(int i =0;i<minions.size();i++) {
							animationGrid.add(minions.get(i), 0, finY);
						}
					});
				}
			}
			// Putting the wave and mininos together and randmomly generating an offset so minions come at 
			// different times.
			for(int i =0;i<currentWave.size();i++) {
				try {
					move(i, 0, currentYVal, currentWave.get(i), minions, currentWave, direction, (int)(Math.random()*85));
				}catch(Exception ex) {
					createModal(ex.getMessage());
				}
			}
		});
		// Getting path for first player
		generatePath(thread);
		// Sending wave to other player
		Thread thread2 = new Thread(()-> {
			List<Minion> currentWave = otherWave.generateRandom();
			List<ImageView> minions = new ArrayList<ImageView>();
			for(Minion m: currentWave) {
				try {
					
					ImageView view = ImageResourceLoadingHandler.getResource(m);
					view.setScaleX(-1);
					view.setViewport(new Rectangle2D(0, 0, SIZE_IMAGE, SIZE_IMAGE));
					Timeline t = new Timeline(new KeyFrame(Duration.millis(100), (e) -> {
						Rectangle2D viewport = view.getViewport();
						view.setViewport(
								new Rectangle2D(
										viewport.getMinX() + 47 > SIZE_IMAGE * 2 ? 0 : viewport.getMinX() + 47,
										viewport.getMinY(),
										SIZE_IMAGE + 2, SIZE_IMAGE + 2
										)
								);
					}));
					t.setCycleCount(Timeline.INDEFINITE);
					t.play();
					
					minions.add(view);
				} catch (FileNotFoundException e) {
					createModal("You're missing resources. Check the github page again...");
				}
			}
			int y =currentYVal;
			for(int i =0;i<direction.size();i++) {
				if(direction.get(i)==3||direction.get(i)==1) {
					y+=direction.get(i)==1?-1:1;
				}
			}
			for(Node n: grid.getChildrenUnmodifiable()) {
				if(GridPane.getColumnIndex(n)==controller.getMapArray().length-1&&GridPane.getRowIndex(n)==y) {
					int finY = y;
					Platform.runLater(()->{
						for(int i =0;i<minions.size();i++) {
							animationGrid.add(minions.get(i), controller.getMapArray().length-1, finY);
						}
					});
				}
			}
			for(int i =0;i<currentWave.size();i++) {
				try {
					List<Integer> reverse = reverse(direction);
					move(i, controller.getMapArray().length-1, y,currentWave.get(i), minions, currentWave, reverse, (int)(Math.random()*85));
				}catch(Exception ex) {
					createModal(ex.getMessage()+"\nTry restarting the game...");
				}
			}
		});
		// Generating path for other player and sending it to them
		generatePath(thread2);
	}
	
	/**
     * purpose: Reverses the path the minions take which is used for the other
     * player.
     * 
     * @param vals - a list of values that represent the direciton the minions
     * are supposed to take.
     * 
     * @return ints - a list of numbers that dictate the path minions will take.
     * 
     */
	private List<Integer> reverse(List<Integer> vals){
		List<Integer> ints = new ArrayList<Integer>();
		for(int i =0;i<vals.size();i++) {
			int dir = vals.get(i);
			int val = 0;
			if(dir==4||dir==2) {
				val = dir==4?2:4;
			}else {
				val = dir==1?3:1;
			}
			ints.add(0, val);
		}
		return ints;
	}
	
	/**
     * purpose: Moves the minions to their new position on the grid.
     * 
     * @param index - the minion's position within the wave list.
     * 
     * @param initialX - the current minion's starting X position on the map
     * 
     * @param initialY - the current minion's starting Y position on the map
     * 
     * @param minion - the Minion that is being moved
     * 
     * @param minions - the list of minions generated for the current round
     * 
     * @param minionsL - the list of minions for the other player
     * 
     * @param direction - determines which direction the minions will go for each player
     * 
     * @param offest - the delay each minion receives so the minions appear at different times
     * 
     */
	private void move(int index, int initialX, int initialY, Minion minion, List<ImageView> minions, List<Minion> minionsL, List<Integer> direction, int offset) {
		if(minion.isDead()) {
			animationGrid.getChildren().remove(minions.get(index));
			checkMinionsFinished(minionsL);
			return;
		}
		
		int x = initialX;
		int y = initialY;
		for(int k =0;k<minion.getStep();k++) {
			int dir = direction.get(k);
			if(dir==4||dir==2) {
				x+=dir==4?1:-1;
			}else {
				y+=dir==1?-1:1;
			}
		}
		int xFin = x;
		int yFin = y;
		Timeline t = new Timeline(new KeyFrame(Duration.millis((MINION_MAX_SPEED/minion.getSpeed()-offset)*(controller.getFastForward()?.5:1)), (e)-> {
			
			try {
				checkTowers(minion, xFin, yFin);
			} catch (FileNotFoundException e2) {
				createModal("You're missing resources...");
			}
			// Check if minion is still alive
			if(minion.isDead()) {
				Platform.runLater(()->{
					animationGrid.getChildren().remove(minions.get(index));
				});
				controller.killMinion(minion);
				checkMinionsFinished(minionsL);
				return;
			}
			if(minion.getStep()>=direction.size()) {
				controller.damageOther(minion);
				minion.takeDamage(minion.getHealth());
				checkMinionsFinished(minionsL);
				Platform.runLater(()->{
					animationGrid.getChildren().remove(minions.get(index));
				});
			// increase its movement
			}else {
				minion.incrementStep();
				Platform.runLater(()->{
					animationGrid.getChildren().remove(minions.get(index));
					animationGrid.add(minions.get(index), xFin, yFin);
				});

				move(index, initialX, initialY, minion, minions, minionsL, direction, (int)(Math.random()*85));
			}
		}));
		transitions.put(minion, t);
		t.play();
	}
	
	/**
     * purpose: Determine if the minion has finished moving for the cycle
     * 
     * @param minions - a list of minions for the current wave
     * 
     */
	private void checkMinionsFinished(List<Minion> minions) {
		boolean flag = true;
		for(int i =0;i<minions.size();i++) {
			if(!minions.get(i).isDead()) {
				flag = false;
			}
		}
		if(flag) {
			controller.setMinionsFinished(true);
		}
	}
	
	/**
     * purpose: Checks to see if the minion is within range of a tower and
     * has the tower fire if it does. Deals damage to the minion when it hits it.
     * 
     * @param minion - the minion being checked
     * 
     * @param x - the minion's x position on the grid
     * 
     * @param y - the minion's y postion on the grid
     * 
     * @throws FileNotFoundException - throws an exception if the ammunition image can't be found
     * 
     */
	private void checkTowers(Minion minion, int x, int y) throws FileNotFoundException {
		Viewable[][][] map = controller.getMapArray();
		for(int i =0;i<map.length;i++) {
			for(int j =0;j<map[i].length;j++) {
				if(map[i][j][0]!=null&&map[i][j][0] instanceof Tower) {
					Tower t = (Tower)map[i][j][0];
					int range = t.getRange();
					if(range+i>=x&&-range+i<=x) {
						if(range+j>=y&&j-range<=y) {
							if(!t.canAttack()) {
								continue;
							}
							if(minion.getPlayer().equals(controller.getPlayer())) {
								if(i<=map.length/2) {
									continue;
								}
							}else {
								if(i>map.length/2) {
									continue;
								}
							}
							ImageView view = new ImageView();
							view.setImage(new Image(new FileInputStream("./resources/images/MortarShell.png")));
							view.setFitHeight(10);
							view.setFitWidth(10);
							Platform.runLater(()->{
								attackGrid.getChildren().add(view);
							});
							view.setX(i*(SIZE_IMAGE+2)+(SIZE_IMAGE+2)/2);
							view.setY(j*(SIZE_IMAGE+2)+(SIZE_IMAGE+2)/2);
							TranslateTransition tt = new TranslateTransition(
									Duration.millis((TOWER_MAX_ATTACK_SPEED/t.getAttackSpeed())*(controller.getFastForward()?5:10)), 
									view);
							tt.setByX((x-i)*SIZE_IMAGE);
							tt.setByY((y-j)*SIZE_IMAGE);
							t.startCooldown();
							tt.setOnFinished((e)->{
								minion.takeDamage(t.getAttack());
								attackGrid.getChildren().remove(view);
								t.endCooldown();
							});
							tt.play();
						}
					}
				}
			}
		}
	}
	
	/**
     * purpose: Determines which node is at a specific position.
     * 
     * @param col - the col position on the grid
     * 
     * @param row - the row position on the grid
     * 
     * @return - the node at the given position
     * 
     */
	private Node findNode(int col, int row) {
		for(Node n: grid.getChildren()) {
			if(GridPane.getColumnIndex(n)==col&&GridPane.getRowIndex(n)==row) {
				return ((HBox)n).getChildren().get(0);
			}
		}
		return null;
	}
					
	/**
     * purpose: Determines the path for the minions to take as they traverse the map
     * 
     * @param callback - the thread of the current player.
     * 
     */
	public void generatePath(Thread callback) {
		Thread thread = new Thread(()-> {
			if(lsPath.size()>0&&direction.size()>0) {
				callback.start();
				return;
			}
			Viewable[][][] map = controller.getMapArray();
			int x = 0;
			int y = 0;
			for (int i = 0; i < map[0].length; i++) {
				if (map[0][i][0] instanceof Path) {
					lsPath.add((ImageView)findNode(0,i));
					y = i;
					currentYVal = i;
				}
			}
			while (true) {
				int topy = y - 1;
				int boty = y + 1;
				int leftx = x - 1;
				int rightx = x + 1;
				// Determining if there is a Path object to the left of the current path
				if (leftx >= 0) {
					if (map[leftx][y][0] instanceof Path&&!lsPath.contains(findNode(leftx, y))) {
						lsPath.add((ImageView)findNode(leftx, y));
						direction.add(2);
						x = leftx;
						continue;
					}
				}
				// Determinig if there is a Path object to the north of the current path
				if (topy >= 0) {
					if (map[x][topy][0] instanceof Path&&!lsPath.contains(findNode(x, topy))) {
						lsPath.add((ImageView)findNode(x, topy));
						direction.add(1);
						y = topy;
						continue;
					}
				}
				// Determinig if there is a Path object to the right of the current path
				if (rightx < map.length) {
					if (map[rightx][y][0] instanceof Path&&!lsPath.contains(findNode(rightx, y))) {
						lsPath.add((ImageView)findNode(rightx, y));
						direction.add(4);
						x = rightx;
						continue;
					}
				}
				// Determining if there is a Path object to the south of the current path
				if (boty < map[0].length) {
					if (map[x][boty][0] instanceof Path && !lsPath.contains(findNode(x, boty))) {
						lsPath.add((ImageView)findNode(x, boty));
						direction.add(3);
						y = boty;
						continue;
					}
				}
				// Stop looking for paths once you reach the last column on the right side of the board
				if (x == map.length-1) {
					break;
				}
				
			}
			callback.start();

		});
		thread.start();
	}
	
	/**
     * purpose: Creates the area for the bottom players cards and stats.
     * 
     * @return bottom - an HBox holding all the objects necessary for a player's area
     * 
     * @throws IOException - throws an exception if the images for the player's
     * cards cannot be found
     * 
     */
	private HBox createBottom() throws IOException {
		Player player = controller.getPlayer();
		HBox bottom = new HBox();
		bottom.setStyle("-fx-border-color: black;");
		FileInputStream input = new FileInputStream("./resources/images/playmat1.png");
		Image image = new Image(input);
		BackgroundImage player1 = new BackgroundImage(image, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
		Background background = new Background(player1);
		bottom.setBackground(background);
		
		int prefHeight = 215;
		bottom.setPrefHeight(prefHeight);
		model.addSubHeight(prefHeight);
		
		VBox stat2 = new VBox();
		Label hp2 = new Label("Health: ");
		Label mp2 = new Label("Gold: ");
		hp2.setTextFill(Color.WHITE);
		mp2.setTextFill(Color.WHITE);
		Text health = new Text();
		health.setFill(Color.WHITE);
		health.setText(player.getHealth()+"");
		Text gold = new Text();
		gold.setFill(Color.WHITE);
		gold.setText(player.getGold()+"");
		// Adding listener to health to determine damage take or healing
		player.getViewableHealth().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
				// TODO Auto-generated method stub
				health.setText(arg2.toString());
				if(arg2.intValue()<=0) {
					Pane pane = new Pane();
					HBox box = new HBox();
					Label label = new Label("You Died");
					box.getChildren().add(label);
					pane.getChildren().add(box);
					Button button = new Button("Go Back");
					button.setOnAction((e)->{
						try {
							mainMenu();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							createModal("Something went terribly wrong.");
						}
					});
					box.getChildren().add(button);
					stage.setScene(new Scene(pane, 300, 200));
					stage.show();
				}
			}
			
		});
		// Adding listener to gold to determine any changes in gold amount
		player.getViewableGold().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
				// TODO Auto-generated method stubs
				gold.setText(arg2.toString());
			}
			
		});
		stat2.getChildren().addAll(hp2, health, mp2, gold);
		
		ListView<ImageView> pane = new ListView<ImageView>();
		pane.setItems(player.getHand());
		pane.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		pane.setOrientation(Orientation.HORIZONTAL);
		pane.setOnMouseClicked((e)->{
			if(pane.getSelectionModel().getSelectedItem()==null) {
				return;
			}
			pane.getSelectionModel().getSelectedItem().getOnMouseClicked().handle(e);
		});
		pane.setPrefWidth(1000);
		pane.setBackground(Background.EMPTY);
		endTurn = new Button("End Turn");
		endTurn.setOnAction((e)->{
			if(!controller.hasConnected()||controller.isPaused()) {
				return;
			}
			controller.endTurn();
			endTurn.setDisable(true);
		});
		stat2.getChildren().add(endTurn);
		bottom.getChildren().add(stat2);
		bottom.getChildren().add(pane);
		input.close();
		return bottom;
	}
	
	/**
     * purpose: Creates the area for the top player's cards and stats.
     * 
     * @return top - an HBox holding all the objects necessary for a player's area
     * 
     * @throws IOException - throws an exception if the images for the player's
     * cards cannot be found
     * 
     */
	private HBox createTop() throws IOException {
		// Set Up Other Player Area
		Player player = controller.getOtherPlayer();
		HBox top = new HBox();
		top.setStyle("-fx-border-color: black;");
		
		FileInputStream input = new FileInputStream("./resources/images/playmat2.png");
		Image image = new Image(input);
		BackgroundImage player2 = new BackgroundImage(image, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
		Background background = new Background(player2);
		top.setBackground(background);
		int prefHeight = 215;
		top.setPrefHeight(prefHeight);
		
		VBox stat1 = new VBox();
		Label hp1 = new Label("Health: ");
		Label mp1 = new Label("Gold: ");
		hp1.setTextFill(Color.WHITE);
		mp1.setTextFill(Color.WHITE);
		Text health = new Text();
		health.setFill(Color.WHITE);
		health.setText(player.getHealth()+"");
		Text gold = new Text();
		gold.setFill(Color.WHITE);
		gold.setText(player.getGold()+"");
		// Adding listener to see if the player's health has changed
		player.getViewableHealth().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
				// TODO Auto-generated method stub
				health.setText(arg2.toString());
				if(arg2.intValue()<=0) {
					Pane pane = new Pane();
					HBox box = new HBox();
					Label label = new Label("You Won");
					box.getChildren().add(label);
					pane.getChildren().add(box);
					Button button = new Button("Go Back");
					button.setOnAction((e)->{
						try {
							mainMenu();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							createModal("Something went terribly wrong.");
						}
					});
					box.getChildren().add(button);
					controller.setRunning(false);
					stage.setScene(new Scene(pane, 300, 200));
					stage.show();
				}
			}
			
		});
		// Adding a listener to player's gold to see if it ever changes
		player.getViewableGold().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
				// TODO Auto-generated method stubs
				gold.setText(arg2.toString());
			}
			
		});
		stat1.getChildren().addAll(hp1, health, mp1, gold);		
		
		top.getChildren().add(stat1);
		
		model.addSubHeight(prefHeight);
		input.close();
		return top;
	}
	
	/**
     * purpose: Creates the area for the market cards that can be bought.
     * 
     * @return market - an HBox holding all the cards that are available for purchase
     * 
     * @throws IOException - throws an exception if the images for the market's
     * cards cannot be found
     * 
     */
	private VBox createMarket() throws IOException {
		// Set Up Market
		VBox market = new VBox();
		market.setStyle("-fx-border-color: black;");
		// Getting images for each card
		FileInputStream input = new FileInputStream("./resources/images/market.png");
		Image image = new Image(input);
		BackgroundImage marketBg = new BackgroundImage(image, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
		Background background = new Background(marketBg);
		market.setBackground(background);
		
		ListView<ImageView> view = new ListView<ImageView>();
		m = controller.getMarket();
		view.setItems(m.getForSale());
		view.setPrefHeight(model.getHeight());
		market.getChildren().add(view);
		// What to do when a card in the market is clicked
		view.setOnMouseClicked((e)->{
			if(view.getSelectionModel().getSelectedItem()==null) {
				e.consume();
				return;
			}
			view.getSelectionModel().getSelectedItem().getOnMouseClicked().handle(e);
		});
		
		int prefWidth = 250;
		model.addSubWidth(prefWidth);
		market.setPrefWidth(prefWidth);
		input.close();
		return market;
	}
	
	/**
     * purpose: Creates the menu bar that is displayed at the top right of the GUI.
     * 
     * @return bar - the MenuBar to be displayed
     */
	private MenuBar createMenuBar() {
		// Create the menu bar.
		MenuBar bar = new MenuBar();
		
		bar.getMenus().add(createFileMenu());
		bar.getMenus().add(createOptionMenu());
		int menuHeight = 30;
		bar.setMinHeight(menuHeight);
		bar.setPrefHeight(menuHeight);
		bar.setMaxHeight(menuHeight);
		model.addSubHeight(menuHeight);
		return bar;
	}
	
	/**
     * purpose: Creates the dropdown menu when file is selected from the main menu bar.
     * 
     * @return file - the dropdown menu to be displayed
     */
	private Menu createFileMenu() {
		// Create the file menu option, will hold save and exit commands.
		Menu file = new Menu();
		
		MenuItem newGame = new MenuItem();
		newGame.setText("New Game");
		newGame.setOnAction((e) -> {
			try {
				newGame();
			} catch (IOException e1) {
				createModal("Something went terribly wrong.");
			}
		});
		// Map editor option
		MenuItem mapEditor = new MenuItem();
		mapEditor.setText("Open Map Editor");
		mapEditor.setOnAction(new MapEditorHandler());
		// Pause menu option
		pause = new MenuItem();
		pause.setText("Pause");
		pause.setOnAction((e)->{
			controller.setPaused(!controller.isPaused());
			for(Minion m: transitions.keySet()) {
				Timeline t = transitions.get(m);
				if(controller.isPaused()) {
					t.pause();
				}
				else{
					t.play();
				}
			}
			if(controller.isPaused()){
				pause.setText("Unpause");
			}else {
				pause.setText("Pause");
			}
		});
		// fast forward menu option
		fastForward = new MenuItem();
		fastForward.setText("Fast Forward");
		fastForward.setOnAction((e)->{
			controller.setFastForward(!controller.getFastForward());
			if(controller.getFastForward()) {
				fastForward.setText("Regular Speed");
			}else {
				fastForward.setText("Fast Forward");
			}
		});
		
		MenuItem chat = new MenuItem("Chat");
		chat.setOnAction((e)->{
			ChatView view = new ChatView();
			try {
				view.create(portNum).show();
				portNum+=10;
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				Stage stage = new Stage();
				Label label = new Label("Ports all blocked.");
				Pane pane = new Pane();
				VBox box = new VBox();
				box.getChildren().add(label);
				pane.getChildren().add(box);
				stage.setScene(new Scene(pane, 400, 400));
				stage.showAndWait();
			}
		});
		
		// exit menu option
		MenuItem exit = new MenuItem();
		exit.setText("Exit");
		exit.setOnAction((e)->{
			controller.setRunning(false);
			Platform.exit();
		});
		
		file.getItems().addAll(newGame, mapEditor, chat, pause,fastForward, exit);
		file.setText("File");
		return file;
	}
	
	/**
     * purpose: Creates the dropdown menu when option is selected in the 
     * main menu bar.
     * 
     * @return options - the dropdown menu for options
     */
	private Menu createOptionMenu() {
		// Create the option menu option, holds sound settings and game visual settings.
		Menu options = new Menu();
		// sound menu option
		MenuItem sound = new MenuItem();
		sound.setText("Sound");
		sound.setOnAction((e)->{
			Stage stage = new Stage();
			Pane pane = new Pane();
			HBox box = new HBox();
			VBox v = new VBox();
			Label label = new Label("Volume Percent");
			Slider slider = new Slider();
			slider.setMin(0);
			slider.setMax(100);
			slider.setValue(100);
			Button button = new Button("Save");
			button.setOnAction((f)->{
				player.setVolume(slider.getValue());
			});
			v.getChildren().addAll(label, slider, button);
			box.getChildren().add(v);
			pane.getChildren().add(box);
			stage.setScene(new Scene(pane, 300, 200));
			stage.show();
		});

		options.getItems().add(sound);
		options.setText("Options");
		return options;
	}

	/**
     * purpose: Randomly selects music from a source folder to play during the game.
     * 
     */
	private void loadMusic() {
		try {
			String randomMusic = findRandomMusic();
			Media music = new Media(randomMusic);
			player = new MediaPlayer(music);
			player.setOnEndOfMedia(new Runnable() {
				@Override
				public void run() {
					String randomMusic = "";
					while(randomMusic.equalsIgnoreCase("")) {
						try {
							randomMusic = findRandomMusic();
							break;
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (URISyntaxException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					Media music = new Media(randomMusic);
					MediaPlayer player = new MediaPlayer(music);
					player.play();
					
				}
			});
			player.play();
		}catch(Exception ex) {
			// Show modal.
			createModal("Please try restarting the game.");
		}
	}
	
	/**
     * purpose: Randomly picks a music file from the music folder
     * 
     * @return - a string that is the file path to the selected music file
     * 
     */
	private String findRandomMusic() throws IOException, URISyntaxException {
		File file = new File("./resources/music");
		List<File> files = new ArrayList<File>();
		for(File f: file.listFiles()) {
			int i = f.getAbsolutePath().lastIndexOf(".");
			System.out.println(f.getAbsolutePath().substring(i+1));
			if(!f.getAbsolutePath().substring(i+1).equals("mp3")) {
				continue;
			}
			files.add(f);
		}
		Collections.shuffle(files);
		return files.get(0).toURI().toString();
	}

	/**
     * purpose: The observer method that takes notice of any changes that occur to the 
     * board state.
     * 
     * @param o - the object that is being observed
     * 
     * @param e - the object that was changed by the observed object
     */
	@Override
	public void update(Observable o, Object e) {
		if(e instanceof Map) {
				Platform.runLater(()->{
					try {
						newGame();
					}catch(Exception ex) {
						createModal("Something went terribly wrong.");
					}
				});
		}else if(e instanceof String) {
			// TODO Auto-generated method stub
			int i = Integer.parseInt(((String)e).split(" ")[0]);
			int j = Integer.parseInt(((String)e).split(" ")[1]);
			try {
				setBoard(i, j);
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				createModal("You're missing resources.");
			}
		}else if(e instanceof Boolean) {
			if((Boolean)e) {
				update();
			}else {
				endTurn.setDisable(false);
				attackGrid.getChildren().clear();
			}
		}else if(e instanceof Integer) {
			if(((Integer) e).intValue()==1) {
				for(Minion m: transitions.keySet()) {
					Timeline t = transitions.get(m);
					if(controller.isPaused()) {
						t.pause();
					}
					else{
						t.play();
					}
				}
				if(controller.isPaused()){
					pause.setText("Unpause");
				}else {
					pause.setText("Pause");
				}
			}else if(((Integer) e).intValue()==2) {
				if(controller.getFastForward()) {
					fastForward.setText("Regular Speed");
				}else {
					fastForward.setText("Fast Forward");
				}
			}
		}
	}
	
	/**
     * purpose: sets all objects within the board.
     * 
     * @param row - the row in the grid where the object is to be set
     * 
     * @param col - the column in the grid where the object is to be set
     * 
     * @throws IOException - throws an exception if the images for the object
     * cannot be found
     * 
     */
	private void setBoard(int row, int col) throws FileNotFoundException {
		Viewable[][][] board = controller.getMapArray();
		Viewable obj = board[col][row][0];
		Node node = null;
		Node toRemove = null;
		for(Node n: grid.getChildren()) {
			if(GridPane.getColumnIndex(n)==col&&GridPane.getRowIndex(n)==row) {
				node = createGridResource(obj, col, row);
				toRemove = n;
			}
		}
		if(node == null || toRemove == null) {
			return;
		}

		grid.getChildren().remove(toRemove);
		grid.add(node, col, row);
	}
	
	// Getter method for the primary stage
	public Stage getPrimaryStage() {
		return stage;
	}

	// Getter method for the controller being used by the view
	public TowerDefenseController getController() {
		return controller;
	}
	
	/**
     * purpose: Tests whether a connection can be made with the given parameters.
     * 
     */
	private class PossibleConnectionCell extends ListCell{
		private InetSocketAddress address;
		public PossibleConnectionCell() {
		}
		
		/**
	     * purpose: Updates the client with all the board attributes used by the other player.
	     * 
	     * @param update - the object to be updated across the network
	     * 
	     * @param empty - tells if the current space is empty
	     */
		@Override
		protected void updateItem(Object update, boolean empty) {
			super.updateItem(update, empty);
			address = (InetSocketAddress)update;
			if(update!=null) {
				HBox box = new HBox();
				Label addressLabel = new Label(address.getHostString());
				box.getChildren().add(addressLabel);
				setGraphic(box);
			}
		}
		
		// Getter method for the socket address being used
		public InetSocketAddress getAddress() {
			return address;
		}
	}
}


