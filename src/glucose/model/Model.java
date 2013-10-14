package glucose.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Top-level model of the entire art car. This contains a list of
 * every cube on the car, which forms a hierarchy of faces, strips
 * and points.
 */
public class Model {
	
	public final List<Tower> towers;
	public final List<Cube> cubes;
	public final List<Face> faces;
	public final List<Strip> strips;
	public final List<Point> points;
	
	public final List<Speaker> speakers;
	public final BassBox bassBox;
	public final BoothFloor boothFloor;
	
	public final float xMin;
	public final float yMin;
	public final float zMin; 
	
	public final float xMax;
	public final float yMax;
	public final float zMax;
	
	public final float cx;
	public final float cy;
	public final float cz;
	
	// Experimental, fast contigous memory accessors to point locations
	public final float[] px;
	public final float[] py;
	public final float[] pz;
	
	public final float[] p;
	
	private final Cube[] _cubes;
		
	public Model(ArrayList<Tower> towerList, Cube[] cubeArr, BassBox bassBox, List<Speaker> speakers) {
		
		_cubes = cubeArr;
		this.speakers = Collections.unmodifiableList(speakers);
		this.bassBox = bassBox;
		this.boothFloor = bassBox.hasLights ? new BoothFloor(bassBox) : new BoothFloor(); 
		
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
		
		if (bassBox != null) {
			for (Strip strip : bassBox.boxStrips) {
				stripList.add(strip);
			}		
			for (Point point : bassBox.points) {
				pointList.add(point);
			}
		}
		if (boothFloor != null) {
			for (Strip strip : boothFloor.strips) {
				stripList.add(strip);
			}
			for (Point point : boothFloor.points) {
				pointList.add(point);
			}
		}
		for (Speaker speaker : speakers) {
			for (Strip strip : speaker.strips) {
				stripList.add(strip);
			}
			for (Point point : speaker.points) {
				pointList.add(point);
			}
		}
		
		this.towers = Collections.unmodifiableList(towerList);
		this.cubes = Collections.unmodifiableList(cubeList);
		this.faces = Collections.unmodifiableList(faceList);
		this.strips = Collections.unmodifiableList(stripList);
		this.points = Collections.unmodifiableList(pointList);

		float _xMax = 0, _yMax = 0, _zMax = 0;
		float _xMin = Float.MAX_VALUE, _yMin = Float.MAX_VALUE, _zMin = Float.MAX_VALUE;
		for (Point p : points) {
			_xMin = Math.min(_xMin, p.x);
			_yMin = Math.min(_yMin, p.y);
			_zMin = Math.min(_zMin, p.z);
			_xMax = Math.max(_xMax, p.x);
			_yMax = Math.max(_yMax, p.y);
			_zMax = Math.max(_zMax, p.z);
		}
		this.xMin = _xMin;
		this.yMin = _yMin;
		this.zMin = _zMin;
		this.xMax = _xMax;
		this.yMax = _yMax;
		this.zMax = _zMax;
		this.cx = (_xMin + _xMax) / 2.f;
		this.cy = (_yMin + _yMax) / 2.f;
		this.cz = (_zMin + _zMax) / 2.f;
		
		int numPoints = points.size();
		this.px = new float[numPoints];
		this.py = new float[numPoints];
		this.pz = new float[numPoints];
		this.p = new float[3*numPoints];
		int pi = 0;
		for (Point p : points) {
			this.px[p.index] = this.p[pi++] = p.x;
			this.py[p.index] = this.p[pi++] = p.y;
			this.pz[p.index] = this.p[pi++] = p.z;
		}
	}
	
	public final float x(int i) {
		return this.p[3*i];
	}
	
	public final float y(int i) {
		return this.p[3*i+1];
	}
	
	public final float z(int i) {
		return this.p[3*i+2];
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
