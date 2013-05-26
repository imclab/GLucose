package glucose.transition;

import glucose.GLucose;

import heronarts.lx.transition.LXTransition;

public abstract class SCTransition extends LXTransition {
	protected final GLucose glucose;
	
	protected SCTransition(GLucose glucose) {
		super(glucose.lx);
		System.out.println("constructed SCTransition");
		this.glucose = glucose;
	}
}
