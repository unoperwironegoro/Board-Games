package uno.perwironegoro.boardgames.pawnrace;

import java.util.Scanner;

import uno.perwironegoro.boardgames.Colour;
import uno.perwironegoro.boardgames.Square;
import uno.perwironegoro.boardgames.pawnrace.ai.BotHPR;
import uno.perwironegoro.boardgames.pawnrace.ai.BotPR;
import uno.perwironegoro.boardgames.pawnrace.ai.Heuristic;
import uno.perwironegoro.boardgames.pawnrace.ai.UtilsAI;

public class PawnRace {
	private static final Heuristic defaultAI = Heuristic.getHeuristic("Max");

	private static PlayerPR player1;
	private static PlayerPR player2;
	private static PlayerPR currentPlayer;
	private static Heuristic h1 = defaultAI;
	private static Heuristic h2 = defaultAI;
	private static BoardPR board;
	private static GamePR game;

	public static void main(String[] args) {
		setUpFromArgs(args);

		Scanner sc = new Scanner(System.in);

		gameloop:
			for(;;) {
				Colour result = null;
				turnloop:
					while(result == null) {
						board.display();

						System.out.println("It is " 
								+ currentPlayer.getColour().toString() + "'s turn");

						if(currentPlayer.isComputer()) {
							long lastTime = System.nanoTime();
							MovePR m = currentPlayer.chooseMove();
							long nowTime = System.nanoTime();
							double timeTaken = (nowTime - lastTime) / 1E9d;
							System.out.println(m.getSAN() + " (" + timeTaken + "s)");
							System.out.println();
							game.applyMove(m);
						}
						else {
							//TODO extract out command/input logic
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
											+ UtilsAI.evaluateBoard(board, game.getLastMove(), 
													currentPlayer.getColour(), Colour.WHITE, h1)
											+ ", B: "
											+ UtilsAI.evaluateBoard(board, game.getLastMove(), 
													currentPlayer.getColour(), Colour.BLACK, h2));
									continue turnloop;
								} else if(input.toLowerCase().equals("evaluatepawn")) {
									String sq = sc.next();
									Square evalSquare = UtilsPR.SANtoSquare(sq, Colour.NONE);

									if(evalSquare != null) {
										Colour c = board.getBoardSquare(evalSquare).getOccupier();
										if(c != Colour.NONE) {
											for(Heuristic h : Heuristic.hs)
												System.out.println(h.getName() + ":: " + sq + ": "
														+ UtilsAI.evaluatePawn(evalSquare, board, game.getLastMove(), 
																currentPlayer.getColour(), c, h, true));
										}
									}
									continue turnloop;
								} else if(input.toLowerCase().equals("validmoves")) {
									for(MovePR vm : UtilsPR.getAllValidMoves(board, currentPlayer.getColour(), game.getLastMove())) {
										System.out.println(vm.getSAN());
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

				System.out.println();
				System.out.println("Do you want to play again? <y/n>");

				if(!sc.next().toLowerCase().equals("y")) {
					System.out.println("Thanks for playing Pawn Race!");
					break gameloop;
				}

				System.out.println("What are the new arguments?");

				args = new String[]{sc.next(), sc.next(), sc.next(), 
						sc.next(), sc.next(), sc.next()};
				setUpFromArgs(args); //TODO Make this bulletproof
			}
		sc.close();
	}

	private static void setUpFromArgs(String[] args) {
		if(args.length == 6 && args[0].equals("C") && args[1].equals("C")) {
			setUpBoardGame(args[2].toLowerCase().charAt(0), args[3].toLowerCase().charAt(0));
			Heuristic h1st = Heuristic.getHeuristic(args[4].toLowerCase());
			Heuristic h2nd = Heuristic.getHeuristic(args[5].toLowerCase());
			setUpPlayers(true, true, h1st, h2nd);
		} else if(args.length == 5
				&& ((args[0].equals("P") && args[1].equals("C"))
						|| (args[0].equals("C") && args[1].equals("P")))) {
			setUpBoardGame(args[2].toLowerCase().charAt(0), args[3].toLowerCase().charAt(0));
			Heuristic h = Heuristic.getHeuristic(args[4].toLowerCase());
			if(args[0].equals("C")) {
				setUpPlayers(true, false, h, null);
			}
			else if(args[1].equals("C")) {
				setUpPlayers(false, true, null, h);
			}
		}
		else if(args.length == 4 
				&& (args[0].equals("P") || args[0].equals("C"))
				&& (args[1].equals("P") || args[1].equals("C"))) {
			setUpBoardGame(args[2].toLowerCase().charAt(0), args[3].toLowerCase().charAt(0));
			setUpPlayers(args[0].equals("C"), args[1].equals("C"), null, null);
		} else {
			System.out.println("Arguments: <P/C> <P/C> <A..H> <A..H> [AIName] [AIName]: "
					+ "White, Black, GapWhite, GapBlack; "
					+ "Last parameters only if AI is to be specified, one per computer player ");
			return;
		}
	}

	private static void setUpBoardGame(char wGap, char bGap) {
		board = new BoardPR(wGap, bGap);
		game = new GamePR(board, Colour.WHITE);
	}

	public static void setUpPlayers(boolean p1isCPU, boolean p2isCPU, Heuristic h1st, Heuristic h2nd) {		
		BotPR botBrain1 = null;
		BotPR botBrain2 = null;

		h1 = h1st;
		h2 = h2nd;

		if(p1isCPU) {
			if(h1 == null) {
				h1 = defaultAI;
				System.out.println("Bot not found. " + defaultAI.getName() + " is playing as White");
			} else {
				System.out.println(h1.getName() + " is playing as White");
			}
			botBrain1 = new BotHPR(board, h1);
		}

		if(p2isCPU) {
			if(h2 == null) {
				h2 = defaultAI;
				System.out.println("Bot not found. " + defaultAI.getName() + " is playing as Black");
			} else {
				System.out.println(h2.getName() + " is playing as Black");
			}
			botBrain2 = new BotHPR(board, h2);
		}

		player1 = new PlayerPR(game, board, Colour.WHITE, botBrain1);
		player2 = new PlayerPR(game, board, Colour.BLACK, botBrain2);

		currentPlayer = player1;

		System.out.println();
	}
}
