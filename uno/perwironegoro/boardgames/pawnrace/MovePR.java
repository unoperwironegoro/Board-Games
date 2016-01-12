package uno.perwironegoro.boardgames.pawnrace;

import uno.perwironegoro.boardgames.Colour;
import uno.perwironegoro.boardgames.Move;
import uno.perwironegoro.boardgames.Square;
import uno.perwironegoro.boardgames.UtilsBoard;

public class MovePR extends Move {
	
	private final MoveType moveType;
	
	public MovePR(Square from, Square to, MoveType moveType) {
		super(from, to);
		this.moveType = moveType;
	}
	
	public MoveType getMoveType() {
		return moveType;
	}
	
	public boolean isSingleMove() {
		return moveType == MoveType.SINGLEMOVE;
	}
	
	public boolean isDoubleMove() {
		return moveType == MoveType.DOUBLEMOVE;
	}
	
	public boolean isCapture() {
		return moveType == MoveType.CAPTURE;
	}

	public boolean isEnPassant() {
		return moveType == MoveType.ENPASSANT;
	}
	
	public String getSAN() {
		switch(moveType) {
		case SINGLEMOVE:
			return squareToSAN(to);
		case DOUBLEMOVE:
			return squareToSAN(to);
		case CAPTURE:
		case ENPASSANT:
			return String.valueOf(UtilsBoard.indexToStringLower(from.getX()))
					+ SymbolsPR.symbolOf(moveType)
					+ squareToSAN(to);
		}
		return squareToSAN(from)
				+ SymbolsPR.symbolOf(moveType)
				+ squareToSAN(to);
	}
	
	public static String squareToSAN(Square s) {
		return String.valueOf(UtilsBoard.indexToStringLower(s.getX())) + s.getY();
	}
	
	public Colour getPlayer() {
		return to.getOccupier();
	}
	
	public enum MoveType {
		SINGLEMOVE,
		DOUBLEMOVE,
		CAPTURE,
		ENPASSANT;
	}
}
