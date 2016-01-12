package uno.perwironegoro.boardgames.pawnrace.ai;

import uno.perwironegoro.boardgames.Colour;
import uno.perwironegoro.boardgames.pawnrace.BoardPR;
import uno.perwironegoro.boardgames.pawnrace.GamePR;
import uno.perwironegoro.boardgames.pawnrace.MovePR;

public abstract class BotPR {
	public BotPR(BoardPR b) {
		this.board = b;
	}
	
	protected BoardPR board;
	
	public void setBoard(BoardPR board) {
		this.board = board;
	}
	
	public abstract MovePR getBestMove(GamePR g, Colour c);
}
