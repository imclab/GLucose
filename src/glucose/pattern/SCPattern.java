package glucose.pattern;

import glucose.GLucose;
import glucose.model.Cube;
import glucose.model.Face;
import glucose.model.Model;
import glucose.model.Point;
import glucose.model.Strip;

import heronarts.lx.pattern.LXPattern;

import java.util.ArrayList;
import java.util.List;

/**
 * Subclass of LXPattern specific to sugar cubes. These patterns
 * get access to the glucose state and geometry, and have some
 * little helpers for interacting with the model.
 */
public abstract class SCPattern extends LXPattern {
	
	protected final GLucose glucose;
	protected final Model model;
	
	protected SCPattern(GLucose glucose) {
		super(glucose.lx);
		this.glucose = glucose;
		this.model = glucose.model;
	}
	
	protected void setColor(Cube cube, int c) {
		for (Point p : cube.points) {
			colors[p.index] = c;
		}
	}	
	
	protected void setColor(Face face, int c) {
		for (Point p : face.points) {
			colors[p.index] = c;
		}
	}
	
	protected void setColor(Strip strip, int c) {
		for (Point p : strip.points) {
			colors[p.index] = c;
		}
	}
	
	protected void setColor(Point p, int c) {
		colors[p.index] = c;
	}
	
	public void noteOnReceived(rwmidi.Note note) {}
	public void noteOffReceived(rwmidi.Note note) {}
	public void controllerChangeReceived(rwmidi.Controller controller) {}

}
