package cs3500.music.view;

import cs3500.music.model.*;

import java.util.List;

/**
 * Created by Viviano on 3/18/2016.
 */
public class ModelDisplayAdapterImpl implements ModelDisplayAdapter<Beat> {

  private final MusicModel model;

  public ModelDisplayAdapterImpl(MusicModel model) {
    this.model = model;
  }
  /**
   * Gets the notes at the given beat
   *
   * @param i beat number, must be >= 0
   * @return beat at i
   * @throws IllegalArgumentException if i < 0
   */
  @Override
  public Beat getBeatAt(int i) {
    return model.getBeatAt(i);
  }

  /**
   * @return the maximum range of this song
   */
  @Override
  public Range getRange() {
    return model.getRange();
  }

  /**
   * @param beat index to test
   * @return true if this song has a beat at a given index
   */
  @Override
  public boolean hasNoteAt(int beat) {
    return model.hasNoteAt(beat);
  }

  /**
   * @return the song's length
   */
  @Override
  public int getLength() {
    return model.getLength();
  }
}
