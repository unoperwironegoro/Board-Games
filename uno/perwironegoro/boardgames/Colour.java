package uno.perwironegoro.boardgames;

public enum Colour {
	BLACK,
	WHITE,
	NONE;
	
	public Colour opposite() {
		switch(this) {
		case BLACK: return WHITE;
		case WHITE: return BLACK;
		default: return NONE;
		}
	}
	
	public String toString() {
		switch(this) {
		case BLACK: return "Black";
		case WHITE: return "White";
		default: return "None";
		}
	}
}
