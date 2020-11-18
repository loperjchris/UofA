package chat;

/**
 * Chat.java
 * 
 * Develops a structure for sending messages to the other networked
 * player.
 * 
 * Usage instructions:
 * 
 * Construct Chat
 * Chat chat = new Chat(user, sender)
 * 
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Chat implements Serializable{
	
	// Field variables for Chat objects
	private static final long serialVersionUID = -3718984659768678611L;
	private ObservableList<Message> messages;
	private User thisUser;
	private Sender recipient;
	
	/**
	 * purpose: Initializes Chat objects and their attributes.
	 * 
	 * @param user the current player utilizing the chat
	 * 
	 * @param sendTo a sender object representing the other player
	 * on the other side of the network connection.
	 */
	public Chat(User user, Sender sendTo) {
		messages = FXCollections.observableList(new ArrayList<Message>());
		thisUser = user;
		recipient = sendTo;
	}
	
	/**
	 * purpose: Adds messages to an observable list that can be transmitted
	 * across the network and interpreted by the other end
	 * 
	 * @param m - the message to be sent
	 */
	public void addMessage(Message m) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				System.out.println("Message Added");
				messages.add(m);
			}
		});
	}
	
	/**
	 * purpose: Pulls the messages out of the observable list to be displayed 
	 * on this end.
	 * 
	 * @return messages - the message to be displayed
	 */
	public ObservableList<Message> getMessages(){
		return messages;
	}
	
	// Getter method for recipient
	public Sender getRecipient() {
		return recipient;
	}
	
	// Getter method for user
	public User getUser() {
		return thisUser;
	}
}
