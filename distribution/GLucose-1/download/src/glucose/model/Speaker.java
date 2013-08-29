package glucose.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Speaker {
	public static final float EDGE_WIDTH = 54.375f;
	public static final float EDGE_HEIGHT = 45.25f;
	public static final float EDGE_DEPTH = 33.75f;
			
	public static final Face.Metrics FRONT_FACE_METRICS = new Face.Metrics(
		new Strip.Metrics(EDGE_WIDTH, 41),
		new Strip.Metrics(EDGE_HEIGHT, 31)
	);
		
	public static final Face.Metrics SIDE_FACE_METRICS = new Face.Metrics(
		new Strip.Metrics(EDGE_DEPTH, 25),
		new Strip.Metrics(EDGE_HEIGHT, 31)
	);
	
	// Iterable list of all faces
	public final List<Face> faces;

	// Iterable list of all strips
	public final List<Strip> strips;
	
	// Iterable list of all points
	public final List<Point> points;
		
	public final float x, y, z, ry;
	public final float cx, cy, cz;
	
	public Speaker(float x, float y, float z, float ry) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.ry = ry;

		Face[] _faces = new Face[4];
		List<Strip> _strips = new ArrayList<Strip>();
		List<Point> _points = new ArrayList<Point>();

		Transform t = new Transform();
		t.translate(x, y, z);
		t.rotateY(ry*Math.PI / 180.);
		
		t.push();
		for (int i = 0; i < _faces.length; ++i) {
			boolean isSide = (i % 2) == 1;
			
			Face.Metrics metrics = isSide ? SIDE_FACE_METRICS : FRONT_FACE_METRICS;
			_faces[i] = new Face(metrics, t);
			for (Strip strip : _faces[i].strips) {
				_strips.add(strip);
				for (Point point : strip.points) {
					_points.add(point);
				}
			}
						
			t.translate(metrics.horizontal.length, 0, 0);
			t.rotateY(Math.PI / 2.);
		}
		t.pop();
		
		t.translate(EDGE_WIDTH/2., EDGE_HEIGHT/2., EDGE_DEPTH/2.);
		cx = (float)t.x();
		cy = (float)t.y();
		cz = (float)t.z();		
		
		faces = Collections.unmodifiableList(Arrays.asList(_faces));
		strips = Collections.unmodifiableList(_strips);
		points = Collections.unmodifiableList(_points);
	}

}
