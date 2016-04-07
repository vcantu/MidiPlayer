package cs3500.music;

import java.io.*;

import javax.sound.midi.InvalidMidiDataException;

import cs3500.music.controller.MusicController;
import cs3500.music.model.MusicModel;
import cs3500.music.util.GenericMusicModelBuilder;
import cs3500.music.util.MusicReader;


public class MusicEditor {


  public static void main(String[] args)
          throws IOException, InvalidMidiDataException {

    MusicModel model = MusicReader.parseFile(
              new FileReader(args[0]),
              new GenericMusicModelBuilder());

    MusicController controller = new MusicController(model);
    controller.start();
  }

}
