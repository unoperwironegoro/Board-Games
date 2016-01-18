package uno.perwironegoro.boardgames.pawnrace.testing;

import uno.perwironegoro.boardgames.Colour;
import uno.perwironegoro.boardgames.pawnrace.BoardPR;
import uno.perwironegoro.boardgames.pawnrace.GamePR;
import uno.perwironegoro.boardgames.pawnrace.PlayerPR;
import uno.perwironegoro.boardgames.pawnrace.SymbolsPR;
import uno.perwironegoro.boardgames.pawnrace.ai.BotHPR;
import uno.perwironegoro.boardgames.pawnrace.ai.Heuristic;

public class TestAIPRs {
	private static final char[] chars = {'a','b','c','d','e','f','g','h'};

	public static void main(String[] args) {
		System.out.println("=====Running the AI tournament!=====");
//		String[] competitors = new String[Heuristic.hs.length]; //TODO display results in a table
//		String[] competitorsInitials = new String[Heuristic.hs.length];
		
		for(int i = 0; i < Heuristic.hs.length; i++) {
			for(int j = i + 1; j < Heuristic.hs.length; j++) {	
				System.out.println(Heuristic.hs[i].getName() 
						+ " vs " + Heuristic.hs[j].getName());
				Colour[] wScores = new Colour[8 * 8];
				Colour[] bScores = new Colour[8 * 8];

				for(int k = 0; k < chars.length; k++) {
					for(int l = 0; l < chars.length; l++) {
						wScores[8 * k + l] = play(Heuristic.hs[i], Heuristic.hs[j], chars[k], chars[l]);
						bScores[8 * k + l] = play(Heuristic.hs[j], Heuristic.hs[i], chars[k], chars[l]);

						System.out.print(".");
					}
				}
				System.out.println();

				System.out.println("AAAAAAAA"
						+ "BBBBBBBB"
						+ "CCCCCCCC"
						+ "DDDDDDDD"
						+ "EEEEEEEE"
						+ "FFFFFFFF"
						+ "GGGGGGGG"
						+ "HHHHHHHH");
				System.out.println("ABCDEFGH"
						+ "ABCDEFGH"
						+ "ABCDEFGH"
						+ "ABCDEFGH"
						+ "ABCDEFGH"
						+ "ABCDEFGH"
						+ "ABCDEFGH"
						+ "ABCDEFGH");

				for(int k = 0; k < wScores.length; k++) {
					System.out.print(SymbolsPR.winSymbolOf(wScores[k]));
				}
				System.out.println();

				for(int k = 0; k < wScores.length; k++) {
					System.out.print(SymbolsPR.winSymbolOf(bScores[k]));
				}
				System.out.println();
				System.out.println("");

				int h1wins = 0;
				int h2wins = 0;
				int draws  = 0;
				
				for(int k = 0; k < wScores.length; k++) {
					switch(wScores[k]) {
					case WHITE: h1wins++; break;
					case BLACK: h2wins++; break;
					case NONE: draws++; break;
					}
					switch(bScores[k]) {
					case WHITE: h2wins++; break;
					case BLACK: h1wins++; break;
					case NONE: draws++; break;
					}
				}
				
				System.out.println(Heuristic.hs[i].getName() + " won " + h1wins + " games, "
						+ Heuristic.hs[j].getName()  + " won " + h2wins + " games, and "
						+ draws + " games were drawn.");
				
				System.out.println();
			}
		}
		System.out.println("=====The AI Tournament is over!=====");
	}


	private static Colour play(Heuristic h1, Heuristic h2, char wGap, char bGap) {
		PlayerPR player1;
		PlayerPR player2;
		PlayerPR currentPlayer;
		BoardPR board;
		GamePR game;

		board = new BoardPR(wGap, bGap);
		game = new GamePR(board, Colour.WHITE);

		player1 = new PlayerPR(game, board, Colour.WHITE, new BotHPR(board, h1));
		player2 = new PlayerPR(game, board, Colour.BLACK, new BotHPR(board, h2));
		currentPlayer = player1;

		Colour result = null;
		for(;;) {
			game.applyMove(currentPlayer.chooseMove());

			currentPlayer = currentPlayer == player1?
					player2 : player1;

			result = game.getGameResult();
			if(result != null) {
				return result;
			}
		}
	}
}
