import java.awt.Color;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.event.MouseInputListener;

public class Board extends JComponent implements MouseInputListener, ComponentListener {
	private static final long serialVersionUID = 1L;
	private Point[][] points;
	private int size = 12;
	public int editType = 0;

	public Board(int length, int height) {
		addMouseListener(this);
		addComponentListener(this);
		addMouseMotionListener(this);
		setBackground(Color.WHITE);
		setOpaque(true);
		initialize(length, height);
	}

	public void iteration() {
		for (int x = 1; x < points.length - 1; ++x)
			for (int y = 1; y < points[x].length - 1; ++y)
				points[x][y].updateVelocity();

		for (int x = 1; x < points.length - 1; ++x)
			for (int y = 1; y < points[x].length - 1; ++y)
				points[x][y].updatePresure();
		this.repaint();
	}

	public void clear() {
		for (int x = 0; x < points.length; ++x)
			for (int y = 0; y < points[x].length; ++y) {
				points[x][y].clear();
			}
		this.repaint();
	}

	private void initialize(int length, int height) {
		points = new Point[length][height];

		for (int x = 0; x < points.length; ++x)
			for (int y = 0; y < points[x].length; ++y) {
				boolean isOnEdge = (x == 0 || x == points.length - 1 || y == 0 || y == points[x].length - 1);
				points[x][y] = new Point(isOnEdge);
			}

		for (int x = 1; x < points.length - 1; ++x) {
			for (int y = 1; y < points[x].length - 1; ++y) {
				Point point = points[x][y];
				// TODO: add von Neuman neighborhood
				// odd ones are shifted to the right
				if (point.isGuard())
					continue;

				int xw = y % 2 == 0 ? x + 1 : x;

				point.setNeighbors(
						points[xw - 1][y - 1],
						points[xw][y - 1],
						points[x + 1][y],
						points[xw][y + 1],
						points[xw - 1][y + 1],
						points[x - 1][y]);
			}
		}
	}

	protected void paintComponent(Graphics g) {
		if (isOpaque()) {
			g.setColor(getBackground());
			g.fillRect(0, 0, this.getWidth(), this.getHeight());
		}
		g.setColor(Color.GRAY);
		drawNetting(g, size);
	}

	private void drawNetting(Graphics g, int gridSpace) {
		Insets insets = getInsets();
		int firstX = insets.left;
		int firstY = insets.top;
		int lastX = this.getWidth() - insets.right;
		int lastY = this.getHeight() - insets.bottom;

		int x = firstX;
		while (x < lastX) {
			int i = 0;
			while (i * size < lastX) {
				int offset = 0;
				if (i % 2 == 0)
					offset += size / 2;

				g.drawLine(x + offset, i * size, x + offset, i * size + size);

				i++;
			}
			// g.drawLine(x, firstY, x, lastY);
			x += gridSpace;
		}
		
		int y = firstY;
		while (y < lastY) {
			g.drawLine(firstX, y, lastX, y);
			y += gridSpace;
		}

		for (x = 0; x < points.length; ++x) {
			for (y = 0; y < points[x].length; ++y) {
				Point point = points[x][y];
				
				if (point.type == 0){
					g.setColor(new Color(1.0f, 1.0f, 1.0f));
				}
				else if (point.type == 1) {
					float a = 0.5f;
	
					if (point.isGuard())
						a = 1;
	
					g.setColor(new Color(a, a, a, point.color));
				}
				
				else if (point.type == 2){
					g.setColor(new Color(1.0f, 0.0f, 0.0f));
				}
				int x_offset = y % 2 == 1 ? 0 : size / 2;
				g.fillRect((x * size) + 1 + x_offset, (y * size) + 1, (size - 1), (size -
						1));
			}
		}

	}

	public void mouseClicked(MouseEvent e) {
		int y = e.getY() / size;
		int offset = y % 2 == 1 ? 0 : size / 2;
		int x = (e.getX() - offset) / size;
		if ((x < points.length) && (x > 0) && (y < points[x].length) && (y > 0)) {
			if(editType == 1) {
				points[x][y].clicked();
				points[x][y].type = editType;
			}
			else
				points[x][y].type = editType;
			this.repaint();
		}
	}

	public void componentResized(ComponentEvent e) {
		int dlugosc = (this.getWidth() / size) + 1;
		int wysokosc = (this.getHeight() / size) + 1;
		initialize(dlugosc, wysokosc);
	}

	public void mouseDragged(MouseEvent e) {
		int y = e.getY() / size;
		int offset = y % 2 == 1 ? 0 : size / 2;
		int x = (e.getX() - offset) / size;
		if ((x < points.length) && (x > 0) && (y < points[x].length) && (y > 0)) {
			if (editType == 1) {
				points[x][y].clicked();
				points[x][y].type = editType;
			} else {
				points[x][y].type= editType;
			}
			this.repaint();
		}
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void componentShown(ComponentEvent e) {
	}

	public void componentMoved(ComponentEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void mouseMoved(MouseEvent e) {
	}

	public void componentHidden(ComponentEvent e) {
	}

	public void mousePressed(MouseEvent e) {
	}

}
