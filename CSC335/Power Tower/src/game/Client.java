package game;
/**
 * Client.java
 * 
 * Opens the ports for the client side of the networking and maintains the
 * connection for as long as the server is running.
 * 
 * Usage instructions:
 * 
 * Construct Client
 * Client client = new Client(host, port, controller, lostModal, tieModal)
 * 
 * Other useful methods:
 * client.run()
 */

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import javafx.application.Platform;
import network.TowerDefenseMoveMessage;
import network.TowerDefenseTurnMessage;
import network.TurnFinishedMessage;
import viewable.gameObjects.Map;

public class Client implements Runnable{
	
	// Field variables for Client object
	private String host;
	
	private int port;
	
	private TowerDefenseController controller;
	
	private Runnable lostModal;
	
	private Runnable tieModal;
	
	/**
     * purpose: Initializes a Client object.
     * 
     * @param host - the name of the host to connect to.
     * 
     * @param port - the port to connect through
     * 
     * @param controller - a Connect4Controller object to have reference back
     * to the controller
     * 
     * @param lostModal - a modal to display if the client player has lost
     * 
     * @param tieModal - a modal to display if the client player has tied
     */
	public Client(String host, int port, TowerDefenseController controller, Runnable lostModal, Runnable tieModal) {
		this.host = host;
		this.port = port;
		this.controller = controller;
		this.lostModal = lostModal;
		this.tieModal = tieModal;
	}
	
	/**
     * purpose: Opens the socket to connect with the server and maintains that
     * connection until the program is closed.
     */
	@Override
	public void run() {
		controller.setSocket(new Socket());
		Socket socket = controller.getSocket();
		ObjectInputStream in = null;
		try {
			socket.connect(new InetSocketAddress(host,port));
			System.out.println("Client connected...");
			controller.setConnected(true);
			controller.setRunning(true);
			controller.setOut(new ObjectOutputStream(socket.getOutputStream()));
			in = new ObjectInputStream(socket.getInputStream());
			TowerDefenseBoard map = (TowerDefenseBoard)in.readObject();
			map.setView(controller.getView());
			controller.setBoard(map);
		} catch (IOException | ClassNotFoundException e) {
			return;
		}
		while(controller.isRunning()) {
			System.out.println("Client listening...");
			try {
				Object message = in.readObject();
				handleMessage(message);
			} catch (IOException e) {
				controller.setRunning(false);
			} catch (ClassNotFoundException e) {
				controller.setRunning(false);
			} catch (Exception e) {
				controller.setRunning(false);
			}
			
		}
		try {
			socket.close();
			controller.setConnected(false);
		} catch (IOException e) {
		}
	}
	
	/**
     * purpose: Determines if players have finished their turns and tells
     * the controller to send a message for the moves that were taken.
     * 
     * @param o - an message Object that represents a full players turn.
     */
	private void handleMessage(Object o) {
		if(o instanceof TurnFinishedMessage) {
			controller.setOtherPlayerFinished(true);
		}else if(o instanceof TowerDefenseTurnMessage) {
			TowerDefenseTurnMessage message = (TowerDefenseTurnMessage)o;
			controller.handleMessage(message);
		}else if(o instanceof TowerDefenseMoveMessage) {
			controller.handleMove((TowerDefenseMoveMessage)o);
		}
	}
}
