package network;

public class PauseMessage extends TowerDefenseMoveMessage{
	private boolean state;
	
	public PauseMessage(boolean b) {
		state = b;
	}
	
	public boolean getState() {
		return state;
	}
}
