package network;

/**
 * TowerDefenseTurnMessage.java
 * 
 * Creates a message to be sent over the network that tells the other
 * player what actions were completed during the other player's turn.
 * 
 * Usage instructions:
 * 
 * Construct TowerDefenseTurnMessage
 * TowerDefenseTurnMessage tdtm = new TowerDefenseTurnMessage(card)
 * 
 */

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TowerDefenseTurnMessage implements Serializable{
	
	// Field variable for TowerDefenseTurnMessage object
	private List<TowerDefenseMoveMessage> moves = new ArrayList<TowerDefenseMoveMessage>();
	private ObjectOutputStream out;
	
	/**
	 * purpose: Initializes TowerDefenseTurnMessage.
	 * 
	 * @param out - the outputstream that this message must be sent out of
	 */
	public TowerDefenseTurnMessage(ObjectOutputStream out){
		this.out = out;
	}
	
    // Getter method for moves
	public List<TowerDefenseMoveMessage> getMoves(){
		return moves;
	}
	
	/**
	 * purpose: Adds the moves being taken to the outputstream.
	 * 
	 * @param move - a move message detailing the player's move
	 */
	public void addMove(TowerDefenseMoveMessage move) {
		try {
			out.writeObject(move);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
