package uno.perwironegoro.boardgames.pawnrace;

import java.util.Scanner;

public class TestBoardPR {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
		System.out.println("Enter the board width");
		int w = sc.nextInt();
		System.out.println("Enter the board height");
		int h = sc.nextInt();
		System.out.println("Enter the black gap character");
		char bG = sc.next().charAt(0);
		System.out.println("Enter the white gap character");
		char wG = sc.next().charAt(0);
		
		BoardPR b = new BoardPR(w, h, bG, wG);
		
		b.display();
		
		sc.close();
	}
}
