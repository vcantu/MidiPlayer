package cs3500.music.util;

import cs3500.music.model.MusicModel;
import cs3500.music.view.ConsoleView;
import cs3500.music.view.GuiViewFrame;
import cs3500.music.view.MidiViewImpl;
import cs3500.music.view.MusicView;

/**
 * Created by Viviano on 3/23/2016.
 */
public class MusicViewFactory {

  public static MusicView build(String type) {
    switch (type) {
      case "console":
        return new ConsoleView();
      case "visual":
        return new GuiViewFrame();
      case "midi":
        return new MidiViewImpl();
      default:
        throw new IllegalArgumentException("invalid view to create");
    }
  }
}
