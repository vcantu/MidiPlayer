package cs3500.music.view;


/**
 * Created by Ryan on 3/20/16.
 */
public class ConsoleView implements MusicView {

  ModelDisplayAdapter model;


  /**
   * Will be called in order to displaying or play the view
   *
   * Use this to initialize or reset any data that must be reset
   */
  @Override
  public void initialize() {
    System.out.println(this.draw(model));
  }


  public String draw(ModelDisplayAdapter model) {
    StringBuilder sb = new StringBuilder();
    String l = "" + model.getLength();
    sb.append(String.format("%" + l.length() + "s", " "));
    sb.append(model.getRange() + "\n");

    for (int i = 0; i < model.getLength(); i++) {
      int c = l.length() - ("" + i).length();
      if (c > 0)
        sb.append(String.format("%d%" + c + "s", i, ""));
      else
        sb.append(i);

      if (model.hasNoteAt(i))//draw beat
        sb.append(model.getBeatAt(i).beatToString(model.getRange()));
      sb.append("\n");
    }
    return sb.toString();
  }


  /**
   * Set the model which will include the content this view will show
   *
   * @param adapter new content of this view
   */
  @Override
  public void setAdapter(ModelDisplayAdapter adapter) {
    this.model = adapter;
  }
}
