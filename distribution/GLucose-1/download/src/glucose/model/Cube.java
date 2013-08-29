package glucose.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Model of a single cube, which has an orientation and position on the
 * car. The position is specified in x,y,z coordinates with rotation. The
 * x axis is left->right, y is bottom->top, and z is front->back.
 * 
 * A cube's x,y,z position is specified as the left, bottom, front corner.
 * 
 * Dimensions are all specified in real-world inches.
 */
public class Cube {

	public final static int FACES_PER_CUBE = 4;	
	public static final int POINTS_PER_STRIP = 16;
	
	public final static int STRIPS_PER_CUBE = FACES_PER_CUBE*Face.STRIPS_PER_FACE;
	public final static int POINTS_PER_CUBE = STRIPS_PER_CUBE*POINTS_PER_STRIP;
	public final static int POINTS_PER_FACE = Face.STRIPS_PER_FACE*POINTS_PER_STRIP;
	
	public final static float EDGE_HEIGHT = 21.75f;
	public final static float EDGE_WIDTH = 24.625f;
	public final static float CHANNEL_WIDTH = 1.5f;
	
	public final static Face.Metrics FACE_METRICS = new Face.Metrics(
		new Strip.Metrics(EDGE_WIDTH, POINTS_PER_STRIP),
		new Strip.Metrics(EDGE_HEIGHT, POINTS_PER_STRIP)
	);
	
	public enum Wiring {
		FRONT_LEFT,
		FRONT_RIGHT,
		REAR_LEFT,
		REAR_RIGHT,
	};
	
	public final Wiring wiring;
	
	// Iterable list of all points in cube
	public final List<Point> points;	

	// Iterable list of all faces
	public final List<Face> faces;

	// Iterable list of all strips
	public final List<Strip> strips;
		
	// Orientation of this cube in space
	public final float x, y, z, rx, ry, rz;
	
	// Position of the center of this cube
	public final float cx, cy, cz;
	
	public Cube(double x, double y, double z, double rx, double ry, double rz) {
		this((float) x, (float) y, (float) z, (float) rx, (float) ry, (float) rz); 
	}
		
	public Cube(float x, float y, float z, float rx, float ry, float rz) {
		this(x, y, z, rx, ry, rz, Wiring.FRONT_LEFT);
	}
	
	public Cube(float x, float y, float z, float rx, float ry, float rz, Wiring wiring) {
		this.wiring = wiring;
		this.x = x; 
		this.y = y;
		this.z = z;
		this.rx = rx;
		this.ry = ry;
		this.rz = rz;
		
		Transform t = new Transform();
		t.translate(x, y, z);
		t.rotateX(rx*Math.PI / 180.);
		t.rotateY(ry*Math.PI / 180.);
		t.rotateZ(rz*Math.PI / 180.);		
 
		Face[] _faces = new Face[FACES_PER_CUBE];
		List<Point> _points = new ArrayList<Point>();
		List<Strip> _strips = new ArrayList<Strip>();

		for (int i = 0; i < _faces.length; i++) {
			_faces[i] = new Face(FACE_METRICS, t);
			for (Strip s : _faces[i].strips) {
				_strips.add(s);
			}
			for (Point p : _faces[i].points) {
				_points.add(p);
			}
			t.translate(EDGE_WIDTH, 0, 0);
			t.rotateY(Math.PI / 2.);
		}
		
		t.translate(EDGE_WIDTH/2., EDGE_HEIGHT/2., EDGE_WIDTH/2.);
		cx = (float)t.x();
		cy = (float)t.y();
		cz = (float)t.z();
		
		this.faces = Collections.unmodifiableList(Arrays.asList(_faces));
		this.strips = Collections.unmodifiableList(_strips);
		this.points = Collections.unmodifiableList(_points);
	}
}
