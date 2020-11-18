package chat;

/**
 * Query.java
 * 
 * Sets up a query system so the chat system knows who the messages are for
 * and where they are coming from.
 * 
 * Usage instructions:
 * 
 * Construct Query
 * Query query = new Query(desiredHostName)
 * Query query = new Query(desiredHost, desiredPort)
 * 
 */

import java.io.Serializable;


public class Query implements Serializable{
	
	// Field variables for Query objects
	private static final long serialVersionUID = 3975675938231942126L;

	private String desiredHost;
	
	private int desiredPort;
	
	private String desiredHostName;
	
	/**
	 * purpose: Initializes a Query object and its attributes.
	 * 
	 * @param desiredHostName - the IP address or hostname of the other
	 * connected computer
	 * 
	 */
	public Query(String desiredHostName) {
		this.desiredHostName = desiredHostName;
	}
	
	/**
	 * purpose: Initializes a Query object and its attributes.
	 * 
	 * @param desiredHost - the IP address or hostname of the other
	 * connected computer
	 * 
	 * @param desiredPort - the port number of the other
	 * connected computer
	 * 
	 */
	public Query(String desiredHost, int desiredPort) {
		this.desiredHost = desiredHost;
		this.desiredPort = desiredPort;
	}
	
	// Setter method for desiredPort
	public void setDesiredPort(int p) {
		desiredPort = p;
	}
	
	// Setter method for desiredHost
	public void setDesiredHost(String h) {
		desiredHost = h;
	}

	// Setter method for desiredHostName
	public void setDesiredHostName(String h) {
		desiredHostName = h;
	}
	
	// Getter method for desiredHostName
	public String getDesiredHostName() {
		return desiredHostName;
	}
	
	// Getter method for desiredHost
	public String getDesiredHost() {
		return desiredHost;
	}
	
	// Getter method for desiredPort
	public int getDesiredPort() {
		return desiredPort;
	}
}
