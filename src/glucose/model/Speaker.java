package glucose.model;

import heronarts.lx.model.LXFixture;
import heronarts.lx.model.LXModel;
import heronarts.lx.model.LXPoint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Model of a speaker enclosure with lights around the outsides
 */
public class Speaker extends LXModel {
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
	
	/**
	 * Immutable list of faces
	 */
	public final List<Face> faces;

	/**
	 * Immutable list of strips
	 */
	public final List<Strip> strips;
	
	/**
	 * Front-left corner x coordinate
	 */
	public final float x;
	
	/**
	 * Front-left corner y coordinate
	 */
	public final float y;
	
	/**
	 * Front-left corner z coordinate
	 */
	public final float z;
	
	/**
	 * Rotation about the y axis
	 */
	public final float ry;
	
	public Speaker(float x, float y, float z, float ry) {
		super(new Fixture(x, y, z, ry));
		Fixture fixture = (Fixture) this.fixtures.get(0);
		this.x = x;
		this.y = y;
		this.z = z;
		this.ry = ry;
		
		this.faces = Collections.unmodifiableList(fixture.faces);
		this.strips = Collections.unmodifiableList(fixture.strips);
	}
	
	private static class Fixture implements LXFixture {
		private final List<Face> faces = new ArrayList<Face>();
		private final List<Strip> strips = new ArrayList<Strip>();
		private final List<LXPoint> points = new ArrayList<LXPoint>();
		
		private final static int NUM_FACES = 4;
		
		private Fixture(float x, float y, float z, float ry) {
			Transform t = new Transform();
			t.translate(x, y, z);
			t.rotateY(ry*Math.PI / 180.);
			
			t.push();
			for (int i = 0; i < NUM_FACES; ++i) {
				boolean isSide = (i % 2) == 1;
				
				Face.Metrics metrics = isSide ? SIDE_FACE_METRICS : FRONT_FACE_METRICS;
				Face face = new Face(metrics, ry + i*90, t);
				this.faces.add(face);
				for (Strip strip : face.strips) {
					this.strips.add(strip);
					for (LXPoint point : strip.points) {
						this.points.add(point);
					}
				}
							
				t.translate(metrics.horizontal.length, 0, 0);
				t.rotateY(Math.PI / 2.);
			}
			t.pop();

		}
		
		public List<LXPoint> getPoints() {
			return this.points;
		}
	}

}
