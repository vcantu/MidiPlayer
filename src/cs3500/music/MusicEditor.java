package cs3500.music;

import java.io.*;

import javax.sound.midi.InvalidMidiDataException;

import cs3500.music.model.MusicModel;
import cs3500.music.util.GenericMusicModelBuilder;
import cs3500.music.util.ModelIOCreator;
import cs3500.music.util.MusicReader;
import cs3500.music.util.MusicViewFactory;
import cs3500.music.view.*;


public class MusicEditor {


  public static void main(String[] args)
          throws IOException, InvalidMidiDataException {

    if (args.length == 2) {
      MusicView view = MusicViewFactory.build(args[0]);
      MusicModel model = MusicReader.parseFile(
              new FileReader(args[1]),
              new GenericMusicModelBuilder());
      view.setAdapter(new ModelDisplayAdapterImpl(model));
      view.initialize();
    }
    else {
      ModelIOCreator console = new ModelIOCreator(
              new InputStreamReader(System.in), System.out);
      console.create();
    }
  }

}
