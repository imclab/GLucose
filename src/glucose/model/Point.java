package glucose.model;

import java.util.List;
import java.util.ArrayList;

/**
 * A Point is a single addressable LED light on the structure.
 */
public class Point {
	
	private static int counter = 0;
		
	private final Strip strip;
	
	/**
	 * Indicates the index into the color array that this point corresponds to
	 */
	public final int index;
	
	// Position in raw coordinate space
	public final float x, y, z;
		
	Point(Strip strip, Transform transform) {
		this.strip = strip;
		
		x = (float) transform.x();
		y = (float) transform.y();
		z = (float) transform.z();
		
		index = Point.counter++;
	}
				
}
