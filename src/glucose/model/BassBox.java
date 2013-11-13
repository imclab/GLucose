package glucose.model;

import glucose.model.Strip.Metrics;

import heronarts.lx.model.LXFixture;
import heronarts.lx.model.LXModel;
import heronarts.lx.model.LXPoint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Model of a large subwoofer box with sets of vertical struts
 * running around the front and sides.
 */
public class BassBox extends LXModel {
	
	public static final float EDGE_WIDTH = 124.f;
	public static final float EDGE_HEIGHT = 32.f;
	public static final float EDGE_DEPTH = 66.f;
	
	public static final float FRONT_STRUT_SPACING = 30.625f;
	public static final float SIDE_STRUT_SPACING = 32.25f;
	
	public static final int NUM_FRONT_STRUTS = 3;
	public static final int NUM_SIDE_STRUTS = 1;
	
	public static final Strip.Metrics STRUT_METRICS =
		new Strip.Metrics(EDGE_HEIGHT, 21);
	
	public static final Face.Metrics FRONT_FACE_METRICS = new Face.Metrics(
		new Strip.Metrics(EDGE_WIDTH, 99),
		new Strip.Metrics(EDGE_HEIGHT, 21)
	);
		
	public static final Face.Metrics SIDE_FACE_METRICS = new Face.Metrics(
		new Strip.Metrics(EDGE_DEPTH, 48),
		new Strip.Metrics(EDGE_HEIGHT, 21)
	);
	
	/**
	 * Immutable list of all faces
	 */
	public final List<Face> faces;

	/**
	 * Immutable list of all strips
	 */
	public final List<Strip> strips;

	/**
	 * Immutable list of all struts
	 */
	public final List<Strip> struts;
	
	/**
	 * Immutable list of all strips on the perimeter box, not including struts
	 */
	public final List<Strip> boxStrips;
	
	/**
	 * Whether the bass box has lights in it
	 */
	public final boolean hasLights;
	
	/**
	 * Whether the bass box exists at all
	 */
	public final boolean exists;
	
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
	 * Makes a bass box dummy object that does not exist
	 * 
	 * @return No bass box
	 */
	public static BassBox noBassBox() {
		return new BassBox();
	}
	
	/**
	 * Makes a dummy bass box with no lights, but a location
	 * 
	 * @param x Front left corner x coordinate
	 * @param y Front left corner y coordinate
	 * @param z Front left corner z coordinate
	 * @return Bass box with front left corner at position, no lights
	 */
	public static BassBox unlitBassBox(float x, float y, float z) {
		return new BassBox(x, y, z, true);
	}
	
	private BassBox() {
		super();
		this.x = this.y = this.z = 0;
		this.exists = false;
		this.hasLights = false;
		this.faces = Collections.unmodifiableList(new ArrayList<Face>());
		this.strips = Collections.unmodifiableList(new ArrayList<Strip>());
		this.struts = Collections.unmodifiableList(new ArrayList<Strip>());
		this.boxStrips = Collections.unmodifiableList(new ArrayList<Strip>());
	}
	
	private BassBox(float x, float y, float z, boolean noLights) {
		super();
		this.x = x;
		this.y = y;
		this.z = z;
		
		this.exists = true;
		this.hasLights = false;

		this.faces = Collections.unmodifiableList(new ArrayList<Face>());
		this.strips = Collections.unmodifiableList(new ArrayList<Strip>());
		this.struts = Collections.unmodifiableList(new ArrayList<Strip>());
		this.boxStrips = Collections.unmodifiableList(new ArrayList<Strip>());
	}
	
	/**
	 * Construct a bass box
	 * 
	 * @param x Front left corner x coordinate
	 * @param y Front left corner y coordinate
	 * @param z Front left corner z coordinate
	 */
	public BassBox(float x, float y, float z) {
		super(new Fixture(x, y, z));
		Fixture fixture = (Fixture) this.fixtures.get(0);

		this.x = x;
		this.y = y;
		this.z = z;

		this.exists = true;
		this.hasLights = true;

		this.faces = Collections.unmodifiableList(fixture.faces);
		this.struts = Collections.unmodifiableList(fixture.struts);
		this.strips = Collections.unmodifiableList(fixture.strips);
		this.boxStrips = Collections.unmodifiableList(fixture.boxStrips);
	}

	private static class Fixture implements LXFixture {
		
		private final List<Face> faces = new ArrayList<Face>();
		private final List<Strip> strips = new ArrayList<Strip>();
		private final List<Strip> struts = new ArrayList<Strip>();
		private final List<Strip> boxStrips = new ArrayList<Strip>();
		private final List<LXPoint> points = new ArrayList<LXPoint>();
		
		private final static int NUM_FACES = 4; 
		
		private Fixture(float x, float y, float z) {
			
			Transform t = new Transform();
			t.translate(x, y, z);		
			
			t.push();
			for (int fi = 0; fi < NUM_FACES; ++fi) {
				boolean isSide = (fi % 2) == 1;
				Face.Metrics metrics = isSide ? SIDE_FACE_METRICS : FRONT_FACE_METRICS;
				float ry = fi*90;
				Face face = new Face(metrics, ry, t);
				this.faces.add(face);
				int si = 0;
				for (Strip strip : face.strips) {
					this.strips.add(strip);
					for (LXPoint point : strip.points) {
						this.points.add(point);
					}
					if (si % 2 == 1) {
						this.boxStrips.add(strip);
					} else {
						int pi = 0;
						if (fi % 2 == 0) {
							final Strip.Metrics firstThree = new Strip.Metrics(
								EDGE_WIDTH / (NUM_FRONT_STRUTS+1),
								25	
							);
							final Strip.Metrics lastOne = new Strip.Metrics(
								EDGE_WIDTH / (NUM_FRONT_STRUTS+1),
								24
							);
						
							for (int fsi = 0; fsi <= NUM_FRONT_STRUTS; ++fsi) {
								Strip.Metrics fsMetrics = (fsi < 3) ? firstThree : lastOne;
								List<LXPoint> vsp = new ArrayList<LXPoint>();
								for (int pj = 0; pj < fsMetrics.numPoints; ++pj) {
									vsp.add(strip.points.get(pi++));
								}
								this.boxStrips.add(new Strip(fsMetrics, ry, vsp, true));
							}
						} else {
							final Strip.Metrics ssMetrics = new Strip.Metrics(
								EDGE_DEPTH / (NUM_SIDE_STRUTS+1),
								24
							);
							for (int ssi = 0; ssi <= NUM_SIDE_STRUTS; ++ssi) {
								List<LXPoint> vsp = new ArrayList<LXPoint>();
								for (int pj = 0; pj < ssMetrics.numPoints; ++pj) {
									vsp.add(strip.points.get(pi++));
								}
								this.boxStrips.add(new Strip(ssMetrics, ry, vsp, true));
							}
						}
					}
					++si;
				}
				
				t.push();
				t.translate(Cube.CHANNEL_WIDTH, EDGE_HEIGHT, 0);
				t.rotateZ(Math.PI / 2.);
				int numStruts = isSide ? NUM_SIDE_STRUTS : NUM_FRONT_STRUTS;
				float strutSpacing = isSide ? SIDE_STRUT_SPACING : FRONT_STRUT_SPACING;
				for (int sti = 0; sti < numStruts; ++sti) {
					t.translate(0, strutSpacing, 0);
					Strip strut = new Strip(STRUT_METRICS, ry, t, false);
					this.struts.add(strut);
				}
				t.pop();
				
				t.translate(metrics.horizontal.length, 0, 0);
				t.rotateY(Math.PI / 2.);
			}
			t.pop();
			
			for (Strip strut : struts) {
				this.strips.add(strut);
				this.boxStrips.add(strut);
				for (LXPoint point : strut.points) {
					this.points.add(point);
				}
			}

		}
		
		public List<LXPoint> getPoints() {
			return this.points;
		}
	}
	
}
