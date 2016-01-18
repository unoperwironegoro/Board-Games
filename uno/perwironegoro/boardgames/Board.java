package uno.perwironegoro.boardgames;

public abstract class Board {
	//The squares are pivoted from (1,1)!
	protected Square[][] squares;
	
	public Board(int width, int height) {
		squares = new Square[width][height];
		for(int x = 0; x < width; x++) {
			for(int y = 0; y < height; y++) {
				squares[x][y] = new Square(x + 1, y + 1);
			}
		}
	}
	
	public void applyMove(Move move) {
		copyOntoBoard(move.from);
		copyOntoBoard(move.to);
	}
	
	//Override to replace the taken piece
	public void unapplyMove(Move move) {
		getBoardSquare(move.from).setOccupier(move.to.getOccupier());
		getBoardSquare(move.to).setOccupier(Colour.NONE);
	}
	
	public Square getSquare(int x, int y) {
		return squares[x - 1][y - 1];
	}
	
	public Square getBoardSquareRelative(Square s, int xo, int yo) {
		return getSquare(s.getX() + xo, s.getY() + yo);
	}
	
	public Square getBoardSquare(Square s) {
		return getSquare(s.getX(), s.getY());
	}
	
	public void copyOntoBoard(Square s) {
		getBoardSquare(s).setOccupier(s.getOccupier());
	}
	
	public void display(Symbols sym) {
		int marginSize = UtilsDisplay.digits(getHeight()) + 1;
		
		String alphaIndex = UtilsDisplay.fitInMargin(marginSize, "", ' ');
		
		for(int x = 0; x < getWidth(); x++) {
			alphaIndex += UtilsBoard.indexToStringUpper(x) + " ";
		}

		System.out.println(alphaIndex);
		System.out.println("");
		for(int y = getHeight() - 1; y >= 0; y--) {
			System.out.print(UtilsDisplay.fitInMargin(marginSize, String.valueOf(y + 1), ' '));
			
			for(int x = 0; x < getWidth(); x++) {
				System.out.print(sym.symbolOf(squares[x][y].getOccupier()) + " ");
			}
			
			System.out.println(y + 1);
		}
		
		System.out.println("");
		System.out.println(alphaIndex);
		System.out.println("");
	}
	
	public int getWidth() {
		return squares.length;
	}
	
	public int getHeight() {
		return squares[0].length;
	}
}
