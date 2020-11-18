package chat;

/**
 * ChatView.java
 * 
 * Sets up the windows for the chat system and enables messaging 
 * between the two connected players.
 * 
 * Usage instructions:
 * 
 * Construct ChatView
 * ChatView.create(portNum)
 * 
 * Other Useful Methods:
 * createChatBottom(p2p)
 * createBackButton(pane, view, input, connect)
 * createMessageStack(pane, name, address, port)
 * 
 */

import java.io.IOException;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import network.ResolveIPAddress;


public class ChatView{
	
	//Field variables for ChatView Objects
	private static Stage STAGE;
	private Thread thread;
	private LoggedInUser user;
	
	/**
	 * purpose: Launches the GUI for the chat system.
	 * 
	 * @param portNum - the port number the network is working on
	 * 
	 * @return primaryStage - the stage the message system is a part of
	 * 
	 * @throws IOException - throws an exception if the port number is 
	 * incorrect or closes before the connection is established.
	 */
	public Stage create(int portNum) throws IOException {
		Stage primaryStage = new Stage();
		STAGE = primaryStage;
		
		PeerToPeerSocket p2p = new PeerToPeerSocket(portNum);
		thread = new Thread(p2p);
		BorderPane pane = createChatBottom(p2p);
		primaryStage.setScene(new Scene(pane, 400, 400));
		primaryStage.setTitle(ResolveIPAddress.getValidIPAddress()+":"+portNum+"-"+portNum+5);
		return primaryStage;
	}
	
	/**
	 * purpose: Creates the chat box that will display messages from both users
	 * and allows the user to send messages to the other player.
	 * 
	 * @param p2p - the peer to peer socket the networked portion of the program
	 * is running on
	 * 
	 * @return box - the borderpane containing all the chat message system 
	 * objects
	 * 
	 */
	private BorderPane createChatBottom(PeerToPeerSocket p2p) {
		BorderPane box = new BorderPane();
		HBox hbox = new HBox();
		ChatViewModel c = new ChatViewModel();
		TextField text = new TextField();
		TextField address = new TextField();
		TextField port = new TextField();
		TextField name = new TextField();
		VBox input = createMessageStack(text, name, address, port);
		Button send = new Button();
		send.setText("Send");
		// Sending of a message
		send.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				try {
					if(text.getText().length()==0) {
						e.consume();
						return;
					}
//					if(c.getSelectedUser().length()>0||name.getText().length()>0) {
//						p2p.sendMessage(c.getSelectedUser().length()>0?c.getSelectedUser():name.getText(), text.getText());
//					}else {
						p2p.sendMessage(address.getText(),Integer.parseInt(port.getText()), text.getText());
					//}
					if(box.getCenter()!=null) {
						return;
					}
					
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					createMessage(e1.getMessage());
				}
			}
		});
		
		VBox loginInfo = new VBox();
		TextField username = new TextField();
		TextField password = new TextField();
		loginInfo.getChildren().addAll(username, password);
		Button login = new Button();
		login.setText("Login");
		login.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				try {
					if(username.getText().trim().isEmpty()||password.getText().trim().isEmpty()) {
						e.consume();
						return;
					}
					System.out.println(username.getText());
					if(p2p.login(username.getText(),password.getText())) {
						System.out.println("Logged in.");
						loginInfo.getChildren().clear();
						thread.start();
						hbox.getChildren().remove(login);
						hbox.getChildren().add(input);
						hbox.getChildren().add(send);
						user = p2p.getUser();

						ListView<Chat> view = new ListView<Chat>();
						ListView<Message> messages = new ListView<Message>();
						messages.setCellFactory(new Callback<ListView<Message>, ListCell<Message>>() {
			                @Override 
			                public ListCell<Message> call(ListView<Message> list) {
			                    return new MessageCell();
			                }
			            });
						view.setItems(p2p.getUser().getOpenChats());
						view.setCellFactory(new Callback<ListView<Chat>, ListCell<Chat>>() {
			                @Override 
			                public ListCell<Chat> call(ListView<Chat> list) {
			                    return new ChatCell(address, port);
			                }
			            });
						view.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Chat>() {

							@Override
							public void changed(ObservableValue arg0, Chat old, Chat newVal) {
								// TODO Auto-generated method stub
								if(newVal==null) {
									return;
								}
								messages.setItems(newVal.getMessages());
								box.setCenter(messages);
								address.setText(newVal.getRecipient()==null?"":newVal.getRecipient().getHost());
				                port.setText(newVal.getRecipient()==null?"":newVal.getRecipient().getPort()+"");
				                name.setText(newVal.getRecipient().getUser());
								//c.setSelectedUser("localhost");
								//System.out.println(newVal.getUser().getUsername());
								HBox backButton = createBackButton(box, view, input, send);
								box.setTop(backButton);
								HBox h = new HBox();
								h.getChildren().add(text);
								h.getChildren().add(send);
								box.setBottom(h);
							}
						});

						box.setCenter(view);
					}
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		hbox.getChildren().addAll(loginInfo, login);
		box.setBottom(hbox);
		box.setPickOnBounds(false);
		return box;
	}
	
	/**
	 * Creates and shows a stage with the desired text.
	 * @param message the message text to show
	 */
	public static void createMessage(String message) {
		Platform.runLater(()->{
			Stage s = new Stage();
			BorderPane textPane = new BorderPane();
			Text label = new Text();
			label.setText("Alert");
			Text reason = new Text();
			reason.setText(message);
	
			textPane.setCenter(reason);
			s.setScene(new Scene(textPane, 600,200));
			s.setTitle("Alert");
			s.initModality(Modality.WINDOW_MODAL);
			s.initOwner(STAGE);
			s.showAndWait();
		});
	}
	
	/**
	 * purpose: Sets up the chat windows.
	 * 
	 * @param pane - the BorderPan holding the chat windows
	 * 
	 * @param view - the list of chat messages that have been sent
	 * 
	 * @param input - the area of the window messages are typed into
	 * 
	 * @param connect - a button that pushes all the settings and connects the users
	 * 
	 * @return box - an HBox holding the graphical representation of the messaging system
	 * 
	 */
	private HBox createBackButton(BorderPane pane, ListView<Chat> view, VBox input, Button connect) {
		// TODO Auto-generated method stub
		HBox box = new HBox();
		Button goBack = new Button();
		goBack.setText("<");
		goBack.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				view.getSelectionModel().clearSelection();
				pane.setCenter(view);
				pane.setTop(null);
				HBox h = new HBox();
				h.getChildren().addAll(input, connect);
				pane.setBottom(h);
			}
		});
		box.getChildren().add(goBack);
		return box;
	}

	/**
	 * purpose: Displays the messages that have been sent in a first in
	 * first out manner.
	 * 
	 * @param text - the text box the message is typed into
	 * 
	 * @param name - the field containig the user's username
	 * 
	 * @param address - the sender's IP address or hostname
	 * 
	 * @param port - the sender's port number
	 * 
	 * @return box - a VBox hold the messages list
	 * 
	 */
	private VBox createMessageStack(TextField text, TextField name, TextField address, TextField port) {
		VBox box = new VBox();
		HBox h = new HBox();
		//h.getChildren().add(name);
		Button b = new Button();
		b.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				// Adding of a message to the current chat flow from the other end
				if(h.getChildren().contains(name)) {
					name.setText("");
					h.getChildren().remove(name);
					h.getChildren().add(0,port);
					h.getChildren().add(0,address);
					// The sending of a message to the other player
				}else {
					h.getChildren().add(0,name);
					h.getChildren().remove(address);
					h.getChildren().remove(port);
					address.setText("");
					port.setText("");
				}
			}
		});
		h.getChildren().addAll(address, port);
		box.getChildren().add(h);
		box.getChildren().add(text);
		return box;
		
	}

	/**
	 * purpose: An inner class to handle the updating of chat messages.
	 * 
	 */
	static class ChatCell extends ListCell<Chat> {
		
		// Field variables for ChatCell Objects
		private TextField address;
		private TextField port;
		
		/**
		 * purpose: Initializes ChatCell objects and their attributes.
		 * 
		 * @param address - the IP address or hostname of the hosting computer
		 * 
		 * @param port - the port number of the hosting computer
		 */
		public ChatCell(TextField address, TextField port) {
			this.address = address;
			this.port = port;
		}
		
		/**
		 * purpose: Pulls the text that was sent between the users.
		 * 
		 * @param item - a Chat object representing a passed message
		 * 
		 * @param empty - a variable used to determine if the message sent was empty
		 */
        @Override
        public void updateItem(Chat item, boolean empty) {
            super.updateItem(item, empty);
            Text text = new Text();
            if (item != null) {
                text.setText(item.getRecipient().getUser());
                setGraphic(text);
            }
        }
    }
	
	class MessageCell extends ListCell<Message>{
		/**
		 * purpose: Displays the messages to the users that were sent and
		 * received
		 * 
		 * @param item - the message that was sent or received
		 * 
		 * @param empty - a variable used to determine if the message sent was empty
		 */
		@Override
		public void updateItem(Message item, boolean empty) {
			super.updateItem(item, empty);
			VBox box = new VBox();
			HBox text = new HBox();
			text.setAlignment(Pos.BOTTOM_RIGHT);
			Text textBox = new Text();
			if(item!=null) {
				System.out.println("this: "+user.getUser().getUsername()+" from: "+item.getFrom().getUser());
				if(item.getFrom().getUser().equals(user.getUser().getUsername())) {
					text.setBackground(new Background(new BackgroundFill(Color.INDIANRED, CornerRadii.EMPTY, Insets.EMPTY)));
				}else {
					text.setBackground(new Background(new BackgroundFill(Color.AQUAMARINE, CornerRadii.EMPTY, Insets.EMPTY)));
				}
				textBox.setText(item.getMessage());
				text.getChildren().add(textBox);
				box.getChildren().addAll(text);
				setGraphic(box);
			}
		}
	}
}
