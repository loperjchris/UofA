package game;

/**
 * TowerDefenseController.java
 * 
 * Handles most of the game logic that occurs as players play the game
 * and changes occur to the board.
 * 
 * Usage instructions:
 * 
 * Construct TowerDefenseController
 * TowerDefenseController controller = new TowerDefenseController(view)
 * 
 * Other useful methods:
 * controller.getBoard()
 * controller.handleMessage(message)
 * controller.handleMove(move)
 * controller.endTurn()
 * controller.addTower(row, col, type)
 * controller.useAbilityCard(card)
 * controller.useAbilityCardOther(card)
 * controller.useTowerCard(row, col)
 * controller.canUpgrade(row, col)
 * controller.upgradeTower(t, row, col)
 * controller.damageOther(amount)
 * controller.killMinion(minion)
 * controller.setSelectedCard(card)
 * controller.setOtherPlayerFinished(b)
 * controller.getNextAvailableHostPort
 * controller.scanPorts()
 * controller.checkHost(host)
 * controller.startClient(host, port)
 * controller.startServer()
 * controller.getPossibleConnections()
 * controller.getPlayer()
 * controller.getOtherPlayer()
 * controller.setOtherPlayer(p)
 * controller.getMarket()
 * controller.setBoard(m)
 * controller.setMinionsFinished(b)
 * controller.isRunning()
 * controller.setRunning(b)
 * controller.hasConnected()
 * controller.setConnected(b)
 * controller.getServer()
 * controller.setServer(s)
 * controller.getSocket()
 * controller.setSocket(s)
 * controller.getOut()
 * controller.setOut(o)
 * 
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import network.AbilityCardUsedMessage;
import network.DamageOtherMessage;
import network.FastForwardMessage;
import network.MarketCardRemovedMessage;
import network.OtherStatIncreaseMessage;
import network.PauseMessage;
import network.ResolveIPAddress;
import network.StatIncreaseMessage;
import network.TowerDefenseMoveMessage;
import network.TowerDefenseTurnMessage;
import network.TowerPlacedMessage;
import network.TowerUpgradedMessage;
import network.TurnFinishedMessage;
import viewable.Viewable;
import viewable.cards.Card;
import viewable.cards.abilityCards.AbilityCard;
import viewable.cards.abilityCards.DamageCard;
import viewable.cards.towers.TowerCard;
import viewable.cards.towers.TowerCardType;
import viewable.gameObjects.Map;
import viewable.gameObjects.Market;
import viewable.gameObjects.Minion;
import viewable.gameObjects.Player;
import viewable.gameObjects.Tower;
import viewable.gameObjects.TowerType;
import viewable.mapObjects.Path;

public class TowerDefenseController {
	private TowerDefenseBoard board;
	private TowerDefenseView view;
	
	private volatile boolean isRunning = true;
	
	private volatile boolean hasConnected = false;

	private volatile boolean fastForwardState;
	
	private volatile ServerSocket server;
	
	private volatile Socket socket;
	
	private volatile ObjectOutputStream out;
	
	private ObservableList<SocketAddress> possibleConnections;
	
	private TowerDefenseTurnMessage currentTurn;
	
	private Player currentPlayer;
	
	private Player otherPlayer;
	
	private volatile boolean minionsFinished;

	private volatile boolean isPaused;
	
	private volatile boolean isServer;
	
	private volatile boolean xOr;
	
  /**
   * purpose: Creates the controller for the game that handles all the game
   * logic.
   * 
   * @param view - A TowerDefenseView object that is the game board view.
   * 
   * @throws IOException - throws a FileNotFoundException in further methods if
   * game resources cannot be found.
   */
	public TowerDefenseController(TowerDefenseView view) throws IOException {
		this.view = view;
		board = new TowerDefenseBoard(view, this);
		currentPlayer = new Player(this);
		otherPlayer = new Player(this);
		minionsFinished = false;
		fastForwardState = false;
		xOr = false;
		possibleConnections = FXCollections.observableArrayList(new ArrayList<SocketAddress>());
	}
	
	/**
	 * Sets the controller up to run another game.
	 * @throws FileNotFoundException when resources are missing
	 */
	public void reset() throws FileNotFoundException {
		currentPlayer.resetAsNew();
		otherPlayer.resetAsNew();
		minionsFinished = false;
		fastForwardState = false;
		xOr = false;
		board.reset();
	}

	/**
     * purpose: Getter method for the board attribute.
     * 
     * @return board - returns the current board the controller is using
     */
	public TowerDefenseBoard getBoard() {
		return board;
	}
	
	public Viewable[][][] getMapArray(){
		return board.getBoard().getBoard();
	}
	
	/**
     * purpose: handles player's moves and sends them as messages.
     * 
     * @param message - a players turn message that tells the other client
     * what moves were made by the player.
     * 
     */
	public void handleMessage(TowerDefenseTurnMessage message) {
		List<TowerDefenseMoveMessage> moves = message.getMoves();
		for(int i =0;i<moves.size();i++) {
			TowerDefenseMoveMessage move = moves.get(i);
			handleMove(move);
		}
	}
	
	/**
     * purpose: Determines if a card can be played and what will happen
     * based on the type of card played.
     * 
     * @param move - a message that says what moves was made whether it was a 
     * tower placement, an upgrade, an ability card, or a damage card.
     * 
     */
	public void handleMove(TowerDefenseMoveMessage move) {
		Platform.runLater(()->{
			// Tower Placement
			if(move instanceof TowerPlacedMessage) {
				TowerPlacedMessage m = (TowerPlacedMessage)move;

				int col = getMapArray().length-1-m.getCol();
				int row = getMapArray()[0].length-1-m.getRow();

				board.addTower(row, col, m.getTower());
				// Using an ability card
			}else if(move instanceof AbilityCardUsedMessage) {
				AbilityCardUsedMessage a = (AbilityCardUsedMessage)move;
				useAbilityCardOther((AbilityCard)a.getCard());
				// Dealing damage
			}else if(move instanceof DamageOtherMessage) {
				DamageOtherMessage d = (DamageOtherMessage)move;
				currentPlayer.gainLife(d.getAmount()*-1);
			}else if(move instanceof StatIncreaseMessage) {
				StatIncreaseMessage s = (StatIncreaseMessage)move;
				currentPlayer.gainLife(s.getHealth());
				currentPlayer.increaseGold(s.getGold());
			}else if(move instanceof MarketCardRemovedMessage) {
				MarketCardRemovedMessage m = (MarketCardRemovedMessage)move;
				board.getMarket().removeFromForSale(m.getIndex());
			}else if(move instanceof OtherStatIncreaseMessage) {
				OtherStatIncreaseMessage o = (OtherStatIncreaseMessage)move;
				otherPlayer.gainLife(o.getHealth());
				otherPlayer.increaseGold(o.getGold());
			}else if(move instanceof TowerUpgradedMessage) {
				TowerUpgradedMessage t = (TowerUpgradedMessage)move;
				int col = getMapArray().length-1-t.getCol();
				int row = getMapArray()[0].length-1-t.getRow();
				System.out.println(row+" "+col);
				System.out.println(getMapArray()[col][row][0]);
				upgradeTower((Tower)getMapArray()[col][row][0], row, col);
			}else if(move instanceof FastForwardMessage) {
				FastForwardMessage f = (FastForwardMessage)move;
				fastForwardState = f.getState();
				board.notifyMenu(2);
			}else if(move instanceof PauseMessage) {
				PauseMessage p = (PauseMessage)move;
				isPaused= p.getState();
				board.notifyMenu(1);
			}
		});
	}
	
	/**
     * purpose: Handles logic that must happen when players end their turns.
     * 
     */
	public void endTurn() {
		currentPlayer.setComplete(true);
		minionsFinished = false;
		xOr = false;
		Thread thread = new Thread(()-> {
			try {
				out.writeObject(new TurnFinishedMessage());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			while(!otherPlayer.isFinished()&&isRunning) {
			}
			board.triggerMinions();
			while(!minionsFinished&&isRunning) {
			}

			minionsFinished = false;
			xOr = false;

			// Handles what to do after player turns
			currentTurn = new TowerDefenseTurnMessage(out);
			Platform.runLater(()->{
				try {
					board.getMarket().repopulateForSale();
					currentPlayer.discardHand();
					currentPlayer.drawCards(5);
					board.beginningOfTurn();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
			// ending turns
			currentPlayer.setComplete(false);
			otherPlayer.setComplete(false);
			currentPlayer.reset();
			otherPlayer.reset();
		});
		thread.start();
	}
	
	/**
     * purpose: adds a tower to the board when a player clicks a tower card
     * to the grid.
     * 
     * @param row - the row of the grid the tower is being added to
     * 
     * @param col - the col of the grid the tower is being added to
     * 
     * @param type - a TowerType that defines what type of tower is being added
     * to the grid.
     * 
     */
	public void addTower(int row, int col, TowerType type) {
		board.addTower(row, col, type);
		currentTurn.addMove(new TowerPlacedMessage(type, row, col));
	}
	

	/**
     * purpose: Removes a bought card from the buyable cards in the market.
     * 
     * @param card - Card object
     * 
     * @return removed - a boolean value stating whether card was successfully removed
     * or not. 
     * 
     */
	public boolean removeFromForSale(Card card) {
		boolean removed = board.getMarket().removeFromForSale(card);
		System.out.println(removed+" "+card);
		if(removed) {
			currentTurn.addMove(new MarketCardRemovedMessage(board.getMarket().getRemovedIndex()));
		}
		return removed;
	}
	
    /**
     * purpose: Determines what to do when an ability card is double clicked
     * by a player.
     * 
     * @param card - Passed the specific card that was played.
     * 
     */
	public void	useAbilityCard(AbilityCard card) {
		if(card instanceof DamageCard) {
			DamageCard d = (DamageCard)card;
			card.ability(otherPlayer);
			currentTurn.addMove(new StatIncreaseMessage(d.getDamage()*-1, 0));
		}else {
			card.ability(currentPlayer);
			currentTurn.addMove(new AbilityCardUsedMessage(card));
		}
		currentPlayer.addToDiscard(card);
	}
	
	/**
     * purpose: Determines how to handle ability cards being played that 
     * effect the other player.
     * 
     * @param card - Passed the specific card that was played.
     * 
     */
	private void useAbilityCardOther(AbilityCard card) {
		card.ability(otherPlayer);
		otherPlayer.addToDiscard(card);
	}
	
	/**
     * purpose: Determines what to do with a TowerCard once it has been used.
     * 
     * @param row - the row of the grid the tower is being added to
     * 
     * @param col - the col of the grid the tower is being added to
     * 
     */
	public void useTowerCard(int row, int col) {
		// if current grid space is just a Path object
		if(getMapArray()[col][row][0] instanceof Path) {

			return;
		}
		// if selected object is a TowerCard
		if(currentPlayer.getSelectedCard()==null||!(currentPlayer.getSelectedCard() instanceof TowerCard)) {
			return;
		}
		TowerType vals = null;
		for(TowerType t: TowerType.values()) {
			if(t.getTower() == ((TowerCard) currentPlayer.getSelectedCard()).getTower()) {
				vals = t;
			}
		}
		if(vals==null) {
			return;
		}
		// adding tower to grid
		addTower(row, col, vals);
		// adding card to player's discard pile and unselecting the card
		currentPlayer.addToDiscard(currentPlayer.getSelectedCard());
		currentPlayer.setSelectedCard(null);
	}
	
	/**
     * purpose: Determines if a card being placed on the grid can upgrade a tower.
     * 
     * @param row - the row of the grid the tower is being added to
     * 
     * @param col - the col of the grid the tower is being added to
     * 
     * @return - returns whether the card being played has an upgradable feature
     * or not
     * 
     */ 
	public boolean canUpgrade(int row, int col) {
		if(getMapArray()[col][row][0]==null) {
			return false;
		}
		if(!(getMapArray()[col][row][0] instanceof Tower)) {
			return false;
		}
		if(currentPlayer.getSelectedCard()==null||!(currentPlayer.getSelectedCard() instanceof TowerCard)) {
			return false;
		}
		TowerCard tCard = (TowerCard)currentPlayer.getSelectedCard();
		return tCard.getTower().isAssignableFrom(getMapArray()[col][row][0].getClass());

	}
	
	/**
     * purpose: Upgrades a tower when the same TowerCard has been added to it
     * on the grid.
     * 
     * @param t - the type of tower that is being added to the grid
     * 
     * @param row - the row of the grid the tower is being added to
     * 
     * @param col - the col of the grid the tower is being added to
     * 
     */
	public void upgradeTower(Tower t, int row, int col) {
		if(currentPlayer.getSelectedCard()==null) {
			for(int i =0;i<TowerCardType.values().length;i++) {
				if(t.getClass().equals(TowerCardType.values()[i].getTower())) {
					try {
						TowerCardType.values()[i].getTowerCard().newInstance().Upgrade(t);
						board.updateBoard(row, col);
					} catch (InstantiationException | IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}else {
			if(!(currentPlayer.getSelectedCard() instanceof TowerCard)) {
				return;
			}
			((TowerCard)currentPlayer.getSelectedCard()).Upgrade(t);
			currentPlayer.addToDiscard(currentPlayer.getSelectedCard());
			currentPlayer.setSelectedCard(null);
			currentTurn.addMove(new TowerUpgradedMessage(row, col));
		}
	}
	
	/**
     * purpose: Calculates what damage is to be dealt to the opponent.
     * 
     * @param amount - how much damage is going to be dealt to the other player
     * 
     */
	public void damageOther(int amount) {
		// send message to other player to take damage.
		currentTurn.addMove(new DamageOtherMessage(amount));
		otherPlayer.gainLife(amount*-1);
	}
	
	/**
	 * This method damages the other player from the server side, sending the correct corresponding message.
	 * @param minion the minion that inflicted the damage.
	 */
	public void damageOther(Minion minion) {
		if(!isServer) {
			return;
		}
		if(minion.getPlayer().equals(otherPlayer)) {
			currentTurn.addMove(new OtherStatIncreaseMessage(minion.getDamage()*-1, 0));
			currentPlayer.damageTaken(minion.getDamage());
		}else {
			damageOther(minion.getDamage());
		}
	}
	
	/**
     * purpose: Determines if a minion is going to be killed.
     * 
     * @param minion - the minion that is being killed
     * 
     */
	public void killMinion(Minion minion) {
		if(!isServer) {
			return;
		}
		System.out.println(minion.getPlayer());
		if(minion.getPlayer().equals(currentPlayer)) {
			currentTurn.addMove(new StatIncreaseMessage(0, minion.getReward(otherPlayer)));
			otherPlayer.increaseGold(minion.getReward(otherPlayer));
		}else {
			currentTurn.addMove(new OtherStatIncreaseMessage(0, minion.getReward(currentPlayer)));
			currentPlayer.increaseGold(minion.getReward(currentPlayer));
		}
	}
	
	/**
     * purpose: sets the card the the player has clicked.
     * 
     * @param card - the Card object that was selected by the player
     * 
     */
	public void setSelectedCard(Card card) {
		currentPlayer.setSelectedCard(card);
	}
	
	/**
     * purpose: sets the boolean variable that tells if the other player has
     * finished taking their turn.
     * 
     * @param b - a boolean variable 
     * 
     */
	public void setOtherPlayerFinished(boolean b) {
		otherPlayer.setComplete(b);
	}
	
	/**
     * purpose: Searches through all ports to find an available one.
     * 
     * @return s - the socket number being used by the host
     * 
     * @throws IOException - exception to be thrown if the user didn't input a proper
     * port number
     * 
     */
	public ServerSocket getNextAvailableHostPort() throws IOException {
		ServerSocket s = new ServerSocket();
		int initial = 55000;
		while(true) {
			// What to do if no open ports were found
			if(initial>55005) {
				throw new IndexOutOfBoundsException("No ports open.");
			}
			// binding to the socket
			try {
				s.bind(new InetSocketAddress(ResolveIPAddress.getValidIPAddress(), initial));
				break;
			}catch(Exception ex) {
				initial++;
				continue;
			}
		}
		return s;
	}
	
	/**
     * purpose: scans for ports that are broadcasting this game.
     * 
     * @throws IOException - throws an exception when the scanned port doens't match
     * the host's port
     * 
     */
	public void scanPorts() throws IOException{
		try {
			if(!InetAddress.getLocalHost().getHostAddress().contains(".")) {
				return;
			}
			possibleConnections.clear();
			String[] command = new String[]{"arp","-a"};
			ProcessBuilder builder = new ProcessBuilder(command);
			builder.directory(new File("./"));
			Process p=builder.start();
			Scanner scan = new Scanner(p.getInputStream());
			scan.nextLine();
			scan.nextLine();
			scan.nextLine();
			while(scan.hasNextLine()) {
				String list = scan.nextLine().trim();
				String address = list.split(" ")[0];
				Thread thread = new Thread(()-> {
					checkHost(address);
				});
				thread.start();
			}
			Thread thread = new Thread(()-> {
				try {
					checkHost(InetAddress.getLocalHost().getHostAddress());
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
			thread.start();
			scan.close();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
     * purpose: Checks to see if the host of the client matches the hostname of
     * the computer hosting the game.
     * 
     * @param host - the name or IP of the hosting computer
     * 
     */
	private void checkHost(String host) {
		try {
			if(!InetAddress.getByName(host).isReachable(100)) {
				return;
			}
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		for(int port = 55000;port<=55005&&isRunning;port++) {
			SocketAddress address = new InetSocketAddress(host, port);
			try {
				Socket s = new Socket(host, port);
				s.close();
				Platform.runLater(()->{
					possibleConnections.add(address);
				});
			}catch(Exception ex) {
				continue;
			}
		}
	}
	
	/**
     * purpose: Begins the connection to the host for the client.
     * 
     * @param host - the name or IP of the host computer
     * 
     * @param port - the port number the host computer is using
     * 
     */
	public void startClient(String host, int port) {
		Thread thread = new Thread(new Client(host, port, this, null, null));
		thread.start();
		isServer = false;
	}
	
	/**
     * purpose: Starts the servers game
     * 
     */
	public void startServer() {
		try {
			setServer(getNextAvailableHostPort());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Thread thread = new Thread(new Server(this, null, null));
		thread.start();
		isServer = true;
	}
	
	/**
     * purpose: Creates an obersvable list of all possible ports.
     * 
     * @return observableList of socketAddresses
     */
	public ObservableList<SocketAddress> getPossibleConnections(){
		return possibleConnections;
	}
	
	/**
     * purpose: Getter method for player.
     * 
     * @return Player object
     * 
     */
	public Player getPlayer() {
		return currentPlayer;
	}
	
	/**
     * purpose: Getter method for opponent.
     * 
     * @return Player object
     * 
     */
	public Player getOtherPlayer() {
		return otherPlayer;
	}
	
	/**
     * purpose: Setter method for opponent object.
     * 
     */
	private void setOtherPlayer(Player p) {
		otherPlayer = p;
	}
	
	/**
     * purpose: Getter method for the market
     *
     * @return Market object
     * 
     */
	public Market getMarket() {
		return board.getMarket();
	}

	/**
     * purpose: setter method for board.
     * 
     * @param m Map object
     * 
     */
	public void setBoard(Map m) {
		board.setBoard(m);
	}
	
	/**
     * purpose: Getter method for the view the board is associated with.
     * 
     * @return TowerDefenseView object
     */
	public TowerDefenseView getView() {
		return view;
	}
	
	/**
     * purpose: Setter method for the board using the TowerDefenseBoard object.
     * 
     * @param m TowerDefenseBoard object
     * 
     */
	public void setBoard(TowerDefenseBoard m) {
		board = m;
		m.getMarket().setController(this);
		m.getMarket().setView(view);
		try {
			m.getMarket().repopulateImages();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		board.setBoard(board.getBoard());

	}
	
	/**
     * purpose: Setter method determining if the wave is complete.
     * 
     * @param b boolean if minions are finished with their wave
     * 
     */
	public void setMinionsFinished(boolean b) {
		if(b&&xOr) {
			minionsFinished = true;
			return;
		}
		xOr = b;
		minionsFinished = false;
	}
	
	/**
     * purpose: Setter for whether the game is paused or not.
     * 
     * @return true if game is paused
     * 
     */
	public boolean isPaused() {
		return isPaused;
	}
	
	/**
     * purpose: Setter method for whether the game is paused or not.
     * 
     * @param b parameter to set if game is paused pauses for true
     * 
     */
	public void setPaused(boolean b) {
		isPaused = b;
		currentTurn.addMove(new PauseMessage(b));
	}
	

	// Getter for isRunning.
	public boolean isRunning() {
		return isRunning;
	}
	
	// Setter for isRunning.
	public void setRunning(boolean b) {
		isRunning = b;
		try {
			if(socket!=null&&!b) {
				socket.close();
			}
			if(server!=null&&!b) {
				server.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// Getter for hasConnected.
	public boolean hasConnected() {
		return hasConnected;
	}
	
	// Setter for hasConnected.
	public void setConnected(boolean b) {
		hasConnected = b;
	}
	
	// Getter for serversocket.
	public ServerSocket getServer() {
		return server;
	}
	
	// Setter for serversocket.
	public void setServer(ServerSocket s) {
		server = s;
	}
	
	// Getter for socket.
	public Socket getSocket() {
		return socket;
	}
	
	// Setter for socket.
	public void setSocket(Socket s) {
		socket = s;
	}
	
	// Getter for output stream.
	public ObjectOutputStream getOut() {
		return out;
	}
	
	// Setter for fast forward state.
	public void setFastForward(boolean b) {
		fastForwardState = b;
		currentTurn.addMove(new FastForwardMessage(b));
	}
	
	// Getter for fastforward state.
	public boolean getFastForward() {
		return fastForwardState;
	}
	
	// Setter for output stream.
	public void setOut(ObjectOutputStream o) {
		out = o;
		currentTurn = new TowerDefenseTurnMessage(o);
	}
}
