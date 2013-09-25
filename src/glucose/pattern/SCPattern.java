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
	
	/**
	 * Invoked by engine when this pattern is focused an a midi note is received.  
	 * 
	 * @param note
	 * @return True if the pattern has consumed this note, false if the top-level
	 *         may handle it
	 */
	public boolean noteOnReceived(rwmidi.Note note) {
		return false;
	}
	
	/**
	 * Invoked by engine when this pattern is focused an a midi note off is received.  
	 * 
	 * @param note
	 * @return True if the pattern has consumed this note, false if the top-level
	 *         may handle it
	 */
	public boolean noteOffReceived(rwmidi.Note note) {
		return false;
	}
	
	/**
	 * Invoked by engine when this pattern is focused an a controller is received  
	 * 
	 * @param note
	 * @return True if the pattern has consumed this controller, false if the top-level
	 *         may handle it
	 */
	public boolean controllerChangeReceived(rwmidi.Controller controller) {
		return false;
	}

}
