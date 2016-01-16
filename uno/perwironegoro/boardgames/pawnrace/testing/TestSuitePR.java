package uno.perwironegoro.boardgames.pawnrace.testing;

import uno.perwironegoro.boardgames.Colour;
import uno.perwironegoro.boardgames.Square;
import uno.perwironegoro.boardgames.UtilsBoard;
import uno.perwironegoro.boardgames.UtilsTestSuite;
import uno.perwironegoro.boardgames.pawnrace.MovePR;
import uno.perwironegoro.boardgames.pawnrace.UtilsPR;
import uno.perwironegoro.boardgames.pawnrace.MovePR.MoveType;

public class TestSuitePR {
	public static void main(String[] args) {
		System.out.println("Testing");
		testAll();
		System.out.println("Finished!");
	}
	
	public static void testAll() {
		testBoardUtils();
		testMovePR();
		testUtilsPR();
	}
	
	private static void testUtilsPR() {
		testSANtoSquare("a2", Colour.BLACK, new Square(1, 2, Colour.BLACK));
		testSANtoSquare("c3", Colour.WHITE, new Square(3, 3, Colour.WHITE));
		testSANtoSquare("h4", Colour.NONE, new Square(8, 4, Colour.NONE));
		testSANtoSquare("g9", Colour.WHITE, new Square(7, 9, Colour.WHITE));
		testSANtoSquare("z2", Colour.BLACK, new Square(26, 2, Colour.BLACK));
	}

	private static void testMovePR() {
		testSquareToSAN(new Square(1, 1, Colour.BLACK), "a1");
		testSquareToSAN(new Square(1, 1, Colour.WHITE), "a1");
		testSquareToSAN(new Square(1, 1, Colour.NONE), "a1");
		testSquareToSAN(new Square(2, 3, Colour.BLACK), "b3");
		testSquareToSAN(new Square(2, 3, Colour.WHITE), "b3");
		testSquareToSAN(new Square(2, 3, Colour.NONE), "b3");
		testSquareToSAN(new Square(5, 4, Colour.BLACK), "e4");
		testSquareToSAN(new Square(5, 4, Colour.WHITE), "e4");
		testSquareToSAN(new Square(5, 4, Colour.NONE), "e4");
		testSquareToSAN(new Square(8, 8, Colour.BLACK), "h8");
		testSquareToSAN(new Square(8, 8, Colour.WHITE), "h8");
		testSquareToSAN(new Square(8, 8, Colour.NONE), "h8");
		System.out.println(".");
		
		testGetSAN(new MovePR(new Square(1, 1), new Square(1, 2), MoveType.SINGLEMOVE), 
				"a2");
		testGetSAN(new MovePR(new Square(1, 1), new Square(1, 2), MoveType.CAPTURE), 
				"axa2");
		testGetSAN(new MovePR(new Square(2, 5), new Square(2, 8), MoveType.DOUBLEMOVE), 
				"b8");
		testGetSAN(new MovePR(new Square(3, 6), new Square(4, 5), MoveType.CAPTURE), 
				"cxd5");
		testGetSAN(new MovePR(new Square(3, 6), new Square(4, 5), MoveType.ENPASSANT), 
				"cxd5");
		testGetSAN(new MovePR(new Square(8, 6), new Square(26, 26), MoveType.DOUBLEMOVE), 
				"z26");
		System.out.println(".");
	}

	private static void testBoardUtils() {
		testIndexToString(1, 'a');
		testIndexToString(2, 'b');
		testIndexToString(3, 'c');
		testIndexToString(4, 'd');
		testIndexToString(5, 'e');
		testIndexToString(6, 'f');
		testIndexToString(7, 'g');
		testIndexToString(8, 'h');
		System.out.print(".");
	}

	private static void testSANtoSquare(String san, Colour occupier, Square expected) {
		UtilsTestSuite.testSquareEq("SANtoValidMove", UtilsPR.SANtoArbitrarySquare(san, occupier), expected);
	}

	private static void testGetSAN(MovePR m, String expected) {
		UtilsTestSuite.testEq("getSAN", m.getSAN(), expected);
	}
	
	private static void testSquareToSAN(Square s, String expected) {
		UtilsTestSuite.testEq("squareToSAN", UtilsPR.squareToSAN(s), expected);
	}

	private static void testIndexToString(int index, char expected) {
		UtilsTestSuite.test("indexToString", UtilsBoard.indexToStringLower(index), expected);
	}
}
