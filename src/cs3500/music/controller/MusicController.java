package cs3500.music.controller;

import cs3500.music.model.MusicModel;
import cs3500.music.model.Note;
import cs3500.music.view.*;

import java.awt.event.KeyEvent;

/**
 * Created by Viviano on 4/4/2016.
 */
public class MusicController implements NoteView.NotesEditedListener,
        MidiView.OnTickListener {

  private static final long MAX_TEMPO = 2000000;
    private static final long MIN_TEMPO = 10000;

  private final MusicModel model;

  private final GuiViewFrame gui;
  private final MidiView midi;

  private final KeyboardListener keys;

  private long tempo;

  public MusicController(MusicModel model) {
    this.model = model;

    ModelDisplayAdapter adapter = new ModelDisplayAdapterImpl(model);
    this.gui = new GuiViewFrame();
    gui.setAdapter(adapter);
    gui.setNotesEditedListener(this);

    this.midi = new MidiViewImpl();
    midi.setAdapter(adapter);
    midi.setOnTickListener(this);
    midi.setTickResolution(10);
    this.tempo = model.getTempo();
    midi.setTempo(tempo);

    keys = new KeyboardListener();
    addKeys();
    gui.addKeyListener(keys);
  }

  public void start() {
    gui.initialize();
    midi.initialize();
  }


  @Override
  public void onNoteRemoved(Note note) {
    model.removeNote(note);
  }

  @Override
  public void onNoteAdded(Note note) {
    model.addNote(note);
  }

  @Override
  public void onBeatChanged(double beat) {
    midi.setBeat((int)beat);
  }

  /**
   * Called every tick while playing
   *
   * @param beat     the current beat of the song
   * @param progress the progress from [0, 100) progress will be 0 at the
   *                 moment a beat is played otherwise it represents the
   */
  @Override
  public void onTick(int beat, int progress) {
    gui.setBeat(beat + progress / 100d);
  }



  private void addKeys() {
    addArrowKeys();
    addTempoKeys();
    //play pause
    keys.addKeyPressedListener(KeyEvent.VK_SPACE, () -> {
      if (!midi.isPlaying())
        midi.play();
      else
        midi.pause();
    });
    keys.addKeyPressedListener(KeyEvent.VK_HOME, () -> {
      midi.setBeat(0);
      gui.setBeat(0);
    });
    keys.addKeyPressedListener(KeyEvent.VK_END, () -> {
      midi.setBeat(model.getLength());
      gui.setBeat(model.getLength());
    });

    keys.addKeyPressedListener(KeyEvent.VK_A, () -> {
      gui.toggleAddMode(!gui.isAddingNotes());
    });

  }

  private void addArrowKeys() {
    keys.addKeyPressedListener(KeyEvent.VK_RIGHT, () -> {
      gui.scrollX(5);
    });
    keys.addKeyPressedListener(KeyEvent.VK_LEFT, () -> {
      gui.scrollX(-5);
    });

    keys.addKeyPressedListener(KeyEvent.VK_UP, () -> {
      gui.scrollY(-5);
    });
    keys.addKeyPressedListener(KeyEvent.VK_DOWN, () -> {
      gui.scrollY(5);
    });
  }

  private void addTempoKeys() {
    keys.addKeyPressedListener(KeyEvent.VK_EQUALS, () -> {
      tempo -= 10000;
      if (tempo < MIN_TEMPO)
        tempo = MIN_TEMPO;
      midi.setTempo(tempo);
    });
    keys.addKeyPressedListener(KeyEvent.VK_MINUS, () -> {
      tempo += 10000;
      if (tempo > MAX_TEMPO)
        tempo = MAX_TEMPO;
      midi.setTempo(tempo);
    });
    keys.addKeyPressedListener(KeyEvent.VK_BACK_SPACE, () -> {
      tempo = model.getTempo();
      midi.setTempo(tempo);
    });
  }


  MusicModel getModel() {
    return model;
  }

  GuiViewFrame getGui() {
    return gui;
  }

  MidiView getMidi() {
    return midi;
  }

  KeyboardListener getKeyboardListener() {
    return keys;
  }

  public long getTempo() {
    return tempo;
  }
}
