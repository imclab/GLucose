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

import glucose.control.SCMidiDevices;
import glucose.model.Model;
import glucose.pattern.SCPattern;
import glucose.ui.Simulation;

import heronarts.lx.HeronLX;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;

import java.awt.event.KeyEvent;

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
	 * The HeronLX engine running patterns and effects
	 */
	public final HeronLX lx;
	
	/**
	 * The model of the entire car
	 */
	public final Model model;
	
	/**
	 * The simulation renderer
	 */
	public final Simulation simulation;
		
	/**
	 * Creates a GLucose instance.
	 * 
	 * @param applet
	 */
	public GLucose(PApplet applet) {	
		this.applet = applet;
		
		// Build the model of the cubes
		this.model = Model.getInstance();
		this.simulation = new Simulation(this);
		
		// Note that there is currently a dependency between the model
		// and the simulation engine. This uses the OpenGL engine to compute
		// the positions of all the points based upon GL matrix transforms.
		// In the future we should separate these so that the position
		// information is computed independently of the rendering pipeline
		this.simulation.buildGeometry();
		
		// Build an LX instance for pattern and pixel state
		this.lx = new HeronLX(applet, this.model.numPoints(), 1);
		this.lx.enableSimulation(false);

		// Register callback on every frame
		applet.registerDraw(this);
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
	 * Invoked when the sketch shuts down. Close any engine components
	 * that should not keep running.
	 */
	public void dispose() {
	}
			
	/**
	 * Core drawing callback. Engine automatically runs on every frame.
	 */
	public void draw() {
		this.simulation.draw();
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

