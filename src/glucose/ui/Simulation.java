package glucose.ui;

import glucose.GLucose;
import glucose.model.Cube;
import glucose.model.Model;
import glucose.model.Point;

import javax.media.opengl.GL; 
import java.awt.event.MouseEvent;

import processing.opengl.PGraphicsOpenGL;

public class Simulation {
	
	private final GLucose glucose;
	
	// Rendering environment
	private final PGraphicsOpenGL pgl;
	private final GL gl;

	// Camera state
	private float xt=-121.f, yt=-57.f, zt=177.f, xr=-0.54f, yr=-0.33f, zr=-3.14f;

	// Mouse camera interaction
	private int mouseX, mouseY;
	
	public Simulation(GLucose glucose) {
		this.glucose = glucose;
		glucose.applet.registerMouseEvent(this);
		
		// Initialize the GL context variables
		pgl = (PGraphicsOpenGL) glucose.applet.g;
		gl = pgl.beginGL();
		pgl.endGL();
	}
	
	public PGraphicsOpenGL getPGL() {
		return pgl;
	}
	
	public void mouseEvent(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		// TODO(mcslee): un-hardcode overlay width here
		if (x >= glucose.applet.width - 144) {
			return;
		}
		if (e.getID() == MouseEvent.MOUSE_DRAGGED) {
			int dx = x - mouseX;
			int dy = y - mouseY;
			xt += dx;
			zr += dx/100.;
			zt += dy;
		}
		mouseX = x;
		mouseY = y;
	}
	
	public void draw() {
		glucose.applet.background(50);
				
		glucose.applet.translate(glucose.applet.width/2.f+xt, glucose.applet.height/2.f+yt, 150.f+zt);
		glucose.applet.rotateX(-0.9299996f + xr);
		glucose.applet.rotateY(0.32999998f + yr);
		glucose.applet.rotateZ(1.559999f + zr);
		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glPushMatrix();
		gl.glLoadIdentity();
		gl.glPointSize(3);
		gl.glEnable(GL.GL_POINT_SMOOTH);
		pgl.beginGL();
		drawFloor();
		glucose.model.draw(gl, glucose.getColors(), false);
		pgl.endGL();
		gl.glPopMatrix();
		
		// Reset camera and depth buffer for 2d overlay UI
		glucose.applet.camera();
		gl.glClear(GL.GL_DEPTH_BUFFER_BIT);
		// Theoretically faster with this? But it makes text and images blocky
		// gl.glDisable(GL.GL_BLEND);
		try {
			glucose.applet.getClass().getMethod("drawUI").invoke(glucose.applet);
		} catch (Exception x) {
			x.printStackTrace();
		}

	}
	
	private void drawFloor() {
		gl.glBegin(GL.GL_POLYGON);
		gl.glColor4d(1, 1, 1, 0.25);
		gl.glVertex3f(0, 0, -6);
		gl.glVertex3f(0, 192, -6);
		gl.glVertex3f(82, 192, -6);
		gl.glVertex3f(82, 0, -6);
		gl.glEnd();
	}
	
}
