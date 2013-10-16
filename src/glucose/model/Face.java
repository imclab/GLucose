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
	
	public static class Metrics {
		final Strip.Metrics horizontal;
		final Strip.Metrics vertical;
		
		public Metrics(Strip.Metrics horizontal, Strip.Metrics vertical) {
			this.horizontal = horizontal;
			this.vertical = vertical;
		}
	}
	
	// All points in this clip
	public final List<Point> points; 
	
	// All strips in this clip
	public final List<Strip> strips;
		
	// Strips in this clip, each comprised of three
	private final Strip[] _strips;
	
	// Center position of the face
	public final float cx,cy,cz;
	
	// Rotation of the face
	public final float ry;
	
	Face(Metrics metrics, float ry, Transform transform) {
		this.ry = ry;
		
		List<Point> _points = new ArrayList<Point>();
		this._strips = new Strip[STRIPS_PER_FACE];
		transform.push();
		transform.translate(0, metrics.vertical.length, 0);
		float ax=0, ay=0, az=0;
		for (int i = 0; i < this._strips.length; i++) {
			boolean isHorizontal = (i % 2 == 0);
			Strip.Metrics stripMetrics = isHorizontal ? metrics.horizontal : metrics.vertical;
			this._strips[i] = new Strip(stripMetrics, ry, transform, isHorizontal);
			transform.translate(isHorizontal ? metrics.horizontal.length : metrics.vertical.length, 0, 0);
			transform.rotateZ(Math.PI/2.);
			for (Point p : this._strips[i].points) {
				_points.add(p);
				ax += p.x;
				ay += p.y;
				az += p.z;
			}
		}
		transform.pop();
		
		cx = ax / (float) _points.size();
		cy = ay / (float) _points.size();
		cz = az / (float) _points.size();
		
		this.strips = Collections.unmodifiableList(Arrays.asList(this._strips));
		this.points = Collections.unmodifiableList(_points);
	}
	
}
