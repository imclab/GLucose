package glucose.model;

import heronarts.lx.model.LXFixture;
import heronarts.lx.model.LXModel;
import heronarts.lx.model.LXPoint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Model of the floor of a DJ booth atop a subwoofer box. Has a set of struts
 * running across it that face upwards. This is separated from the subwoofer
 * box since these lights are visible to the DJ in the booth and should be
 * independently addressed so they are not blindingly bright and flashy.
 */
public class BoothFloor extends LXModel {
	
	public static final Strip.Metrics STRIP_METRICS =
		new Strip.Metrics(BassBox.EDGE_DEPTH, 48);
	
	public static final float[] STRIP_OFFSETS = new float[] { 
		20.f,
		54.25f,
		BassBox.EDGE_WIDTH-54.25f-Cube.CHANNEL_WIDTH,
		BassBox.EDGE_WIDTH-20.f-Cube.CHANNEL_WIDTH,
	};
	
	public static final float PLEXI_WIDTH = 0.5f;
		
	/**
	 * Immutable list of all strips
	 */
	public final List<Strip> strips;
		
	BoothFloor() {
		this.strips = Collections.unmodifiableList(new ArrayList<Strip>());
	}
	
	public BoothFloor(BassBox bassBox) {
		super(new Fixture(bassBox));
		Fixture fixture = (Fixture) this.fixtures.get(0);
		this.strips = Collections.unmodifiableList(fixture.strips);				
	}
	
	private static class Fixture implements LXFixture {
		
		private final List<Strip> strips = new ArrayList<Strip>();
		private final List<LXPoint> points = new ArrayList<LXPoint>();
		
		private Fixture(BassBox bassBox) {
			Transform t = new Transform();
			t.translate(bassBox.x, bassBox.y + bassBox.EDGE_HEIGHT, bassBox.z + BassBox.EDGE_DEPTH);
			t.rotateY(-Math.PI/2.);
			t.rotateX(-Math.PI/2.);
			t.translate(0, Cube.CHANNEL_WIDTH, 0);
			for (float offset : STRIP_OFFSETS) {
				t.push();
				t.translate(0, offset, 0);
				Strip s = new Strip(STRIP_METRICS, 0, t, false);
				this.strips.add(s);
				for (LXPoint p : s.points) {
					this.points.add(p);
				}
				t.pop();
			}
		}
		
		public List<LXPoint> getPoints() {
			return this.points;
		}
	}

}
