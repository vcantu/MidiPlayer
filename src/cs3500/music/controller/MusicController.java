package cs3500.music.controller;

import cs3500.music.model.MusicModel;
import cs3500.music.model.Note;
import cs3500.music.view.*;

import java.awt.event.KeyEvent;

/**
 * Created by Viviano on 4/4/2016.
 */
public class MusicController implements NoteView.NotesEditedListener {

  private final MusicModel model;

  private final GuiViewFrame gui;
  private final MidiView midi;

  private final KeyboardListener keys;

  private Note currNote = null;

  public MusicController(MusicModel model) {
    this.model = model;

    this.gui = new GuiViewFrame();
    this.midi = new MidiViewImpl();

    ModelDisplayAdapter adapter = new ModelDisplayAdapterImpl(model);

    gui.setAdapter(adapter);
    gui.setNotesEditedListener(this);
    midi.setAdapter(adapter);


    keys = new KeyboardListener();
    addArrowKeys();
    addDeleteKey();

    gui.addKeyListener(keys);
  }

  public void start() {
    gui.initialize();
    //midi.initialize();
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
  public void onNoteSelected(Note note) {
    this.currNote = note;
  }

  @Override
  public void onNoteDeselected() {
    this.currNote = null;
  }


  private void addArrowKeys() {
    keys.addKeyPressedListener(KeyEvent.VK_RIGHT, () -> {
      gui.scrollX(5);
    });
    keys.addKeyPressedListener(KeyEvent.VK_LEFT, () -> {
      gui.scrollX(-5);
    });

    keys.addKeyPressedListener(KeyEvent.VK_UP, () -> {
      gui.scrollY(5);
    });
    keys.addKeyPressedListener(KeyEvent.VK_DOWN, () -> {
      gui.scrollY(-5);
    });
  }

  private void addDeleteKey() {
    keys.addKeyPressedListener(KeyEvent.VK_DELETE, () -> {
      model.removeNote(currNote);
      gui.initialize();
    });
  }
}
