package glucose.model;

import java.util.List;
import java.util.ArrayList;

/**
 * A Point is a single addressable LED light on the structure.
 */
public class Point {
	
	static int counter = 0;
		
	private Strip strip;
	
	/**
	 * Indicates the index into the color array that this point corresponds to
	 */
	public final int index;
	
	// Position in raw coordinate space
	public final float x, y, z;
	
	// Same as the above, here for legacy reasons
	public final float fx, fy, fz;
	
	Point(Strip strip, Transform transform) {
		this.strip = strip;
		
		x = fx = (float) transform.x();
		y = fy = (float) transform.y();
		z = fz = (float) transform.z();
		
		index = Point.counter++;
	}
				
}
