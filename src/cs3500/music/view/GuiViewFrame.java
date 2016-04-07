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
    return new Dimension(1500, 800);
  }


  /**
   * Set this views current beat
   *
   * @param beat
   */
  public void setBeat(double beat) {
    noteView.setBeat(beat);
  }

  /**
   * Sets the current red line beat
   *
   * @param beat beat to set decimal because it can be set in between
   * @param forceView force the view to look at it
   */
  public void setBeat(double beat, boolean forceView) {
    noteView.setBeat(beat, forceView);
  }

  /**
   * Set this views viewable beat
   *
   * @param beat
   */
  public void setViewBeat(int beat) {
    this.noteView.setDrawBeat(beat);
  }

  /**
   * Call this to switch the view between add mode and move mode
   *
   * @param status true if add mode false if move mode
   */
  public void toggleAddMode(boolean status) {
    noteView.toggleAddMode(status);
  }

  /**
   * @return whether this view is in add mode or note
   */
  public boolean isAddingNotes() {
    return noteView.isAddingNotes();
  }

  /**
   * Scroll the view by deltaX pixels
   *
   * @param deltaX amount of pixels
   */
  public void scrollX(int deltaX) {
    noteView.incrementX(deltaX);
  }

  /**
   * Scroll the view by deltaY pixels
   *
   * @param deltaY amount of pixels
   */
  public void scrollY(int deltaY) {
    noteView.incrementY(deltaY);
  }

  /**
   * Set this in order for the view to work properly
   * @param listener
   */
  public void setNotesEditedListener(NoteView.NotesEditedListener listener) {
    this.noteView.setNotesEditedListener(listener);
  }
}
