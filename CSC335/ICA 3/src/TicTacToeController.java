
public class TicTacToeController {

	private TicTacToeModel model;
	
	public TicTacToeController(TicTacToeModel model) {
		this.model = model;
	}
	
	public boolean isGameOver() {
		boolean emptySpace = false;
		
		// Board is full, maybe a win, maybe a tie
		for(int i=0; i<TicTacToeModel.BOARD_DIMENSION; i++) {
			for(int j=0; j<TicTacToeModel.BOARD_DIMENSION; j++) {
				if(model.getAtLocation(i, j) == TicTacToeModel.BLANK) {
					emptySpace = true;
				}
			}
		}
		
		if(!emptySpace) {
			return true;
		}
		
		// Row wins
		for(int i=0; i<TicTacToeModel.BOARD_DIMENSION; i++) {
			if(model.getAtLocation(i, 0) != TicTacToeModel.BLANK && 
					model.getAtLocation(i, 0) == model.getAtLocation(i, 1) &&
					model.getAtLocation(i, 1) == model.getAtLocation(i, 2)) {
				return true;
			}
		}
		
		// Column wins
		for(int i=0; i<TicTacToeModel.BOARD_DIMENSION; i++) {
			if(model.getAtLocation(0, i) != TicTacToeModel.BLANK && 
					model.getAtLocation(0, i) == model.getAtLocation(1, i) &&
					model.getAtLocation(1, i) == model.getAtLocation(2, i)) {
				return true;
			}
		}
		
		// Diagonally
		if(model.getAtLocation(0, 0) != TicTacToeModel.BLANK && 
				model.getAtLocation(0, 0) == model.getAtLocation(1, 1) &&
				model.getAtLocation(1, 1) == model.getAtLocation(2, 2)) {
			return true;
		}
		
		// Diagonally the other way
		if(model.getAtLocation(0, 2) != TicTacToeModel.BLANK && 
				model.getAtLocation(0, 2) == model.getAtLocation(1, 1) &&
				model.getAtLocation(1, 1) == model.getAtLocation(2, 0)) {
			return true;
		}
		
		return false;
		
	}
}
