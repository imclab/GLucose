package glucose;

import glucose.model.Model;

import heronarts.lx.transition.LXTransition;

public abstract class SCTransition extends LXTransition {
	protected final GLucose glucose;
	protected final Model model;
	
	protected SCTransition(GLucose glucose) {
		super(glucose.lx);
		this.glucose = glucose;
		this.model = glucose.model;
	}
}
