import java.util.ArrayList;

public class Point implements Runnable {
	private boolean guard = false;
	private boolean changed = true;

	public Point[] neighbors = { null, null, null, null, null, null };
	public static Integer[] types = { 0, 1, 2 };
	public int type;
	public int velocity;
	public int vel_direction;

	private boolean[] outs = { false, false, false, false, false, false };
	private boolean[] ins = { false, false, false, false, false, false };
	private boolean staticParticle = false;

	private int[] counts = { 0, 0, 0, 0, 0, 0 };
	private ArrayList<Integer> history = new ArrayList<Integer>();
	static int historySize = 50;

	public static int[] lookup = new int[256];

	public int in = 0;

	public Point(boolean isOnEdge) {
		guard = isOnEdge;
		recalculateLookup();
		clear();
	}

	public void recalculateLookup() {
		for (int i = 0; i < 256; i++) {
			staticParticle = (i & (1 << 7)) != 0;
			for (int j = 0; j < 6; j++)
				ins[j] = (i & (2 << j)) != 0;

			boolean p = (i & 1) == 1;
			update(p);

			int r = p ? 1 : 0;
			for (int k = 0; k < 6; k++)
				if (outs[k])
					r |= 2 << k; // i += pow(2, i+1)
			if (staticParticle)
				r |= 1 << 7;

			lookup[i] = r;
			clear();
		}
	}

	public void move() {
		for (int i = 0; i < 6; i++)
			if (outs[i]) {
				outs[i] = false;

				if (neighbors[i] == null)
					continue;

				if (neighbors[i].type == 2)
					in |= 1 << i + 1;
				else {
					neighbors[i].in |= 1 << ((i + 3) % 6) + 1;
					neighbors[i].changed = true;
				}
			}
	}

	public void run() {
		if (historySize > 0) {
			history.add(in);
			for (int i = 1; i < 7; i++) {
				if ((in & (1 << i)) != 0) {
					counts[i - 1]++;
				}
			}
			if (history.size() > historySize) {
				int old = history.remove(0);
				for (int i = 1; i < 7; i++) {
					if ((old & (1 << i)) != 0) {
						counts[i - 1]--;
					}
				}
			}
		}
		boolean p = Math.random() < 0.5;
		fromInt(lookup[in | (p ? 1 : 0)]);
		in = 0;
	}

	public int toInt(boolean p) {
		int r = p ? 0 : 1;

		for (int i = 0; i < 6; i++)
			if (ins[i]) {
				r |= 2 << i; // i += pow(2, i+1)
				ins[i] = false;
			}

		if (staticParticle)
			r |= 1 << 7;

		return r;
	}

	public void fromInt(int i) {
		staticParticle = (i & (1 << 7)) != 0;
		for (int j = 0; j < 6; j++)
			outs[j] = (i & (2 << j)) != 0;
	}

	public void update(boolean p) {
		collision2(p);
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
		for (int i = 0; i < outs.length; ++i) {
			outs[i] = true;
			staticParticle = false;
		}
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

	public void collision2(boolean p) {
		if (ins[0] && ins[3]) {
			if (p) {
				outs[1] = true;
				outs[4] = true;
			} else {
				outs[2] = true;
				outs[5] = true;
			}
			ins[0] = false;
			ins[3] = false;

		}
		if (ins[1] && ins[4]) {
			if (p) {
				outs[0] = true;
				outs[3] = true;
			} else {
				outs[2] = true;
				outs[5] = true;
			}
			ins[1] = false;
			ins[4] = false;
		}
		if (ins[2] && ins[5]) {
			if (p) {
				outs[1] = true;
				outs[4] = true;
			} else {
				outs[0] = true;
				outs[3] = true;
			}
			ins[2] = false;
			ins[5] = false;
		}
		if (ins[0] && ins[4] && !staticParticle) {
			outs[2] = true;
			staticParticle = true;
			ins[0] = false;
			ins[4] = false;
		}
		if (ins[1] && ins[5] && !staticParticle) {
			outs[3] = true;
			staticParticle = true;
			ins[1] = false;
			ins[5] = false;
		}
		if (ins[2] && ins[0] && !staticParticle) {
			outs[4] = true;
			staticParticle = true;
			ins[2] = false;
			ins[0] = false;
		}
		if (ins[1] && ins[3] && !staticParticle) {
			outs[5] = true;
			staticParticle = true;
			ins[1] = false;
			ins[3] = false;
		}
		if (ins[2] && ins[4] && !staticParticle) {
			outs[0] = true;
			staticParticle = true;
			ins[2] = false;
			ins[4] = false;
		}
		if (ins[3] && ins[5] && !staticParticle) {
			outs[1] = true;
			staticParticle = true;
			ins[3] = false;
			ins[5] = false;
		}
	}

	public void collision3() {
		if (ins[0] && ins[2] && ins[4]) {
			outs[1] = true;
			outs[3] = true;
			outs[5] = true;
			ins[0] = false;
			ins[2] = false;
			ins[4] = false;

		}
		if (ins[1] && ins[3] && ins[5]) {
			outs[0] = true;
			outs[2] = true;
			outs[4] = true;
			ins[1] = false;
			ins[3] = false;
			ins[5] = false;
		}
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
				outs[i % 6] = true;
	}

	public float angle() {
		int s = 0;
		int n = 0;
		for (int i = 0; i < 6; i++) {
			s += counts[i] * i;
			n += counts[i];
		}

		if (n == 0)
			return 0;

		return 30 - 60 * s / n;

	}
}