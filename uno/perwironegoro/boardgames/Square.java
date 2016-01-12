package uno.perwironegoro.boardgames;

public class Square {
	private Colour occupier;
	private final int x, y;
	
	public Square(int x, int y, Colour c) {
		this(x, y);
		this.occupier = c;
	}
	
	public Square(int x, int y) {
		this.x = x;
		this.y = y;
		this.occupier = Colour.NONE;
	}

	public Colour getOccupier() {
		return occupier;
	}
	
	public boolean isEmpty() {
		return occupier == Colour.NONE;
	}

	public void setOccupier(Colour occupier) {
		this.occupier = occupier;
	}

	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}

	public boolean isOccupiedBy(Colour p) {
		return occupier == p;
	}
	
	public boolean sameCoordsAs(Square s) {
		return x == s.getX() && y == s.getY();
	}
	
	public boolean sameAs(Square s) {
		return x == s.x && y == s.y && occupier == s.occupier;
	}
}
