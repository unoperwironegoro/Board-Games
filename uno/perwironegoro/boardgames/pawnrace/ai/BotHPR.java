package uno.perwironegoro.boardgames.pawnrace.ai;

import uno.perwironegoro.boardgames.Colour;
import uno.perwironegoro.boardgames.pawnrace.BoardPR;
import uno.perwironegoro.boardgames.pawnrace.GamePR;
import uno.perwironegoro.boardgames.pawnrace.MovePR;

//Minimax AI
public class BotHPR extends BotPR{
	public final Heuristic h;
	
	public BotHPR(BoardPR b, Heuristic h) {
		super(b);
		this.h = h;		
	}
	
	public Heuristic getH() {
		return h;
	}
	
	public String getName() {
		return h.getName();
	}

	@Override
	public MovePR getBestMove(GamePR g, Colour c) {
		BoardTree bt = new BoardTree(board, c, 4, null);
		MovePR move = bt.findBestMove(g, h);
		return move;
	}
}
