package uno.perwironegoro.boardgames.pawnrace;

import uno.perwironegoro.boardgames.Colour;
import uno.perwironegoro.boardgames.pawnrace.MovePR.MoveType;

public class SymbolsPR {
	public static final String
	BLACK = "B",
	WHITE = "W",
	NONE = ".",
	
	CAPTURESYMBOL = "x",
	MOVESYMBOL = "-";
	
	public static String symbolOf(Colour c){
		switch(c) {
		case BLACK: return BLACK;
		case WHITE: return WHITE;
		default: return NONE;
		}
	}
	
	public static boolean isCaptureSymbol(String s) {
		return s.equals(CAPTURESYMBOL);
	}
	
	public static boolean isMoveSymbol(String s) {
		return s.equals(MOVESYMBOL);
	}
	
	public static String symbolOf(MoveType mt) {
		switch(mt) {
		case DOUBLEMOVE:
		case SINGLEMOVE: return MOVESYMBOL;
		case ENPASSANT:
		case CAPTURE: return CAPTURESYMBOL;
		default: return null;
		}
	}
}
