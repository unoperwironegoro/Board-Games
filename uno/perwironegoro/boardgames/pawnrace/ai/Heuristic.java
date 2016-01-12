package uno.perwironegoro.boardgames.pawnrace.ai;

public class Heuristic {

	protected int
	betweenEdge,
	pastEdge,
	onEdge,
	passed,
	pawnExists,
	protectScore;

	/**
	 * Multipliers: passed . , pastEdge .'' , onEdge :' , betweenEdge '.',
	 * pawn exists, protecting score
	 */
	public Heuristic(int passed, int pastEdge, int onEdge, int betweenEdge, int pawnExists, int protectScore) {
		this.passed = passed;
		this.pastEdge = pastEdge;
		this.onEdge = onEdge;
		this.betweenEdge = betweenEdge;
		
		this.pawnExists = pawnExists;
		this.protectScore = protectScore;
	}
}
