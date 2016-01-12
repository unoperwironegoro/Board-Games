package uno.perwironegoro.boardgames.pawnrace.ai;

import uno.perwironegoro.boardgames.Colour;
import uno.perwironegoro.boardgames.pawnrace.BoardPR;
import uno.perwironegoro.boardgames.pawnrace.GamePR;
import uno.perwironegoro.boardgames.pawnrace.MovePR;

//Minimax AI
public class BotMaxPR extends BotPR {
	public static final Heuristic H 
	= new Heuristic(10, 4, 3, 2, 
			10, 0);

	public BotMaxPR(BoardPR b) {
		super(b);
	}

	@Override
	public MovePR getBestMove(GamePR g, Colour c) {
		BoardTree bt = new BoardTree(board, c, 4, null);
		MovePR move = bt.findBestMove(g, H);
		return move;
	}
}
