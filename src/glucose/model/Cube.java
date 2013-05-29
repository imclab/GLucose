package glucose.model;

import java.util.ArrayList;
import java.util.List;
import javax.media.opengl.GL;

/**
 * Model of a single cube, which has an orientation and position on the
 * car.
 */
public class Cube {
	
	private final static int CLIPS_PER_CUBE = 4;
	
	// Iterable list of all cubes
	public final static List<Cube> list = new ArrayList<Cube>();
	
	// Iterable list of all points in cube
	public final List<Point> points = new ArrayList<Point>();
	
	// Neighbors of this cube
	private final List<Cube> neighbors = new ArrayList<Cube>();
		
	// Each cube has 4 clips to render its faces
	private final Clip[] clips;
	
	// Orientation of this cube in space
	float x, y, z, rx, ry, rz;
	
	// Scaled position of the center of this cube
	public float fx, fy, fz;
	
	// Which is the primary face and how clip is rotated on physical car
	private boolean flipFlag = false;
	private int face = 0;
	private int rotations=0;

	Cube(double z, double x, double y, double rz, double rx, double ry) {
		this((float) z, (float) x, (float) y, (float) rz, (float) rx, (float) ry); 
	}
	
	Cube(double z, double x, double y, double rz, double rx, double ry, boolean flipFlag, int face, int rotations) {
		this((float) z, (float) x, (float) y, (float) rz, (float) rx, (float) ry, flipFlag, face, rotations);
	}
	
	Cube(float z, float x, float y, float rz, float rx, float ry) {
		this(z, x, y, rz, rx, ry, false, 0, 0);
	}
	
	Cube(float z, float x, float y, float rz, float rx, float ry, boolean flipFlag, int face, int rotations) {
		this.z = z;
		this.x = 192-x; 
		this.y = y;
		this.rz = rz;
		this.rx = rx;
		this.ry = ry;
		this.flipFlag = flipFlag;
		this.face = face;
		this.rotations = rotations;
 
		this.clips = new Clip[CLIPS_PER_CUBE];
		// prolly will have to rotate c's in some odd way
		for (int i = 0; i < this.clips.length; i++) {
			this.clips[i] = new Clip(this);
			for (Point p : this.clips[i].points) {
				this.points.add(p);
			}
		}
		
		list.add(this);
	}
	
	void draw(GL gl, int[] colors, boolean updatePosition) {
		gl.glPushMatrix();

		gl.glTranslatef(z, x, y);
		gl.glRotatef(rz, 1, 0, 0);
		gl.glRotatef(rx, 0, 1, 0);
		gl.glRotatef(ry, 0, 0, 1);

		// CHIRALITY OPERATION
		if (flipFlag) { // left handed orientation, clips rotate clockwise around positive cube z vector
			gl.glTranslatef(10, 0, 0);
			gl.glScalef(-1, 1, 1);
			gl.glTranslatef(-10, 0, 0);
		}

		// FACE OPERATION - variable face toggles through the 6 possible locations for the "bottom yellow" face in mapper
		switch (face) {
		case 0:
			break;
		case 1:
			gl.glTranslatef(10, 0, 10);
			gl.glRotatef(90, 0, 1, 0);
			gl.glTranslatef(-10, 0, -10);
			break;
		case 2:
			gl.glTranslatef(0, 10, 10);
			gl.glRotatef(90, 1, 0, 0);
			gl.glTranslatef(0, -10, -10);
			break;
		case 3:
			gl.glTranslatef(10, 0, 10);
			gl.glRotatef(90, 0, -1, 0);
			gl.glTranslatef(-10, 0, -10);
			break;
		case 4:
			gl.glTranslatef(0, 10, 10);
			gl.glRotatef(90, -1, 0, 0);
			gl.glTranslatef(0, -10, -10);
			break;
		case 5:
			gl.glTranslatef(0, 10, 10);
			gl.glRotatef(180, 1, 0, 0);
			gl.glTranslatef(0, -10, -10);
			break;
		}

		// ROTATIONS OPERATION - rotates about the mapper "bottom yellow" face depending on rotations variable
		gl.glTranslatef(10, 10, 0);
		gl.glRotatef(90*rotations, 0, 0, 1);
		gl.glTranslatef(-10, -10, 0);

		// CLIP ORDERING OPERATIONS - orient the clips as red, green, blue, white (first in...last out) according to mapper
		gl.glPushMatrix();
		gl.glTranslatef(0, 0, 20);
		gl.glRotatef(-90, 1, 0, 0);
		clips[0].draw(gl, colors, updatePosition);
		gl.glPopMatrix();

		gl.glPushMatrix();
		gl.glTranslatef(20, 0, 20);
		gl.glRotatef(90, 1, 0, 0);
		gl.glRotatef(180, 0, 0, 1);
		gl.glRotatef(90, 0, 1, 0);
		clips[1].draw(gl, colors, updatePosition);
		gl.glPopMatrix();

		gl.glPushMatrix();
		gl.glTranslatef(20, 20, 20);
		gl.glRotatef(180, 0, 0, 1);
		gl.glRotatef(-90, 1, 0, 0);
		clips[2].draw(gl, colors, updatePosition);
		gl.glPopMatrix();

		gl.glPushMatrix();
		gl.glTranslatef(0, 20, 20);
		gl.glRotatef(90, 1, 0, 0);
		gl.glRotatef(180, 0, 0, 1);
		gl.glRotatef(-90, 0, 1, 0);
		clips[3].draw(gl, colors, updatePosition);
		gl.glPopMatrix();
		
		gl.glPopMatrix();
	}

}
