package uno.perwironegoro.boardgames.pawnrace;

import uno.perwironegoro.boardgames.Colour;
import uno.perwironegoro.boardgames.Symbols;
import uno.perwironegoro.boardgames.pawnrace.MovePR.MoveType;

public class SymbolsPR implements Symbols{
	public static final String
	BLACK = "B",
	WHITE = "W",
	NONE = ".",
	STALEMATE = "S",
	
	CAPTURESYMBOL = "x",
	MOVESYMBOL = "-";
	
	public String symbolOf(Colour c){
		switch(c) {
		case BLACK: return BLACK;
		case WHITE: return WHITE;
		default: return NONE;
		}
	}
	
	public static String winSymbolOf(Colour c){
		switch(c) {
		case BLACK: return BLACK;
		case WHITE: return WHITE;
		default: return STALEMATE;
		}
	}
	
	public static String winMessageOf(Colour c){
		switch(c) {
		case BLACK: return "Black wins!";
		case WHITE: return "White wins!";
		default: return "It's a stalemate!";
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
