public class Point {
	private boolean guard = false;

	public Point[] neighbors = { null, null, null, null, null, null };
	public static Integer[] types = {0, 1, 2};
	public int type;
	public float color;

	public Point(boolean isOnEdge) {
		guard = isOnEdge;
		clear();
	}

	public boolean isGuard() {
		return guard;
	}

	public void setNeighbors(Point nw, Point ne, Point e, Point se, Point sw, Point w) {
		neighbors[0] = nw;
		neighbors[1] = ne;
		neighbors[2] = e;
		neighbors[3] = se;
		neighbors[4] = sw;
		neighbors[5] = w;
	}

	public void clicked() {
		type = 1;
		color = 1;
		for (Point n : neighbors) {
			n.type = 1;
			n.color = 0.7f;
		}
	}

	public void clear() {
		type = 0;
		// TODO: clear velocity and pressure
	}

	public void updateVelocity() {
		// TODO: velocity update
	}

	public void updatePresure() {
		// TODO: pressure update
	}

	public float getColor() {
		return color;
	}
}