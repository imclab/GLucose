package glucose.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.media.opengl.GL;

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
	
	public final static float EDGE_HEIGHT = 21.75f;
	public final static float EDGE_WIDTH = 24.625f;
		
	// Iterable list of all points in cube
	public final List<Point> points;	

	// Iterable list of all faces
	public final List<Face> faces;

	// Iterable list of all strips
	public final List<Strip> strips;
	
	// Each cube has 4 faces
	private final Face[] _faces;
	
	// Orientation of this cube in space
	public final float x, y, z, rx, ry, rz;
	
	// Scaled position of the center of this cube
	public float fx, fy, fz;
	
	public Cube(double x, double y, double z, double rx, double ry, double rz) {
		this((float) x, (float) y, (float) z, (float) rx, (float) ry, (float) rz); 
	}
		
	public Cube(float x, float y, float z, float rx, float ry, float rz) {
		this.x = x; 
		this.y = y;
		this.z = z;
		this.rx = rx;
		this.ry = ry;
		this.rz = rz;
 
		this._faces = new Face[FACES_PER_CUBE];
		List<Point> _points = new ArrayList<Point>();
		List<Strip> _strips = new ArrayList<Strip>();

		for (int i = 0; i < this._faces.length; i++) {
			this._faces[i] = new Face(this);
			for (Strip s : this._faces[i].strips) {
				_strips.add(s);
			}
			for (Point p : this._faces[i].points) {
				_points.add(p);
			}
		}		
		
		this.faces = Collections.unmodifiableList(Arrays.asList(this._faces));
		this.strips = Collections.unmodifiableList(_strips);
		this.points = Collections.unmodifiableList(_points);
	}
	
	void draw(GL gl, int[] colors, boolean updatePosition) {
		gl.glPushMatrix();

		gl.glTranslatef(x, -y, z);
		gl.glRotatef(rx, 1, 0, 0);
		gl.glRotatef(ry, 0, -1, 0);
		gl.glRotatef(rz, 0, 0, 1);

		// The first face is head on, the next is round to the right, etc.
		for (Face f : _faces) {
			f.draw(gl, colors, updatePosition);
			gl.glTranslatef(Cube.EDGE_WIDTH, 0, 0);
			gl.glRotatef(90, 0, -1, 0);
		}
		
		gl.glPopMatrix();
	}

}
