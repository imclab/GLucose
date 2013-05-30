package glucose.model;

import processing.opengl.PGraphicsOpenGL;

import javax.media.opengl.GL;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Top-level model of the entire art car. This contains a list of
 * every cube on the car, which forms a hierarchy of clips, strips
 * and points.
 */
public class Model {
		
	public final List<Cube> cubes;
	public final List<Clip> clips;
	public final List<Strip> strips;
	public final List<Point> points;
	
	public final float xMax;
	public final float yMax;
	public final float zMax; 
	
	private final Cube[] _cubes;
	
	// TODO(mcslee): find a cleaner way of doing this. the zeroPoint
	// is used in conjunction with the GL environment to determine where
	// the center of the scene is, and all other point positions are
	// subsequently computed relative to this
	static final Point zeroPoint = new Point();
	
	public Model(PGraphicsOpenGL pgl) {
		
		// TODO(mcslee): find a cleaner way of representing this data, probably
		// serialized in some more neutral form. also figure out what's going on
		// with the indexing starting at 1 and some indices missing.
		_cubes = new Cube[79];
		_cubes[1]  = new Cube(17.25, 0, 0, 0, 0, 80, true, 2, 3);
		_cubes[2]  = new Cube(50.625, -1.5, 0, 0, 0, 55, false, 4, 0);
		_cubes[3]  = new Cube(70.75, 12.375, 0, 0, 0, 55, false, 4, 0);
		_cubes[4]  = new Cube(49.75, 24.375, 0, 0, 0, 48, false, 0, 0);//dnw
		_cubes[5]  = new Cube(14.25, 32, 0, 0, 0, 80, false, 2, 1);
		_cubes[6]  = new Cube(50.375, 44.375, 0, 0, 0, 0, false, 0, 0);//dnw
		_cubes[7]  = new Cube(67.5, 64.25, 0, 27, 0, 0, false, 0, 0);//dnw
		_cubes[8]  = new Cube(44, 136, 0, 0, 0, 0, false, 1, 2);
		_cubes[9]  = new Cube(39, 162, 0, 0, 0, 0, false, 1, 0);
		_cubes[10] = new Cube(58, 182, -4, 12, 0, 0, false, 3, 3);
		_cubes[11] = new Cube(28, 182, -4, 12, 0, 0, false, 0, 0);
		_cubes[12] = new Cube(0, 182, -4, 12, 0, 0, false, 0, 2);
		_cubes[13] = new Cube(18.75, 162, 0, 0, 0, 0, false, 0, 0);
		_cubes[14] = new Cube(13.5, 136, 0, 0, 0, 0, false, 1, 1);
		_cubes[15] = new Cube(6.5, -8.25, 20, 0, 0, 25, false, 5, 3);
		_cubes[16] = new Cube(42, 15, 20, 0, 0, 4, true, 2, 2);
		_cubes[17] = new Cube(67, 24, 20, 0, 0, 25);
		_cubes[18] = new Cube(56, 41, 20, 0, 0, 30, false, 3, 1);
		_cubes[19] = new Cube(24, 2, 20, 0, 0, 25, true, 0, 3);
		_cubes[20] = new Cube(26, 26, 20, 0, 0, 70, true, 2, 3);
		_cubes[21] = new Cube(3.5, 10.5, 20, 0, 0, 35, true, 1, 0);
		_cubes[22] =  new Cube(63, 133, 20, 0, 0, 80, false, 0, 2);
		_cubes[23] = new Cube(56, 159, 20, 0, 0, 65);
		_cubes[24] = new Cube(68, 194, 20, 0, -45, 0);
		_cubes[25] = new Cube(34, 194, 20, 20, 0, 35 );
		_cubes[26] = new Cube(10, 194, 20, 0, -45, 0 ); // wired a bit funky
		_cubes[27] = new Cube(28, 162, 20, 0, 0, 65);
		_cubes[28] = new Cube(15.5, 134, 20, 0, 0, 20);
		_cubes[29] = new Cube(13, 29, 40, 0, 0, 0, true, 0, 0);
		_cubes[30] = new Cube(55, 15, 40, 0, 0, 50, false, 0, 2);
		_cubes[31] = new Cube(78, 9, 40, 0, 0, 60, true, 5, 2);
		_cubes[32] = new Cube(80, 39, 40, 0, 0, 80, false, 0, 3);
		_cubes[33] = new Cube(34, 134, 40, 0, 0, 50, false, 0, 3);
		_cubes[34] = new Cube(42, 177, 40, 0, 0, 0);
		_cubes[35] = new Cube(41, 202, 40, 20, 0, 80);
		_cubes[36] = new Cube(21, 178, 40, 0, 0, 35);
		_cubes[37] = new Cube(18, 32, 60, 0, 0, 65, true, 0, 1);
		_cubes[38] = new Cube(44, 20, 60, 0, 0, 20); //front power cube
		_cubes[39] = new Cube(39, 149, 60, 0, 0, 15);
		_cubes[40] = new Cube(60, 186, 60, 0, 0, 45);
		_cubes[41] = new Cube(48, 213, 56, 20, 0, 25);
		_cubes[42] = new Cube(22, 222, 60, 10, 10, 15, false, 0, 3);
		_cubes[43] = new Cube(28, 198, 60, 20, 0, 20, true, 5, 0);
		_cubes[44] = new Cube(12, 178, 60, 0, 0, 50, false, 4, 1);
		_cubes[45] = new Cube(18, 156, 60, 0, 0, 40);
		_cubes[46] = new Cube(30, 135, 60, 0, 0, 45);
		_cubes[47] = new Cube(10, 42, 80, 0, 0, 17, true, 0, 2);
		_cubes[48] = new Cube(34, 23, 80, 0, 0, 45, false, 0, 1);
		_cubes[49] = new Cube(77, 28, 80, 0, 0, 45);
		_cubes[50] = new Cube(53, 22, 80, 0, 0, 45);
		_cubes[51] = new Cube(48, 175, 80, 0, 0, 45); 
		_cubes[52] = new Cube(66, 172, 80, 0, 0, 355, true, 5, 1);// _,195,_ originally
		_cubes[53] = new Cube(33, 202, 80, 25, 0, 85, false, 1, 3);
		_cubes[54] = new Cube(32, 176, 100, 0, 0, 20, false, 0, 2);
		_cubes[55] = new Cube(5.75, 69.5, 0, 0, 0, 80);
		_cubes[56] = new Cube(1, 53, 0, 40, 70, 70);
		_cubes[57] = new Cube(-15, 24, 0, 15, 0, 0);
		//_cubes[58] what the heck happened here? never noticed before 4/8/2013
		//_cubes[59] what the heck happened here? never noticed before 4/8/2013
		_cubes[60] = new Cube(40, 164, 120, 0, 0, 12.5, false, 4, 3);
		_cubes[61] = new Cube(32, 148, 100, 0, 0, 3, false, 4, 2);
		_cubes[62] = new Cube(30, 132, 90, 10, 350, 5);
		_cubes[63] = new Cube(22, 112, 100, 0, 20, 0, false, 4, 0);
		_cubes[64] = new Cube(35, 70, 95, 15, 345, 20);
		_cubes[65] = new Cube(38, 112, 98, 25, 0, 0, false, 4, 3);
		_cubes[66] = new Cube(70, 164, 100, 0, 0, 22);
		_cubes[68] = new Cube(29, 94, 105, 15, 20, 10, false, 4, 0);
		_cubes[69] = new Cube(30, 77, 100, 15, 345, 20, false, 2, 1);
		_cubes[70] = new Cube(38, 96, 95, 30, 0, 355);
		//_cubes[71]= new Cube(38,96,95,30,0,355);
		_cubes[72] = new Cube(44, 20, 100, 0, 0, 345);
		_cubes[73] = new Cube(28, 24, 100, 0, 0, 13, true, 5, 1);
		_cubes[74] = new Cube(8, 38, 100, 10, 0, 0, true, 5, 1);
		_cubes[75] = new Cube(20, 58, 100, 0, 0, 355, false, 2, 3);
		_cubes[76] = new Cube(22, 32, 120, 15, 327, 345, false, 4, 0); 
		_cubes[77] = new Cube(50, 132, 80, 0, 0, 0, false, 0, 2); 
		_cubes[78] = new Cube(20, 140, 80, 0, 0, 0, false, 0, 3);

		// Make unmodifiable accessors to the model data
		List<Cube> cubeList = new ArrayList<Cube>();
		List<Clip> clipList = new ArrayList<Clip>();
		List<Strip> stripList = new ArrayList<Strip>();
		List<Point> pointList = new ArrayList<Point>();
		for (Cube cube : _cubes) {
			if (cube != null) {
				cubeList.add(cube);
				for (Clip clip : cube.clips) {
					clipList.add(clip);
					for (Strip strip : clip.strips) {
						stripList.add(strip);
						for (Point point : strip.points) {
							pointList.add(point);
						}
					}
				}
			}
		}
		
		cubes = Collections.unmodifiableList(cubeList);
		clips = Collections.unmodifiableList(clipList);
		strips = Collections.unmodifiableList(stripList);
		points = Collections.unmodifiableList(pointList);
		
		// Note that there is currently a dependency between the model
		// and the simulation engine. This uses the OpenGL engine to compute
		// the positions of all the points based upon GL matrix transforms.
		// In the future we should separate these so that the position
		// information is computed independently of the rendering pipeline
		buildGeometry(pgl);

		float _xMax = 0, _yMax = 0, _zMax = 0;
		for (Point p : points) {
			_xMax = Math.max(_xMax, p.fx);
			_yMax = Math.max(_yMax, p.fy);
			_zMax = Math.max(_zMax, p.fz);
		}
		this.xMax = _xMax;
		this.yMax = _yMax;
		this.zMax = _zMax;		
		
		// TODO(mcslee): add 2nd pass to swap out points for immutable
		// points so it is not possible to overwrite fx/fy/fz
	}
		
	/**
	 * This method uses openGL to render the model with a flag set such
	 * that as the model is iterated through, the transformation matrix
	 * is used to calculate the position of each point, based upon the
	 * rotations and translations performed. This need only be done once,
	 * after the model is built.
	 */
	private void buildGeometry(PGraphicsOpenGL pgl) {
		int[] colors = new int[points.size()];
		GL gl = pgl.beginGL();
		draw(gl, colors, true);
		pgl.endGL();
		normalizeGeometry();
	}
	
	/**
	 * Creates normalized constants for the geometry of all the points, based
	 * upon a range from 0 to N.
	 */
	private void normalizeGeometry() {
		float minY, maxX, maxZ;		
		minY = Float.MAX_VALUE;
		maxX = maxZ = Float.MIN_VALUE;
		for (Point p : points) {
			minY = Math.min(minY, p.y);
			maxX = Math.max(maxX, p.x);
			maxZ = Math.max(maxZ, p.z);
		}
		
		// Set the normalized position values of all cubes
		for (Point p : points) {
			p.fx = maxX - p.x;
			p.fy = p.y - minY;
			p.fz = maxZ - p.z;
		}
				
		// Set the scaled center positions of all cubes
		for (Cube c : cubes) {
			float sx = 0, sy = 0, sz = 0, num = 0;
			for (Point p : c.points) {
				sx += p.fx;
				sy += p.fy;
				sz += p.fz;
				++num;
			}
			c.fx = sx / num;
			c.fy = sy / num;
			c.fz = sz / num;
		}
	}	
	
	public void draw(GL gl, int[] colors, boolean updatePosition) {
		if (updatePosition) {
			zeroPoint.draw(gl, colors, updatePosition);
		}
		for (Cube c : this.cubes) {
			c.draw(gl, colors, updatePosition);
		}
	}
}
