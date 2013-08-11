package glucose.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * A face is a component of a cube. It is comprised of four strips forming
 * the lights on this side of a cube. A whole cube is formed by four faces.
 */
public class Face {

	public final static int STRIPS_PER_FACE = 4;
	public final static int POINTS_PER_FACE = STRIPS_PER_FACE * Strip.POINTS_PER_STRIP;
		
	// All points in this clip
	public final List<Point> points; 
	
	// All strips in this clip
	public final List<Strip> strips;
	
	// Cube that this clip belongs to
	private Cube cube;
	
	// Strips in this clip, each comprised of three
	private final Strip[] _strips;
	
	// Center position of the face
	public final float cx,cy,cz;
	
	Face(Cube cube, Transform transform) {
		this.cube = cube;
		List<Point> _points = new ArrayList<Point>();
		this._strips = new Strip[STRIPS_PER_FACE];
		float ax=0, ay=0, az=0;
		transform.push();
		transform.translate(0, Cube.EDGE_HEIGHT, 0);
		for (int i = 0; i < this._strips.length; i++) {
			boolean isHorizontal = (i % 2 == 0);
			this._strips[i] = new Strip(this, transform, isHorizontal);
			transform.translate(isHorizontal ? Cube.EDGE_WIDTH : Cube.EDGE_HEIGHT, 0, 0);
			transform.rotateZ(Math.PI/2.);
			for (Point p : this._strips[i].points) {
				_points.add(p);
				ax += p.x;
				ay += p.y;
				az += p.z;
			}
		}
		transform.pop();
		
		cx = ax / (float)POINTS_PER_FACE;
		cy = ay / (float)POINTS_PER_FACE;
		cz = az / (float)POINTS_PER_FACE;
		
		this.strips = Collections.unmodifiableList(Arrays.asList(this._strips));
		this.points = Collections.unmodifiableList(_points);
	}
	
}
