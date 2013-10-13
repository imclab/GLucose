package glucose.transform;

import glucose.model.Point;

public class Coord {

	public float x, y, z;
	public final int index;
	
	public Coord(Point p) {
		x = p.x;
		y = p.y;
		z = p.z;
		index = p.index;
	}
}
