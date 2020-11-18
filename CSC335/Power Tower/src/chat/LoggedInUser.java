package chat;

/**
 * LoggedInUser.java
 * 
 * Keeps track of who is using the chat system and the messages
 * they have sent.
 * 
 * Usage instructions:
 * 
 * Construct LoggedInUser
 * LoggedInUser user = new LoggedInUser(u)
 * 
 * Other Useful Methods:
 * user.addChat(m)
 * user.addMessageOnMainThread(c)
 * user.addOwnMessage(m)
 * user.getUser()
 * user.getOpenChats()
 * user.removeChat(c)
 * 
 */

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class LoggedInUser{
	
	// Field variables for LoggedInUser objects
	private ObservableList<Chat> openChats;
	private User user;
	
	/**
	 * purpose: Initializes a LoggedInUser object and its attributes
	 * 
	 * @param u - a User object determining who is using the chat system
	 */
	public LoggedInUser(User u) {
		openChats = FXCollections.observableList(new ArrayList<Chat>());
		user = u;
	}
	
	/**
	 * purpose: Adds a message to the chat stream
	 * 
	 * @param m - the message being sent
	 * 
	 */
	public void addChat(Message m) {
		for(Chat c: openChats) {
			System.out.println("Recipient: "+c.getRecipient().getUser()+" sent from: "+m.getFrom().getUser());
			if(c.getRecipient()!=null&&c.getRecipient().getHost().equals(m.getFrom().getHost())&&c.getRecipient().getPort()==m.getFrom().getPort()) {
				c.addMessage(m);
				return;
			}
		}
		System.out.println("Added message: "+m.getFrom().getPort());
		Chat addedChat = new Chat(user, m.getFrom());
		addMessageOnMainThread(addedChat);
		addedChat.addMessage(m);
	}
	
	/**
	 * purpose: Adds the message to the thread holding the chat system
	 * 
	 * @param c - the Chat object holding all the chat messages
	 * 
	 */
	public void addMessageOnMainThread(Chat c) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				openChats.add(c);
			}
		});
	}
	
	/**
	 * purpose: Allows the user to add a new message to the list of messages
	 * 
	 * @param m - the message the user wants to add
	 * 
	 * @throws IOException - throws a message if the stream is up and running
	 * correctly
	 */
	public void addOwnMessage(Message m) throws IOException {
		for(Chat c: openChats) {
			System.out.println("Own message host: "+c.getRecipient().getHost()+":"+c.getRecipient().getPort()+" sent from: "+m.getQuery().getDesiredHost()+":"+m.getQuery().getDesiredPort());
		if(c.getRecipient()!=null&&c.getRecipient().getHost().equals(m.getQuery().getDesiredHost())&&c.getRecipient().getPort()==m.getQuery().getDesiredPort()) {
				c.addMessage(m);
				return;
			}
		}
		System.out.println("Own message: "+m.getQuery().getDesiredPort());
		System.out.println(m.getQuery().getDesiredHostName());
		Chat addedChat = new Chat(user, new Sender(m.getQuery().getDesiredHostName(), m.getQuery().getDesiredHost(), m.getQuery().getDesiredPort()));
		addMessageOnMainThread(addedChat);
		addedChat.addMessage(m);
	}
	
	// Getter method for user
	public User getUser() {
		return user;
	}
	
	// Getter method for openChats
	public ObservableList<Chat> getOpenChats() {
		return openChats;
	}
	
	/**
	 * purpose: Removes chats that have been terminated
	 * 
	 * @param c - a Chat object of previously used chats
	 * 
	 */
	public void removeChat(Chat c) {
		if(c==null||!openChats.contains(c)) {
			return;
		}
		openChats.remove(c);
	}
}
