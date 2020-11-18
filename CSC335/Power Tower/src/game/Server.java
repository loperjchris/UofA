package game;
/**
 * Server.java
 * 
 * Opens the ports for the server side of the networking and maintains the
 * connection for as long as the program is running.
 * 
 * Usage instructions:
 * 
 * Construct Server
 * Server server= new Server(host, port, controller, lostModal, tieModal)
 * 
 * Other useful methods:
 * client.run()
 */

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import javafx.application.Platform;
import network.TowerDefenseMoveMessage;
import network.TowerDefenseTurnMessage;
import network.TurnFinishedMessage;

public class Server implements Runnable{
	
	// Field variables for Client object
	private String host;
	
	private int port;
	
	private TowerDefenseController controller;
	
	private Runnable lostModal;
	
	private Runnable tieModal;
	
	/**
     * Purpose: Initializes a Server object.
     * 
     * @param c - a TowerDefenseController object to have reference back
     * to the controller
     * 
     * @param lostModal - a modal to display if the client player has lost
     * 
     * @param tieModal - a modal to display if the client player has tied
     */
	public Server(TowerDefenseController c, Runnable lostModal, Runnable tieModal) {
		controller = c;
		this.lostModal = lostModal;
		this.tieModal = tieModal;
	}
	
	/**
     * Purpose: Opens the socket to establish a connection and waits for a
     * client to connect. Maintains the connection until the program is ended.
     * 
     */
	@Override
	public void run() {
		ObjectInputStream in = null;
		ServerSocket server = controller.getServer();
		try {
			System.out.println(server.getLocalSocketAddress());
			Socket socket = server.accept();
			controller.setSocket(socket);
			controller.setConnected(true);
			controller.setRunning(true);
			controller.setOut(new ObjectOutputStream(socket.getOutputStream()));
			in = new ObjectInputStream(socket.getInputStream());
			controller.getOut().writeObject(controller.getBoard().flip());
			controller.getOut().flush();
			while(controller.isRunning()) {
				try {
					Object obj = in.readObject();
					handleMessage(obj);
				}catch (ClassNotFoundException e) {
					e.printStackTrace();
					controller.setRunning(false);
				} catch(IndexOutOfBoundsException e) {
					System.out.println("Illegal Move");
				}catch (Exception e) {
					e.printStackTrace();
					controller.setRunning(false);
				}
			}
			socket.close();
			server.close();
		} catch (IOException e) {
			e.printStackTrace();
			try {
				server.close();
				ServerSocket s = new ServerSocket();
				s.bind(server.getLocalSocketAddress());
				controller.setServer(s);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				return;
			}
			controller.setConnected(false);
			if(controller.isRunning()) {
				run();
			}
			
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/**
     * @purpose: Determines if players have finished their turns and tells
     * the controller to send a message for the moves that were taken.
     * 
     * @param o - an message Object that represents a full players turn.
     */
	private void handleMessage(Object o) {
		if(o instanceof TurnFinishedMessage) {
			controller.setOtherPlayerFinished(true);
		}else if(o instanceof TowerDefenseTurnMessage) {
			controller.handleMessage((TowerDefenseTurnMessage)o);
		}else if(o instanceof TowerDefenseMoveMessage) {
			controller.handleMove((TowerDefenseMoveMessage)o);
		}
	}
}
