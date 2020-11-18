/**
 * CryptogramGUIView.java
 * 
 * Displays the graphical representation of the cryptogram game, updates with
 * user guesses and controls what to display when textboxes are changed,
 * buttons are clicked, checkboxes are checked or unchecked, or the game is won
 * 
 * Usage instructions:
 * 
 * Construct CryptogramGUIVIew
 * launch(args) is called on this class to begin the GUI through the start
 * method.
 * 
 * Other useful methods:
 * setup(borderPane, formatted) - creates the layout of the stage;
 * 
 * HandleKeyPressed(label) - is an inner class that controls what happens
 * when a key is pressed while focused on a text field.
 * 
 * processWin() - called when the user input matches the original quote and 
 * a modal is displayed telling the user they won.
 * 
 * update(arg0, arg1) - is an inherited method from the Observer interface.
 * This method tells this class that a change has occured in the model and how
 * to update the GUI so the user is able to see the change.
 * 
 * ******Additional audio feature added. Need to remove comment slashes (//) 
 * from lines 123-125, 199-201, 278-280, 355-357
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;

public class CryptogramGUIView extends Application implements Observer{
	
	// Variable fields used for CryptogramGUIView object construction.
	private Stage primaryStage;
	private CryptogramController controller;
	private GridPane grid;
	private VBox rVBox;
	private Label freqLabell;
	private Label freqLabelr;
	private List<String> formatted;
	Boolean hasWon;
	
	/*
     * Purpose: Updates the GUI when a change is made by the model.
     * 
     * @param arg0 - the Observable class that notified its observers of any
     * changes to the model.
     * 
     * @param arg1 - the object that the observable is saying was changed. 
     * 
     * @return None.
     */
	@SuppressWarnings("unchecked")
	@Override
	public void update(Observable arg0, Object arg1) {
		// Setting the frequency and hint commands
		if (arg1 instanceof String) {
			// Updating user map when hint button is pressed
			if (((String) arg1).length() == 6) {
				controller.updateUserMap(((String) arg1).substring(0, 1).charAt(0),
						((String) arg1).substring(4, 5).charAt(0));
			} else {
				int half = ((String) arg1).length() / 2;
				String firstHalf = ((String) arg1).substring(0, half);
				String secondHalf = ((String) arg1).substring(half);
				freqLabell = new Label(firstHalf);
				freqLabell.setFont(Font.font("Monospaced", 17));
				freqLabelr = new Label(secondHalf);
				freqLabelr.setFont(Font.font("Monospaced", 17));
			}
			// Updating formatted when the model is created
		} else if (arg1 instanceof List){
			if (((List<?>) arg1).get(0) instanceof String) {
				formatted = (List<String>) arg1;
			}
			// Updating changes to win state
		} else if (arg1 instanceof Boolean) {
			hasWon = (Boolean) arg1;
			// Updating text boxes when user inputs a letter or hint is pressed
		} else {
			TextField text = null;
			Label label = null;
			Pair<Character, Character> pair = (Pair<Character, Character>) arg1;
			// Getting every textField in the GUI and updating those that match
			// the label of the textField the user changed.
			for (Node node : grid.getChildren()) {
				if (node instanceof VBox) {
					if (!((VBox) node).getChildren().isEmpty()) {
						text = (TextField) ((VBox) node).getChildren().get(0);
						label = (Label) ((VBox) node).getChildren().get(1);
						if (label.getText().charAt(0) == (pair.getKey())) {
							text.setText(pair.getValue().toString());
						}
					}
				}
			}
			// Checking if the user won after every change.
			if (hasWon) {
				processWin();
				
			}
		}
	}
	
	/*
     * Purpose: Creates the modal and disables all options except 'New Puzzle'
     * once the user has successfully solved the cryptogram.
     * 
     * @param None.
     * 
     * @return None.
     */
	private void processWin() {
		//Media keySound = new Media(new File("WinSound.mp3").toURI().toString());
		//MediaPlayer mediaplayer = new MediaPlayer(keySound);
		//mediaplayer.play();
		// Disabling all interactive objects except 'New Puzzle'
		grid.setDisable(true);
		for (Node node : rVBox.getChildren()) {
			node.setDisable(true);
		}
		rVBox.getChildren().get(0).setDisable(false);
		Stage message = new Stage();
		VBox alert = new VBox();
		HBox top = new HBox();
		HBox bottom = new HBox();
		message.setMinHeight(230);
		message.setMinWidth(470);
		message.setTitle("Message");
		Image info = null;
		try {
			info = new Image(new FileInputStream("info.png"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		// Message Label
		Label messageLabel = new Label("Message");
		messageLabel.setTranslateX(10);
		messageLabel.setTranslateY(38);
		messageLabel.setFont(new Font("Arial", 20));
		// info icon Image
		ImageView imageView = new ImageView(info);
		imageView.setTranslateX(290);
		imageView.setTranslateY(10);
		imageView.setFitWidth(80);
		imageView.setFitHeight(80);
		imageView.setPreserveRatio(true);
		top.getChildren().add(messageLabel);
		top.getChildren().add(imageView);
		top.setMinSize(470, 100);
		// You won Label
		Label youWon = new Label("You won!");
		youWon.setTranslateX(10);
		youWon.setTranslateY(18);
		// OK Button
		Button ok = new Button("OK");
		ok.setTranslateX(300);
		ok.setTranslateY(50);
		ok.setMinWidth(90);
		ok.setOnAction(event -> {
			message.close();
		});
		bottom.getChildren().add(youWon);
		bottom.getChildren().add(ok);
		bottom.setMinSize(470, 100);
		bottom.setBackground(new Background(new BackgroundFill(Color.rgb
				(235, 235, 235), CornerRadii.EMPTY, Insets.EMPTY)));
		alert.getChildren().add(top);
		Separator sep = new Separator();
		sep.resize(0, 50);
		alert.getChildren().add(sep);
		alert.getChildren().add(bottom);
		Scene infoScene = new Scene(alert);
		message.setScene(infoScene);
		message.initOwner(primaryStage);
		message.initModality(Modality.APPLICATION_MODAL);
		message.showAndWait();
	}

	/*
     * Purpose: Sets up and displays the stage.
     * 
     * @param primaryStage - is a Stage created by JavaFX.
     * 
     * @return None.
     */
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		//Media keySound = new Media(new File("Duel.mp3").toURI().toString());
		//MediaPlayer mediaplayer = new MediaPlayer(keySound);
		//mediaplayer.play();
		CryptogramModel model = new CryptogramModel("window");
		model.addObserver(this);
		controller = new CryptogramController(model);
		controller.format();
		BorderPane borderPane = new BorderPane();
		borderPane.setMinSize(1143, 400);
		Scene scene = new Scene(borderPane);
		setup(borderPane, formatted);
		primaryStage.setTitle("Cryptograms");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	/*
     * Creates all the necessary textFields, Labels, Buttons, and checkbox
     * for the GUI and configures how each one interacts with certain user
     * actions.
     * 
     * @param borderPane - a BorderPane object that holds the rest of the 
     * objects to be displayed on stage.
     * 
     * @param formatted - the encrypted quote string with 30 character line
     * breaks
     * 
     * @return None.
     */
	public void setup(BorderPane borderPane, List<String> formatted) {
		String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		grid = new GridPane();
		for (int i = 0; i < formatted.size(); i++) {
			// Adding 30 VBoxes to grid for each line that contains characters
			for (int j = 0; j < 30; j++) {
				VBox vbox = new VBox();
				grid.add(vbox, j, i);
				if (j < formatted.get(i).length()) {
					Label label = new Label(formatted.get(i).substring(j, j + 1));
					// Creating a textField and Label for each VBox that holds a character
					if (ALPHABET.indexOf(formatted.get(i).substring(j, j + 1)) >= 0) {
						TextField text = new TextField();
						text.setPrefColumnCount(1);
						text.setAlignment(Pos.CENTER);
						vbox.getChildren().add(text);
						// What to do when a key is pressed in this text field
						text.setOnKeyPressed(new HandleKeyPress(label));
						// What to do when the key is released
						text.setOnKeyReleased(event -> {
							String oldText = text.getText();
							// Handles removing additional characters other
							// than the user's guess
							if (oldText.length() > 1) {
								text.clear();
								text.setText(oldText.substring(1));
							}
						});
						// Creating disabled textFields for non alphabetic 
						// characters
					} else {
						TextField text = new TextField(formatted.get(i).
								substring(j, j + 1));
						text.setDisable(true);
						text.setPrefColumnCount(1);
						text.setAlignment(Pos.CENTER);
						vbox.getChildren().add(text);
					}
					vbox.getChildren().add(label);
					vbox.setAlignment(Pos.CENTER);
				}
			}
		}
		borderPane.setCenter(grid);
		// Adding right most column to stage
		Button newPuzzle = new Button("New Puzzle");
		newPuzzle.setOnAction((event) -> start(primaryStage));
		Button hint = new Button("Hint");
		hint.setOnAction(event -> {
			//Media keySound = new Media(new File("Cheater.m4a").toURI().toString());
			//MediaPlayer mediaplayer = new MediaPlayer(keySound);
			//mediaplayer.play();
			controller.requestHint();
		});
		CheckBox freq = new CheckBox("Show Freq");
		controller.requestFrequency();
		freqLabell.setVisible(false);
		freqLabelr.setVisible(false);
		freq.setSelected(false);
		freq.setOnAction(event -> {
			if (freq.isSelected()) {
				freqLabell.setVisible(true);
				freqLabelr.setVisible(true);
			} else {
				freqLabell.setVisible(false);
				freqLabelr.setVisible(false);
			}
		});
		rVBox = new VBox();
		rVBox.getChildren().add(newPuzzle);
		rVBox.getChildren().add(hint);
		rVBox.getChildren().add(freq);
		HBox freqLabels = new HBox();
		freqLabell.setTranslateY(11);
		freqLabels.getChildren().add(freqLabell);
		freqLabelr.setTranslateX(20);
		freqLabels.getChildren().add(freqLabelr);
		rVBox.getChildren().add(freqLabels);
		rVBox.setPadding(new Insets(0,5,0,50));
		borderPane.setRight(rVBox);
	}
	
	class HandleKeyPress implements EventHandler<KeyEvent> {

		// Field variables for handling a key press
		Label label;
		
		public HandleKeyPress(Label label) {
			this.label = label;
		}

		/*
	     * Purpose: Determines what to do when any key is pressed inside a 
	     * TextField.
	     * 
	     * @param key - a KeyEvent created by pressing a key in a TextField.
	     * 
	     * @return None.
	     */
		@Override
		public void handle(KeyEvent key) {
			TextField text = (TextField)key.getSource();
			// Moving to the next enabled TextField if TAB is pressed
			if (key.getCode() == KeyCode.TAB) {
				for (int i = 0; i < grid.getChildren().size(); i++) {
					VBox curBox = (VBox) grid.getChildren().get(i);
					VBox nextBox = null;
					if (curBox.getChildren().get(0).equals(key.getSource())) {
						if (i != grid.getChildren().size() - 1) {
							for (int j = i + 1; j < grid.getChildren().size(); j++) {
								nextBox = (VBox) grid.getChildren().get(j);
								if (!nextBox.getChildren().isEmpty()) {
									break;
								}
							}
						}
						if (nextBox != null) {
							nextBox.requestFocus();
							break;
						}
					}
				}
				// Clearing TextField if backspace is pressed
			} else if (key.getCode() == KeyCode.BACK_SPACE) {
				key.consume();
				text.clear();
				// Consuming event if non alphabetic characters are pressed
			} else if (!key.getText().matches("[a-zA-Z]+")) {
				key.consume();
				// Calling controller to update model if an alphabetic
				// character is pressed.
			} else {
				//Media keySound = new Media(new File("Point.wav").toURI().toString());
				//MediaPlayer mediaplayer = new MediaPlayer(keySound);
				//mediaplayer.play();
				text.replaceText(0, 0, "");
				controller.updateUserMap(label.getText().toUpperCase().charAt(0),
						key.getText().toUpperCase().charAt(0));
			}
		}
		
	}

}
