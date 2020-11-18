package chat;

/**
 * Sender.java
 * 
 * Creates an Sender object so the chat system can keep track of
 * who is sending messages and receiving them.
 * 
 * Usage instructions:
 * 
 * Construct Sender
 * Sender sender = new Sender(username)
 * Sender sender = new Sender(username, host, port)
 * 
 */

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;

import network.ResolveIPAddress;

public class Sender implements Serializable{
	
	// Field variables for Sender objects
	private static final long serialVersionUID = -937615790253733296L;

	private String username;
	
	private String host;
	
	private int port;
	
	/**
	 * purpose: Initializes a Sender object and its attributes.
	 * 
	 * @param username - a String representing the user's login ID
	 * 
	 * @throws UnknownHostException - throws an error if the username
	 * doesn't match an existing username
	 */
	public Sender(String username) throws UnknownHostException {
		this(username, ResolveIPAddress.getValidIPAddress(),0);
	}
	
	/**
	 * purpose: Initializes a Sender object and its attributes.
	 * 
	 * @param username - a String representing the user's login ID
	 * 
	 * @param host - the IP address or hostname of the hosting computer
	 * 
	 * @param port - the port number the game is running on
	 * 
	 * throws UnknownHostException - throws an error if the username
	 * doesn't match an existing username
	 */
	public Sender(String username, String host, int port) {
		this.username = username;
		this.host = host;
		this.port = port;
	}
	
	/**
	 * purpose: Determines if two Sender objects are equal.
	 * 
	 * @param o - an object that should be an instance of sender
	 * 
	 */
	@Override
	public boolean equals(Object o) {
		if(o==null||!(o instanceof Sender)) {
			return false;
		}
		Sender s = (Sender)o;
		if(s.getHost().equals(getHost())&&s.getPort()==getPort()&&s.getUser().equals(getUser())) {
			return true;
		}
		return false;
	}
	
	// Getter method for username
	public String getUser() {
		return username==null?"":username;
	}
	
	// Getter method for host
	public String getHost() {
		return host==null?"":host;
	}
	
	// Getter method for port
	public int getPort() {
		return port;
	}
	
	// Setter method for user
	public void setUser(String user) {
		this.username = user;
	}
	
	// Setter method for host
	public void setHost(String host) {
		this.host = host;
	}
	
	// Setter method for port
	public void setPort(int port) {
		this.port = port;
	}
}
