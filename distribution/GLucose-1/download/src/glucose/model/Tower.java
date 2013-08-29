package glucose.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Tower {
	// Iterable list of all points in cube
	public final List<Cube> cubes;
	public final List<Face> faces;
	public final List<Strip> strips;
	public final List<Point> points;
	
	public Tower(ArrayList<Cube> cubeList) {
		
		ArrayList<Face> faceList = new ArrayList<Face>();
		ArrayList<Strip> stripList = new ArrayList<Strip>();
		ArrayList<Point> pointList = new ArrayList<Point>();
		
		for (Cube cube : cubeList) {
			for (Face face : cube.faces) {
				faceList.add(face);
				for (Strip strip : face.strips) {
					stripList.add(strip);
					for (Point p : strip.points) {
						pointList.add(p);
					}
				}
			}
		}
		this.cubes = Collections.unmodifiableList(cubeList);
		this.faces = Collections.unmodifiableList(faceList);
		this.strips = Collections.unmodifiableList(stripList);
		this.points = Collections.unmodifiableList(pointList);
	}
}
