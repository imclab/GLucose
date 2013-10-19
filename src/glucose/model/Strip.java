package glucose.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * A strip is a linear run of points along a single edge of one cube.
 */
public class Strip {
	
	public static final float POINT_SPACING = 18.625f / 15.f;
	
	public static class Metrics {
		
		public final float length;
		public final int numPoints;
		
		public Metrics(float length, int numPoints) {
			this.length = length;
			this.numPoints = numPoints;
		}
	}
	
	public final Metrics metrics;
	
	public final boolean isHorizontal;
	
	public final float cx, cy, cz;
	
	public final float ry;
	
	public final List<Point> points;

	public Object obj1 = null, obj2 = null;
	
	Strip(Metrics metrics, float ry, List<Point> points, boolean isHorizontal) {
		this.isHorizontal = isHorizontal;
		this.metrics = metrics;
		this.points = Collections.unmodifiableList(points);
		
		float ax=0, ay=0, az=0;
		for (Point p : points) {
			ax += p.x;
			ay += p.y;
			az += p.z;
		}
		cx = ax / (float) metrics.numPoints;
		cy = ay / (float) metrics.numPoints;
		cz = az / (float) metrics.numPoints;
		
		this.ry = ry;
	}
	
	Strip(Metrics metrics, float ry, Transform transform, boolean isHorizontal) {
		this.metrics = metrics;
		this.isHorizontal = isHorizontal;
		Point[] _points = new Point[metrics.numPoints];
		
		float offset = (metrics.length - (metrics.numPoints - 1) * POINT_SPACING) / 2.f;		
		
		float ax=0, ay=0, az=0;
		transform.push();
		transform.translate(offset, -Cube.CHANNEL_WIDTH/2.f, 0);
		for (int i = 0; i < _points.length; i++) {
			Point p = new Point(this, transform);
			_points[i] = p;
			ax += p.x;
			ay += p.y;
			az += p.z;
			transform.translate(POINT_SPACING, 0, 0);			
		}
		transform.pop();
		
		this.ry = ry;
		
		cx = ax / (float) metrics.numPoints;
		cy = ay / (float) metrics.numPoints;
		cz = az / (float) metrics.numPoints;
		
		points = Collections.unmodifiableList(Arrays.asList(_points));
	}
	
}
