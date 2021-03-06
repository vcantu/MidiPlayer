package cs3500.music.tests;

import org.junit.Test;

import cs3500.music.model.GenericMusicModel;
import cs3500.music.model.MusicModel;
import cs3500.music.model.MusicNote;
import cs3500.music.model.Note;
import cs3500.music.model.Pitch;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the GenericMusicModel class
 */
public class MusicModelTest {

  @Test
  public void lengthTest() {
    MusicModel m = new GenericMusicModel(10);

    assertEquals(10, m.getLength());
    m.setLength(20);
    assertEquals(20, m.getLength());
  }

  @Test (expected = IllegalArgumentException.class)
  public void setLentLessThanZero1() {
    MusicModel m = new GenericMusicModel(-1);
  }

  @Test (expected = IllegalArgumentException.class)
  public void setLentLessThanZero2() {
    MusicModel m = new GenericMusicModel(0);
    m.setLength(-1);
  }

  @Test
  public void addNoteChangeLength() {
    MusicModel m = new GenericMusicModel(0);

    m.addNote(new MusicNote(0, 1, Pitch.A1));
    assertEquals(1, m.getLength());

    m.addNote(new MusicNote(4, 10, Pitch.A10));
    assertEquals(14, m.getLength());

    m.setLength(10);
    assertEquals(10, m.getLength());
  }


  @Test
  public void editNoteTests() {
    MusicModel m = new GenericMusicModel();

    m.addNote(new MusicNote(0, 1, Pitch.A1));

    assertEquals(true, m.hasNoteAt(0));

    Note note = new MusicNote(3, 3, Pitch.A2);
    m.addNote(note);

    for (int i=3; i<6; i++) {
      assertEquals(true, m.hasNoteAt(i));
      assertEquals(note, m.getNoteAt(i, Pitch.A2));
    }

    m.removeNote(note);

    for (int i=3; i<6; i++) {
      assertEquals(false, m.hasNoteAt(i));
    }

    m.addNote(note);
    m.removeNote(4, Pitch.A2);

    for (int i=3; i<6; i++) {
      assertEquals(false, m.hasNoteAt(i));
    }
  }

  @Test
  public void clearAndIsEmptyTest() {
    MusicModel m = new GenericMusicModel();

    m.addNote(new MusicNote(0, 1, Pitch.A1));
    m.addNote(new MusicNote(2, 1, Pitch.A1));
    m.addNote(new MusicNote(4, 1, Pitch.A1));
    m.addNote(new MusicNote(10, 1, Pitch.A1));

    m.clearSong();

    for (int i=0; i<11; i++) {
      assertEquals(false, m.hasNoteAt(i));
    }
  }


  @Test
  public void tempoTests() {
    MusicModel m = new GenericMusicModel();

    m.setTempo(13);
    assertEquals(m.getTempo(), 13);

    m.setTempo(0);
    assertEquals(m.getTempo(), 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void badTempTest() {
    MusicModel m = new GenericMusicModel();

    m.setTempo(-3);
    m.setTempo(-100);
  }



}