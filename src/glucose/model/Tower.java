package glucose.model;

import heronarts.lx.model.LXFixture;
import heronarts.lx.model.LXModel;
import heronarts.lx.model.LXPoint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Model of a set of cubes stacked in a tower
 */
public class Tower extends LXModel {
	/**
	 * Immutable list of cubes
	 */
	public final List<Cube> cubes;
	
	/**
	 * Immutable list of faces
	 */
	public final List<Face> faces;
	
	/**
	 * Immutable list of strips
	 */
	public final List<Strip> strips;
	
	/**
	 * Constructs a tower model from these cubes
	 * 
	 * @param cubes Array of cubes
	 */
	public Tower(List<Cube> cubes) {
		super(cubes.toArray(new Cube[]{}));
		
		List<Cube> cubeList = new ArrayList<Cube>();
		List<Face> faceList = new ArrayList<Face>();
		List<Strip> stripList = new ArrayList<Strip>();
		
		for (Cube cube : cubes) {
			cubeList.add(cube);
			for (Face face : cube.faces) {
				faceList.add(face);
				for (Strip strip : face.strips) {
					stripList.add(strip);
				}
			}
		}
		this.cubes = Collections.unmodifiableList(cubeList);
		this.faces = Collections.unmodifiableList(faceList);
		this.strips = Collections.unmodifiableList(stripList);
	}
}
