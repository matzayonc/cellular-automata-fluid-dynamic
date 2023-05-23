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
				if(neighbors[((i + 3) % 6)].type == 2) {
					ins[i] = false;
					outs[i] = true;
				}
				else {
					ins[i] = false;
					outs[(i + 3) % 6] = true;
				}
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
		if (ins[0] && ins[3]) {
			if(Math.random()<0.5){
				outs[1]=true;
				outs[4]=true;
			}
			else{
				outs[2]=true;
				outs[5]=true;
			}
			ins[0]=false;
			ins[3]=false;

		}
		if (ins[1] && ins[4]) {
			if(Math.random()<0.5){
				outs[0]=true;
				outs[3]=true;
			}
			else{
				outs[2]=true;
				outs[5]=true;
			}
			ins[1]=false;
			ins[4]=false;
		}
		if (ins[2] && ins[5]) {
			if(Math.random()<0.5){
				outs[1]=true;
				outs[4]=true;
			}
			else{
				outs[0]=true;
				outs[3]=true;
			}
			ins[2]=false;
			ins[5]=false;
		}
	}

	public void collision3() {
		if (ins[0] && ins[2] && ins[4]) {
			outs[1] = true;
			outs[3] = true;
			outs[5] = true;
			ins[0]=false;
			ins[2]=false;
			ins[4]=false;

		}
		if (ins[1] && ins[3] && ins[5]) {
			outs[0] = true;
			outs[2] = true;
			outs[4] = true;
			ins[1]=false;
			ins[3]=false;
			ins[5]=false;
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