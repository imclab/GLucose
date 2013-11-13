package glucose.transform;

import heronarts.lx.model.LXPoint;

import processing.core.PVector;

/**
 * A mutable version of an LXPoint, which has had a transformation applied
 * to it, and may have other transformations applied to it. 
 */
public class Coord extends PVector {

	private static final long serialVersionUID = 1L;
	
	public final int index;
	
	public Coord(LXPoint point) {
		super(point.x, point.y, point.z);
		this.index = point.index;
	}
}
