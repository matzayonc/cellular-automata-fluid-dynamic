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
	private int sizeH = 20;
	private int sizeW = (int) ((float) sizeH / (Math.sqrt(3) / 2.f));
	public int editType = 0;

	static final float flowRate = 0.1f;

	public Board(int length, int height) {
		addMouseListener(this);
		addComponentListener(this);
		addMouseMotionListener(this);
		setBackground(Color.WHITE);
		setOpaque(true);
		initialize(length, height);
	}

	public void iteration() {
		for (int y = 0; y < points[0].length; ++y)
			points[0][y].spawn(flowRate);

		for (int x = 0; x < points.length; ++x)
			for (int y = 0; y < points[x].length; ++y)
				points[x][y].move();

		for (int x = 1; x < points.length - 1; ++x)
			for (int y = 1; y < points[x].length - 1; ++y)
				points[x][y].update();

		this.repaint();
	}

	public void clear() {
		for (int x = 1; x < points.length - 1; ++x)
			for (int y = 1; y < points[x].length - 1; ++y) {
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

		for (int x = 0; x < points.length; ++x)
			for (int y = 0; y < points[x].length; ++y)
				if(x <= 0 || y <= 0 || x >= points.length - 1 || y >= points[x].length - 1)
					points[x][y].type = 2;
		
		for (int x = 1; x < points.length - 1; ++x) {
			for (int y = 1; y < points[x].length - 1; ++y) {
				Point point = points[x][y];
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
		drawNetting(g);
	}

	private void drawNetting(Graphics g) {
		Insets insets = getInsets();
		int firstX = insets.left;
		int firstY = insets.top;
		int lastX = this.getWidth() - insets.right;
		int lastY = this.getHeight() - insets.bottom;

		int x = firstX;
		while (x < lastX) {
			int i = 0;
			while (i * sizeW < lastX) {
				int offset = 0;
				if (i % 2 == 0)
					offset += sizeW / 2;

				g.drawLine(x + offset, i * sizeH, x + offset, i * sizeH + sizeH);
				i++;
			}
			// g.drawLine(x, firstY, x, lastY);
			x += sizeW;
		}

		int y = firstY;
		while (y < lastY) {
			g.drawLine(firstX, y, lastX, y);
			y += sizeH;
		}

		for (x = 0; x < points.length; ++x) {
			for (y = 0; y < points[x].length; ++y) {
				Point point = points[x][y];

				if (point.type == 0 || point.type == 1) {
					float c = point.getColorIntensity();
					g.setColor(new Color(c, c, c));
					// } else if (point.type == 1) {
					// float a = 0.5f;

					// if (point.isGuard())
					// a = 1;

					// g.setColor(new Color(a, a, a, point.color));
				}

				else if (point.type == 2) {
					g.setColor(new Color(1.0f, 0.0f, 0.0f));
				}
				int x_offset = y % 2 == 1 ? 0 : sizeW / 2;
				g.fillRect((x * sizeW) + 1 + x_offset, (y * sizeH) + 1, (sizeW - 1), (sizeH -
						1));
			}
		}

	}		
	public void interact(Point point, int type) {
		point.type = editType;

		if (type == 0)
			point.fill();
		else if (type == 1)
			point.clear();
	}

	public void mouseClicked(MouseEvent e) {
		int y = e.getY() / sizeH;
		int offset = y % 2 == 1 ? 0 : sizeW / 2;
		int x = (e.getX() - offset) / sizeW;
		if ((x < points.length) && (x > 0) && (y < points[x].length) && (y > 0)) {
			interact(points[x][y], editType);
			this.repaint();
		}
	}
	
	public void componentResized(ComponentEvent e) {
		int width = (this.getWidth() / sizeW) + 1;
		int length = (this.getHeight() / sizeH) + 1;
		initialize(width, length);
	}
	
	public void mouseDragged(MouseEvent e) {
		int y = e.getY() / sizeH;
		int offset = y % 2 == 1 ? 0 : sizeW / 2;
		int x = (e.getX() - offset) / sizeW;
		if ((x < points.length) && (x > 0) && (y < points[x].length) && (y > 0)) {
			interact(points[x][y], editType);
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
