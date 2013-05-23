package glucose.pattern;

import glucose.GLucose;
import glucose.control.BasicKnob;
import glucose.control.Knob;
import glucose.model.Cube;
import glucose.model.Clip;
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
public abstract class SCPattern extends LXPattern implements Knob.Listener {
	
	protected final GLucose glucose;
	
	private final List<Knob> knobs = new ArrayList<Knob>();
	
	protected SCPattern(GLucose glucose) {
		super(glucose.lx);
		this.glucose = glucose;
	}
	
	protected void setColor(Cube cube, int c) {
		for (Point p : cube.points) {
			colors[p.index] = c;
		}
	}	
	
	protected void setColor(Clip clip, int c) {
		for (Point p : clip.points) {
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

	protected final void addKnob(BasicKnob knob) {
		knobs.add(knob);
		knob.setListener(this);
	}
	
	public final List<Knob> getKnobs() {
		return knobs;
	}	
	
	/**
	 * Notifies the pattern when one of its knob values has changed. This
	 * doesn't always have to be implemented. Patterns can alternatively
	 * just inspect knob values in real time.
	 */
	public /* abstract */ void onKnobChange(Knob knob) {}

}
