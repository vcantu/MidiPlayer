package cs3500.music.controller;


import cs3500.music.controller.MusicController;
import cs3500.music.model.GenericMusicModel;
import cs3500.music.model.MusicNote;
import cs3500.music.model.Pitch;
import org.junit.Test;

import java.awt.*;
import java.awt.event.KeyEvent;

import static junit.framework.TestCase.assertEquals;



public class MusicControllerTest {

  @Test
  public void testAKeys() throws AWTException {

    GenericMusicModel model = new GenericMusicModel(20);

    model.addNote(new MusicNote(0, 4, 1, Pitch.C0, 5));
    model.addNote(new MusicNote(5, 4, 1, Pitch.C3, 5));


    MusicController controller = new MusicController(model);

    // Test Keyboard input
    controller.getKeyboardListener().fakePress(KeyEvent.VK_A);
    assertEquals(controller.getGui().isAddingNotes(), true);

    controller.getKeyboardListener().fakePress(KeyEvent.VK_A);
    assertEquals(controller.getGui().isAddingNotes(), false);

    // Test MIDI
    assertEquals(controller.getMidi().isPlaying(), false);

  }

  @Test
  public void testController() {
    GenericMusicModel model = new GenericMusicModel(20);

    MusicController controller = new MusicController(model);

    // Test Controller
    controller.getGui().getListener().onNoteAdded(new MusicNote(12, 4, 1, Pitch.C0, 5));
    assertEquals(controller.getModel().hasNoteAt(12), true);

    controller.getGui().getListener().onNoteRemoved(new MusicNote(12, 4, 1, Pitch.C0, 5));
    assertEquals(controller.getModel().hasNoteAt(12), false);

    // Test Controller
    controller.getGui().getListener().onNoteAdded(new MusicNote(0, 4, 1, Pitch.C8, 5));
    assertEquals(controller.getModel().hasNoteAt(0), true);

    controller.getGui().getListener().onNoteRemoved(new MusicNote(0, 4, 1, Pitch.C8, 5));
    assertEquals(controller.getModel().hasNoteAt(0), false);

  }


}