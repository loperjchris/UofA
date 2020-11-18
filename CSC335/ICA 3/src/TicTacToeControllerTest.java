import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class TicTacToeControllerTest {

	@Test
	void testIsGameOver() {
		TicTacToeModel board = new TicTacToeModel();
		TicTacToeController ticTacToe = new TicTacToeController(board);
		
		assertFalse(ticTacToe.isGameOver());
	}
	
	@Test
	void testIsGameOverFillBoard() {
		TicTacToeModel board = new TicTacToeModel();
		TicTacToeController ticTacToe = new TicTacToeController(board);
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				board.placeX(i, j);
			}
		}
		assertTrue(ticTacToe.isGameOver());
	}
	
	@Test
	void testIsGameOverRowWin() {
		TicTacToeModel board = new TicTacToeModel();
		TicTacToeController ticTacToe = new TicTacToeController(board);
		for (int i = 0; i < 3; i++) {
			board.placeO(0, i);
		}
		assertTrue(ticTacToe.isGameOver());
	}
	
	@Test
	void testIsGameOverColWin() {
		TicTacToeModel board = new TicTacToeModel();
		TicTacToeController ticTacToe = new TicTacToeController(board);
		for (int i = 0; i < 3; i++) {
			board.placeO(i, 0);
		}
		assertTrue(ticTacToe.isGameOver());
	}
	
	@Test
	void testIsGameOverDag1Win() {
		TicTacToeModel board = new TicTacToeModel();
		TicTacToeController ticTacToe = new TicTacToeController(board);
		for (int i = 0; i < 3; i++) {
			board.placeO(i, i);
		}
		assertTrue(ticTacToe.isGameOver());
	}
	
	@Test
	void testIsGameOverDag2Win() {
		TicTacToeModel board = new TicTacToeModel();
		TicTacToeController ticTacToe = new TicTacToeController(board);
		for (int i = 0; i < 3; i++) {
			board.placeO(2 - i, i);
		}
		assertTrue(ticTacToe.isGameOver());
	}
}
