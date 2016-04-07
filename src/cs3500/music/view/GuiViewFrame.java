package cs3500.music.view;

import javax.swing.*;
import java.awt.*;

/**
 * A skeleton Frame (i.e., a window) in Swing
 */
public class GuiViewFrame extends JFrame implements MusicView {

  private final NoteView noteView; // You may want to refine this to a subtype of JPanel
  private ModelDisplayAdapter adapter;

  /**
   * Creates new GuiView
   */
  public GuiViewFrame() {
    this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    this.noteView = new NoteView();

    getContentPane().setLayout(new BorderLayout());
    this.getContentPane().add(noteView);

    this.pack();
  }

  /**
   * Will be called before displaying or playing the view
   *
   * Use this to initialize or reset any data that must be reset
   */
  @Override
  public void initialize(){
    this.setVisible(true);
  }

  /**
   * Set the model which will include the content this view will show
   *
   * @param adapter new content of this view
   */
  @Override
  public void setAdapter(ModelDisplayAdapter adapter) {
    this.adapter = adapter;
    this.noteView.setAdapter(this.adapter);
  }

  @Override
  public Dimension getPreferredSize(){
    return new Dimension(1500, 500);
  }


  /**
   * Relay methods
   */

  public void setBeat(double beat) {
    noteView.setBeat(beat);
  }

  public void setDrawBeat(int beat) {
    noteView.setDrawBeat(beat);
  }

  public void setAddingNote(boolean state) {
    noteView.setAddingNote(state);
  }

  public boolean isAddingNotes() {
    return noteView.isAddingNotes();
  }

  public void scrollX(int deltaX) {
    noteView.incrementX(deltaX);
  }

  public void scrollY(int deltaY) {
    noteView.incrementY(deltaY);
  }

  public void setNotesEditedListener(NoteView.NotesEditedListener listener) {
    this.noteView.setNotesEditedListener(listener);
  }
}
