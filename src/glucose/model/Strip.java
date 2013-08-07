package glucose.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * A strip is a linear run of points along a single edge of one cube.
 */
public class Strip {

	public static final int POINTS_PER_STRIP = 16;
	
	public static final float POINT_SPACING = 18.625f / 15.f;
		
	public final boolean isHorizontal;
	
	private final Face face;
	
	public final List<Point> points;
	
	private final Point[] _points;
	
	Strip(Face face, Transform transform, boolean isHorizontal) {
		this.face = face;
		this.isHorizontal = isHorizontal;
		this._points = new Point[POINTS_PER_STRIP];

		float edgeLength = isHorizontal ? Cube.EDGE_WIDTH : Cube.EDGE_HEIGHT;
		float offset = (edgeLength - (POINTS_PER_STRIP - 1) * POINT_SPACING) / 2.f;		
		
		transform.push();
		transform.translate(offset, -Cube.CHANNEL_WIDTH/2.f, 0);
		for (int i = 0; i < this._points.length; i++) {
			this._points[i] = new Point(this, transform);
			transform.translate(POINT_SPACING, 0, 0);			
		}
		transform.pop();
		
		points = Collections.unmodifiableList(Arrays.asList(_points));
	}
	
}
