package cs3500.music.view;

import cs3500.music.model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Objects;

/**
 * Created by Viviano on 3/18/2016.
 */
public class NoteView extends JPanel implements MouseListener, MouseMotionListener {

  private ModelDisplayAdapter adapter;
  private final int stepH = 20, stepW = 20;
  //padding
  private int paddingLeft = 40, paddingRight = 20,
          paddingTop = 40, paddingBottom = 20;
  private int currBeat = 0, currPitch = 0;
  private int offX = 0, offY = 0;

  //floater fields
  private int mouseX = -1, mouseY = -1, diffX = -1, diffY = -1;
  private Note floater;
  private boolean onDelete = false;

  //listener
  private NotesEditedListener listener;

  private double beat = 0;

  private boolean addingNote = false;
  private boolean drawAddNote = false;
  private int addBeat = 0, addWidth = 0;
  private Pitch addPitch;

  //move ticker
  private boolean followingTick = false;

  /**
   * Creates a new <code>JPanel</code> with a double buffer
   * and a flow layout.
   */
  public NoteView() {
    addMouseListener(this);
    addMouseMotionListener(this);
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    if (adapter == null)
      return;

    Graphics2D g2d = (Graphics2D)g;

    drawNoteGrid(4, g2d);
    drawLine(g2d);
    drawNoteRange(0, paddingTop, g2d);
    drawBeatNums(paddingLeft, paddingTop, 8, g2d);

    drawBuilder(g2d);
    drawDelete(g2d);
    drawFloater(g2d);
  }

  private void drawBuilder(Graphics2D g2d) {
    if (drawAddNote) {
      int left = leftFromBeat(this.addBeat);
      int w = addWidth;

       Range r = adapter.getRange();
      int y = r.max.ordinal() - addPitch.ordinal();


      int top = topFromPitch(y);
      int h = topFromPitch(y + 1) - top;


      g2d.setColor(Color.BLUE);
      g2d.fillRect(left, top, stepW, h);

      if (w > stepW) {
        g2d.setColor(Color.GREEN);
        g2d.fillRect(left + stepW, top, w - stepW, h);
      }
    }
  }

  private void drawLine(Graphics2D g2d) {
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);

    int x = (int)(paddingLeft + (beat - currBeat) * stepW) - offX;

    if (x >= paddingLeft && x <= getWidth() - paddingRight) {
      g2d.setStroke(new BasicStroke(5));
      g2d.setColor(Color.RED);
      Range r = adapter.getRange();
      g2d.drawLine(x, paddingTop + 1, x,
              Math.min(getHeight() - paddingBottom - 2,
                      paddingTop + (r.length() - currPitch)
                              * stepH - offY - 2));

      g2d.fillOval(x - 10, paddingTop - 10, 20, 20);
      g2d.setStroke(new BasicStroke(1));
    }

  }

  private void drawDelete(Graphics2D g2d) {
    if (floater != null) {
      if (!onDelete)
        g2d.setColor(new Color(238, 99, 99));
      else
        g2d.setColor(Color.RED);
      g2d.setStroke(new BasicStroke(5));
      int w = this.getWidth();
      int h = this.getHeight();
      g2d.drawLine(w - 40, h - 10, w - 10, h - 40);
      g2d.drawLine(w - 10, h - 10, w - 40, h - 40);
    }
  }

  /**
   * Draws the floating note on the mouse position
   *
   * @param g2d
   */
  private void drawFloater(Graphics2D g2d) {
    if (floater != null) {
      g2d.setColor(Color.BLUE);
      g2d.fillRect(mouseX - diffX, mouseY - diffY, stepW, stepH);
      if (floater.getDuration() > 1)
        g2d.setColor(Color.GREEN);
        g2d.fillRect(mouseX - diffX + stepW, mouseY - diffY,
                stepW * (floater.getDuration() - 1), stepH);
    }
  }

  /**
   * Draws the Pitch ranges
   *
   * @param g2d
   */
  private void drawNoteRange(int startX, int startY, Graphics2D g2d) {
    g2d.setColor(Color.BLACK);
    Range range = adapter.getRange();
    int rangeLen = range.length();
    range.setReverse(true);

    for (int y = currPitch; y < rangeLen &&
            (y - currPitch) * stepH < getHeight(); y++) {
      Pitch p = Pitch.values()[range.max.ordinal() - y];
      int top = startY + stepH * (y - currPitch) + (stepH / 2) + 4 - offY;
      if (top < getHeight() - paddingBottom + stepH / 2)
        g2d.drawString(p.toString(), startX, top);
    }
  }

  /**
   * Draws the beat numbers
   *
   * @param every
   * @param g2d
   */
  private void drawBeatNums(int startX, int startY, int every, Graphics2D g2d) {
    g2d.setColor(Color.BLACK);
    for (int x = currBeat; x < adapter.getLength() &&
            (x - currBeat) * stepW < getWidth(); x++) {
      if (x % every == 0) {
        int left = startX + stepH * (x - currBeat);
        if (x - currBeat > 0)
          left -= offX;

        if (leftFromBeat(x + 1) < getWidth() - paddingRight)
          drawCenteredString(x + "", left, paddingTop - 5, g2d);
      }
    }
  }

  //**
  //helper for drawBeatNums
  public void drawCenteredString(String s, int x, int y, Graphics g) {
    FontMetrics fm = g.getFontMetrics();
    x -= fm.stringWidth(s) / 2;
    g.drawString(s, x, y);
  }


  //**
  //Small helper for drawNoteGrid
  private int leftFromBeat(int x) {
    int left = paddingLeft + (x - currBeat) * stepW;
    if (x - currBeat > 0)
      left -= offX;
    return left;
  }

  //**
  //Small helper for drawNoteGrid
  private int topFromPitch(int y) {
    int top = paddingTop + (y - currPitch) * stepH;
    if (y - currPitch > 0)
      top -= offY;
    return top;
  }

  /**
   * Draws the note grid
   *
   * @param splitEvery
   * @param g2d
   */
  private void drawNoteGrid(int splitEvery, Graphics2D g2d) {
    Range range = adapter.getRange();
    int rangeLen = range.length();

    int leftBound = paddingLeft;
    int topBound = paddingTop;
    int rightBound = Math.min(getWidth() - paddingLeft,
            (adapter.getLength() - currBeat + 2) * stepW - offX);
    int bottomBound = Math.min(getHeight() - paddingBottom,
            (rangeLen - currPitch + 2) * stepH - offY);

    for (int x = currBeat, left = leftFromBeat(x);
         x < adapter.getLength() && leftFromBeat(x + 1) <= getWidth() - paddingRight;
         x++, left = leftFromBeat(x)) {

      Beat b = adapter.getBeatAt(x);

      //fix width
      int width = stepW;
      if (x - currBeat == 0)
        width -= offX;
      if (leftFromBeat(x + 2) > getWidth() - paddingRight)
        width = getWidth() - paddingRight - leftFromBeat(x + 1);
      int right = left + width;

      for (int y = currPitch, top = topFromPitch(y);
           y < rangeLen && top < getHeight() - paddingRight;
           y++, top = topFromPitch(y)) {

        //fix height
        int height = stepH;
        if (y - currPitch == 0)
          height -= offY;
        if (topFromPitch(y + 1) > getHeight() - paddingRight)
          height = getHeight() - paddingRight - top;
        int bottom = top + height;


        //set colors
        if (adapter.hasNoteAt(x)) {
          switch (b.getStatusAt(Pitch.values()[range.max.ordinal() - y])) {
            default:
            case EMPTY:
              g2d.setColor(Color.BLACK);
              break;
            case HEAD:
              g2d.setColor(Color.BLUE);
              g2d.fillRect(left, top, width, height);
              break;
            case TAIL:
              g2d.setColor(Color.GREEN);
              g2d.fillRect(left, top, width, height);
              break;
          }
        }
        //reset stroke
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2));

        //draw top
        g2d.drawLine(left, top, right, top);
        //draw horizontal split
        if (x % splitEvery == 0) {
          g2d.drawLine(left, top, left, bottom);
        }
      }
    }
    g2d.drawLine(leftBound, topBound, leftBound, bottomBound);
    g2d.drawLine(rightBound, topBound, rightBound, bottomBound);
    g2d.drawLine(leftBound, bottomBound, rightBound, bottomBound);
  }


  /**
   * Returns the beat at a given X position
   *
   * @param x x position in pixels
   * @return a positive number or -1 if position is out of bounds
   */
  private int beatFromX(int x) {
    x -= paddingLeft;
    return currBeat + (x + offX) / stepW;
  }

  /**
   * Returns the pitch at a given Y position
   *
   * @param y y position in pixels
   * @return a positive number or -1 if position is out of bounds
   */
  private int pitchFromY(int y) {
    y -= paddingTop;
    int r = adapter.getRange().length() -
            (currPitch + (y + offY) / stepW) - 1;

    return adapter.getRange().min.ordinal() + r;
  }

  /**
   * Get the note at the given coordinates
   * @param x x position in pixels
   * @param y y position in pixels
   * @return a Note or null if there is no Note there
   */
  public Note getNoteAt(int x, int y) {
    int b = beatFromX(x);
    int p = pitchFromY(y);

    if (adapter.hasNoteAt(b)) {
      return adapter.getBeatAt(b).getNoteAt(Pitch.values()[p]);
    }
    return null;
  }

  /**
   * Sets the beat this the given one
   *
   * @param beat beat to set
   */
  public void setDrawBeat(int beat) {
    if (beat < 0)
      currBeat = 0;
    if (beat > adapter.getLength())
      beat = adapter.getLength() - 1;
    currBeat = beat;
    offX = 0;
    this.repaint();
  }

  /**
   * Call this to switch the view between add mode and move mode
   *
   * @param status true if add mode false if move mode
   */
  public void toggleAddMode(boolean status) {
    addingNote = status;
  }

  /**
   * @return whether this view is in add mode or note
   */
  public boolean isAddingNotes() {
    return addingNote;
  }

  /**
   * Helper that calls setBeat with forceView as true
   *
   * @param beat beat to set decimal because it can be set in between
   */
  public void setBeat(double beat) {
    setBeat(beat, true);
  }

  /**
   * Sets the current red line beat
   *
   * @param beat beat to set decimal because it can be set in between
   * @param forceView force the view to look at it
   */
  public void setBeat(double beat, boolean forceView) {
    if (beat < 0)
      beat = 0;
    if (beat > adapter.getLength())
      beat = adapter.getLength();

    this.beat = beat;

    if (forceView) {
      if (this.beat > (currBeat + (double) offX / stepW) + 4) {
        currBeat = (int) this.beat - 4;
        double f = this.beat - (long) this.beat;
        offX = (int) (f * stepW);
      }
      if (this.beat < currBeat) {
        currBeat = (int)this.beat;
        double f = this.beat - (long) this.beat;
        offX = (int) (f * stepW);
      }
    }
    this.repaint();
  }

  /**
   * Increments the x scroll position
   *
   * @param delta increments X by this many pixels
   */
  public void incrementX(int delta) {
    offX += delta;
    while (offX < 0) {
      offX += stepW;
      currBeat--;
    }
    while (offX >= stepW) {
      offX -= stepW;
      currBeat++;
    }

    if (currBeat < 0) {
      currBeat = 0;
      offX = 0;
    }
    if (currBeat >= adapter.getLength() - 1) {
      currBeat = adapter.getLength() - 1;
      offX = 0;
    }
    this.repaint();
  }

  /**
   * Increments scroll position
   *
   * @param delta increments Y by this many pixels
   */
  public void incrementY(int delta) {
    offY += delta;
    while (offY < 0) {
      offY += stepH;
      currPitch--;
    }
    while (offY >= stepH) {
      offY -= stepH;
      currPitch++;
    }

    if (currPitch < 0) {
      currPitch = 0;
      offY = 0;
    }
    if (currPitch >= adapter.getRange().length() - 1) {
      currPitch = adapter.getRange().length() - 1;
      offY = 0;
    }
    this.repaint();
  }

  /**
   * Sets this views adapter
   *
   * @param adapter ModelDisplayAdapter
   */
  public void setAdapter(ModelDisplayAdapter adapter) {
    Objects.requireNonNull(adapter);
    this.adapter = adapter;
  }

  /**
   * Invoked when the mouse button has been clicked (pressed
   * and released) on a component.
   *
   * @param e
   */
  @Override
  public void mouseClicked(MouseEvent e) {
  }

  /**
   * Invoked when a mouse button has been pressed on a component.
   *
   * @param e
   */
  @Override
  public void mousePressed(MouseEvent e) {
    //if want to move bar then set it
    int x1 = e.getX(), y1 = e.getY();
    int x2 = (int)(paddingLeft + (beat - currBeat) * stepW) - offX,
            y2 = paddingTop;
    double distance = Math.sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2));

    if (distance <= 10) {
      this.followingTick = true;
      return;
    }

    //if adding mode is on
    if (!addingNote) {
      floater = this.getNoteAt(e.getX(), e.getY());

      if (floater != null) {
        int x = paddingLeft + stepW *
                (floater.getBeat() - currBeat) - offX;

        diffX = e.getX() - x;

        int y = paddingTop + stepH * (
                (adapter.getRange().max.ordinal()
                        - pitchFromY(e.getY())) - currPitch) - offY;
        diffY = e.getY() - y;

        if (this.listener != null)
          this.listener.onNoteRemoved(floater);

        repaint();
      }
    }
    //if not adding note then move note
    else {
      this.addBeat = this.beatFromX(e.getX());
      this.addPitch = Pitch.values()[this.pitchFromY(e.getY())];
      this.addWidth = 0;
      drawAddNote = true;
      repaint();
    }

  }

  /**
   * Invoked when a mouse button has been released on a component.
   *
   * @param e
   */
  @Override
  public void mouseReleased(MouseEvent e) {
    followingTick = false;
    if (!addingNote) {
      if (floater != null) {
        int x = mouseX - diffX + stepW / 2;
        int y = mouseY - diffY + stepH / 2;

        int b = beatFromX(x);
        int p = pitchFromY(y);

        if (b != -1 && p != -1 && !onDelete) {
          Note newNote = new MusicNote(b, floater.getDuration(),
                  floater.getInstrument(), Pitch.values()[p], floater.getVolume());

          if (this.listener != null)
            this.listener.onNoteAdded(newNote);
        }
        floater = null;

        repaint();
      }
    }
    else {
      Note newNote = new MusicNote(addBeat, Math.max(1, (addWidth + stepW) / stepW), addPitch);
      if (listener != null)
        listener.onNoteAdded(newNote);
      drawAddNote = false;
      repaint();
    }

  }

  /**
   * Invoked when the mouse enters a component.
   *
   * @param e
   */
  @Override
  public void mouseEntered(MouseEvent e) {
  }

  /**
   * Invoked when the mouse exits a component.
   *
   * @param e
   */
  @Override
  public void mouseExited(MouseEvent e) {
  }

  /**
   * Invoked when a mouse button is pressed on a component and then
   * dragged.  <code>MOUSE_DRAGGED</code> events will continue to be
   * delivered to the component where the drag originated until the
   * mouse button is released (regardless of whether the mouse position
   * is within the bounds of the component).
   * <p>
   * Due to platform-dependent Drag&amp;Drop implementations,
   * <code>MOUSE_DRAGGED</code> events may not be delivered during a native
   * Drag&amp;Drop operation.
   *
   * @param e
   */
  @Override
  public void mouseDragged(MouseEvent e) {
    updateMousePos(e.getX(), e.getY());
    repaint();
  }

  /**
   * Invoked when the mouse cursor has been moved onto a component
   * but no buttons have been pushed.
   *
   * @param e
   */
  @Override
  public void mouseMoved(MouseEvent e) {
    updateMousePos(e.getX(), e.getY());
  }

  /**
   * Updates mouse position and all variables depending on it
   *
   * @param x mouse x position
   * @param y mouse y position
   */
  private void updateMousePos(int x, int y) {
    mouseX = x;
    mouseY = y;
    onDelete = x >= getWidth() - 40 && y >= getHeight() - 40;

    addWidth = mouseX - leftFromBeat(addBeat);
    if (followingTick) {
      beat = (double)(x + offX - paddingLeft) / stepW + currBeat;
      if (listener != null)
        listener.onBeatChanged(beat);
    }
  }

  /**
   * Set this in order for the view to work properly
   * @param listener
   */
  public void setNotesEditedListener(NotesEditedListener listener) {
    this.listener = listener;
  }

  /**
   * Listener for Note Changing events
   */
  public interface NotesEditedListener {

    /**
     * Called when the view removes a note
     *
     * @param note note removed
     */
    void onNoteRemoved(Note note);

    /**
     * Called when the view adds a note
     *
     * @param note
     */
    void onNoteAdded(Note note);

    /**
     * If the view moves changes the current playing beat this will be called
     * Make sure any notion of current beat in any other view syncs with this method
     *
     * @param beat convert to int to get actual beat int. Any decimal is a result
     *             of the bar being in between beats
     */
    void onBeatChanged(double beat);
  }

  /** DO NOT USE THIS - TESTING PURPOSES
   */
  public NotesEditedListener getListener() {
    return this.listener;
  }


}
