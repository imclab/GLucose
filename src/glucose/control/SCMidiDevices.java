package glucose.control;

import glucose.GLucose;

import heronarts.lx.control.LXParameter;
import heronarts.lx.control.MidiParameterController;

import rwmidi.MidiInputDevice;
import rwmidi.RWMidi;

public class SCMidiDevices {
	
	public static final void initializeStandardDevices(GLucose glucose, LXParameter[] patternParameters, LXParameter[] transitionParameters, LXParameter[] effectParameters) {
		for (MidiInputDevice device : RWMidi.getInputDevices()) {
			if ((device.getName().indexOf("nanoKONTROL") != -1) ||
				(device.getName().indexOf("SLIDER/KNOB KORG INC.") != -1)) {
				initializeNanoKontrol(glucose, device, patternParameters);
			}
		}
	}
	
	private static final void initializeNanoKontrol(GLucose glucose, MidiInputDevice device, LXParameter[] patternParameters) {
		System.out.println("MIDI: nanoKontrol initialized");
		MidiParameterController nanoKontrol = new MidiParameterController(device);
		int cc = 14;
		for (LXParameter parameter : patternParameters) {
			nanoKontrol.addCCMapping(cc++, parameter);
		}
	}
}
