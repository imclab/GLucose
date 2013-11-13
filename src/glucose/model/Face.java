package glucose.model;

import heronarts.lx.model.LXFixture;
import heronarts.lx.model.LXModel;
import heronarts.lx.model.LXPoint;
import heronarts.lx.transform.LXTransform;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * A face is a component of a cube. It is comprised of four strips forming
 * the lights on this side of a cube. A whole cube is formed by four faces.
 */
public class Face extends LXModel {

	public final static int STRIPS_PER_FACE = 4;
	
	public static class Metrics {
		final Strip.Metrics horizontal;
		final Strip.Metrics vertical;
		
		public Metrics(Strip.Metrics horizontal, Strip.Metrics vertical) {
			this.horizontal = horizontal;
			this.vertical = vertical;
		}
	}
	
	/**
	 * Immutable list of strips
	 */
	public final List<Strip> strips;
		
	/**
	 * Rotation of the face about the y-axis
	 */
	public final float ry;
	
	Face(Metrics metrics, float ry, LXTransform transform) {
		super(new Fixture(metrics, ry, transform));
		Fixture fixture = (Fixture) this.fixtures.get(0);
		this.ry = ry;
		this.strips = Collections.unmodifiableList(fixture.strips);
	}
	
	private static class Fixture implements LXFixture {
		
		private final List<Strip> strips = new ArrayList<Strip>();
		private final List<LXPoint> points = new ArrayList<LXPoint>();
		
		private Fixture(Metrics metrics, float ry, LXTransform transform) {
			transform.push();
			transform.translate(0, metrics.vertical.length, 0);
			for (int i = 0; i < STRIPS_PER_FACE; i++) {
				boolean isHorizontal = (i % 2 == 0);
				Strip.Metrics stripMetrics = isHorizontal ? metrics.horizontal : metrics.vertical;
				Strip strip = new Strip(stripMetrics, ry, transform, isHorizontal);
				this.strips.add(strip);
				transform.translate(isHorizontal ? metrics.horizontal.length : metrics.vertical.length, 0, 0);
				transform.rotateZ(Math.PI/2.);
				for (LXPoint p : strip.points) {
					this.points.add(p);
				}
			}
			transform.pop();
		}
		
		public List<LXPoint> getPoints() {
			return this.points;
		}

	}		
}
