package cs3500.music;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;

import cs3500.music.model.MusicModel;
import cs3500.music.util.GenericMusicModelBuilder;
import cs3500.music.util.MusicReader;
import cs3500.music.view.*;


public class MusicEditor {

  private static String
          mary = "res/mary-little-lamb.txt",
          myst1 = "res/mystery-1.txt",
          myst2 = "res/mystery-2.txt",
          myst3 = "res/mystery-3.txt";

  public static void main(String[] args) throws IOException, InvalidMidiDataException {
    MusicView view = new GuiViewFrame();
    MidiView midiView = new MidiViewImpl();
    // You probably need to connect these views to your model, too...
    MusicModel model =
            MusicReader.parseFile(
            new FileReader(new File(myst1)),
            new GenericMusicModelBuilder());

    ModelDisplayAdapter adapter = new ModelDisplayAdapterImpl(model);
    view.setModel(adapter);
    midiView.setModel(adapter);

    view.initialize();
    midiView.initialize();

  }

}
