package chat;

/**
 * Interim.java
 * 
 * Launches the chat system within the main game.
 * 
 * Usage instructions:
 * 
 * Construct Interim
 * Interim interim = new Interim(primaryStage)
 * 
 */

import java.io.IOException;

import javafx.application.Application;
import javafx.stage.Stage;

public class Interim extends Application{
	
	/**
	 * purpose: Launches the chat system within the main game
	 * 
	 * @param primaryStage - the main stage where the main game is launched
	 */
	@Override
	public void start(Stage primaryStage) {
		ChatView view = new ChatView();
		try {
			primaryStage = view.create(7000);
			ChatView view2 = new ChatView();
			Stage stage = view2.create(6000);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
		primaryStage.show();
	}
}
