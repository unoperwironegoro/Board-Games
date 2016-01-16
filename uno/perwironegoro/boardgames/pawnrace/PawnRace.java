package uno.perwironegoro.boardgames.pawnrace;

import java.awt.Font;
import java.awt.Image;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import uno.perwironegoro.boardgames.Colour;
import uno.perwironegoro.boardgames.Square;
import uno.perwironegoro.boardgames.pawnrace.ai.BotHPR;
import uno.perwironegoro.boardgames.pawnrace.ai.BotPR;
import uno.perwironegoro.boardgames.pawnrace.ai.Heuristic;
import uno.perwironegoro.boardgames.pawnrace.ai.UtilsAI;

public class PawnRace {
	private static final Heuristic defaultAI = Heuristic.getHeuristic("Max");

	private PlayerPR player1;
	private PlayerPR player2;
	private PlayerPR currentPlayer;
	private Heuristic h1;
	private Heuristic h2;
	private Scanner sc;
	private BoardPR board;
	private GamePR game;
	private Random rand;
	private int delay;

	private BoardJPanelPR gui;
	private JTextField jtfMove, jtfInfo;

	public static final String WINDOWTITLE = "Pawn Race :: Harjuno Perwironegoro";
	public static final String ICONFILENAME = "icon.png";
	public static final String SSQUAREFILENAME = "square_select.png";
	public static final String BSQUAREFILENAME = "square_black.png";
	public static final String WSQUAREFILENAME = "square_white.png";
	public static final String BPAWNFILENAME = "pawn_black.png";
	public static final String WPAWNFILENAME = "pawn_white.png";
	static Image WSQUARE, SSQUARE, BSQUARE, WPAWN, BPAWN;
	private static Image ICON;


	public static void main(String[] args) {
		PawnRace pr = new PawnRace();
		pr.rand = new Random();
		if (args.length > 0 && args[0].equals("gui")) {
			if (args.length > 1) {
				try {
					pr.delay = Integer.parseInt(args[1]);
				} catch (NumberFormatException e) {
					pr.delay = 2000;
				}
			}
			pr.loadImages();
			pr.createGUI();
			pr.setUpFromJOPs();
		} else {
			pr.sc = new Scanner(System.in);
			pr.setUpFromArgs(args);
		}

		for (;;) {
			switch (pr.playGame()) {
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

			if (pr.gui == null) {
				System.out.println();
				System.out.println("Do you want to play again? <y/n>");

				if (!pr.sc.next().toLowerCase().equals("y")) {
					System.out.println("Thanks for playing Pawn Race!");
					pr.sc.close();
					return;
				}

				System.out.println("What are the new arguments?");

				boolean validArgs = false;
				while (!validArgs) {
					args = new String[] { pr.sc.next(), pr.sc.next(), pr.sc.next(), pr.sc.next(), pr.sc.next(),
							pr.sc.next() };
					validArgs = pr.setUpFromArgs(args);
				}
			} else {
				pr.setUpFromJOPs();
			}
		}
	}

	private void loadImages() {
		String pathPrefix = "/uno/perwironegoro/boardgames/resources/";
		try {
			ICON = ImageIO.read(getClass().getResource(pathPrefix + ICONFILENAME));
			WSQUARE = ImageIO.read(getClass().getResource(pathPrefix + WSQUAREFILENAME));
			BSQUARE = ImageIO.read(getClass().getResource(pathPrefix + BSQUAREFILENAME));
			SSQUARE = ImageIO.read(getClass().getResource(pathPrefix + SSQUAREFILENAME));
			WPAWN = ImageIO.read(getClass().getResource(pathPrefix + WPAWNFILENAME));
			BPAWN = ImageIO.read(getClass().getResource(pathPrefix + BPAWNFILENAME));
		} catch (IOException e) {
		}
	}

	private void setUpFromJOPs() {
		String[] gapOptions = { "A", "B", "C", "D", "E", "F", "G", "H" };
		String sqW = null;
		String sqB = null;

		// Supports player vs bot only
		Object[] playerOptions = { "White", "Black (picks gaps)" };
		int p = JOptionPane.showOptionDialog(null, "Will you play as White or Black?", "Finding Game :: Player Setup",
				JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, new ImageIcon(ICON), playerOptions,
				playerOptions[0]);

		if (p == 1) {
			JPanel splitjp1 = new JPanel();
			splitjp1.setLayout(new BoxLayout(splitjp1, BoxLayout.Y_AXIS));

			Font font2 = new Font("SansSerif", Font.BOLD, 20);
			JTextField messagejp = new JTextField();
			messagejp.setText("Where should the gaps be?");
			messagejp.setFont(font2);
			messagejp.setEditable(false);

			JPanel splitjp2top = new JPanel();
			splitjp2top.setLayout(new BoxLayout(splitjp2top, BoxLayout.X_AXIS));
			JComboBox<String> gapW = new JComboBox<String>(gapOptions);
			JComboBox<String> gapB = new JComboBox<String>(gapOptions);
			splitjp2top.add(gapW);
			splitjp2top.add(gapB);

			JPanel splitjp2bottom = new JPanel();
			splitjp2bottom.setLayout(new BoxLayout(splitjp2bottom, BoxLayout.X_AXIS));
			splitjp2bottom.add(new JLabel(new ImageIcon(WPAWN)));
			splitjp2bottom.add(new JLabel(new ImageIcon(WPAWN)));
			splitjp2bottom.add(new JLabel(new ImageIcon(BPAWN)));
			splitjp2bottom.add(new JLabel(new ImageIcon(BPAWN)));

			splitjp1.add(messagejp);
			splitjp1.add(splitjp2bottom);
			splitjp1.add(splitjp2top);

			JOptionPane.showMessageDialog(null, splitjp1, "Board Setup", JOptionPane.PLAIN_MESSAGE);
			sqW = (String) gapW.getSelectedItem();
			sqB = (String) gapB.getSelectedItem();
		} else {
			sqW = gapOptions[rand.nextInt(gapOptions.length)];
			sqB = gapOptions[rand.nextInt(gapOptions.length)];
		}

		setUpBoardGame(sqW.toLowerCase().charAt(0), sqB.toLowerCase().charAt(0));
		setUpPlayers(p == 1, p != 1, Heuristic.hs[rand.nextInt(Heuristic.hs.length)],
				Heuristic.hs[rand.nextInt(Heuristic.hs.length)]);

		gui.setBoard(board);
	}

	private void printInfo(String s) {
		if (gui == null) {
			System.out.println(s);
		} else {
			jtfInfo.setText(s);
		}
	}

	private void createGUI() {
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setTitle(WINDOWTITLE);
		f.setIconImage(ICON);

		JPanel jp = new JPanel();
		jp.setLayout(new BoxLayout(jp, BoxLayout.Y_AXIS));

		JPanel splitjp = new JPanel();
		splitjp.setLayout(new BoxLayout(splitjp, BoxLayout.X_AXIS));

		Font font1 = new Font("SansSerif", Font.PLAIN, 20);

		jtfMove = new JTextField();
		jtfMove.setFont(font1);
		jtfMove.setEditable(false);
		jtfMove.setHorizontalAlignment(JTextField.CENTER);

		Font font2 = new Font("SansSerif", Font.BOLD, 20);

		jtfInfo = new JTextField();
		jtfInfo.setFont(font2);
		jtfInfo.setEditable(false);
		jtfInfo.setHorizontalAlignment(JTextField.CENTER);

		gui = new BoardJPanelPR(jtfMove);
		gui.setBoard(new BoardPR(BoardPR.ARBCHAR, BoardPR.ARBCHAR));

		jp.add(gui);
		jp.addMouseListener(gui);
		jp.add(splitjp);

		splitjp.add(jtfInfo);
		splitjp.add(jtfMove);

		f.add(jp);
		f.pack();
		f.setLocationRelativeTo(null);
		f.setVisible(true);
	}

	private Colour playGame() {
		Colour result = null;
		while (result == null) {

			display();

			printInfo("It is " + currentPlayer.getColour().toString() + "'s turn");

			if (currentPlayer.isComputer()) {
				long lastTime = 0;
				lastTime = System.currentTimeMillis();
				MovePR m = currentPlayer.chooseMove();
				long nowTime = System.currentTimeMillis();
				double timeTakenMillis = nowTime - lastTime;
				double timeTakenSeconds = timeTakenMillis / 1E3d;
				System.out.println(m.getSAN() + " (" + timeTakenSeconds + "s)");
				System.out.println();
				if (delay != 0) {
					try {
						TimeUnit.MILLISECONDS.sleep((long) (500 + rand.nextInt(delay)));

					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				game.applyMove(m);
			} else if (gui == null) {
				// TODO extract out command/input logic
				MovePR m = null;
				while (m == null) {
					System.out.println("Please enter your next move");
					String input = sc.next();

					if (input.toLowerCase().equals("undo")) {
						System.out.println("How many times do you want to undo?");
						int undos = Math.abs(sc.nextInt());
						while (undos > 0) {
							undos--;
							if (game.unapplyMove()) {
								if (currentPlayer == player1) {
									currentPlayer = player2;
								} else {
									currentPlayer = player1;
								}
							}
						}
						continue;
					} else if (input.toLowerCase().equals("evaluate")) {
						System.out.println("W: "
								+ UtilsAI.evaluateBoard(board, game.getLastMove(), currentPlayer.getColour(),
										Colour.WHITE, h1)
								+ ", B: " + UtilsAI.evaluateBoard(board, game.getLastMove(), currentPlayer.getColour(),
										Colour.BLACK, h2));
						continue;
					} else if (input.toLowerCase().equals("evaluatepawn")) {
						String sq = sc.next();
						Square evalSquare = UtilsPR.SANtoSquare(sq, board, Colour.NONE);

						if (evalSquare != null) {
							Colour c = board.getBoardSquare(evalSquare).getOccupier();
							if (c != Colour.NONE) {
								for (Heuristic h : Heuristic.hs)
									System.out
									.println(h.getName() + ":: " + sq + ": " + UtilsAI.evaluatePawn(evalSquare,
											board, game.getLastMove(), currentPlayer.getColour(), c, h, true));
							}
						}
						continue;
					} else if (input.toLowerCase().equals("validmoves")) {
						for (MovePR vm : UtilsPR.getAllValidMoves(board, currentPlayer.getColour(),
								game.getLastMove())) {
							System.out.println(vm.getSAN());
						}
						continue;
					} else {
						m = UtilsPR.SANtoValidMove(input, board, game.getLastMove(), currentPlayer.getColour());
						if (m == null) {
							System.out.println("Please enter the SAN for your move");
						}
					}
				}
				game.applyMove(m);
			} else {
				gui.setValidMoves(UtilsPR.getAllValidMoves(board, currentPlayer.getColour(), game.getLastMove()));

				String promptText = "Your move!";
				jtfMove.setText(promptText);

				MovePR m = null;
				while (m == null) {
					String input = jtfMove.getText();
					m = UtilsPR.SANtoValidMove(input, board, game.getLastMove(), currentPlayer.getColour());
				}
				game.applyMove(m);
			}

			if (currentPlayer == player1) {
				currentPlayer = player2;
			} else {
				currentPlayer = player1;
			}

			result = game.getGameResult();
		}
		display();

		return result;
	}

	private boolean setUpFromArgs(String[] args) {
		// TODO Make this bulletproof
		if (args.length == 6 && args[0].equals("C") && args[1].equals("C")) {
			setUpBoardGame(args[2].toLowerCase().charAt(0), args[3].toLowerCase().charAt(0));
			Heuristic h1st = Heuristic.getHeuristic(args[4].toLowerCase());
			Heuristic h2nd = Heuristic.getHeuristic(args[5].toLowerCase());
			setUpPlayers(true, true, h1st, h2nd);
			return true;
		} else if (args.length == 5
				&& ((args[0].equals("P") && args[1].equals("C")) || (args[0].equals("C") && args[1].equals("P")))) {
			setUpBoardGame(args[2].toLowerCase().charAt(0), args[3].toLowerCase().charAt(0));
			Heuristic h = Heuristic.getHeuristic(args[4].toLowerCase());
			if (args[0].equals("C")) {
				setUpPlayers(true, false, h, null);
				return true;
			} else {
				setUpPlayers(false, true, null, h);
				return true;
			}
		} else if (args.length == 4 && (args[0].equals("P") || args[0].equals("C"))
				&& (args[1].equals("P") || args[1].equals("C"))) {
			setUpBoardGame(args[2].toLowerCase().charAt(0), args[3].toLowerCase().charAt(0));
			setUpPlayers(args[0].equals("C"), args[1].equals("C"), null, null);
			return true;
		} else {
			System.out.println(
					"Arguments: <P/C> <P/C> <A..H> <A..H> [AIName] [AIName]: " + "White, Black, GapWhite, GapBlack; "
							+ "Last parameters only if AI is to be specified, one per computer player ");
			return false;
		}
	}

	public void setUpBoardGame(char wGap, char bGap) {
		board = new BoardPR(wGap, bGap);
		game = new GamePR(board, Colour.WHITE);
	}

	public void setUpPlayers(boolean p1isCPU, boolean p2isCPU, Heuristic h1st, Heuristic h2nd) {
		BotPR botBrain1 = null;
		BotPR botBrain2 = null;

		h1 = h1st;
		h2 = h2nd;

		if (p1isCPU) {
			if (h1 == null) {
				h1 = defaultAI;
				System.out.println("White bot not found.");
				System.out.println(h1.getName() + " is playing as White");
			}
			botBrain1 = new BotHPR(board, h1);
		}

		if (p2isCPU) {
			if (h2 == null) {
				h2 = defaultAI;
				System.out.println("Black bot not found.");
				System.out.println(h2.getName() + " is playing as Black");
			}
			botBrain2 = new BotHPR(board, h2);
		}

		player1 = new PlayerPR(game, board, Colour.WHITE, botBrain1);
		player2 = new PlayerPR(game, board, Colour.BLACK, botBrain2);

		currentPlayer = player1;

		System.out.println();
	}

	private void display() {
		if (gui != null) {
			gui.repaint();
		}
		board.display();
	}
}
