package cs3500.music.model;

import cs3500.music.view.ConsoleView;
import cs3500.music.view.ModelDisplayAdapterImpl;
import cs3500.music.view.MusicView;

/**
 * Created by Viviano on 2/29/2016.
 */
public class MusicConsoleView {


  public static void main(String[] args) {
    GenericMusicModel model = new GenericMusicModel();
    model.addNote(new MusicNote(0, 4, Pitch.A6));
    model.addNote(new MusicNote(0, 3, Pitch.C6));
    model.addNote(new MusicNote(5, 10, Pitch.GS6));
    model.addNote(new MusicNote(10, 5, Pitch.GS6));

    MusicView view = new ConsoleView();

    view.setAdapter(new ModelDisplayAdapterImpl(model));

    view.initialize();
  }

}
