package uno.perwironegoro.boardgames.pawnrace.ai;

import java.util.ArrayList;
import java.util.Random;

import uno.perwironegoro.boardgames.Colour;
import uno.perwironegoro.boardgames.pawnrace.BoardPR;
import uno.perwironegoro.boardgames.pawnrace.GamePR;
import uno.perwironegoro.boardgames.pawnrace.MovePR;
import uno.perwironegoro.boardgames.pawnrace.UtilsPR;

//Random move AI
public class BotRandellPR extends BotPR {
	private Random rand;
	
	public BotRandellPR(BoardPR board) {
		super(board);
		rand = new Random();
	}
	
	@Override
	public MovePR getBestMove(GamePR g, Colour c) {
		ArrayList<MovePR> moves = UtilsPR.getAllValidMoves(
				UtilsPR.getAllPawns(board, c), board, c, g.getLastMove());
		return moves.get(rand.nextInt(moves.size()));
	}
}
