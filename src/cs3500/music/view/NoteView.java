package cs3500.music.view;

import cs3500.music.model.Beat;
import cs3500.music.model.Pitch;
import cs3500.music.model.Range;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Viviano on 3/18/2016.
 */
public class NoteView extends JPanel {

  private ModelDisplayAdapter model;
  private int stepH = 20, stepW = 20,
          paddingLeft = 40, paddingRight = 10,
          paddingTop = 40, paddingBottom = 10;

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    if (model == null)
      return;

    Graphics2D g2d = (Graphics2D)g;

    drawNoteGrid(23, 0, paddingLeft, paddingTop, 4, g2d);
    drawNoteRange(0, paddingTop, g2d);
    drawBeatNums(23, 0, paddingLeft, paddingTop, 8, g2d);
  }

  private void drawNoteRange(int startX, int startY, Graphics2D g2d) {
    Range range = model.getRange();
    range.setReverse(true);

    int y = 0;
    for (Pitch p : range) {
      g2d.drawString(p.toString(), startX,
              startY + stepH * y + (stepH / 2) + 4);
      y++;
    }
  }

  private void drawBeatNums(int startBeat, int offX, int startX, int startY, int every, Graphics2D g2d) {

    for (int x = startBeat; x < model.getLength(); x++) {
      if (x % every == 0)
        g2d.drawString(x + "", startX + stepH * (x - startBeat) - 5, startY - 5);
    }
  }

  private void drawNoteGrid(int startBeat, int offX,
          int startX, int startY, int splitEvery, Graphics2D g2d) {
    if (offX > 0) {
      throw new IllegalArgumentException("Offset cannot be positive");
    }
    Range range = model.getRange();
    int rangeLen = range.length();

    for (int x = startBeat; x < model.getLength() ||
            x * stepW < getWidth(); x++) {
      Beat b = model.getBeatAt(x);
      for (int y = 0; y < rangeLen; y++) {
        int left = startY + (x - startBeat) * stepW, width = stepW;
        int top = startY + y * stepH, height = stepH;
        if (x - startBeat > 0) {
          left -= offX;
        }
        else {
          width += offX;
        }
        int right = left + width;
        int bottom = top + height;

        if (model.hasNoteAt(x)) {
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
        g2d.drawLine(left, top, right, top);
        g2d.drawLine(left, bottom, right, bottom);
        if (x % splitEvery == 0) {
          g2d.drawLine(left, top, left, bottom);
        }
      }
    }
    //draw last line
    g2d.drawLine(
            startX + model.getLength() * stepW, startY,
            startX + model.getLength() * stepW, startY + rangeLen * stepH);
  }

  public void setModel(ModelDisplayAdapter model) {
    this.model = model;
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
    if (this.model == null)
      return super.getPreferredSize();
    else
      return new Dimension(model.getLength() * stepW + paddingLeft + paddingRight,
              model.getRange().length() * stepH + paddingTop + paddingBottom);
  }
}
