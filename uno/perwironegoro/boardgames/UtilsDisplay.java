package uno.perwironegoro.boardgames;

public class UtilsDisplay {
	public static int digits(int n) {
		assert(n >= 0): "the number must be non-negative";
		int count = 0;
		while(n > 0) {
			n /= 10;
			count++;
		}
		return count;
	}
	
	public static String fitInMargin(int marginSize, String label, char padChar) {
		String paddedLabel = "";
		assert(marginSize > 0);
		if(label.length() >= marginSize) {
			paddedLabel = label.substring(0, marginSize - 1);
		} else {
			paddedLabel = label;
			while(paddedLabel.length() < marginSize) {
				paddedLabel += padChar;
			}
		}
		return paddedLabel;
	}
}
