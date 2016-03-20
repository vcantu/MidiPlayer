package cs3500.music.view;

import cs3500.music.model.Beat;
import cs3500.music.model.Note;
import cs3500.music.model.Range;

import javax.sound.midi.*;

/**
 * A skeleton for MIDI playback
 */
public class MidiViewImpl implements MidiView {

  private ModelDisplayAdapter model;
  private final Synthesizer synth;
  private final Receiver receiver;

  public MidiViewImpl() {
    Synthesizer tempSynth = null;
    Receiver tempRec = null;
    try {
      tempSynth = MidiSystem.getSynthesizer();
      tempRec = tempSynth.getReceiver();
      tempSynth.open();
    } catch (MidiUnavailableException e) {
      e.printStackTrace();
    }
    synth = tempSynth;
    receiver = tempRec;
  }

  public void playNote(Note note, int tempo) throws InvalidMidiDataException {
    MidiMessage start = new ShortMessage(ShortMessage.NOTE_ON, note.getInstrument(),
            note.getPitch().ordinal(), note.getVolume());
    MidiMessage stop = new ShortMessage(ShortMessage.NOTE_OFF, note.getInstrument(),
            note.getPitch().ordinal(), note.getVolume());
    this.receiver.send(start, -1);
    this.receiver.send(stop, this.synth.getMicrosecondPosition() + tempo * note.getDuration());
  }

  /**
   * Will be called in order to displaying or play the view
   * <p>
   * Use this to initialize or reset any data that must be reset
   */
  @Override
  public void initialize() {
    run();
  }

  /**
   * Set the model which will include the content this view will show
   *
   * @param model new content of this view
   */
  @Override
  public void setModel(ModelDisplayAdapter model) {
    this.model = model;
  }

  /**
   * When an object implementing interface <code>Runnable</code> is used
   * to create a thread, starting the thread causes the object's
   * <code>run</code> method to be called in that separately executing
   * thread.
   * <p>
   * The general contract of the method <code>run</code> is that it may
   * take any action whatsoever.
   *
   * @see Thread#run()
   */
  @Override
  public void run() {
    if (model == null)
      return;

    for (int beat = 0; beat < model.getLength(); beat++){
      if (model.hasNoteAt(beat)) {
        Beat b = model.getBeatAt(beat);
        for (Note n : b) {
          if (n.getBeat() == beat) {
            try {
              playNote(n, model.getTempo());
            } catch (InvalidMidiDataException e) {
              e.printStackTrace();
            }
          }
        }
      }
      try {
        Thread.sleep(model.getTempo() / 1000);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    this.receiver.close();
  }
}
