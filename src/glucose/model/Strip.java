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
	
	public static final float POINT_OFFSET = 1.5f;
	public static final float POINT_SPACING = 18.625f / 15.f;
		
	public static final float CHANNEL_WIDTH = 1.5f;
		
	public final boolean isHorizontal;
	
	private final Face face;
	
	public final List<Point> points;
	
	private final Point[] _points;
	
	Strip(Face face, boolean isHorizontal) {
		this.face = face;
		this.isHorizontal = isHorizontal;
		this._points = new Point[POINTS_PER_STRIP];
		for (int i = 0; i < this._points.length; i++) {
			this._points[i] = new Point(this);
		}
		points = Collections.unmodifiableList(Arrays.asList(_points));
	}
	
	void draw(GL gl, int[] colors, boolean updatePosition) {
		float edgeLength = isHorizontal ? Cube.EDGE_WIDTH : Cube.EDGE_HEIGHT;
		float offset = (edgeLength - (POINTS_PER_STRIP - 1) * POINT_SPACING) / 2.f;
		gl.glPushMatrix();
		gl.glTranslatef(offset, CHANNEL_WIDTH/2.f, 0);
		for (Point p : this._points) {
			p.draw(gl, colors, updatePosition);
			gl.glTranslatef(POINT_SPACING, 0, 0);
		}
		gl.glPopMatrix();
	}
}
