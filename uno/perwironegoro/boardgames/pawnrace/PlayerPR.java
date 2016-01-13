package uno.perwironegoro.boardgames.pawnrace;

import uno.perwironegoro.boardgames.Colour;
import uno.perwironegoro.boardgames.pawnrace.ai.BotPR;

public class PlayerPR {
	private GamePR game;
	private Colour colour;
	private BotPR botBrain;

	public PlayerPR(GamePR game, BoardPR board, Colour colour, BotPR botBrain) {
		this.game = game;
		this.colour = colour;
		this.botBrain = botBrain;
		if(botBrain != null) {
			this.botBrain.setBoard(board);
		}
	}

	public boolean isComputer() {
		return botBrain != null;
	}
	
	public BotPR getBotBrain() {
		return botBrain;
	}

	public Colour getColour() {
		return colour;
	}

	public void makeMove() {
		long lastTime = System.nanoTime();
		MovePR bestMove = botBrain.getBestMove(game, colour);
		long nowTime = System.nanoTime();
		double timeTaken = (nowTime - lastTime) / 1E9d;
		System.out.println(bestMove.getSAN() + " (" 
		+ timeTaken + "s)");
		System.out.println();
		game.applyMove(bestMove);
	}
}
