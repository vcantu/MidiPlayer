package cs3500.music.view;

import cs3500.music.model.*;

import java.util.List;

/**
 * Created by Viviano on 3/18/2016.
 */
public interface ModelDisplayAdapter {

  /**
   * Gets the notes at the given beat
   *
   * @param i beat number, must be >= 0
   * @return beat an immutable beat
   * @throws IllegalArgumentException if i < 0
   */
  Beat getBeatAt(int i);

  /**
   * @return the maximum range of this song
   */
  Range getRange();

  /**
   * @param beat index to test
   * @return true if this song has a beat at a given index
   */
  boolean hasNoteAt(int beat);

  /**
   * @return the song's length
   */
  int getLength();
}
