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

import glucose.control.VirtualEffectKnob;
import glucose.control.VirtualPatternKnob;
import glucose.control.VirtualTransitionKnob;

import glucose.model.Cube;
import glucose.model.Model;

import glucose.pattern.SCPattern;

import heronarts.lx.LX;
import heronarts.lx.effect.LXEffect;
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
	 * The model of the entire car
	 */
	public final Model model;
		
	/**
	 * Currently selected effect.
	 */
	private int selectedEffectIndex = 0;
	
	/**
	 * Set of available transitions
	 */
	private LXTransition[] transitions;
	
	/**
	 * Currently selected transition
	 */
	private int selectedTransitionIndex = 0;
	
	public static final int NUM_PATTERN_KNOBS = 8;
	public static final int NUM_TRANSITION_KNOBS = 4;
	public static final int NUM_EFFECT_KNOBS = 4;
	
	public static final int LEFT_DECK = 0;
	public static final int RIGHT_DECK = 1;

	public final List<VirtualPatternKnob> patternKnobs;
	public final List<VirtualTransitionKnob> transitionKnobs;
	public final List<VirtualEffectKnob> effectKnobs;
	
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
	public GLucose(PApplet applet, Model model) {	
		this.applet = applet;
		
		// The model of the cubes
		this.model = model;
						
		// Build an LX instance for pattern and pixel state
		this.lx = new LX(applet, this.model.points.size(), 1);
		this.lx.setSimulationEnabled(false);

		List<VirtualPatternKnob> _patternKnobs = new ArrayList<VirtualPatternKnob>(NUM_PATTERN_KNOBS);
		for (int i = 0; i < NUM_PATTERN_KNOBS; ++i) {
			_patternKnobs.add(new VirtualPatternKnob(this, i));
		}
		patternKnobs = Collections.unmodifiableList(_patternKnobs);

		List<VirtualTransitionKnob> _transitionKnobs = new ArrayList<VirtualTransitionKnob>(NUM_PATTERN_KNOBS);
		for (int i = 0; i < NUM_TRANSITION_KNOBS; ++i) {
			_transitionKnobs.add(new VirtualTransitionKnob(this, i));
		}
		transitionKnobs = Collections.unmodifiableList(_transitionKnobs);
		
		List<VirtualEffectKnob> _effectKnobs = new ArrayList<VirtualEffectKnob>(NUM_PATTERN_KNOBS);
		for (int i = 0; i < NUM_EFFECT_KNOBS; ++i) {
			_effectKnobs.add(new VirtualEffectKnob(this, i));
		}
		effectKnobs = Collections.unmodifiableList(_effectKnobs);

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
	 * The current pattern
	 * 
	 * @return The currently running pattern
	 */
	public SCPattern getPattern() {
		return (SCPattern)lx.getPattern();
	}
	
	/**
	 * 
	 * @return The set of available crossfader transitions
	 */
	public LXTransition[] getTransitions() {
		return this.transitions;
	}
	
	/**
	 * Specify the available set of transitions
	 * 
	 * @param transitions
	 */
	public void setTransitions(LXTransition[] transitions) {
		this.transitions = transitions;
		setSelectedTransition(0);
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
	 * Set the selected transition relative to current value
	 * 
	 * @param delta
	 */
	public void incrementSelectedTransitionBy(int delta) {
		selectedTransitionIndex += delta;
		if (selectedTransitionIndex < 0) {
			selectedTransitionIndex += transitions.length;
		}
		setSelectedTransition(selectedTransitionIndex);
	}
	
	/**
	 * Sets the selected transition
	 * 
	 * @param index
	 */
	public void setSelectedTransition(int index) {
		selectedTransitionIndex = index % transitions.length;
		lx.engine.getDeck(RIGHT_DECK).setFaderTransition(getSelectedTransition());
	}
	
	/**
	 * Sets the selected transition to the given transition
	 * 
	 * @param transition
	 */
	public void setSelectedTransition(LXTransition transition) {
		for (int i = 0; i < transitions.length; ++i) {
			if (transition == transitions[i]) {
				setSelectedTransition(i);
				break;
			}
		}
	}
		
	/**
	 * The selected transition
	 * 
	 * @return The currently selected transition
	 */
	public LXTransition getSelectedTransition() {
		return this.transitions[this.selectedTransitionIndex];
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

