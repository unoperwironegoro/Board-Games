package uno.perwironegoro.boardgames;

public abstract class Move {
	protected final Square from, to;
	
	public Move(Square from, Square to) {
		this.from = from;
		this.to = to;
	}
	
	public Square getTo() {
		return to;
	}
	
	public Square getFrom() {
		return from;
	}
	
	public boolean sameAs(Move m) {
		return to.sameAs(m.to) && from.sameAs(m.from);
	}
}
