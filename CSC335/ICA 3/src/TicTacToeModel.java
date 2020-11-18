/**
 * 
 *
 */
public class TicTacToeModel {
	public static int BLANK = 0;
	public static int X = 1;
	public static int O = 2;
	
	public static int BOARD_DIMENSION = 3;
	
	int[][] board;
	
	
	
	public TicTacToeModel() {
		board = new int[BOARD_DIMENSION][BOARD_DIMENSION];
	}
	
	public void placeX(int row, int col) {
		board[row][col] = X;
	}
	
	public void placeO(int row, int col) {
		board[row][col] = O;
	}
	
	public int getAtLocation(int row, int col) {
		return board[row][col];
	}
	
}
