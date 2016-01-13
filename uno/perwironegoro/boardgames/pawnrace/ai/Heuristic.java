package uno.perwironegoro.boardgames.pawnrace.ai;

public class Heuristic {

	public static final Heuristic
	Max = new Heuristic(10, 4, 3, 2, 10, 0, "Max"),
	Edgar = new Heuristic(10, 4, 3, 2, 10, 0, "Edgar");
	
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
		for(Heuristic h : Heuristic.getAllHeuristics()) {
			if(name.equals(h.getName().toLowerCase())) {
				return h;
			}
		}
		return null;
	}
	
	public static Heuristic[] getAllHeuristics() {
		Heuristic[] hs = new Heuristic[2];
		hs[0] = Max;
		hs[1] = Edgar;
		return hs;
	}
}
