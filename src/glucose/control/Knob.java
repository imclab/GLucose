package glucose.control;

public interface Knob {
	
	public interface Listener {
		public void onKnobChange(Knob knob);
	}
	
	public double getValue();
	public float getValuef();
	public Knob setValue(double value);
	public String getLabel();
}
