package glucose.control;

import glucose.GLucose;

import java.util.List;

public class VirtualPatternKnob implements Knob {
	
	private final GLucose glucose;
	private final int index;
	
	public VirtualPatternKnob(GLucose glucose, int index) {
		this.glucose = glucose;
		this.index = index;
	}
	
	private Knob getKnob() {
		List<Knob> knobs = glucose.getPattern().getKnobs();
		if (index < knobs.size()) {
			return knobs.get(index);
		}
		return null;
	}
	
	public String getLabel() {
		Knob k = getKnob();
		if (k != null) {
			return k.getLabel();
		}
		return "";
	}
	
	public Knob setValue(double value) {
		Knob k = getKnob();
		if (k != null) {
			k.setValue(value);
		}
		return this;
	}
	
	public double getValue() {
		Knob k = getKnob();
		if (k != null) {
			return k.getValue();
		}
		return 0;
	}
	
	public float getValuef() {
		return (float)getValue();
	}
}
