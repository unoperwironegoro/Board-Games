package uno.perwironegoro.boardgames.pawnrace.ai;

import java.util.ArrayList;

import uno.perwironegoro.boardgames.Colour;
import uno.perwironegoro.boardgames.pawnrace.BoardPR;
import uno.perwironegoro.boardgames.pawnrace.GamePR;
import uno.perwironegoro.boardgames.pawnrace.MovePR;
import uno.perwironegoro.boardgames.pawnrace.UtilsPR;

public class BoardTree {
	private int score;
	private boolean isFinished;
	private MovePR lastMove;
	private BoardPR b;
	private Colour c;
	private ArrayList<BoardTree> nextBoards;

	public BoardTree(BoardPR b, Colour c, int explorations, MovePR lm) {
		nextBoards = new ArrayList<BoardTree>();
		this.lastMove = lm; 
		this.c = c;
		this.b = b;
		if(explorations > 0) {
			exploreBoardTree(explorations, lm);
		}
	}

	public void exploreBoardTree(int explorations, MovePR lm) {
		if(isFinished) {
			return;
		}
		Colour gameResult = UtilsPR.getGameResult(b, c, lm);
		if(gameResult != null || isFinished) {
			this.isFinished = true;
			return;
		}
		for(MovePR m : UtilsPR.getAllValidMoves(b, c, lastMove)) {
			BoardPR nextBoard = new BoardPR(b);
			nextBoard.applyMove(m);
			BoardTree nextBoardTree = new BoardTree(nextBoard, c.opposite(), explorations - 1, m);
			nextBoards.add(nextBoardTree);
		}
	}

	//Must be called from a boardtree with at least one exploration
	public MovePR findBestMove(GamePR g, Heuristic h) {
		score = evaluateScores(h, g.getCurrentPlayer());
		for(BoardTree bt : nextBoards) {
			if(score == bt.score) {
				return bt.lastMove;
			}
		}
		return null;
	}

	private int evaluateScores(Heuristic h, Colour movingPlayer) {
		if(nextBoards.isEmpty()) {
			Colour gameResult = UtilsPR.getGameResult(b, c, lastMove);
			if(gameResult == Colour.NONE) {
				return -(int) 1E4d;
			} else if(gameResult == movingPlayer) {
				return Integer.MAX_VALUE;
			} else if(gameResult == movingPlayer.opposite()) {
				return Integer.MIN_VALUE;
			} else {
				return UtilsAI.evaluateBoard(b, lastMove, c, movingPlayer, h)
						- UtilsAI.evaluateBoard(b, lastMove, c, movingPlayer.opposite(), h);
			}
		} else {
			int minMaxScore = c == movingPlayer? 
					Integer.MIN_VALUE 
					: Integer.MAX_VALUE;
			for(BoardTree bt : nextBoards) {
				bt.score = bt.evaluateScores(h, movingPlayer);
				minMaxScore = c == movingPlayer? 
						Math.max(bt.score, minMaxScore) 
						: Math.min(bt.score, minMaxScore);
			}
			return minMaxScore;
		}
	}

	//unused
	//	public BoardTree pickBranch(MovePR m) {
	//		return nextBoards.get(m.getSAN());
	//	}
}
