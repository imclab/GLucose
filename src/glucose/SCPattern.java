package glucose;

import heronarts.lx.parameter.LXParameter;
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
	
	protected SCPattern(GLucose glucose) {
		super(glucose.lx);
		this.glucose = glucose;
	}
	
	/**
	 * Reset this pattern to its default state.
	 */
	public final void reset() {
		for (LXParameter parameter : getParameters()) {
			parameter.reset();
		}
		onReset();
	}
	
	/**
	 * Subclasses may override to add additional reset functionality.
	 */
	protected /*abstract*/ void onReset() {}
	
	/**
	 * Invoked by the engine when a grid controller button press occurs
	 * 
	 * @param row Row index on the gird
	 * @param col Column index on the grid
	 * @return True if the event was consumed, false otherwise
	 */
	public boolean gridPressed(int row, int col) {
		return false;
	}
	
	/**
	 * Invoked by the engine when a grid controller button release occurs
	 * 
	 * @param row Row index on the gird
	 * @param col Column index on the grid
	 * @return True if the event was consumed, false otherwise
	 */
	public boolean gridReleased(int row, int col) {
		return false;
	}
	
	/**
	 * Invoked by engine when this pattern is focused an a midi note is received.  
	 * 
	 * @param note
	 * @return True if the pattern has consumed this note, false if the top-level
	 *         may handle it
	 */
	public boolean noteOn(rwmidi.Note note) {
		return false;
	}
	
	/**
	 * Invoked by engine when this pattern is focused an a midi note off is received.  
	 * 
	 * @param note
	 * @return True if the pattern has consumed this note, false if the top-level
	 *         may handle it
	 */
	public boolean noteOff(rwmidi.Note note) {
		return false;
	}
	
	/**
	 * Invoked by engine when this pattern is focused an a controller is received  
	 * 
	 * @param note
	 * @return True if the pattern has consumed this controller, false if the top-level
	 *         may handle it
	 */
	public boolean controllerChange(rwmidi.Controller controller) {
		return false;
	}

}
