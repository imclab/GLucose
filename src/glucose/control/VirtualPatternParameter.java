package glucose.control;

import glucose.GLucose;

import heronarts.lx.control.LXParameter;
import heronarts.lx.control.LXVirtualParameter;

import java.util.List;

public class VirtualPatternParameter extends LXVirtualParameter {
	
	private final GLucose glucose;
	private final int index;
	
	public VirtualPatternParameter(GLucose glucose, int index) {
		this.glucose = glucose;
		this.index = index;
	}
	
	@Override
	protected LXParameter getRealParameter() {
		List<LXParameter> parameters = glucose.getPattern().getParameters();
		if (index < parameters.size()) {
			return parameters.get(index);
		}
		return null;
	}	
}
