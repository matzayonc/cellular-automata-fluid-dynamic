public class Point {
	private boolean guard = false;

	public Point[] neighbors = { null, null, null, null, null, null };
	public static Integer[] types = { 0, 1, 2 };
	public int type;
	public int velocity;
	public int vel_direction;

	private boolean[] outs = { false, false, false, false, false, false };
	private boolean[] ins = { false, false, false, false, false, false };
	private boolean staticParticle = false;

	public Point(boolean isOnEdge) {
		guard = isOnEdge;
		clear();
	}

	public void move() {
		for (int i = 0; i < 6; i++) {
			if (neighbors[i] != null)
				ins[i] = neighbors[i].outAndClear((i + 3) % 6);
		}
	}

	public void update() {
		collision2();
		collision3();

		// all remaining particles move to opposite directions
		for (int i = 0; i < 6; i++)
			if (ins[i]) {
				ins[i] = false;
				outs[(i + 3) % 6] = true;
			}
	}

	public void setNeighbors(Point nw, Point ne, Point e, Point se, Point sw, Point w) {
		neighbors[0] = nw;
		neighbors[1] = ne;
		neighbors[2] = e;
		neighbors[3] = se;
		neighbors[4] = sw;
		neighbors[5] = w;
	}

	public void fill() {
		type = 1;
		staticParticle = true;
		for (int i = 0; i < outs.length; ++i)
			outs[i] = true;
	}

	public void clear() {
		type = 0;
		staticParticle = false;
		for (int i = 0; i < neighbors.length; ++i) {
			ins[i] = false;
			outs[i] = false;
		}
	}

	public boolean isGuard() {
		return guard;
	}

	public void collision2() {
		Point a = null, b = null;
		boolean collided = false;
		if (neighbors[0].vel_direction + neighbors[3].vel_direction == 0) {
			a = neighbors[0];
			b = neighbors[3];
			collided = true;

		}
		if (neighbors[1].vel_direction + neighbors[4].vel_direction == 0) {
			a = neighbors[1];
			b = neighbors[4];
			collided = true;
		}
		if (neighbors[2].vel_direction + neighbors[5].vel_direction == 0) {
			a = neighbors[2];
			b = neighbors[5];
			collided = true;
		}
		if (collided) {
			int temp = a.vel_direction;
			a.vel_direction = b.vel_direction;
			b.vel_direction = temp;
		}
	}

	public void collision3() {
		Point a = null, b = null, c = null;
		boolean collided = false;
		int sum1 = neighbors[0].vel_direction + neighbors[2].vel_direction + neighbors[4].vel_direction;
		int sum2 = neighbors[1].vel_direction + neighbors[3].vel_direction + neighbors[5].vel_direction;
		if (sum1 == 2 || sum1 == -2) {
			a = neighbors[0];
			b = neighbors[2];
			c = neighbors[4];
			collided = true;

		}
		if (sum2 == 2 || sum2 == -2) {
			a = neighbors[1];
			b = neighbors[3];
			c = neighbors[5];
			collided = true;
		}

		if (collided) {
			a.vel_direction = a.vel_direction * (-1);
			b.vel_direction = b.vel_direction * (-1);
			c.vel_direction = c.vel_direction * (-1);
		}
	}

	public boolean outAndClear(int index) {
		boolean ret = outs[index];
		outs[index] = false;
		return ret;
	}

	public boolean in(int index) {
		return ins[index];
	}

	public float getColorIntensity() {
		int c = 0;
		for (int i = 0; i < 6; ++i)
			if (outs[i])
				++c;
		if (staticParticle)
			++c;

		return 1 - (c / 7.0f);
	}

	public void spawn(float chance) {
		for (int i = 1; i <= 3; ++i)
			if (Math.random() < chance)
				outs[(i + 3) % 6] = true;
	}
}