package glucose.control;

import glucose.GLucose;

import java.util.Map;
import java.util.HashMap;

import rwmidi.Controller;
import rwmidi.Note;
import rwmidi.RWMidi;
import rwmidi.MidiInputDevice;

public class MidiKnobController {
	
	private final MidiInputDevice device;
	private final Map<Integer, Knob> ccMappings = new HashMap<Integer, Knob>();
	private final Map<Integer, Knob> noteMappings = new HashMap<Integer, Knob>();
	
	public MidiKnobController(MidiInputDevice device) {
		this.device = device;
		this.device.createInput(this);
	}
	
	public MidiKnobController addCCMapping(int cc, Knob knob) {
		ccMappings.put(cc, knob);
		return this;
	}
	
	public MidiKnobController addNoteMapping(int noteNum, Knob knob) {
		noteMappings.put(noteNum, knob);
		return this;
	}
	
	public void controllerChangeReceived(Controller controller) {
		if (ccMappings.containsKey(controller.getCC())) {
			Knob knob = ccMappings.get(controller.getCC());
			knob.setValue(controller.getValue() / 127.);
		} else {
			System.out.println("Unmapped CC: " + controller.getCC());
		}
	}
	
	public void noteOnReceived(Note note) {
		if (noteMappings.containsKey(note.getPitch())) {
			Knob knob = noteMappings.get(note.getPitch());
			knob.setValue(note.getVelocity() / 127.);
		}
	}
	
	public void noteOffReceived(Note note) {
		if (noteMappings.containsKey(note.getPitch())) {
			Knob knob = noteMappings.get(note.getPitch());
			knob.setValue(0);
		}
	}
	
	public static final void initializeStandardDevices(GLucose glucose) {
		for (MidiInputDevice device : RWMidi.getInputDevices()) {
			if (device.getName().indexOf("nanoKONTROL") != -1) {
				initializeNanoKontrol(glucose, device);
			} else if (device.getName().indexOf("SLIDER/KNOB KORG INC.") != -1) {
				initializeNanoKontrol(glucose, device);
			}
		}
	}
	
	private static final void initializeNanoKontrol(GLucose glucose, MidiInputDevice device) {
		System.out.println("MIDI: nanoKontrol initialized");
		MidiKnobController nanoKontrol = new MidiKnobController(device);
		int cc = 14;
		for (VirtualPatternKnob knob : glucose.patternKnobs) {
			nanoKontrol.addCCMapping(cc++, knob);
		}
	}
}
