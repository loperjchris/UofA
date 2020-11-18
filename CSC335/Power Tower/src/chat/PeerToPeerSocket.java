package chat;

/**
 * PeerToPeerSocket.java
 * 
 * Opens up the port that messages will be streamed through.
 * 
 * Usage instructions:
 * 
 * Construct PeerToPeerSocket
 * PeerToPeerSocket p2p = new PeerToPeerSocket()
 * PeerToPeerSocket p2p = new PeerToPeerSocket(offset)
 * 
 * Other Useful Methods:
 * p2p.run()
 * p2p.checkServers()
 * p2p.checkSockets()
 * p2p.handleMessages(obj, s)
 * p2p.verifyQuery(q, s)
 * p2p.connect(host, port, callback, failed)
 * p2p.sendMessage(hostname, message)
 * p2p.sendMessage(hostTo, port, message)
 * p2p.login(username, password)
 * p2p.getUser()
 * p2p.setUser(u)
 * 
 */

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.application.Platform;
import network.ResolveIPAddress;


public class PeerToPeerSocket implements Runnable{
	
	// Field variables for PeerToPeerSocket objects
	private volatile List<ServerSocket> servers;
	private volatile List<Socket> activeConnections;
	private volatile List<Sender> currentConnections;
	private Map<Socket, Object[]> mapConnections;
	private String host;
	private LoggedInUser user;
	private Sender from;
	private Thread failedGeneric;
	
	/**
	 * purpose: Initializes a PeerToPeerSocket object and its attributes.
	 * 
	 * @throws IOException - throws an exception if the port number is 
	 * incorrect or closes before the connection is established.
	 */
	public PeerToPeerSocket() throws IOException {
		this(6881);
	}
	
	/**
	 * purpose: Initializes a PeerToPeerSocket object and its attributes.
	 * 
	 * @param offset - a number to add to the port to hit an unused port
	 * 
	 * @throws IOException - throws an exception if the port number is 
	 * incorrect or closes before the connection is established.
	 */
	public PeerToPeerSocket(int offset) throws IOException {
		this.host = ResolveIPAddress.getValidIPAddress();
		mapConnections = new HashMap<Socket, Object[]>();
		servers = new ArrayList<ServerSocket>();
		currentConnections = new ArrayList<Sender>();
		for(int i = offset;i<offset+5;i++) {
			try {
				servers.add(new ServerSocket(i));
			}catch(Exception ex) {
				ex.printStackTrace();
				continue;
			}
		}
		if(servers.size()==0) {
			throw new IOException();
		}
		activeConnections = new ArrayList<Socket>();
	}
	
	/**
	 * purpose: Keeps the connections running and checked.
	 * 
	 */
	@Override
	public void run() {
		while(true) {
			checkServers();
			checkSockets();
		}
	}
	
	/**
	 * purpose: Checks to see if the server is still up and communicating.
	 * 
	 */
	private void checkServers() {
		List<ServerSocket> toRemove = new ArrayList<ServerSocket>();
		for(ServerSocket server: servers) {
			try {
				server.setSoTimeout(30);
				Socket s = server.accept();
				
				// If connection is accepted, send the initial user information.
				ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
				ObjectInputStream in = new ObjectInputStream(s.getInputStream());
				out.writeObject(new Message(from, new Query(host, server.getLocalPort()),""));
				
				// Get the response object which should inform us about the other PC.
				Message res = (Message)in.readObject();
				Sender mesFrom = res.getFrom();
				mesFrom.setHost(s.getInetAddress().getHostAddress());
				mesFrom.setPort(s.getPort());
				currentConnections.add(res.getFrom());
				activeConnections.add(s);
				mapConnections.put(s, new Object[] {out, in});
				
				// Ensure we don't try to connect to this server again.
				toRemove.add(server);
			} catch (SocketTimeoutException e) {
				// TODO Auto-generated catch block
				continue;
			} catch(IOException e) {
				e.printStackTrace();
				return;
			}
			catch(Exception ex) {
				ex.printStackTrace();
				// In the case that something unexpected happens, check to make sure that the sockets all connect.
				List<Socket> connections = new ArrayList<Socket>();
				for(Socket con: activeConnections) {
					try {
						// Read the next byte in the stream to ensure that the stream is still connected.
						con.getInputStream().mark(1);
						if(con.getInputStream().read()==-1) {
							servers.add(new ServerSocket(con.getLocalPort()));
							continue;
						}else {
							con.getInputStream().reset();
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						try {
							servers.add(new ServerSocket(con.getLocalPort()));
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						continue;
					}
					connections.add(con);
				}
				activeConnections = connections;
			}
		}
		servers.removeAll(toRemove);
	}
	
	/**
	 * purpose: Checks to see if the sock being used is still open and useable.
	 * Blocks the port while in use
	 * 
	 */
	private void checkSockets() {
		List<Socket> toRemove = new ArrayList<Socket>();
		for(Socket s: activeConnections) {
			try {
				ObjectOutputStream out = (ObjectOutputStream)(mapConnections.get(s)[0]);
				out.writeObject(new KeepAlive());
				// Try to read in an object.
				ObjectInputStream in = (ObjectInputStream)(mapConnections.get(s)[1]);

				Object obj = in.readObject();
				
				// Handle the message case.
				if(obj instanceof Message){
					try {
						handleMessage(obj, s);
					}catch(IOException ex) {
						ex.printStackTrace();
					}
				}else if(obj instanceof AckMessage) {
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
						}
					});
				}
			}catch(Exception ex) {
				try {
					s.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				toRemove.add(s);
				System.out.println(ex.getMessage());
				continue;
			}
		}
		activeConnections.removeAll(toRemove);
	}
	
	/**
	 * purpose: Determines how to send a message and how to receive one.
	 * 
	 * @param obj - the sender of the message
	 * 
	 * @param s - the socket the connection is running over
	 * 
	 * @throws IOException - throws an exception if the port number is 
	 * incorrect or closes before the connection is established.
	 */
	public void handleMessage(Object obj, Socket s) throws IOException {
		Message message = (Message)obj;
		System.out.println(message);
		// Can we verify that the message is meant for us?
		if(verifyQuery(message.getQuery(),s)) {
			message.getFrom().setPort(s.getPort());
			user.addChat(message);
			if(!currentConnections.contains(message.getFrom())) {
				
			}
			//ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
			// Write an acknowledge message to the user we received the message from.
			// [TODO] make this write the message directly to the user, ie connect to the user and then write the message.
			//out.writeObject(new AckMessage(from, message.getFrom()));
			currentConnections.add(message.getFrom());
		}else {
			// Add the person we got this message from to the list of current known connections.
			currentConnections.add(message.getFrom());
			for(Socket socket: activeConnections) {
				if(socket.equals(s)) {
					continue;
				}
				
				// Try to update useful data, like usernames and whatnot with the new data.
				ObjectOutputStream out = (ObjectOutputStream)mapConnections.get(s)[0];
				Query query = message.getQuery();
//				if(query.getDesiredHostName()!=null&&query.getDesiredHostName().length()>0) {
//					// Check if we can update the query with the correct data.
//					for(Sender q: currentConnections) {
//						if(q.getUser()!=null&&q.getDesiredHostName().equals(query.getDesiredHostName())) {
//							if(q.getDesiredHost()!=null&&q.getDesiredHost().length()==0) {
//								q.setDesiredHost(query.getDesiredHost());
//							}
//							if(q.getDesiredPort()==0) {
//								q.setDesiredPort(query.getDesiredPort());
//							}
//							if(query.getDesiredHost()!=null&&query.getDesiredHost().length()==0) {
//								query.setDesiredHost(q.getDesiredHost());
//							}
//							if(query.getDesiredPort()==0) {
//								query.setDesiredPort(q.getDesiredPort());
//							}
//						}else if(query.getDesiredHost().equals(q.getDesiredHost())&&query.getDesiredPort()==q.getDesiredPort()) {
//							if(q.getDesiredHostName()!=null&&!q.getDesiredHostName().equals(query.getDesiredHostName())) {
//								query.setDesiredHostName(q.getDesiredHostName());
//							}
//						}
//					}
//				}
				// Send the message on.
				out.writeObject(message);
			}
		}
	}

	/**
	 * This method checks to make sure that the message is either meant for the user or for host.
	 * @param q the query.
	 * @param s the socket that the connection is held on.
	 * @return a boolean value of true if the message is meant for the host, false otherwise
	 */
	public boolean verifyQuery(Query q, Socket s) {
		// Username takes preference. 
		// [TODO] verify that the user we're connecting to has the correct salt and whatnot.
		boolean isDesiredHost = q.getDesiredHost().equals(host);
		boolean isDesiredPort = q.getDesiredPort()==s.getLocalPort()||q.getDesiredPort()==s.getPort();
		System.out.println(isDesiredHost+" "+isDesiredPort+" "+(isDesiredHost&&isDesiredPort));
		return isDesiredHost&&isDesiredPort;
	}
	
	/**
	 * This method connects to the desired host and port.
	 * @param host The ip address.
	 * @param port The port number.
	 * @param callback A thread to run upon completion of the connection.
	 * @param failed A thread to run if the connection failed.
	 * @throws Exception If IOException occurs.
	 */
	public void connect(String host, int port, Thread callback, Thread failed) throws Exception{
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				for(Socket s: activeConnections) {
					// Do we already have this connection stored?
					if(s.getPort()==port&&(s.getInetAddress().getHostName().equals(host)||s.getInetAddress().getHostAddress().equals(host))) {
						callback.start();
						System.out.println("Succeeded");
						return;
					}
				}
				// Poll for 10 seconds to ensure that the server we try to connect to is atleast open once.
				Socket s = null;
				long timer = System.currentTimeMillis();
				while(timer+1000>System.currentTimeMillis()) {
					try {
						s = new Socket(host, port);
						break;
					}catch(Exception ex) {
						continue;
					}
				}
				if(s==null) {
					System.out.println("Couldn't connect.");
					failed.start();
					return;
				}else {
					System.out.println("Connected");
				}
				try {
					// Try to read in the connection data.
					ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
					ObjectInputStream in = new ObjectInputStream(s.getInputStream());
					Message init = (Message)in.readObject();
					Sender from = init.getFrom();
					from.setPort(s.getPort());
					System.out.println(s.getPort());
					currentConnections.add(from);
					System.out.println(init.getFrom().getUser());
					mapConnections.put(s, new Object[] {out, in});
					System.out.println("Read in on client side...");
					out.writeObject(new Message(from, new Query(init.getFrom().getUser()),""));
					System.out.println("Send out on client side...");
					mapConnections.put(s, new Object[] {out, in});
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				activeConnections.add(s);
				callback.start();
			}
		});
		thread.start();
	}
	
	/**
	 * purpose: Established the connection between the two networked computers.
	 * 
	 * @param hostname - the IP address or hostname of the computer hosting the program
	 * 
	 * @param callback - the thread between the two users so messages can be sent
	 * between the two
	 * 
	 * @param failed - tells the program if the message failed to send
	 * 
	 * @throws IOException - throws an exception if the port number is 
	 * incorrect or closes before the connection is established.
	 */
	public void connect(String hostname, Thread callback, Thread failed) throws Exception {
		int port = 0;
		String host = "";
		for(Sender q:currentConnections) {
			if(q.getUser()!=null&&q.getUser().equals(hostname)) {
				port = q.getPort();
				host = q.getHost();
			}
		}
		connect(host, port, callback, failed);
	}
	
	/**
	 * purpose: Sends the message to the outputstream to be received
	 * by the other player.
	 * 
	 * @param hostname - the IP address or hostname of the computer that
	 * hosted the game
	 * 
	 * @param message - the message that is being sent to the other player
	 * 
	 * @throws IOException - throws an exception if the port number is 
	 * incorrect or closes before the connection is established.
	 */
	public void sendMessage(String hostname, String message) throws Exception {
		Thread thread2 = new Thread(new Runnable() {
			@Override
			public void run() {
				if(user!=null) {
					try {
						user.addOwnMessage(new Message(from, new Query(hostname), message));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				int port = 0;
				String hostN = "";
				for(Sender s : currentConnections) {
					if(s.getUser().equals(hostname)) {
						port = s.getPort();
						hostN = s.getHost();
					}
				}
				for(Socket s: activeConnections) {
					System.out.println(s.getInetAddress().getHostAddress()+" "+s.getInetAddress().getHostName());
					if(s.getPort()==port&&(s.getInetAddress().getHostName().equals(hostN)||s.getInetAddress().getHostAddress().equals(hostN))) {
						try {
							System.out.println("Writing");
							ObjectOutputStream out = (ObjectOutputStream)mapConnections.get(s)[0];
							out.writeObject(new Message(from, new Query(hostname), message));
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		});
		Thread failed = new Thread(new Runnable() {
			@Override
			public void run() {
				Query query = new Query(hostname);
				Message m = new Message(from, query, message);
				ChatView.createMessage("Failed to send message. No guarantee that peers are connected.");
				for(Socket s: activeConnections) {
					try {
						ObjectOutputStream out = (ObjectOutputStream)mapConnections.get(s)[0];
						out.writeObject(m);
						System.out.println("Message sent");
					}catch(Exception ex) {
						continue;
					}
				}
			}
		});
		connect(host, thread2, failed);
	}
	
	/**
	 * purpose: Sends the message to the outputstream to be received
	 * by the other player.
	 * 
	 * @param hostTo - the IP address or hostname of the other computer that
	 * is connected
	 * 
	 * @param port - the port the two computer are communicating on
	 * 
	 * @param message - the message that is being sent to the other player
	 * 
	 * @throws IOException - throws an exception if the port number is 
	 * incorrect or closes before the connection is established.
	 */
	public void sendMessage(String hostTo, int port, String message) throws Exception {
		if(hostTo.equals("localhost")) {
			hostTo = InetAddress.getLocalHost().getHostAddress();
		}
		String hostFinal = hostTo;
		Thread thread2 = new Thread(new Runnable() {
			@Override
			public void run() {
				if(user!=null) {
					Query to = new Query(hostFinal, port);
					for(Sender q: currentConnections) {
						System.out.println("host: "+q.getHost()+" port: "+q.getPort()+" user: "+q.getUser()+" desired host: "+hostFinal+" desired port: "+port);
						if(to.getDesiredHost().equals(q.getHost())&&to.getDesiredPort()==q.getPort()) {
							to.setDesiredHostName(q.getUser());
						}
					}
					Message m = new Message(from, to, message);
					try {
						user.addOwnMessage(m);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				Query query = new Query(hostFinal, port);
				Message m = new Message(from, query, message);
				for(Socket s: activeConnections) {
					System.out.println(s.getInetAddress().getHostAddress()+" "+s.getInetAddress().getHostName());
					if(s.getPort()==port&&(s.getInetAddress().getHostName().equals(hostFinal)||s.getInetAddress().getHostAddress().equals(hostFinal))) {
						try {
							System.out.println("Trying to write.");
							ObjectOutputStream out = (ObjectOutputStream)mapConnections.get(s)[0];
							out.writeObject(m);
							System.out.println("Writing object "+message);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				Thread.yield();
			}
		});
		
		Thread failed = new Thread(new Runnable() {
			@Override
			public void run() {
				Query query = new Query(hostFinal, port);
				Message m = new Message(from, query, message);
				ChatView.createMessage("Failed to send message. No guarantee that peers are connected.");
				for(Socket s: activeConnections) {
					try {
						if(s.getInetAddress().getHostAddress().equals(s.getInetAddress().getHostAddress())&&s.getPort()==port) {
							ObjectOutputStream out = (ObjectOutputStream)(mapConnections.get(s)[0]);
							out.writeObject(m);
							System.out.println("Message sent");
						}
					}catch(Exception ex) {
						continue;
					}
				}
			}
		});
		connect(hostTo, port, thread2, failed);
	}
	
	/**
	 * purpose: Allows the users to login and maintain a continuous chat.
	 * 
	 * @param username - the user's login ID
	 * 
	 * @param password - the user's password used to verify the user
	 * 
	 * @return true if the user logged in successfully, false otherwise
	 * 
	 * @throws IOException - throws an exception if the port number is 
	 * incorrect or closes before the connection is established.
	 * 
	 * @throws NoSuchAlgorithmException - throws an exception if the user's username and/or
	 * password is incorrect.
	 */
	public boolean login(String username, String password) throws IOException, NoSuchAlgorithmException {
		File users = new File("users.txt");
		// Need to read in the file adding each user to an array and then adding the new user to the end if need be.
		users.createNewFile();
		User user = new User(username, password);
		ObjectInputStream in = null;
		try {
			in = new ObjectInputStream(new FileInputStream(users));
	        while (true) {
	            User testUser = (User)(in.readObject());
	            if(testUser.getUsername().equals(username)) {
	            	if(testUser.checkPassword(password)) {
	            		this.user = new LoggedInUser(testUser);
	            		System.out.println(this.user);
	            		from = new Sender(testUser.getUsername());
	            		return true;
	            	}
	            	return false;
	            }
	        }
	    } catch (EOFException ignored) {
	        // as expected
	    } catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
	        if (in != null)
	            in.close();
	    }
		this.user = new LoggedInUser(user);
		in = null;
		List<Object> previous = new ArrayList<Object>();
		try {
			in = new ObjectInputStream(new FileInputStream(users));
	        while (true) {
	            previous.add(in.readObject());
	        }
	    } catch (EOFException ignored) {
	        // as expected
	    } catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
	        if (in != null)
	            in.close();
	    }
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(users));
		for(int i =0;i<previous.size();i++) {
			out.writeObject(previous.get(i));
		}
		out.writeObject(user);
		out.close();
		from = new Sender(user.getUsername());
		return true;
	}
	
	// Getter method for user
	public LoggedInUser getUser() {
		return user;
	}
	
	// Setter method for user
	public void setUser(LoggedInUser u) {
		user = u;
	}
}
