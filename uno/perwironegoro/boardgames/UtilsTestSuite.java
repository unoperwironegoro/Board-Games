package uno.perwironegoro.boardgames;

public class UtilsTestSuite {
	public static <T> void test(String identifier, T received, T expected) {
		if(received != expected) {
			System.out.println(identifier 
					+ ":: received: " + received 
					+ ", expected: " + expected);
		}
	}
	
	public static <T> void testEq(String identifier, T received, T expected) {
		if(!received.equals(expected)) {
			System.out.println(identifier 
					+ ":: received: " + received 
					+ ", expected: " + expected);
		}
	}
	
	public static void testSquareEq(String identifier, Square received, Square expected) {
		if(received.getX() != expected.getX() || received.getY() != expected.getY()
				|| received.getOccupier() != expected.getOccupier()) {
			System.out.println(identifier 
					+ ":: received: " + received.getX() + " " 
					+ received.getY() + " " + received.getOccupier() 
					+ ", expected: " + received.getX() + " " 
					+ received.getY() + " " + received.getOccupier());
		}
	}
}
