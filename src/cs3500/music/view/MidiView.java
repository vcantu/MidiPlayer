package cs3500.music.view;

import cs3500.music.controller.MusicController;

/**
 * Created by Viviano on 3/20/2016.
 */
public interface MidiView extends MusicView {

  /**
   * Set this song's tempo
   * @param tempo
   */
  void setTempo(long tempo);

  /**
   * Begins playing the song
   */
  void play();

  /**
   * Pauses the song
   */
  void pause();

  /**
   * @return true if this view is currently playing, false if paused or stopped
   */
  boolean isPlaying();

  /**
   * Sets the starting beat of this song
   */
  void setBeat(int beat);

  /**
   * If an onTickListener has been set you can set the resolution of the ticks
   * The 100 / resolution is the amount of times the ticks will be called
   * between beats
   *
   * I.E. if the resolution is 1 onTick will be called 100 times per beat
   *      if the resolution is 100 onTick will be called 1 time per beat
   *      if the resolution is 50 onTick will be called 2 times per beat
   *
   * If no resolution is set the default resolution should be 25
   *
   * @param resolution a number from [1, 100]
   * @throws IllegalArgumentException if resolution is not within [1, 100]
   */
  void setTickResolution(int resolution) throws IllegalArgumentException;



  /**
   * Set this to register an OnTickListener
   *
   * @param listener listener to register
   */
  void setOnTickListener(OnTickListener listener);

  interface OnTickListener {
    /**
     * Called every tick while playing
     *
     * @param beat the current beat of the song
     * @param progress the progress from [0, 100) progress will be 0 at the
     *                 moment a beat is played otherwise it represents the
     *                 time it has been waiting for the next beat
     */
    void onTick(int beat, int progress);
  }
}
