package uno.perwironegoro.boardgames.pawnrace.ai;

import uno.perwironegoro.boardgames.Colour;
import uno.perwironegoro.boardgames.Square;
import uno.perwironegoro.boardgames.pawnrace.BoardPR;
import uno.perwironegoro.boardgames.pawnrace.MovePR;
import uno.perwironegoro.boardgames.pawnrace.UtilsPR;

public class UtilsAI {

	/* Evaluates a given player's score based on the current position
	 * of their pawns on the board
	 */
	public static int evaluateBoard(BoardPR b, MovePR lastMove, Colour nextPlayer, Colour c, Heuristic h) {
		int acc = 0;

		for(Square pawnSquare : UtilsPR.getAllPawns(b, c)) {
			if(UtilsPR.isEndSquare(pawnSquare, b, c)) {
				acc += (int) 1E5; // Not max int because of possible overflow
				continue;
			}

			acc += evaluatePawn(pawnSquare, b, lastMove, nextPlayer, c, h);
		}
		return acc;
	}

	public static int evaluatePawn(Square pawnSquare, BoardPR b, MovePR lastMove, 
			Colour nextPlayer, Colour c, Heuristic h) {
		return evaluatePawn(pawnSquare, b, lastMove, nextPlayer, c, h, false);
	}
	
	public static int evaluatePawn(Square pawnSquare, BoardPR b, MovePR lastMove, 
			Colour nextPlayer, Colour c, Heuristic h, boolean debug) {

		int moveDir = UtilsPR.moveDirection(c);

		if(UtilsPR.isStartSquare(pawnSquare, b, c.opposite())) {
			return (int) 1E4;
		}

		//========= Score multipliers for a pawn's current advancement ==========================
		int squaresAdvanced = UtilsPR.squaresAdvanced(pawnSquare, b);
		int moveScore = squaresAdvanced;// * squaresAdvanced;

		boolean enemyOnFile = false;
		boolean enemyOnFileL = false;
		boolean enemyOnFileR = false;
		boolean isPassed = false;

		int yBound = c == Colour.WHITE? b.getHeight() - pawnSquare.getY() 
				: pawnSquare.getY() - 1;

		for(int dy = 1; dy < yBound; dy++) {
			int yo = dy * moveDir;
			if(pawnSquare.getX() > 1) {
				if(b.getBoardSquareRelative(pawnSquare, -1, yo).isOccupiedBy(c.opposite())) {
					enemyOnFileL = true;
				}
			}
			if(pawnSquare.getX() < b.getWidth()) {
				if(b.getBoardSquareRelative(pawnSquare, 1, yo).isOccupiedBy(c.opposite())) {
					enemyOnFileR = true;
				}
			}

			if(b.getBoardSquareRelative(pawnSquare, 0, yo).isOccupiedBy(c.opposite())) {
				enemyOnFile = true;
			}
		}

		//Passed
		if(!enemyOnFileL && !enemyOnFile && !enemyOnFileR) {
			moveScore *= h.passed;
			isPassed = true;

			int mostEnemyAdvanced = 0;
			for(Square enemyPawnSquare : UtilsPR.getAllPawns(b, c.opposite())) {
				mostEnemyAdvanced = Math.max(mostEnemyAdvanced, 
						UtilsPR.squaresAdvanced(enemyPawnSquare, b));
			}
			if(squaresAdvanced > mostEnemyAdvanced 
					|| (squaresAdvanced == mostEnemyAdvanced && nextPlayer == c)) {
				moveScore += (squaresAdvanced - mostEnemyAdvanced + 1) * 1E3;
			}
		}
		//One enemy pawn adj
		else if((!enemyOnFileL && !enemyOnFile && enemyOnFileR)
				|| (enemyOnFileL && !enemyOnFile && !enemyOnFileR)) {
			moveScore *= h.pastEdge;
		}
		//On one edge
		else if((!enemyOnFileL && enemyOnFile && enemyOnFileR)
				|| (enemyOnFileL && enemyOnFile && !enemyOnFileR)) {
			moveScore *= h.onEdge;

		}
		//locked
		else if(!enemyOnFileL && enemyOnFile && !enemyOnFileR) {
			return 0; // Can't contribute to victory - all points are a result of being a hindrance
		} 
		//between edges
		else if(enemyOnFileL && !enemyOnFile && enemyOnFileR) {
			moveScore *= h.betweenEdge;

		} else /*if(enemyOnFileL && enemyOnFile && enemyOnFileR)*/ {
			//don't multiply
		}

		//========= Extra score / no score for pawn based on current threats ======================			
		int protectScore = 0;

		boolean isThreatened = false;
		boolean isProtected = false;

		boolean isEPThreatened = false;
		boolean isEPProtected = false;

		if(pawnSquare.getX() > 1) {
			if(lastMove != null && lastMove.isDoubleMove() && lastMove.getTo().sameCoordsAs(pawnSquare)
					&& b.getBoardSquareRelative(pawnSquare, -1, 0).isOccupiedBy(c.opposite())) {
				isEPThreatened = true;
				//Protection lasts for one move only.
				if(b.getBoardSquareRelative(pawnSquare, -1, -2*moveDir).isOccupiedBy(c)) {
					isEPProtected = true;
				}
			}

			if(b.getBoardSquareRelative(pawnSquare, -1, moveDir).isOccupiedBy(c.opposite())) {
				isThreatened = true;
			} else if(b.getBoardSquareRelative(pawnSquare, -1, moveDir).isOccupiedBy(c)) {
				protectScore += h.protectScore;
			}
			if(b.getBoardSquareRelative(pawnSquare, -1, -moveDir).isOccupiedBy(c)) {
				isProtected = true;
			}
		}

		if(pawnSquare.getX() < b.getWidth()) {
			if(lastMove != null && lastMove.isDoubleMove() && lastMove.getTo().sameCoordsAs(pawnSquare)
					&& b.getBoardSquareRelative(pawnSquare, 1, 0).isOccupiedBy(c.opposite())) {
				isEPThreatened = true;
				//Protection lasts for one move only.
				if(b.getBoardSquareRelative(pawnSquare, 1, -2*moveDir).isOccupiedBy(c)) {
					isEPProtected = true;
				}
			}

			if(b.getBoardSquareRelative(pawnSquare, 1, moveDir).isOccupiedBy(c.opposite())) {
				isThreatened = true;
			} else if(b.getBoardSquareRelative(pawnSquare, 1, moveDir).isOccupiedBy(c.opposite())) {
				protectScore += h.protectScore;
			}
			if(b.getBoardSquareRelative(pawnSquare, 1, -moveDir).isOccupiedBy(c)) {
				isProtected = true;
			}
		}

		int pawnScore = h.pawnExists;
		if(isPassed) {
			pawnScore *= h.passed;
		}
		
		pawnScore += moveScore + protectScore;

		if(((isThreatened && !isProtected) || (isEPThreatened && !isEPProtected)) 
				&& nextPlayer != c) {
			pawnScore = 0;
		}
		
		if(debug) {
			System.out.println("L M R: " + enemyOnFileL + " " + enemyOnFile 
					+ " " + enemyOnFileR);
			System.out.println("moveScore: " + moveScore);
			System.out.println("protectScore: " + protectScore);
			System.out.println("pawnScore: " + pawnScore);
			System.out.println("isThreatened: " + isThreatened);
			System.out.println("isProtected: " + isProtected);
			System.out.println("isEPThreatened: " + isEPThreatened);
			System.out.println("isEPProtected: " + isEPProtected);
		}

		return pawnScore;
	}
}
