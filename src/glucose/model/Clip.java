package glucose.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.media.opengl.GL;

/**
 * A clip is a component of a cube. It is comprised of three strips forming
 * a shape of the letter C. A whole cube is formed by four clips, which
 * combined comprise all 12 visible edges of a cube.
 * 
 * The best way to remember what a Clip means is to recall that it starts
 * with C. Clip starts with C, it's lights in the shape of a C.
 */
public class Clip {

	private final static int STRIPS_PER_CLIP = 3;
		
	// All points in this clip
	public final List<Point> points; 
	
	// All strips in this clip
	public final List<Strip> strips;
	
	// Cube that this clip belongs to
	private Cube cube;
	
	// Strips in this clip, each comprised of three
	private final Strip[] _strips;
	
	Clip(Cube cube) {
		this.cube = cube;
		List<Point> _points = new ArrayList<Point>();
		this._strips = new Strip[STRIPS_PER_CLIP];
		for (int i = 0; i < this._strips.length; i++) {
			this._strips[i] = new Strip(this);
			for (Point p : this._strips[i].points) {
				_points.add(p);
			}
		}
		this.strips = Collections.unmodifiableList(Arrays.asList(this._strips));
		this.points = Collections.unmodifiableList(_points);
	}
		
	void draw(GL gl, int[] colors, boolean updatePosition) {
		gl.glPushMatrix();
		for (Strip s : this._strips) {
			s.draw(gl, colors, updatePosition);
			gl.glTranslatef(20, 0, 0);
			gl.glRotatef(90, 0, 0, 1);
		}
		gl.glPopMatrix();
	}
	
}
