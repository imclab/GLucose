package glucose.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.media.opengl.GL;

/**
 * A strip is a linear run of points along a single edge of one cube.
 */
public class Strip {

	public static final int POINTS_PER_STRIP = 16;
		
	private final Clip clip;
	
	public final List<Point> points;
	
	private final Point[] _points;
	
	Strip(Clip clip) {
		this.clip = clip;
		this._points = new Point[POINTS_PER_STRIP];
		for (int i = 0; i < this._points.length; i++) {
			this._points[i] = new Point(this);
		}
		points = Collections.unmodifiableList(Arrays.asList(_points));
	}
	
	void draw(GL gl, int[] colors, boolean updatePosition) {
		gl.glPushMatrix();
		for (Point p : this._points) {
			gl.glTranslatef(1, 0, 0);
			p.draw(gl, colors, updatePosition);
		}
		gl.glPopMatrix();
	}
}
