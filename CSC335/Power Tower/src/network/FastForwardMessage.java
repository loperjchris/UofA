package network;

public class FastForwardMessage extends TowerDefenseMoveMessage{
	private boolean state;
	public FastForwardMessage(boolean b) {
		state = b;
	}
	
	public boolean getState() {
		return state;
	}
}
