package cs3500.music.controller;

import cs3500.music.model.MusicModel;
import cs3500.music.model.Note;
import cs3500.music.view.*;

/**
 * Created by Viviano on 4/4/2016.
 */
public class MusicController implements NoteView.NotesEditedListener {

  private final MusicModel model;

  private final GuiViewFrame gui;
  private final MidiView midi;

  public MusicController(MusicModel model) {
    this.model = model;

    this.gui = new GuiViewFrame();
    this.midi = new MidiViewImpl();

    ModelDisplayAdapter adapter = new ModelDisplayAdapterImpl(model);

    gui.setAdapter(adapter);
    midi.setAdapter(adapter);
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
}
