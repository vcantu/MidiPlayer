package cs3500.music.tests;

import org.junit.Test;

import cs3500.music.model.GenericMusicModel;
import cs3500.music.model.MusicNote;
import cs3500.music.model.Pitch;
import cs3500.music.util.GenericMusicModelBuilder;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the GenericMusicModelBuilder class
 */
public class GenericMusicModelBuilderTest {


  @Test
  public void buildTest() {

    GenericMusicModel gm = new GenericMusicModel();
    GenericMusicModelBuilder m = new GenericMusicModelBuilder();

    assertEquals(m.build().getLength(), gm.getLength());
    assertEquals(m.build().getTempo(), gm.getTempo());
  }


  @Test
  public void addNoteTest() {
    GenericMusicModel gm = new GenericMusicModel();
    GenericMusicModelBuilder m = new GenericMusicModelBuilder();


    MusicNote n = new MusicNote(0, 4, 3, Pitch.C3, 5);
    gm.addNote(n);

    assertEquals(m.addNote(0, 3, 3, 60, 6).build().getNoteAt(1, Pitch.C3), n);




  }




}
