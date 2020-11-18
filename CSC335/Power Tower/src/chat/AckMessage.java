package chat;

/**
 * AckMessage.java
 * 
 * Handles the creating of an acknowledgement message for the purpose
 * of networking.
 * 
 * Usage instructions:
 * 
 * Construct AckMessage
 * AckMessage ack = new AckMessage(to, from)
 * 
 */

import java.io.Serializable;


public class AckMessage implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3878864033800198219L;

	//Field variables for AckMessage Object
	private Sender to;
	
	private Sender from;
	
	/**
	 * purpose: Initializes a AckMessage object with all of its attributes.
	 * 
	 * @param to - the Sender the message is begin sent to
	 * 
	 * @param from - the Sender sending the message
	 */
	public AckMessage(Sender to, Sender from) {
		this.to = to;
		this.from = from;
	}
}
