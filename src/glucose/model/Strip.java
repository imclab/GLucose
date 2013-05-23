package glucose.model;

import java.util.ArrayList;
import java.util.List;

import javax.media.opengl.GL;

/**
 * A strip is a linear run of points along a single edge of one cube.
 */
public class Strip {

	private final int POINTS_PER_STRIP = 16;
	
	// Iterable list of all strips
	public static final List<Strip> list = new ArrayList<Strip>();
	
	
	private final List<Strip> neighbors = new ArrayList<Strip>();
	
	private final Clip clip;
	
	public final Point[] points;

	private float x, y, z;
	
	Strip(Clip clip) {
		this.clip = clip;
		this.points = new Point[POINTS_PER_STRIP];
		for (int i = 0; i < this.points.length; i++) {
			this.points[i] = new Point(this);
		}
		
		list.add(this);
	}
	
	void draw(GL gl, int[] colors, boolean updatePosition) {
		gl.glPushMatrix();
		for (Point p : points) {
			gl.glTranslatef(1, 0, 0);
			p.draw(gl, colors, updatePosition);
		}
		gl.glPopMatrix();
	}
}
