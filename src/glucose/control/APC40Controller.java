package glucose.control;

import glucose.GLucose;

import heronarts.lx.HeronLX;
import heronarts.lx.effect.LXEffect;

import rwmidi.Note;
import rwmidi.MidiInputDevice;

public class APC40Controller {
	
	private final GLucose glucose;
	private boolean shiftOn = false;
	private LXEffect releaseEffect;
	
	APC40Controller(GLucose glucose, MidiInputDevice device) {
		this.glucose = glucose;
		device.createInput(this);
	}
	
	public void noteOnReceived(Note note) {
		final HeronLX lx = glucose.lx; 
		switch (note.getPitch()) {
		case 94: // right bank
			if (!shiftOn) {
				glucose.incrementSelectedEffectBy(-1);
			} else {
				glucose.incrementSelectedTransitionBy(-1);
			}
			break;
		case 95: // left bank
			if (!shiftOn) {
				glucose.incrementSelectedEffectBy(1);
			} else {
				glucose.incrementSelectedTransitionBy(1);
			}
			break;
		case 96: // up bank
			lx.goNext();
			break;
		case 97: // down bank
			lx.goPrev();
			break;
			
		case 98: // shift
			shiftOn = true;
			break;
			
		case 99: // tap tempo
			lx.tempo.tap();
			break;
		case 100: // nudge+
			lx.tempo.setBpm(lx.tempo.bpm() + (shiftOn ? 1 : .1));
			break;
		case 101: // nudge-
			lx.tempo.setBpm(lx.tempo.bpm() - (shiftOn ? 1 : .1));
			break;
			
		case 91: // play
		case 93: // rec
			releaseEffect = glucose.getSelectedEffect(); 
			releaseEffect.enable();
			break;
			
		case 92: // stop
			glucose.getSelectedEffect().disable();
			break;
		}
	}
	
	public void noteOffReceived(Note note) {
		switch (note.getPitch()) {
		case 93: // rec
			if (releaseEffect != null) {
				releaseEffect.disable();
			}
			break;
		
		case 98: // shift
			shiftOn = false;
			break;
		}
	}
}
