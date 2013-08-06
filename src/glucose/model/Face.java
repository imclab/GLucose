package glucose.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.media.opengl.GL;

/**
 * A face is a component of a cube. It is comprised of four strips forming
 * the lights on this side of a cube. A whole cube is formed by four faces.
 */
public class Face {

	public final static int STRIPS_PER_FACE = 4;
		
	// All points in this clip
	public final List<Point> points; 
	
	// All strips in this clip
	public final List<Strip> strips;
	
	// Cube that this clip belongs to
	private Cube cube;
	
	// Strips in this clip, each comprised of three
	private final Strip[] _strips;
	
	Face(Cube cube) {
		this.cube = cube;
		List<Point> _points = new ArrayList<Point>();
		this._strips = new Strip[STRIPS_PER_FACE];
		for (int i = 0; i < this._strips.length; i++) {
			this._strips[i] = new Strip(this, (i % 2 == 0));
			for (Point p : this._strips[i].points) {
				_points.add(p);
			}
		}
		this.strips = Collections.unmodifiableList(Arrays.asList(this._strips));
		this.points = Collections.unmodifiableList(_points);
	}
		
	void draw(GL gl, int[] colors, boolean updatePosition) {
		gl.glPushMatrix();
		gl.glTranslatef(0, -Cube.EDGE_HEIGHT, 0);
		for (Strip s : this._strips) {
			s.draw(gl, colors, updatePosition);
			gl.glTranslatef(s.isHorizontal ? Cube.EDGE_WIDTH : Cube.EDGE_HEIGHT, 0, 0);
			gl.glRotatef(90, 0, 0, 1);
		}
		gl.glPopMatrix();
	}
	
}
