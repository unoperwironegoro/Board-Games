package uno.perwironegoro.boardgames.pawnrace.ai;

public class Heuristic {

	public static final Heuristic[] hs ={ 
			new Heuristic(10, 4, 3, 2, 10, 0, "Max"),
			new Heuristic(10, 6, 5, 2, 10, 0, "Ed"),
			new Heuristic(5, 4, 3, 2, 10, 0, "John"),
			new Heuristic(5, 2, 3, 4, 10, 0, "Jennie"),
			new Heuristic(10, 6, 5, 5, 15, 5, "Terry"),
			new Heuristic(10, 6, 5, 5, 15, 15, "Dave"),
			new Heuristic(10, 3, 5, 5, 15, 15, "Ellie"),
			new Heuristic(1, 1, 1, 1, 5, 0, "Hayley")};

	protected final int
	betweenEdge,
	pastEdge,
	onEdge,
	passed,
	pawnExists,
	protectScore;

	protected final String name;

	/**
	 * Multipliers: passed . , pastEdge .'' , onEdge :' , betweenEdge '.',
	 * pawn exists, protecting score
	 */
	public Heuristic(int passed, int pastEdge, int onEdge, int betweenEdge, int pawnExists, int protectScore, String name) {
		this.passed = passed;
		this.pastEdge = pastEdge;
		this.onEdge = onEdge;
		this.betweenEdge = betweenEdge;

		this.pawnExists = pawnExists;
		this.protectScore = protectScore;

		this.name = name;
	}

	public String getName() {
		return name;
	}

	public static Heuristic getHeuristic(String name) {
		for(Heuristic h : hs) {
			if(name.toLowerCase().equals(h.getName().toLowerCase())) {
				return h;
			}
		}
		return null;
	}
}
