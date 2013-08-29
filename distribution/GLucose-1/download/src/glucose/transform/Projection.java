package glucose.transform;

import glucose.model.Model;
import glucose.model.Point;

import java.lang.Math;

import java.util.Arrays;
import java.util.Iterator;

public class Projection implements Iterable<Coord> {

	final private Coord[] coords;
	
	public Iterator<Coord> iterator() {
		return Arrays.asList(coords).iterator();
	}
	
	public Projection(Model model) {
		coords = new Coord[model.points.size()];
		int i = 0;
		for (Point p : model.points) {
			coords[i++] = new Coord(p);
		}
	}

	public Projection reset(Model model) {
		int i = 0;
		for (Point p : model.points) {
			coords[i].x = p.fx;
			coords[i].y = p.fy;
			coords[i].z = p.fz;
			++i;
		}
		return this;
	}

	public Projection scale(float sx, float sy, float sz) {
		for (Coord v : coords) {
			v.x *= sx;
			v.y *= sy;
			v.z *= sz;
		}
		return this;
	}

	public Projection translate(float tx, float ty, float tz) {
		for (Coord v : coords) {
			v.x += tx;
			v.y += ty;
			v.z += tz;
		}
		return this;
	}
	
	public Projection center(Model model) {
		return translate(-model.xMax/2.f, -model.yMax/2.f, -model.zMax/2.f);
	}

	public Projection translateCenter(Model model, float tx, float ty, float tz) {
		return translate(-model.xMax/2.f + tx, -model.yMax/2.f + ty, -model.zMax/2.f + tz);
	}

	public Projection reflectX() {
		for (Coord v : coords) {
			v.x = -v.x;
		}
		return this;
	}
	
	public Projection reflectY() {
		for (Coord v : coords) {
			v.y = -v.y;
		}
		return this;
	}
	
	public Projection reflectZ() {
		for (Coord v : coords) {
			v.z = -v.z;
		}
		return this;
	}
			
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

		for (Coord v : coords) {
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
