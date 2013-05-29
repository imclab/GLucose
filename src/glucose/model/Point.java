package glucose.model;

import java.util.List;
import java.util.ArrayList;

import javax.media.opengl.GL;

/**
 * A Point is a single addressable LED light on the structure.
 */
public class Point {
	
	static int counter = 0;
	
	// Iterable list of all points
	public static final List<Point> list = new ArrayList<Point>();
	
	private Strip strip;
	
	/**
	 * Indicates the index into the color array that this point corresponds to
	 */
	public final int index;
	
	// Position in raw coordinate space
	float x, y, z;
	
	// Positions, scaled onto 128/255 shape
	public float fx, fy, fz;
	
	private boolean mark = false;
	private boolean mapped = true;
		
	private final List<Point> neighbors= new ArrayList<Point>();  

	Point() {
		this(null);
	}
	
	Point(Strip strip) {
		this.strip = strip;
		x = y = z = fx = fy = fz = 0;
		// Only add real model points to the list
		if (strip != null) {
			index = Point.counter++;
			list.add(this);
		} else {
			index = -1;
		}
	}
				
	void updatePosition(GL gl) {
		float m[] = new float[16];
		gl.glGetFloatv(GL.GL_MODELVIEW_MATRIX, m, 0);

		// TODO(mcslee): fix this hack, should not have global state for zeroPoint
		x = m[13] - Model.zeroPoint.x;
		y = m[14] - Model.zeroPoint.y;
		z = m[12] - Model.zeroPoint.z;		
	}
		
	void draw(GL gl, int[] colors, boolean updatePosition) {
		gl.glBegin(GL.GL_POINTS);
		
		// TODO(mcslee): update, should be an FX, not inside draw
//		for (PostProcessor post : postprocessors) {
//			float[] rgb = post.filter(this);
//			r = rgb[0];
//			g = rgb[1];
//			b = rgb[2];
//		}  
		
		int c = (index < 0) ? 0 : colors[index];
		byte a = (byte) ((c >> 24) & 0xFF);
		byte r = (byte) ((c >> 16) & 0xFF);
		byte g = (byte) ((c >> 8) & 0xFF);
		byte b = (byte) ((c) & 0xFF);
		gl.glColor4ub(r, g, b, a);
				
		gl.glVertex3f(0, 0, 0);
		gl.glEnd();
		
		if (updatePosition) { 
			updatePosition(gl);
		}
	}
}
