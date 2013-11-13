package glucose.model;

import heronarts.lx.model.LXFixture;
import heronarts.lx.model.LXModel;
import heronarts.lx.model.LXPoint;

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
public class Cube extends LXModel {

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
	
	/**
	 * How a cube is wired. Data input enters on the bottom of the cube, on
	 * one of its four corners.
	 */
	public enum Wiring {
		FRONT_LEFT,
		FRONT_RIGHT,
		REAR_LEFT,
		REAR_RIGHT,
	};
	
	/**
	 * Which way this cube is wired
	 */
	public final Wiring wiring;
	
	/**
	 * Immutable list of all cube faces
	 */
	public final List<Face> faces;

	/**
	 * Immutable list of all strips
	 */
	public final List<Strip> strips;
		
	/**
	 * Front left corner x coordinate 
	 */
	public final float x;
	
	/**
	 * Front left corner y coordinate 
	 */
	public final float y;
	
	/**
	 * Front left corner z coordinate 
	 */
	public final float z;
	
	/**
	 * Rotation about the x-axis 
	 */
	public final float rx;
	
	/**
	 * Rotation about the y-axis 
	 */
	public final float ry;
	
	/**
	 * Rotation about the z-axis 
	 */
	public final float rz;
		
	public Cube(double x, double y, double z, double rx, double ry, double rz) {
		this((float) x, (float) y, (float) z, (float) rx, (float) ry, (float) rz); 
	}
		
	public Cube(float x, float y, float z, float rx, float ry, float rz) {
		this(x, y, z, rx, ry, rz, Wiring.FRONT_LEFT);
	}
	
	public Cube(float x, float y, float z, float rx, float ry, float rz, Wiring wiring) {
		super(new Fixture(x, y, z, rx, ry, rz));
		Fixture fixture = (Fixture) this.fixtures.get(0);
		
		while (rx < 0) rx += 360;
		while (ry < 0) ry += 360;
		while (rz < 0) rz += 360;
		rx = rx % 360;
		ry = ry % 360;
		rz = rz % 360;
		
		this.wiring = wiring;
		this.x = x; 
		this.y = y;
		this.z = z;
		this.rx = rx;
		this.ry = ry;
		this.rz = rz;
				
		this.faces = Collections.unmodifiableList(fixture.faces);
		this.strips = Collections.unmodifiableList(fixture.strips);
	}
	
	private static class Fixture implements LXFixture {
		
		private final List<Face> faces = new ArrayList<Face>();
		private final List<Strip> strips = new ArrayList<Strip>();
		private final List<LXPoint> points = new ArrayList<LXPoint>();
		
		private Fixture(float x, float y, float z, float rx, float ry, float rz) {
			Transform t = new Transform();
			t.translate(x, y, z);
			t.rotateX(rx*Math.PI / 180.);
			t.rotateY(ry*Math.PI / 180.);
			t.rotateZ(rz*Math.PI / 180.);		
	 
			for (int i = 0; i < FACES_PER_CUBE; i++) {
				Face face = new Face(FACE_METRICS, (ry + 90*i) % 360, t);
				this.faces.add(face);
				for (Strip s : face.strips) {
					this.strips.add(s);
				}
				for (LXPoint p : face.points) {
					this.points.add(p);
				}
				t.translate(EDGE_WIDTH, 0, 0);
				t.rotateY(Math.PI / 2.);
			}			
		}
		
		public List<LXPoint> getPoints() {
			return this.points;
		}
	}

}
