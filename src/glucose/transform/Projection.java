package glucose.transform;

import heronarts.lx.model.LXPoint;
import heronarts.lx.model.LXModel;

import java.lang.Math;

import java.util.Arrays;
import java.util.Iterator;

/**
 * Class to compute projections of an entire model. These are applied cheaply by
 * using direct manipulation rather than matrix multiplication.
 */
public class Projection implements Iterable<Coord> {

	private final Coord[] coords;
	
	private final LXModel model;
	
	public Iterator<Coord> iterator() {
		return Arrays.asList(this.coords).iterator();
	}
	
	/**
	 * Constructs a projection view of the given model
	 * 
	 * @param model Model
	 */
	public Projection(LXModel model) {
		this.coords = new Coord[model.points.size()];
		int i = 0;
		for (LXPoint point : model.points) {
			this.coords[i++] = new Coord(point);
		}
		this.model = model;
	}

	/**
	 * Reset all points in the projection to the model
	 * 
	 * @return this, for method chaining
	 */
	public Projection reset() {
		int i = 0;
		for (LXPoint point : this.model.points) {
			this.coords[i].x = point.x;
			this.coords[i].y = point.y;
			this.coords[i].z = point.z;
			++i;
		}
		return this;
	}

	/**
	 * Scales the projection
	 * 
	 * @param sx x-factor
	 * @param sy y-factor
	 * @param sz z-factor
	 * @return this, for method chaining
	 */
	public Projection scale(float sx, float sy, float sz) {
		for (Coord v : coords) {
			v.x *= sx;
			v.y *= sy;
			v.z *= sz;
		}
		return this;
	}

	/**
	 * Translates the projection
	 * 
	 * @param tx x-translation
	 * @param ty y-translation
	 * @param tz z-translation
	 * @return this, for method chaining
	 */
	public Projection translate(float tx, float ty, float tz) {
		for (Coord v : coords) {
			v.x += tx;
			v.y += ty;
			v.z += tz;
		}
		return this;
	}
	
	/**
	 * Centers the projection, by translating it such that the origin (0, 0, 0)
	 * becomes the center of the model 
	 * 
	 * @return this, for method chaning
	 */
	public Projection center() {
		return translate(-this.model.cx, -this.model.cy, -this.model.cz);
	}

	/**
	 * Translates the model from its center, so (0, 0, 0) becomes (tx, ty, tz)
	 * 
	 * @param tx x-translation
	 * @param ty y-translation
	 * @param tz z-translation
	 * @return this, for method chaining
	 */
	public Projection translateCenter(float tx, float ty, float tz) {
		return translate(-this.model.cx + tx, -this.model.cy + ty, -this.model.cz + tz);
	}

	/**
	 * Reflects the projection about the x-axis
	 * 
	 * @return this, for method chaining
	 */
	public Projection reflectX() {
		for (Coord v : this.coords) {
			v.x = -v.x;
		}
		return this;
	}
	
	/**
	 * Reflects the projection about the y-axis
	 * 
	 * @return this, for method chaining
	 */
	public Projection reflectY() {
		for (Coord v : this.coords) {
			v.y = -v.y;
		}
		return this;
	}
	
	/**
	 * Reflects the projection about the z-axis
	 * 
	 * @return this, for method chaining
	 */
	public Projection reflectZ() {
		for (Coord v : this.coords) {
			v.z = -v.z;
		}
		return this;
	}
	
	/**
	 * Rotates the projection about a vector
	 * 
	 * @param angle Angle to rotate by, in radians
	 * @param l vector x-value
	 * @param m vector y-value
	 * @param n vector z-value
	 * @return this, for method chaining
	 */
	public Projection rotate(float angle, float l, float m, float n) {
		float ss = l*l + m*m + n*n;
		if (ss != 1) {
			float sr = (float) Math.sqrt(ss);
			l /= sr;
			m /= sr;
			n /= sr;
		}

		float sinv = (float) Math.sin(angle);
		float cosv = (float) Math.cos(angle);
		float a1 = l*l*(1-cosv) + cosv;
		float a2 = l*m*(1-cosv) - n*sinv;
		float a3 = l*n*(1-cosv) + m*sinv;
		float b1 = l*m*(1-cosv) + n*sinv;
		float b2 = m*m*(1-cosv) + cosv;
		float b3 = m*n*(1-cosv) - l*sinv;
		float c1 = l*n*(1-cosv) - m*sinv;
		float c2 = m*n*(1-cosv) + l*sinv;
		float c3 = n*n*(1-cosv) + cosv;
		float xp, yp, zp;

		for (Coord v : this.coords) {
			xp = v.x*a1 + v.y*a2 + v.z*a3;
			yp = v.x*b1 + v.y*b2 + v.z*b3;
			zp = v.x*c1 + v.y*c2 + v.z*c3;
			v.x = xp;
			v.y = yp;
			v.z = zp;
		}

		return this;
	}
}
