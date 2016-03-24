package cs3500.music;

import java.io.*;

import javax.sound.midi.InvalidMidiDataException;

import cs3500.music.model.MusicModel;
import cs3500.music.util.GenericMusicModelBuilder;
import cs3500.music.util.ModelIOCreator;
import cs3500.music.util.MusicReader;
import cs3500.music.view.*;


public class MusicEditor {


  public static void main(String[] args)
          throws IOException, InvalidMidiDataException {

    ModelIOCreator console = new ModelIOCreator(
            new InputStreamReader(System.in), System.out);
    console.create();
  }

}
