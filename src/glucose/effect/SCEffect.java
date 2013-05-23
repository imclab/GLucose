package glucose.effect;

import glucose.GLucose;

import heronarts.lx.effect.LXEffect;

public abstract class SCEffect extends LXEffect {
	
	protected final GLucose glucose;
	
	protected SCEffect(GLucose glucose) {
		super(glucose.lx);
		this.glucose = glucose;
	}
}
