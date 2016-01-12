package uno.perwironegoro.boardgames.pawnrace;

import java.util.Scanner;

import uno.perwironegoro.boardgames.Colour;
import uno.perwironegoro.boardgames.Square;
import uno.perwironegoro.boardgames.pawnrace.ai.BotMaxPR;
import uno.perwironegoro.boardgames.pawnrace.ai.BotPR;
import uno.perwironegoro.boardgames.pawnrace.ai.UtilsAI;

public class PawnRace {
	public static void main(String[] args) {
		PlayerPR player1;
		PlayerPR player2;
		PlayerPR currentPlayer;
		BotPR botBrain1 = null;
		BotPR botBrain2 = null;
		BoardPR board;
		GamePR game;

		//		if(args.length == 2 
		//				&& (args[0].equals("P") || args[0].equals("C")) 
		//				&& args[1].equals("C")) {
		//			botBrain2 = new Bot1PR();
		//			board = new BoardPR(botBrain2.getWhiteGap(), botBrain2.getBlackGap());
		//		} else 
		if(args.length == 4 
				&& (args[0].equals("P") || args[0].equals("C"))
				&& (args[1].equals("P") || args[1].equals("C"))) {
			board = new BoardPR(args[2].toLowerCase().charAt(0), args[3].toLowerCase().charAt(0));
			if(args[0].equals("C")) {
				botBrain1 = new BotMaxPR(board);
			}
			if(args[1].equals("C")) {
				botBrain2 = new BotMaxPR(board);
			}
		} else {
			System.out.println("Arguments: <P/C> <P/C> [A..H] [A..H]: "
					+ "White, Black, GapWhite, GapBlack; "
					+ "Last two parameters only if Black is P");
			return;
		}

		game = new GamePR(board, Colour.WHITE);

		player1 = new PlayerPR(game, board, Colour.WHITE, botBrain1);
		player2 = new PlayerPR(game, board, Colour.BLACK, botBrain2);

		currentPlayer = player1;

		Scanner sc = new Scanner(System.in);

		Colour result = null;
		turnloop:
			while(result == null) {
				board.display();

				System.out.println("It is " 
						+ currentPlayer.getColour().toString() + "'s turn");

				if(currentPlayer.isComputer()) {
					currentPlayer.makeMove();
				}
				else {
					MovePR m = null;
					while(m == null) {
						String input = sc.next();
						if(input.toLowerCase().equals("undo")) {
							System.out.println("How many times do you want to undo?");
							int undos = Math.abs(sc.nextInt());
							while(undos > 0) {
								undos--;
								if(game.unapplyMove()) {
									if(currentPlayer == player1) {
										currentPlayer = player2;
									} else {
										currentPlayer = player1;
									}
								}
							}
							continue turnloop;
						} else if(input.toLowerCase().equals("evaluate")) {
							System.out.println("W: "
									+ UtilsAI.evaluateBoard(board, game.getLastMove(), currentPlayer.getColour(), Colour.WHITE, BotMaxPR.H)
									+ ", B: "
									+ UtilsAI.evaluateBoard(board, game.getLastMove(), currentPlayer.getColour(), Colour.BLACK, BotMaxPR.H));
							continue turnloop;
						} else if(input.toLowerCase().equals("evaluatepawn")) {
							String sq = sc.next();
							Square evalSquare = UtilsPR.SANtoSquare(sq, Colour.NONE);
							
							if(evalSquare != null) {
								Colour c = board.getBoardSquare(evalSquare).getOccupier();
								if(c != Colour.NONE) {
									System.out.println(sq + ": "
											+ UtilsAI.evaluatePawn(evalSquare, board, game.getLastMove(), 
													currentPlayer.getColour(), c, BotMaxPR.H, true));
								}
							}
							continue turnloop;
						} else {
							m = UtilsPR.SANtoValidMove(input, board, 
									game.getLastMove(), currentPlayer.getColour());
							if(m == null) {
								System.out.println("Please enter the SAN for your move");
							}
						}
					}
					game.applyMove(m);
				}

				if(currentPlayer == player1) {
					currentPlayer = player2;
				} else {
					currentPlayer = player1;
				}

				result = game.getGameResult();
			}

		board.display();

		switch(result) {
		case WHITE:
			System.out.println("White wins!");
			break;
		case BLACK:
			System.out.println("Black wins!");
			break;
		default:
			System.out.println("It's a Stalemate!");
			break;
		}
		sc.close();
	}
}
