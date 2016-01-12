package uno.perwironegoro.boardgames.pawnrace;

import java.util.ArrayList;
import uno.perwironegoro.boardgames.Colour;

public class GamePR {
	private final ArrayList<MovePR> moves;
	private int moveIndex;
	private Colour player;
	private BoardPR board;

	public GamePR(BoardPR board, Colour currentPlayer) {
		this.board = board;
		moves = new ArrayList<MovePR>();
		moveIndex = 0;
		this.player = currentPlayer;
	}

	public Colour getCurrentPlayer() {
		return player;
	}

	public MovePR getLastMove() {
		if(moves.size() == 0) {
			return null;
		}
		return moves.get(moveIndex - 1);
	}

	public void applyMove(MovePR move) {
		if(!UtilsPR.moveIsValid(move, board, getLastMove())) {
			return;
		}
		board.applyMove(move);
		moves.add(move);
		moveIndex++;
		switchPlayer();
	}

	public boolean unapplyMove() {
		if(getLastMove() == null) {
			return false;
		}
		board.unapplyMove(getLastMove());
		moveIndex--;
		switchPlayer();
		return true;
	}
	
	private void switchPlayer() {
		player = player.opposite();
	}
	
	public Colour getGameResult() {
		return UtilsPR.getGameResult(board, player, getLastMove());
	}
}
