package uno.perwironegoro.boardgames.pawnrace;

import java.util.ArrayList;

import uno.perwironegoro.boardgames.Board;
import uno.perwironegoro.boardgames.Colour;
import uno.perwironegoro.boardgames.Square;
import uno.perwironegoro.boardgames.UtilsBoard;
import uno.perwironegoro.boardgames.pawnrace.MovePR.MoveType;

public class UtilsPR {
	// Returns the square that the taken piece would have to be
	// for an en passant, relative to the taken square. Also useful
	// for checking an empty square in a double move
	public static Square getBoardSquareBefore(Square to, Board b, Colour c) {
		return b.getBoardSquareRelative(to, 0, -moveDirection(c));
	}

	public static Square getBoardSquareBefore(Square to, Board b) {
		Colour c = to.getOccupier();
		return getBoardSquareBefore(to, b, c);
	}

	public static int moveDirection(Colour c) {
		switch (c) {
		case BLACK:
			return -1;
		case WHITE:
			return 1;
		default:
			return 0;
		}
	}

	// Returns null if the move is invalid
	public static MovePR SANtoValidMove(String san, Board b, MovePR lm, Colour player) {
		if (san == null) {
			return null;
		}

		if (san.length() == 2) {
			Square to = SANtoSquare(san, b, player);
			if (to == null) {
				return null;
			}

			Square sB = getBoardSquareBefore(to, b, player);
			Square sBB = getBoardSquareBefore(sB, b, player);
			if (sB.isOccupiedBy(player)) {
				Square from = new Square(to.getX(), sB.getY(), Colour.NONE);
				return new MovePR(from, to, MoveType.SINGLEMOVE);

			} else if (sBB.isOccupiedBy(player)) {
				Square from = new Square(to.getX(), sBB.getY(), Colour.NONE);
				return new MovePR(from, to, MoveType.DOUBLEMOVE);
			}
		} else if (san.length() == 4) {
			int fromFileIndex = UtilsBoard.alphaCharToIndex(san.charAt(0));
			String mtString = String.valueOf(san.charAt(1));
			Square to = SANtoSquare(san.substring(2, 4), b, player);
			if (to == null) {
				return null;
			}
			if (SymbolsPR.isCaptureSymbol(mtString)
					&& b.getSquare(fromFileIndex, to.getY() - moveDirection(player)).isOccupiedBy(player)) {
				Square from = new Square(fromFileIndex, to.getY() - moveDirection(player), Colour.NONE);
				MovePR mCap = new MovePR(from, to, MoveType.CAPTURE);
				MovePR mEP = new MovePR(from, to, MoveType.ENPASSANT);
				if (moveIsValid(mCap, b, lm)) {
					return mCap;
				} else if (moveIsValid(mEP, b, lm)) {
					return mEP;
				}
			}
		} else if (san.length() == 5) {
			Square from = SANtoSquare(san.substring(0, 2), b, Colour.NONE);
			Square to = SANtoSquare(san.substring(3, 5), b, player);
			if (to == null) {
				return null;
			}
			if (from == null) {
				return null;
			}

			String mtString = String.valueOf(san.charAt(2));
			if (SymbolsPR.isCaptureSymbol(mtString)) {
				MovePR mCap = new MovePR(from, to, MoveType.CAPTURE);
				MovePR mEP = new MovePR(from, to, MoveType.ENPASSANT);
				if (moveIsValid(mCap, b, lm)) {
					return mCap;
				} else if (moveIsValid(mEP, b, lm)) {
					return mEP;
				}
			} else if (SymbolsPR.isMoveSymbol(mtString)) {
				MovePR mSingle = new MovePR(from, to, MoveType.SINGLEMOVE);
				MovePR mDouble = new MovePR(from, to, MoveType.DOUBLEMOVE);
				if (moveIsValid(mSingle, b, lm)) {
					return mSingle;
				} else if (moveIsValid(mDouble, b, lm)) {
					return mDouble;
				}
			}
		}
		return null;
	}

	public static Square SANtoSquare(String san, Board b, Colour occupier) {
		if (san.length() == 2) {
			int x = UtilsBoard.alphaCharToIndex(san.charAt(0));
			int y = UtilsBoard.numCharToIndex(san.charAt(1));
			if (1 <= x && x <= b.getWidth() && 1 <= y && y <= b.getHeight()) {
				return new Square(x, y, occupier);
			}
		}
		return null;
	}

	public static Square SANtoArbitrarySquare(String san, Colour occupier) {
		if (san.length() == 2) {
			int x = UtilsBoard.alphaCharToIndex(san.charAt(0));
			int y = UtilsBoard.numCharToIndex(san.charAt(1));
			return new Square(x, y, occupier);
		}
		return null;
	}

	public static String squareToSAN(Square s) {
		return String.valueOf(UtilsBoard.indexToStringLower(s.getX())) + s.getY();
	}

	public static boolean moveIsValid(MovePR m, Board b, MovePR lm) {
		Colour p = m.getPlayer();
		if (moveIsContained(m, b) && moveInRightDirection(m, p) && movedPawnExists(b, m)) {
			switch (m.getMoveType()) {
			case DOUBLEMOVE:
				return isValidDoubleMove(b, m);
			case SINGLEMOVE:
				return isValidSingleMove(b, m);
			case CAPTURE:
				return isValidCapture(b, m);
			case ENPASSANT:
				return isValidEnPassant(b, m, lm);
			}
		}
		return false;
	}

	private static boolean moveIsContained(MovePR m, Board b) {
		int xf = m.getFrom().getX();
		int xt = m.getTo().getX();
		int yf = m.getFrom().getY();
		int yt = m.getTo().getY();
		return 1 <= xf && xf <= b.getWidth() && 1 <= xt && xt <= b.getWidth() && 1 <= yf && yf <= b.getHeight()
				&& 1 <= yt && yt <= b.getHeight();
	}

	private static boolean movedPawnExists(Board b, MovePR m) {
		Colour p = m.getPlayer();
		return b.getBoardSquare(m.getFrom()).isOccupiedBy(p);
	}

	private static boolean isValidDoubleMove(Board b, MovePR m) {
		Colour p = m.getPlayer();
		return isStartSquare(m.getFrom(), b, p) && fileDifference(m) == 0 && Math.abs(rankDifference(m)) == 2
				&& getBoardSquareBefore(m.getTo(), b).isEmpty() && b.getBoardSquare(m.getTo()).isEmpty();
	}

	private static boolean isValidSingleMove(Board b, MovePR m) {
		return fileDifference(m) == 0 && Math.abs(rankDifference(m)) == 1 && b.getBoardSquare(m.getTo()).isEmpty();
	}

	private static boolean isValidCapture(Board b, MovePR m) {
		Colour p = m.getPlayer();
		return isDiagonalMovement(m) && b.getBoardSquare(m.getTo()).isOccupiedBy(p.opposite());
	}

	private static boolean isValidEnPassant(Board b, MovePR m, MovePR lm) {
		Colour p = m.getPlayer();
		return isDiagonalMovement(m) && b.getBoardSquare(m.getTo()).isEmpty()
				&& getBoardSquareBefore(m.getTo(), b).isOccupiedBy(p.opposite()) && lm.isDoubleMove()
				&& getBoardSquareBefore(lm.getTo(), b).sameCoordsAs(m.getTo());
	}

	private static boolean isDiagonalMovement(MovePR m) {
		return Math.abs(fileDifference(m)) == 1 && Math.abs(rankDifference(m)) == 1;
	}

	private static int fileDifference(MovePR m) {
		return m.getTo().getX() - m.getFrom().getX();
	}

	private static int rankDifference(MovePR m) {
		return m.getTo().getY() - m.getFrom().getY();
	}

	private static boolean moveInRightDirection(MovePR m, Colour p) {
		return Math.signum(rankDifference(m)) == moveDirection(p);
	}

	public static boolean isStartSquare(Square s, Board b, Colour p) {
		return s.getY() == startY(p, b);
	}

	public static boolean isEndSquare(Square s, Board b, Colour p) {
		return s.getY() == endY(p, b);
	}

	public static boolean endRanksNonEmpty(Board board) {
		for (int x = 1; x <= board.getWidth(); x++) {
			if (!board.getSquare(x, board.getHeight()).isEmpty() || !board.getSquare(x, 1).isEmpty()) {
				return true;
			}
		}
		return false;
	}

	public static boolean isStalemate(BoardPR b, Colour p, MovePR lm) {
		return getAllValidMoves(b, p, lm).isEmpty();
	}

	private static boolean hasNoPawns(Board board, Colour player) {
		return UtilsPR.getAllPawns(board, player).isEmpty();
	}

	public static Colour getGameResult(BoardPR board, Colour player, MovePR lastMove) {
		for (int x = 1; x <= board.getWidth(); x++) {
			if (!board.getSquare(x, board.getHeight()).isEmpty() || hasNoPawns(board, Colour.BLACK)) {
				return Colour.WHITE;
			}
			if (!board.getSquare(x, 1).isEmpty() || hasNoPawns(board, Colour.WHITE)) {
				return Colour.BLACK;
			}
		}
		if (UtilsPR.isStalemate(board, player, lastMove)) {
			return Colour.NONE;
		}
		return null;
	}

	public static int squaresAdvanced(Square pawnSquare, BoardPR b) {
		return Math.abs(pawnSquare.getY() - UtilsPR.startY(pawnSquare.getOccupier(), b));
	}

	public static int startY(Colour c, Board b) {
		switch (c) {
		case BLACK:
			return b.getHeight() - 1;
		case WHITE:
			return 2;
		default:
			return 0;
		}
	}

	public static int endY(Colour c, Board b) {
		switch (c) {
		case BLACK:
			return 1;
		case WHITE:
			return b.getHeight();
		default:
			return 0;
		}
	}

	public static ArrayList<Square> getAllPawns(Board b, Colour c) {
		ArrayList<Square> pawnSquares = new ArrayList<Square>();
		for (int x = 1; x <= b.getWidth(); x++) {
			for (int y = 1; y <= b.getHeight(); y++) {
				Square currentSquare = b.getSquare(x, y);
				if (currentSquare.isOccupiedBy(c)) {
					pawnSquares.add(currentSquare);
				}
			}
		}
		return pawnSquares;
	}

	/*
	 * Used by the AI to determine a valid move
	 */
	public static ArrayList<MovePR> getAllValidMoves(BoardPR b, Colour c, MovePR lastMove) {
		return getAllValidMoves(getAllPawns(b, c), b, c, lastMove);
	}

	public static ArrayList<MovePR> getAllValidMoves(ArrayList<Square> pawnSquares, BoardPR b, Colour c,
			MovePR lastMove) {
		ArrayList<MovePR> validMoves = new ArrayList<MovePR>();
				
		if (lastMove != null && lastMove.isDoubleMove()) {
			Square sB = getBoardSquareBefore(lastMove.getTo(), b, c.opposite());
			Square to = new Square(sB.getX(), sB.getY(), c);
			
			Square douS = lastMove.getTo();

			if (douS.getX() > 1 && b.getBoardSquareRelative(douS, -1, 0).isOccupiedBy(c)) {
				Square from = new Square(douS.getX() - 1, douS.getY(), Colour.NONE);
				validMoves.add(new MovePR(from, to, MoveType.ENPASSANT));
			}
			if (douS.getX() < b.getWidth() && b.getBoardSquareRelative(douS, 1, 0).isOccupiedBy(c)) {
				Square from = new Square(douS.getX() + 1, douS.getY(), Colour.NONE);
				validMoves.add(new MovePR(from, to, MoveType.ENPASSANT));
			}
		}

		for (Square pawnSquare : pawnSquares) {
			int moveDir = UtilsPR.moveDirection(c);

			if (1 <= pawnSquare.getY() + moveDir && pawnSquare.getY() + moveDir <= b.getHeight()) {
				if (b.getBoardSquareRelative(pawnSquare, 0, moveDir).isEmpty()) {
					if (UtilsPR.isStartSquare(pawnSquare, b, c)
							&& b.getBoardSquareRelative(pawnSquare, 0, 2 * moveDir).isEmpty()) {
						MovePR m = new MovePR(new Square(pawnSquare.getX(), pawnSquare.getY(), Colour.NONE),
								new Square(pawnSquare.getX(), pawnSquare.getY() + 2 * moveDir, c), MoveType.DOUBLEMOVE);
						validMoves.add(m);
					}
					MovePR m = new MovePR(new Square(pawnSquare.getX(), pawnSquare.getY(), Colour.NONE),
							new Square(pawnSquare.getX(), pawnSquare.getY() + moveDir, c), MoveType.SINGLEMOVE);
					validMoves.add(m);
				}

				if (pawnSquare.getX() > 1
						&& b.getBoardSquareRelative(pawnSquare, -1, moveDir).isOccupiedBy(c.opposite())) {
					MovePR m = new MovePR(new Square(pawnSquare.getX(), pawnSquare.getY(), Colour.NONE),
							new Square(pawnSquare.getX() - 1, pawnSquare.getY() + moveDir, c), MoveType.CAPTURE);
					validMoves.add(m);
				}

				if (pawnSquare.getX() < b.getWidth()
						&& b.getBoardSquareRelative(pawnSquare, 1, moveDir).isOccupiedBy(c.opposite())) {
					MovePR m = new MovePR(new Square(pawnSquare.getX(), pawnSquare.getY(), Colour.NONE),
							new Square(pawnSquare.getX() + 1, pawnSquare.getY() + moveDir, c), MoveType.CAPTURE);
					validMoves.add(m);
				}
			}
		}

		return validMoves;
	}
}
