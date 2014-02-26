/**
 * ##library.name##
 * ##library.sentence##
 * ##library.url##
 *
 * Copyright ##copyright## ##author##
 * All Rights Reserved
 * 
 * @author      ##author##
 * @modified    ##date##
 * @version     ##library.prettyVersion## (##library.version##)
 */

package glucose;

import heronarts.lx.LX;
import heronarts.lx.effect.LXEffect;
import heronarts.lx.model.LXModel;
import heronarts.lx.pattern.LXPattern;
import heronarts.lx.transition.LXTransition;

import processing.core.PApplet;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Core engine library for the sugar cubes system.
 */
public class GLucose {
	
	public final static String VERSION = "##library.prettyVersion##";

	/**
	 * Returns the version of the library.
	 * 
	 * @return String
	 */
	public static String version() {
		return VERSION;
	}
	
	/**
	 * A reference to the applet context.
	 */
	public final PApplet applet;
	
	/**
	 * The LX engine running patterns and effects
	 */
	public final LX lx;
			
	/**
	 * Currently selected effect.
	 */
	private int selectedEffectIndex = 0;
		
	public static final int LEFT_DECK = 0;
	public static final int RIGHT_DECK = 1;
	
	private final List<EffectListener> effectListeners = new ArrayList<EffectListener>();
	
	public interface EffectListener {
		public void effectSelected(LXEffect effect);
	}
	
	public final GLucose addEffectListener(EffectListener listener) {
		this.effectListeners.add(listener);
		return this;
	}
	
	public final GLucose removeEffectListener(EffectListener listener) {
		this.effectListeners.remove(listener);
		return this;
	}
	
	/**
	 * Creates a GLucose instance.
	 * 
	 * @param applet
	 */
	public GLucose(PApplet applet, LXModel model) {	
		this.applet = applet;
								
		// Build an LX instance for pattern and pixel state
		this.lx = new LX(applet, model);
		this.lx.setSimulationEnabled(false);

	}
	
	/**
	 * Helper to get all the current colors on the structure
	 * 
	 * @return Array of all the colors ordered by point index
	 */
	public int[] getColors() {
		return lx.getColors();
	}
	
	/**
	 * The selected effect
	 * 
	 * @return The currently selected effect
	 */
	public LXEffect getSelectedEffect() {
		return lx.getEffects().get(this.selectedEffectIndex);
	}
	
	/**
	 * Increments the selected effect by an offset
	 * 
	 * @param delta
	 */
	public void incrementSelectedEffectBy(int delta) {
		selectedEffectIndex += delta;
		if (selectedEffectIndex < 0) {
			selectedEffectIndex += lx.getEffects().size();
		}
		setSelectedEffect(selectedEffectIndex);
	}
	
	/**
	 * Sets the selected effect
	 * 
	 * @param index
	 */
	public void setSelectedEffect(int index) {
		selectedEffectIndex = index % lx.getEffects().size();
		LXEffect selectedEffect = getSelectedEffect();
		for (EffectListener listener : this.effectListeners) {
			listener.effectSelected(selectedEffect);
		}
	}
	
	public void setSelectedEffect(LXEffect effect) {
		int i = 0;
		for (LXEffect fx : lx.getEffects()) {
			if (fx == effect) {
				setSelectedEffect(i);
			}
			++i;
		}
	}

	/**
	 * Invoked when the sketch shuts down. Close any engine components
	 * that should not keep running.
	 */
	public void dispose() {
	}

	/**
	 * Utility logging function
	 * 
	 * @param s Logs the string with relevant prefix
	 */
	private static void log(String s) {
		System.out.println("GLucose: " + s);
	}

}

