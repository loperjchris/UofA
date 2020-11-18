package chat;

/**
 * Message.java
 * 
 * Creates an object that can be sent through the input and output
 * streams and displayed to the users.
 * 
 * Usage instructions:
 * 
 * Construct Message
 * Message message = new Message(from, query, message)
 * 
 */

import java.io.IOException;

/**
 * Message.java
 * 
 * Creates Message objects to be used by the chat system
 * Allows the program to stream messages to the other player
 * 
 * Usage instructions:
 * 
 * Construct Message
 * Message message = new Message(from, query, message)
 * 
 */

import java.io.Serializable;


public class Message implements Serializable{
	
	// Field variables for Message objects
	private static final long serialVersionUID = 6L;

	private String message;
	
	private Query query;
	
	private Sender from;
	
	/**
	 * purpose: Initializes a Message object and its attributes.
	 * 
	 * @param from - the user that is sending the message
	 * 
	 * @param query - a Query object to determine if the message can
	 * be sent
	 * 
	 * @param message - a string that is what the user typed and wants
	 * to send
	 * 
	 */
	public Message(Sender from, Query query, String message) {
		this.query = query;
		this.message = message;
		this.from = from;
	}
	
	// Getter method for from
	public Sender getFrom() {
		return from;
	}
	
	// Getter method for query
	public Query getQuery() {
		return query;
	}
	
	// getter method for message
	public String getMessage() {
		return message;
	}
	
	/**
	 * purpose: Determines if two messages are equal to each other.
	 * 
	 * @param o - an Object that should represent a message
	 * 
	 * @return a boolean value that states if the object passed in matches
	 * exactly to this message
	 * 
	 */
	@Override
	public boolean equals(Object o) {
		if((o==null)||!(o instanceof Message)) {
			return false;
		}
		Message m = (Message)o;
		if(m.getMessage().equals(getMessage())) {
			return true;
		}
		return false;
	}
}
