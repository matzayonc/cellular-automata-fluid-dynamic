public class Point {
	private boolean guard = false;

	public Point[] neighbors;
	public float pressure;

	public Point(boolean isOnEdge) {
		guard = isOnEdge;
		clear();
	}

	public boolean isGuard() {
		return guard;
	}

	public void clicked() {
		pressure = 1;
	}

	public void clear() {
		// TODO: clear velocity and pressure
	}

	public void updateVelocity() {
		// TODO: velocity update
	}

	public void updatePresure() {
		// TODO: pressure update
	}

	public float getPressure() {
		return pressure;
	}
}