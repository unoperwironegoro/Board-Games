package uno.perwironegoro.boardgames.pawnrace.ai;

import uno.perwironegoro.boardgames.Colour;
import uno.perwironegoro.boardgames.Square;
import uno.perwironegoro.boardgames.pawnrace.BoardPR;
import uno.perwironegoro.boardgames.pawnrace.UtilsPR;

public class UtilsAI {

	public static int evaluateBoard(BoardPR b, Colour nextPlayer, Colour c, Heuristic h) {
		int acc = 0;
		int moveDir = UtilsPR.moveDirection(c);

		for(Square pawnSquare : UtilsPR.getAllPawns(b, c)) {
			if(UtilsPR.isEndSquare(pawnSquare, b, c)) {
				acc += (int) 1E5; // Not max int because of possible overflow
				continue;
			}

			if(UtilsPR.isStartSquare(pawnSquare, b, c.opposite())) {
				acc += (int) 1E4;
				continue;
			}

			int squaresAdvanced = UtilsPR.squaresAdvanced(pawnSquare, b);
			int moveScore = squaresAdvanced;// * squaresAdvanced;
			int protectScore = 0;

			boolean enemyOnFile = false;
			boolean enemyOnFileL = false;
			boolean enemyOnFileR = false;

			boolean isThreatened = false;
			boolean isProtected = false;

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

			if(pawnSquare.getX() > 1) {
				if(b.getBoardSquareRelative(pawnSquare, -1, moveDir).isOccupiedBy(c.opposite())) {
					isThreatened = true;
				} else if(b.getBoardSquareRelative(pawnSquare, -1, moveDir).isOccupiedBy(c)) {
					protectScore += 5;
				}
				if(b.getBoardSquareRelative(pawnSquare, -1, -moveDir).isOccupiedBy(c)) {
					isProtected = true;
				}
			}

			if(pawnSquare.getX() < b.getWidth()) {
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
			
			//Passed
			if(!enemyOnFileL && !enemyOnFile && !enemyOnFileR) {
				moveScore *= h.passed;
				
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
				continue; // Can't contribute to victory - all points are a result of being a hindrance
			} 
			//between edges
			else if(enemyOnFileL && !enemyOnFile && enemyOnFileR) {
				moveScore *= h.betweenEdge;

			} else /*if(enemyOnFileL && enemyOnFile && enemyOnFileR)*/ {
				//don't multiply
			}

			pawnScore += moveScore + protectScore;
			
			if(isThreatened && nextPlayer != c) {
				pawnScore = isProtected? pawnScore : 0;
			}
			
			acc += pawnScore;
		}
		return acc;
	}

}
