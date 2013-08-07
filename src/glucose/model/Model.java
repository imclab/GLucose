package glucose.model;

import glucose.GLucose;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Top-level model of the entire art car. This contains a list of
 * every cube on the car, which forms a hierarchy of faces, strips
 * and points.
 */
public class Model {
		
	public final List<Cube> cubes;
	public final List<Face> faces;
	public final List<Strip> strips;
	public final List<Point> points;
	
	public final float xMin;
	public final float yMin;
	public final float zMin; 
	
	public final float xMax;
	public final float yMax;
	public final float zMax; 
	
	private final Cube[] _cubes;
		
	public Model(GLucose.Mapping mapping) {
		
		_cubes = mapping.buildCubeArray();

		// Make unmodifiable accessors to the model data
		List<Cube> cubeList = new ArrayList<Cube>();
		List<Face> faceList = new ArrayList<Face>();
		List<Strip> stripList = new ArrayList<Strip>();
		List<Point> pointList = new ArrayList<Point>();
		for (Cube cube : _cubes) {
			if (cube != null) {
				cubeList.add(cube);
				for (Face face : cube.faces) {
					faceList.add(face);
					for (Strip strip : face.strips) {
						stripList.add(strip);
						for (Point point : strip.points) {
							pointList.add(point);
						}
					}
				}
			}
		}
		
		cubes = Collections.unmodifiableList(cubeList);
		faces = Collections.unmodifiableList(faceList);
		strips = Collections.unmodifiableList(stripList);
		points = Collections.unmodifiableList(pointList);

		float _xMax = 0, _yMax = 0, _zMax = 0;
		float _xMin = Float.MAX_VALUE, _yMin = Float.MAX_VALUE, _zMin = Float.MAX_VALUE;
		for (Point p : points) {
			_xMin = Math.min(_xMin, p.fx);
			_yMin = Math.min(_yMin, p.fy);
			_zMin = Math.min(_zMin, p.fz);
			_xMax = Math.max(_xMax, p.fx);
			_yMax = Math.max(_yMax, p.fy);
			_zMax = Math.max(_zMax, p.fz);
		}
		this.xMin = _xMin;
		this.yMin = _yMin;
		this.zMin = _zMin;
		this.xMax = _xMax;
		this.yMax = _yMax;
		this.zMax = _zMax;
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
	 * TODO(mcslee): clean this up to internal-only
	 * 
	 * @param index
	 * @return
	 */
	public int getRawIndexForCube(int index) {
		Cube c = cubes.get(index);
		for (int i = 0; i < _cubes.length; ++i) {
			if (c == _cubes[i]) {
				return i;
			}
		}
		return 0;
	}	
	
}
