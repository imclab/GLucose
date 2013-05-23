package glucose.control;

import glucose.control.Knob.Listener;

public class BasicKnob implements Knob {
	private Listener listener;
	private final String label;
	private double value;
	
	public BasicKnob(String label) {
		this(null, label);
	}
	
	public BasicKnob(String label, double value) {
		this(null, label, value);
	}
	
	public BasicKnob(Listener listener, String label) {
		this(listener, label, 0);
	}
	
	public BasicKnob(Listener listener, String label, double value) {
		this.listener = listener;
		this.label = label;
		this.value = value;
	}
	
	public String getLabel() {
		return this.label;
	}
	
	public Knob setListener(Listener listener) {
		this.listener = listener;
		return this;
	}
	
	public Knob setValue(double value) {
		if (this.value == value) {
			return this;
		}
		if (value < 0) {
			this.value = 0;
		} else if (value > 1) {
			this.value = 1;
		} else {
			this.value = value;
		}
		listener.onKnobChange(this);
		return this;
	}
		
	public double getValue() {
		return value;
	}
	
	public float getValuef() {
		return (float)getValue();
	}

}
