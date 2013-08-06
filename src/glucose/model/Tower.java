package glucose.model;

import java.util.ArrayList;
import java.util.List;

public class Tower {
	// Iterable list of all points in cube
	public final List<Cube> cubes;
	
	Tower() {
		cubes = new ArrayList<Cube>();
	}
	
	public void addCube(Cube cube) {
		cubes.add(cube);
	}

}
