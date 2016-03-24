package cs3500.music.util;

import cs3500.music.model.MusicModel;
import cs3500.music.view.ModelDisplayAdapterImpl;
import cs3500.music.view.MusicView;

import java.io.*;
import java.util.Scanner;

/**
 * Created by Viviano on 3/23/2016.
 */
public class ModelIOCreator {

  private final Scanner scan;
  private final Appendable appendable;

  public ModelIOCreator(Readable readable, Appendable appendable) {
    this.scan = new Scanner(readable);
    this.appendable = appendable;
  }

  public void create() throws FileNotFoundException {
    safeAppend("Which view do you want? \'console\', \'visual\' or \'midi\'\n");
    if (!scan.hasNext())
      return;
    String type = this.scan.next();

    safeAppend("Which song do you want to play? \'mary\', \'myst1\', \'myst2\' " +
            "or \'myst3\'\n");
    if (!scan.hasNext())
      return;
    String song = this.scan.next();

    try {
      viewFromParams(type, song).initialize();
    } catch (IllegalArgumentException e) {
      safeAppend("Bad Input!\n");
    }
    create();
  }

  public MusicView viewFromParams(String type, String song)
          throws FileNotFoundException {
    MusicView view = MusicViewFactory.build(type);
    MusicModel model =
            MusicReader.parseFile(
                    new FileReader(songFile(song)),
                    new GenericMusicModelBuilder());
    view.setModel(new ModelDisplayAdapterImpl(model));
    return view;
  }

  private File songFile(String shortName) {
    String filename = "";
    switch (shortName) {
      case "mary":
        filename =  "mary-little-lamb.txt";
        break;
      case "myst1":
        filename = "mystery-1.txt";
        break;
      case "myst2":
        filename = "mystery-2.txt";
        break;
      case "myst3":
        filename = "mystery-3.txt";
        break;
      default:
        throw new IllegalArgumentException("Invalid song name!");
    }
    return new File(filename);
  }

  private void safeAppend(String str) {
    try {
      appendable.append(str);
    } catch (IOException e) {
      //do nothing
    }
  }


}
