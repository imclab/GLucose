package glucose.model;

import heronarts.lx.model.LXFixture;
import heronarts.lx.model.LXModel;
import heronarts.lx.model.LXPoint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * A strip is a linear run of points along a single edge of one cube.
 */
public class Strip extends LXModel {
	
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
	
	/**
	 * Whether this is a horizontal strip
	 */
	public final boolean isHorizontal;
	
	/**
	 * Rotation about the y axis
	 */
	public final float ry;

	public Object obj1 = null, obj2 = null;
	
	Strip(Metrics metrics, float ry, List<LXPoint> points, boolean isHorizontal) {
		super(points);
		this.isHorizontal = isHorizontal;
		this.metrics = metrics;		
		this.ry = ry;
	}
	
	Strip(Metrics metrics, float ry, Transform transform, boolean isHorizontal) {
		super(new Fixture(metrics, ry, transform));
		this.metrics = metrics;
		this.isHorizontal = isHorizontal;
		this.ry = ry;
	}
	
	private static class Fixture implements LXFixture {
		private final List<LXPoint> points = new ArrayList<LXPoint>();
		
		private Fixture(Metrics metrics, float ry, Transform transform) {
			float offset = (metrics.length - (metrics.numPoints - 1) * POINT_SPACING) / 2.f;
			transform.push();
			transform.translate(offset, -Cube.CHANNEL_WIDTH/2.f, 0);
			for (int i = 0; i < metrics.numPoints; i++) {
				LXPoint point = new LXPoint(transform.x(), transform.y(), transform.z());
				this.points.add(point);
				transform.translate(POINT_SPACING, 0, 0);			
			}
			transform.pop();
		}
		
		public List<LXPoint> getPoints() {
			return this.points;
		}
		
	}
	
}
