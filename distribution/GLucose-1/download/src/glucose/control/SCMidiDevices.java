package glucose.control;

import glucose.GLucose;

import heronarts.lx.control.LXParameter;
import heronarts.lx.control.MidiParameterController;

import rwmidi.MidiInputDevice;
import rwmidi.RWMidi;

public class SCMidiDevices {
	
	public static final void initializeStandardDevices(GLucose glucose) {
		for (MidiInputDevice device : RWMidi.getInputDevices()) {
			String deviceName = device.getName();
			if ((deviceName.indexOf("nanoKONTROL") != -1) ||
				(deviceName.indexOf("SLIDER/KNOB KORG INC.") != -1)) {
				initializeNanoKontrol(glucose, device);
			} else if (deviceName.indexOf("APC40") != -1) {
				initializeAPC40(glucose, device);
			}
		}
	}
	
	private static final void initializeNanoKontrol(GLucose glucose, MidiInputDevice device) {
		System.out.println("MIDI: nanoKontrol initialized");
		MidiParameterController nanoKontrol = new MidiParameterController(device);
		int cc = 14;
		for (LXParameter parameter : glucose.patternKnobs) {
			nanoKontrol.addCCMapping(cc++, parameter);
		}
	}
	
	private static final void initializeAPC40(GLucose glucose, MidiInputDevice device) {
		System.out.println("MIDI: APC40 initialized");
		MidiParameterController apc40 = new MidiParameterController(device);
		int cc = 48;
		for (LXParameter parameter : glucose.patternKnobs) {
			apc40.addCCMapping(cc++, parameter);
		}
		
		cc = 16;
		for (LXParameter parameter : glucose.transitionKnobs) {
			apc40.addCCMapping(cc++, parameter);
		}
		for (LXParameter parameter : glucose.effectKnobs) {
			apc40.addCCMapping(cc++, parameter);
		}
		new APC40Controller(glucose, device);
	}
}
