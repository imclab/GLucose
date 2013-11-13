package glucose.model;

import heronarts.lx.model.LXFixture;
import heronarts.lx.model.LXModel;
import heronarts.lx.model.LXPoint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Top-level model of the entire art car. This contains a list of
 * every cube on the car, which forms a hierarchy of faces, strips
 * and points.
 */
public class Model extends LXModel {
	
	public final List<Tower> towers;
	public final List<Cube> cubes;
	public final List<Face> faces;
	public final List<Strip> strips;
	
	public final List<Speaker> speakers;
	public final BassBox bassBox;
	public final BoothFloor boothFloor;
	
	private final Cube[] _cubes;
	
	public Model(List<Tower> towerList, Cube[] cubeArr, BassBox bassBox, List<Speaker> speakers) {
		super(new Fixture(towerList, cubeArr, bassBox, speakers));
		Fixture fixture = (Fixture) this.fixtures.get(0);
		
		_cubes = cubeArr;
		this.speakers = Collections.unmodifiableList(speakers);
		this.bassBox = bassBox;
		this.boothFloor = fixture.boothFloor; 
		
		// Make unmodifiable accessors to the model data
		List<Cube> cubeList = new ArrayList<Cube>();
		List<Face> faceList = new ArrayList<Face>();
		List<Strip> stripList = new ArrayList<Strip>();
		for (Cube cube : _cubes) {
			if (cube != null) {
				cubeList.add(cube);
				for (Face face : cube.faces) {
					faceList.add(face);
					for (Strip strip : face.strips) {
						stripList.add(strip);
					}
				}
			}
		}
		
		if (bassBox != null) {
			for (Strip strip : bassBox.boxStrips) {
				stripList.add(strip);
			}		
		}
		if (boothFloor != null) {
			for (Strip strip : boothFloor.strips) {
				stripList.add(strip);
			}
		}
		for (Speaker speaker : speakers) {
			for (Strip strip : speaker.strips) {
				stripList.add(strip);
			}
		}
		
		this.towers = Collections.unmodifiableList(towerList);
		this.cubes = Collections.unmodifiableList(cubeList);
		this.faces = Collections.unmodifiableList(faceList);
		this.strips = Collections.unmodifiableList(stripList);
		
	}

	private static class Fixture implements LXFixture {
		
		private List<LXPoint> points = new ArrayList<LXPoint>(); 
		
		private BoothFloor boothFloor;
		
		private Fixture(List<Tower> towerList, Cube[] cubeArr, BassBox bassBox, List<Speaker> speakers) {
			this.boothFloor = bassBox.hasLights ? new BoothFloor(bassBox) : new BoothFloor();
			for (Cube cube : cubeArr) {
				if (cube != null) {
					for (LXPoint point : cube.points) {
						this.points.add(point);
					}
				}
			}
			for (LXPoint point : bassBox.points) {
				this.points.add(point);
			}
			for (LXPoint point : boothFloor.points) {
				this.points.add(point);
			}
			for (Speaker speaker : speakers) {
				for (LXPoint point : speaker.points) {
					this.points.add(point);
				}
			}
		}
		
		public List<LXPoint> getPoints() {
			return this.points;
		}
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
