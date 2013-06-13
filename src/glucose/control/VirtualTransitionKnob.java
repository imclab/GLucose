package glucose.control;

import glucose.GLucose;

import heronarts.lx.control.LXParameter;
import heronarts.lx.control.LXVirtualParameter;

import java.util.List;

public class VirtualTransitionKnob extends LXVirtualParameter {

	private final GLucose glucose;
	private final int index;

	public VirtualTransitionKnob(GLucose glucose, int index) {
		this.glucose = glucose;
		this.index = index;
	}

	public LXParameter getRealParameter() {
		List<LXParameter> parameters = glucose.getSelectedTransition().getParameters();
		if (index < parameters.size()) {
			return parameters.get(index);
		}
		return null;
	}
}
