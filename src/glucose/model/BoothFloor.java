package glucose.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BoothFloor {
	
	public static final Strip.Metrics STRIP_METRICS =
		new Strip.Metrics(BassBox.EDGE_DEPTH, 48);
	
	public static final float[] STRIP_OFFSETS = new float[] { 
		20.f,
		54.25f,
		BassBox.EDGE_WIDTH-54.25f-Cube.CHANNEL_WIDTH,
		BassBox.EDGE_WIDTH-20.f-Cube.CHANNEL_WIDTH,
	};
	
	public static final float PLEXI_WIDTH = 0.5f;
			
	// Iterable list of all strips
	public final List<Strip> strips;
	
	// Iterable list of all points
	public final List<Point> points;
	
	BoothFloor() {
		strips = Collections.unmodifiableList(new ArrayList<Strip>());
		points = Collections.unmodifiableList(new ArrayList<Point>());
	}
	
	public BoothFloor(BassBox bassBox) {
		List<Strip> _strips = new ArrayList<Strip>();
		List<Point> _points = new ArrayList<Point>();		
		
		Transform t = new Transform();
		t.translate(bassBox.x, bassBox.y + bassBox.EDGE_HEIGHT, bassBox.z + BassBox.EDGE_DEPTH);
		t.rotateY(-Math.PI/2.);
		t.rotateX(-Math.PI/2.);
		t.translate(0, Cube.CHANNEL_WIDTH, 0);
		for (float offset : STRIP_OFFSETS) {
			t.push();
			t.translate(0, offset, 0);
			Strip s = new Strip(STRIP_METRICS, t, false);
			_strips.add(s);
			for (Point p : s.points) {
				_points.add(p);
			}
			t.pop();
		}
		
		strips = Collections.unmodifiableList(_strips);
		points = Collections.unmodifiableList(_points);
	}
}
