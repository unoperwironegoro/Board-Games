package uno.perwironegoro.boardgames.pawnrace;

import uno.perwironegoro.boardgames.Board;
import uno.perwironegoro.boardgames.Colour;
import uno.perwironegoro.boardgames.UtilsBoard;

public class BoardPR extends Board {
	public static final char ARBCHAR = '@';

	public BoardPR(BoardPR b) {
		super(b.getWidth(), b.getHeight());
		for (int x = 1; x <= b.getWidth(); x++) {
			for (int y = 1; y <= b.getHeight(); y++) {
				copyOntoBoard(b.getSquare(x, y));
			}
		}
	}

	public BoardPR(char blackGap, char whiteGap) {
		this(8, 8, blackGap, whiteGap);
	}

	public BoardPR(int width, int height, char whiteGap, char blackGap) {
		super(width, height);
		assert (width >= 2) : "Min board width: 2";
		assert (height >= 4) : "Max board height: 4";
		for (int x = 0; x < width; x++) {
			squares[x][height - 2].setOccupier(Colour.BLACK);
			squares[x][1].setOccupier(Colour.WHITE);
		}
		if (blackGap != ARBCHAR) {
			squares[UtilsBoard.alphaCharToIndex(blackGap) - 1][height - 2].setOccupier(Colour.NONE);
		}
		if (whiteGap != ARBCHAR) {
			squares[UtilsBoard.alphaCharToIndex(whiteGap) - 1][1].setOccupier(Colour.NONE);
		}
	}

	public void applyMove(MovePR move) {
		super.applyMove(move);

		if (move.isEnPassant()) {
			UtilsPR.getBoardSquareBefore(move.getTo(), this).setOccupier(Colour.NONE);
		}
	}

	public void unapplyMove(MovePR move) {
		super.unapplyMove(move);

		Colour movedPlayer = move.getTo().getOccupier();

		if (move.isEnPassant()) {
			getBoardSquare(UtilsPR.getBoardSquareBefore(move.getTo(), this)).setOccupier(movedPlayer.opposite());
		} else if (move.isCapture()) {
			getBoardSquare(move.getTo()).setOccupier(movedPlayer.opposite());
		}
	}
}
