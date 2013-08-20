package glucose.model;

import glucose.model.Strip.Metrics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BassBox {
	
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
	
	// Iterable list of all faces
	public final List<Face> faces;

	// Iterable list of all strips
	public final List<Strip> strips;
	
	// Iterable list of all virtual "box-size" strips
	public final List<Strip> boxStrips;
	
	// Iterable list of all points
	public final List<Point> points;
		
	public final float x, y, z;
	public final float cx, cy, cz;
	
	public BassBox(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;

		Face[] _faces = new Face[4];
		List<Strip> _boxStrips = new ArrayList<Strip>();
		List<Strip> _strips = new ArrayList<Strip>();
		List<Point> _points = new ArrayList<Point>();

		Transform t = new Transform();
		t.translate(x, y, z);		
		
		t.push();
		for (int fi = 0; fi < _faces.length; ++fi) {
			boolean isSide = (fi % 2) == 1;
			Face.Metrics metrics = isSide ? SIDE_FACE_METRICS : FRONT_FACE_METRICS;
			_faces[fi] = new Face(metrics, t);
			int si = 0;
			for (Strip strip : _faces[fi].strips) {
				_strips.add(strip);
				for (Point point : strip.points) {
					_points.add(point);
				}
				if (si % 2 == 1) {
					_boxStrips.add(strip);
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
							List<Point> vsp = new ArrayList<Point>();
							for (int pj = 0; pj < fsMetrics.numPoints; ++pj) {
								vsp.add(strip.points.get(pi++));
							}
							_boxStrips.add(new Strip(fsMetrics, vsp, true));
						}
					} else {
						final Strip.Metrics ssMetrics = new Strip.Metrics(
							EDGE_DEPTH / (NUM_SIDE_STRUTS+1),
							24
						);
						for (int ssi = 0; ssi <= NUM_SIDE_STRUTS; ++ssi) {
							List<Point> vsp = new ArrayList<Point>();
							for (int pj = 0; pj < ssMetrics.numPoints; ++pj) {
								vsp.add(strip.points.get(pi++));
							}
							_boxStrips.add(new Strip(ssMetrics, vsp, true));
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
				Strip strut = new Strip(STRUT_METRICS, t, false);  
				_strips.add(strut);
				_boxStrips.add(strut);
				for (Point point : strut.points) {
					_points.add(point);
				}
			}
			t.pop();
			
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
		boxStrips = Collections.unmodifiableList(_boxStrips);		
		points = Collections.unmodifiableList(_points);
	}

}
