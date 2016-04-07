package cs3500.music.view;

import cs3500.music.model.Beat;
import cs3500.music.model.Note;
import cs3500.music.model.Range;

import javax.sound.midi.*;

/**
 * A skeleton for MIDI playback
 */
public class MidiViewImpl implements MidiView, Runnable {

  private ModelDisplayAdapter model;
  private final Synthesizer synth;
  private final Receiver receiver;

  private long tempo = 2000;

  private boolean playing = false;
  private int currBeat = 0;

  private OnTickListener listener;
  private int resolution = 25;


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

  public void playNote(Note note, int tempo) {
    try {
      MidiMessage start = new ShortMessage(ShortMessage.NOTE_ON, note.getInstrument() - 1,
              note.getPitch().ordinal(), note.getVolume());
      MidiMessage stop = new ShortMessage(ShortMessage.NOTE_OFF, note.getInstrument() - 1,
              note.getPitch().ordinal(), note.getVolume());

      this.receiver.send(start, -1);
      this.receiver.send(stop, this.synth.getMicrosecondPosition() + tempo * note.getDuration());
    } catch (InvalidMidiDataException e) {
      e.printStackTrace();
    }
  }

  /**
   * Will be called in order to displaying or play the view
   * <p>
   * Use this to initialize or reset any data that must be reset
   */
  @Override
  public void initialize() {
    play();
  }

  /**
   * Set the model which will include the content this view will show
   *
   * @param adapter new content of this view
   */
  @Override
  public void setAdapter(ModelDisplayAdapter adapter) {
    this.model = adapter;
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

    playing = true;

    for (; currBeat < model.getLength(); currBeat++){
      if (!playing) {
        break;
      }
      if (model.hasNoteAt(currBeat)) {
        Beat b = model.getBeatAt(currBeat);
        for (Note n : b) {
          if (n.getBeat() == currBeat)
              playNote(n, (int) tempo);
        }
      }
      try {

        if (listener != null)
          listener.onTick(currBeat, 0);//first tick
        double f = resolution / 100d;
        long milli = tempo / 1000;
        long nano = tempo % 1000;

        for (int i = resolution; i <= 100; i += resolution) {
          if (listener != null)
            listener.onTick(currBeat, i);
          Thread.sleep((long)(milli * f), (int)(nano * f));
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    playing = false;
  }

  /**
   * Set this song's tempo
   *
   * @param tempo
   */
  @Override
  public void setTempo(long tempo) {
    this.tempo = tempo;
  }

  /**
   * Begins playing the song
   */
  @Override
  public void play() {
    if (!playing) {
      Thread t = new Thread(this);
      t.start();
    }
  }

  /**
   * Pauses the song
   */
  @Override
  public void pause() {
    playing = false;
  }

  /**
   * @return true if this view is currently playing, false if paused or stopped
   */
  @Override
  public boolean isPlaying() {
    return playing;
  }

  /**
   * Sets the starting beat of this song
   *
   * @param beat
   */
  @Override
  public void setBeat(int beat) {
    currBeat = beat;
  }

  /**
   * If an onTickListener has been set you can set the resolution of the ticks
   * The 100 / resolution is the amount of times the ticks will be called
   * between beats
   * <p>
   * I.E. if the resolution is 1 onTick will be called 100 times per beat
   * if the resolution is 100 onTick will be called 1 time per beat
   * if the resolution is 50 onTick will be called 2 times per beat
   * <p>
   * If no resolution is set the default resolution should be 25
   *
   * @param resolution a number from [1, 100]
   * @throws IllegalArgumentException if resolution is not within [1, 100]
   */
  @Override
  public void setTickResolution(int resolution) throws IllegalArgumentException {
    if (resolution < 1 || resolution > 100)
      throw new IllegalArgumentException("resolution out of the [1, 100] bounds: "
              + resolution);
    this.resolution = resolution;
  }

  /**
   * Set this to register an OnTickListener
   *
   * @param listener listener to register
   */
  @Override
  public void setOnTickListener(OnTickListener listener) {
    this.listener = listener;
  }
}
