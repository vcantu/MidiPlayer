package cs3500.music.view;

import cs3500.music.model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * Created by Viviano on 3/18/2016.
 */
public class NoteView extends JPanel implements MouseListener, MouseMotionListener {

  private ModelDisplayAdapter adapter;
  private final int stepH = 20, stepW = 20;
  //padding
  private int paddingLeft = 40, paddingRight = 10,
          paddingTop = 40, paddingBottom = 10;
  private int currBeat = 0, currPitch = 0;
  private int offX = 0, offY = 0;

  //floater fields
  private int mouseX = -1, mouseY = -1, diffX = -1, diffY = -1;
  private Note floater;
  private Note selected;

  //listener
  private NotesEditedListener listener;


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

    drawNoteGrid(paddingLeft, paddingTop, 4, g2d);
    drawNoteRange(0, paddingTop, g2d);
    drawBeatNums(paddingLeft, paddingTop, 8, g2d);
    drawFloater(g2d);
    drawSelected(paddingLeft, paddingTop, g2d);
  }

  /**
   * Redraws and highlights the selected note
   * 
   * @param g2d
   */
  private void drawSelected(int startX, int startY, Graphics2D g2d) {
    if (selected != null) {
      int b = selected.getBeat();
      int d = selected.getDuration();
      int p = selected.getPitch().ordinal();

      int left = xFromBeat(b);
      int top = yFromPitch(p + 1);
      int right = xFromBeat(b + d);
      int bottom = yFromPitch(p);

      if (left < startX)
        left = startX;
      if (right > startX + (adapter.getLength() - currBeat) * stepW)
        return;//don't draw

      if (top < startY)
        top = startY;


      g2d.setColor(new Color(225, 0, 0, 150));
      g2d.fillRect(left, top, right - left, bottom - top);
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
    Range range = adapter.getRange();
    int rangeLen = range.length();
    range.setReverse(true);

    for (int y = currPitch; y < rangeLen &&
            (y - currPitch) * stepH < getHeight(); y++) {
      Pitch p = Pitch.values()[range.max.ordinal() - y];
      int top = startY + stepH * (y - currPitch) + (stepH / 2) + 4 - offY;
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
    for (int x = currBeat; x < adapter.getLength() &&
            (x - currBeat) * stepW < getWidth(); x++) {
      if (x % every == 0) {
        int left = startX + stepH * (x - currBeat) - 5;
        if (x - currBeat > 0)
          left -= offX;
        g2d.drawString(x + "", left, startY - 5);
      }
    }
  }

  /**
   * Draws the note grid
   * 
   * @param splitEvery
   * @param g2d
   */
  private void drawNoteGrid(int startX, int startY, int splitEvery, Graphics2D g2d) {

    Range range = adapter.getRange();
    int rangeLen = range.length();

    for (int x = currBeat; x < adapter.getLength() &&
            (x - currBeat) * stepW < getWidth(); x++) {
      Beat b = adapter.getBeatAt(x);
      for (int y = currPitch; y < rangeLen &&
              (y - currPitch) * stepH < getHeight(); y++) {

        //set bounds
        int left = startX + (x - currBeat) * stepW, width = stepW;
        int top = startY + (y - currPitch) * stepH, height = stepH;
          if (x - currBeat > 0)
            left -= offX;
          else
            width -= offX;
          if (y - currPitch > 0)
            top -= offY;
          else
            height -= offY;

        int right = left + width;
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
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2));

        //draw first line
        if (x - currBeat == 0) {
          g2d.drawLine(left, top, left, bottom);
        }

        //draw top
        g2d.drawLine(left, top, right, top);
        //draw bottom
        g2d.drawLine(left, bottom, right, bottom);

        //draw horizontal split
        if (x % splitEvery == 0) {
          g2d.drawLine(left, top, left, bottom);
        }

        //draw last line
        if (x == adapter.getLength() - 1) {
          g2d.drawLine(right, top, right, bottom);
        }
      }
    }
  }
  
  

  /**
   * Returns the beat at a given X position
   *
   * @param x x position in pixels
   * @return a positive number or -1 if position is out of bounds
   */
  private int beatFromX(int x) {
    if (x >= paddingLeft && true) {
      x -= paddingLeft;
      int r = currBeat + (x + offX) / stepW;
      if (r > this.adapter.getLength() - 1)
        return -1;
      return r;
    }
    return -1;
  }

  /**
   * Returns the pitch at a given Y position
   *
   * @param y y position in pixels
   * @return a positive number or -1 if position is out of bounds
   */
  private int pitchFromY(int y) {
    if (y >= paddingTop) {
      y -= paddingTop;
      int r = adapter.getRange().length() -
              (currPitch + (y + offY) / stepW) - 1;
      if (r < 0)
        return -1;
      return adapter.getRange().min.ordinal() + r;
    }
    return -1;
  }

  private int xFromBeat(int beat) {
    return paddingLeft + ((beat - currBeat) * stepW) - offX;
  }

  private int yFromPitch(int p) {
    p -= adapter.getRange().min.ordinal();
    p = adapter.getRange().length() - p;
    p -= currPitch;

    return paddingTop + (p * stepH) - offY;
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

    if (b != -1 && p != -1 && adapter.hasNoteAt(b)) {
      return adapter.getBeatAt(b).getNoteAt(Pitch.values()[p]);
    }
    return null;
  }

  /**
   * Sets the beat this the given one
   *
   * @param beat beat to set
   */
  public void setBeat(int beat) {
    if (beat < 0 || beat > adapter.getLength())
      throw new IllegalArgumentException("invalid beat number");
    currBeat = beat;
    offX = 0;
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
   * @param adapter ModelDisplayAdapter
   */
  public void setAdapter(ModelDisplayAdapter adapter) {
    this.adapter = adapter;
  }


  /**
   * If the <code>preferredSize</code> has been set to a
   * non-<code>null</code> value just returns it.
   * If the UI delegate's <code>getPreferredSize</code>
   * method returns a non <code>null</code> value then return that;
   * otherwise defer to the component's layout manager.
   *
   * @return the value of the <code>preferredSize</code> property
   * @see #setPreferredSize
   */
  @Override
  public Dimension getPreferredSize() {
    if (this.adapter == null)
      return super.getPreferredSize();
    else
      return new Dimension(adapter.getLength() * stepW + paddingLeft + paddingRight,
              adapter.getRange().length() * stepH + paddingTop + paddingBottom);
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
    this.selected = null;
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

  /**
   * Invoked when a mouse button has been released on a component.
   *
   * @param e
   */
  @Override
  public void mouseReleased(MouseEvent e) {
    if (floater != null) {
      int x = mouseX - diffX + stepW / 2;
      int y = mouseY - diffY + stepH / 2;

      int b = beatFromX(x);
      int p = pitchFromY(y);

      Note newNote = new MusicNote(b, floater.getDuration(),
              floater.getInstrument(), Pitch.values()[p], floater.getVolume());

      if (this.listener != null)
        this.listener.onNoteAdded(newNote);

      if (floater.equals(newNote)) {
        this.selected = newNote;
        this.listener.onNoteSelected(newNote);
      }

      floater = null;
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
    mouseX = e.getX();
    mouseY = e.getY();
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
    mouseX = e.getX();
    mouseY = e.getY();
  }


  public void setNotesEditedListener(NotesEditedListener listener) {
    this.listener = listener;
  }

  public interface NotesEditedListener {

    void onNoteRemoved(Note note);

    void onNoteAdded(Note note);

    void onNoteSelected(Note note);

    void onNoteDeselected();
  }
}
