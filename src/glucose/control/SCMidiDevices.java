package glucose.control;

import glucose.GLucose;

import heronarts.lx.control.MidiParameterController;

import rwmidi.MidiInputDevice;
import rwmidi.RWMidi;

public class SCMidiDevices {
	
	public static final void initializeStandardDevices(GLucose glucose) {
		for (MidiInputDevice device : RWMidi.getInputDevices()) {
			if (device.getName().indexOf("nanoKONTROL") != -1) {
				initializeNanoKontrol(glucose, device);
			} else if (device.getName().indexOf("SLIDER/LXParameter KORG INC.") != -1) {
				initializeNanoKontrol(glucose, device);
			}
		}
	}
	
	private static final void initializeNanoKontrol(GLucose glucose, MidiInputDevice device) {
		System.out.println("MIDI: nanoKontrol initialized");
		MidiParameterController nanoKontrol = new MidiParameterController(device);
		int cc = 14;
		for (VirtualPatternParameter parameter : glucose.patternKnobs) {
			nanoKontrol.addCCMapping(cc++, parameter);
		}
	}
}
