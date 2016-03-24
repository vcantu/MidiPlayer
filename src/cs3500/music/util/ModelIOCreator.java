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

  public void create() {
    safeAppend("Which view do you want? \'console\', \'visual\' or \'midi\'\n");
    String type = this.scan.next();
    safeAppend("Which song do you want to play? \'mary\', \'myst1\', \'myst2\' " +
            "or \'myst3\'\n");
    String song = this.scan.next();

    try {
      viewFromParams(type, song).initialize();
    } catch (Exception e) {
      safeAppend("Bad input! try again!\n");
    }
    create();
  }

  public MusicView viewFromParams(String type, String song)
          throws FileNotFoundException {
    MusicView view = MusicViewFactory.build(type);
    MusicModel model =
            MusicReader.parseFile(
                    new FileReader(new File(songFile(song))),
                    new GenericMusicModelBuilder());
    view.setModel(new ModelDisplayAdapterImpl(model));
    return view;
  }

  private String songFile(String shortName) {
    switch (shortName) {
      case "mary":
        return "res/mary-little-lamb.txt";
      case "myst1":
        return "res/mystery-1.txt";
      case "myst2":
        return "res/mystery-2.txt";
      case "myst3":
        return "res/mystery-3.txt";
      default:
        throw new IllegalArgumentException("Invalid song name!");
    }
  }

  private void safeAppend(String str) {
    try {
      appendable.append(str);
    } catch (IOException e) {
      //do nothing
    }
  }


}
