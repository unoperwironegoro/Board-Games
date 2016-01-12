package uno.perwironegoro.boardgames;

public class UtilsBoard {
	public static char indexToStringLower(int index) {
		return (char)(index - 1 + 'a');
	}

	public static char indexToStringUpper(int index) {
		return (char)(index + 'A');
	}

	//Turns the corresponding character from an alphaindex to
	// the playing board index, (pivot at 1)
	public static int alphaCharToIndex(char c) {
		return c - 'a' + 1;
	}
	
	//Like above, but takes it from a numerical character
	public static int numCharToIndex(char c) {
		return c - '1' + 1;
	}
}
