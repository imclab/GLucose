package glucose.control;

import glucose.GLucose;

import heronarts.lx.control.LXParameter;
import heronarts.lx.control.LXVirtualParameter;

import java.util.List;

public class VirtualEffectKnob extends LXVirtualParameter {
	
	private final GLucose glucose;
	
	private final int index;

	public VirtualEffectKnob(GLucose glucose, int index) {
		this.glucose = glucose;
		this.index = index;
	}

	public LXParameter getRealParameter() {
		List<LXParameter> parameters = glucose.getSelectedEffect().getParameters();
		if (index < parameters.size()) {
			return parameters.get(index);
		}
		return null;
	}
}
