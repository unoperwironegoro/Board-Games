package uno.perwironegoro.boardgames.pawnrace;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JTextField;

import uno.perwironegoro.boardgames.Colour;

public class BoardJPanelPR extends JPanel implements MouseListener {
	private static final long serialVersionUID = 1L;
	private static final int WIDTH = 16 * 32, HEIGHT = 16 * 32;
	private static final int ARBNUM = -165;
	private BoardPR b;
	private JTextField jtfMove;
	private int fromX, fromY;
	private ArrayList<MovePR> validMoves;

	public BoardJPanelPR(JTextField jtfMove) {
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		this.jtfMove = jtfMove;
		validMoves = new ArrayList<MovePR>(); 
		fromX = ARBNUM;
		fromY = ARBNUM;
	}

	public void setBoard(BoardPR b) {
		this.b = b;
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		int sqWidth = getWidth() / b.getWidth();
		int sqHeight = getHeight() / b.getHeight();

		for (int x = 1; x <= b.getWidth(); x++) {
			for (int y = 1; y <= b.getHeight(); y++) {
				if ((x + y) % 2 == 0) {
					g.drawImage(PawnRace.BSQUARE, (x - 1) * sqWidth, (b.getHeight() - y) * sqHeight, sqWidth, sqHeight,
							null);
				} else {
					g.drawImage(PawnRace.WSQUARE, (x - 1) * sqWidth, (b.getHeight() - y) * sqHeight, sqWidth, sqHeight,
							null);
				}

				if (x == fromX && y == fromY) {
					g.drawImage(PawnRace.SSQUARE, (x - 1) * sqWidth, (b.getHeight() - y) * sqHeight, sqWidth, sqHeight,
							null);
				}

				Colour c = b.getSquare(x, y).getOccupier();
				switch (c) {
				case WHITE:
					g.drawImage(PawnRace.WPAWN, (x - 1) * sqWidth, (b.getHeight() - y) * sqHeight, sqWidth, sqHeight,
							null);
					break;
				case BLACK:
					g.drawImage(PawnRace.BPAWN, (x - 1) * sqWidth, (b.getHeight() - y) * sqHeight, sqWidth, sqHeight,
							null);
					break;
				default:
					break;
				}
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (fromX == ARBNUM && fromY == ARBNUM) {
			int x1 = e.getX();
			int y1 = e.getY();
			int sqX = x1 / (getWidth() / b.getWidth()) + 1;
			int sqY = b.getHeight() - (y1 / (getHeight() / b.getHeight()));
			
			for(MovePR m : validMoves) {
				if(sqY == m.getFrom().getY() && sqX == m.getFrom().getX()) {
					fromX = sqX;
					fromY = sqY;
					
					repaint();
					break;
				}
			}
		} else {
			int x2 = e.getX();
			int y2 = e.getY();
			
			int toX = x2 / (getWidth() / b.getWidth()) + 1;
			int toY = b.getHeight() - (y2 / (getHeight() / b.getHeight()));
			
			for(MovePR m : validMoves) {
				if(fromY == m.getFrom().getY() && fromX == m.getFrom().getX()
						&& toY == m.getTo().getY() && toX == m.getTo().getX()) {
					String san = m.getSAN();
					jtfMove.setText(san);
					break;
				}
			}
			
			fromX = ARBNUM;
			fromY = ARBNUM;
			
			repaint();
		}

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
	}
	
	public void setValidMoves(ArrayList<MovePR> validMoves) {
		this.validMoves = validMoves;
	}

}
