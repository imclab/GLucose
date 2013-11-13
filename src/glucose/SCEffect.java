package glucose;

import glucose.model.Model;

import heronarts.lx.effect.LXEffect;

public abstract class SCEffect extends LXEffect {
	
	protected final GLucose glucose;
	protected final Model model;
	
	protected SCEffect(GLucose glucose) {
		this(glucose, false);
	}
	
	protected SCEffect(GLucose glucose, boolean momentary) {
		super(glucose.lx, momentary);
		this.glucose = glucose;
		this.model = glucose.model;
	}
}
