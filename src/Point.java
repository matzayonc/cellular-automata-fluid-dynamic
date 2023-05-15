public class Point {
	private boolean guard = false;

	public Point[] neighbors = { null, null, null, null, null, null };
	public static Integer[] types = { 0, 1, 2 };
	public int type;
	public float color;
	public int velocity;

	public int vel_direction;

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
		velocity=1;
		vel_direction = 2;
		for (Point n : neighbors) {
			n.type = 1;
			n.color = Math.max(0.7f, n.color);
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
	public void collision2() {
		Point a = null, b=null;
		boolean collided=false;
		if (neighbors[0].vel_direction + neighbors[3].vel_direction == 0) {
			a=neighbors[0];
			b=neighbors[3];
			collided = true;

		}
		if (neighbors[1].vel_direction + neighbors[4].vel_direction == 0) {
			a=neighbors[1];
			b=neighbors[4];
			collided = true;
		}
		if (neighbors[2].vel_direction + neighbors[5].vel_direction == 0) {
			a=neighbors[2];
			b=neighbors[5];
			collided = true;
		}
		if(collided){
			int temp = a.vel_direction;
			a.vel_direction = b.vel_direction;
			b.vel_direction = temp;
		}
	}
	public void collision3() {
		Point a = null, b=null, c=null;
		boolean collided=false;
		int sum1 = neighbors[0].vel_direction + neighbors[2].vel_direction +neighbors[4].vel_direction;
		int sum2 = neighbors[1].vel_direction + neighbors[3].vel_direction + neighbors[5].vel_direction;
		if (sum1 == 2 || sum1 == -2) {
			a=neighbors[0];
			b=neighbors[2];
			c=neighbors[4];
			collided = true;

		}
		if (sum2 == 2 || sum2 == -2) {
			a=neighbors[1];
			b=neighbors[3];
			c=neighbors[5];
			collided = true;
		}

		if(collided){
			a.vel_direction = a.vel_direction*(-1);
			b.vel_direction = b.vel_direction*(-1);
			c.vel_direction = c.vel_direction*(-1);
		}
	}
	public float getColor() {
		return color;
	}
}