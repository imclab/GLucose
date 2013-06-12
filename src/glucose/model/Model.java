package glucose.model;

import glucose.GLucose;

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
	
	public Model(GLucose.Mapping mapping, PGraphicsOpenGL pgl) {
		
		_cubes = mapping.buildCubeArray();

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
	 * TODO(mcslee): clean this up to internal-only
	 * 
	 * @param index
	 * @return
	 */
	public Cube getCubeByRawIndex(int index) {
		return _cubes[index];
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
