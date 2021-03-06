package cs3500.music.model;

/**
 * Created by Viviano on 2/29/2016.
 */
public enum Pitch {
  C0 ("C0"),
  CS0 ("C#0"),
  D0 ("D0"),
  DS0 ("D#0"),
  E0 ("E0"),
  F0 ("F0"),
  FS0 ("F#0"),
  G0 ("G0"),
  GS0 ("G#0"),
  A1 ("A1"),
  AS1 ("A#1"),
  B1 ("B1"),
  C1 ("C1"),
  CS1 ("C#1"),
  D1 ("D1"),
  DS1 ("D#1"),
  E1 ("E1"),
  F1 ("F1"),
  FS1 ("F#1"),
  G1 ("G1"),
  GS1 ("G#1"),
  A2 ("A2"),
  AS2 ("A#2"),
  B2 ("B2"),
  C2 ("C2"),
  CS2 ("C#2"),
  D2 ("D2"),
  DS2 ("D#2"),
  E2 ("E2"),
  F2 ("F2"),
  FS2 ("F#2"),
  G2 ("G2"),
  GS2 ("G#2"),
  A3 ("A3"),
  AS3 ("A#3"),
  B3 ("B3"),
  C3 ("C3"),
  CS3 ("C#3"),
  D3 ("D3"),
  DS3 ("D#3"),
  E3 ("E3"),
  F3 ("F3"),
  FS3 ("F#3"),
  G3 ("G3"),
  GS3 ("G#3"),
  A4 ("A4"),
  AS4 ("A#4"),
  B4 ("B4"),
  C4 ("C4"),
  CS4 ("C#4"),
  D4 ("D4"),
  DS4 ("D#4"),
  E4 ("E4"),
  F4 ("F4"),
  FS4 ("F#4"),
  G4 ("G4"),
  GS4 ("G#4"),
  A5 ("A5"),
  AS5 ("A#5"),
  B5 ("B5"),
  C5 ("C5"),
  CS5 ("C#5"),
  D5 ("D5"),
  DS5 ("D#5"),
  E5 ("E5"),
  F5 ("F5"),
  FS5 ("F#5"),
  G5 ("G5"),
  GS5 ("G#5"),
  A6 ("A6"),
  AS6 ("A#6"),
  B6 ("B6"),
  C6 ("C6"),
  CS6 ("C#6"),
  D6 ("D6"),
  DS6 ("D#6"),
  E6 ("E6"),
  F6 ("F6"),
  FS6 ("F#6"),
  G6 ("G6"),
  GS6 ("G#6"),
  A7 ("A7"),
  AS7 ("A#7"),
  B7 ("B7"),
  C7 ("C7"),
  CS7 ("C#7"),
  D7 ("D7"),
  DS7 ("D#7"),
  E7 ("E7"),
  F7 ("F7"),
  FS7 ("F#7"),
  G7 ("G7"),
  GS7 ("G#7"),
  A8 ("A8"),
  AS8 ("A#8"),
  B8 ("B8"),
  C8 ("C8"),
  CS8 ("C#8"),
  D8 ("D8"),
  DS8 ("D#8"),
  E8 ("E8"),
  F8 ("F8"),
  FS8 ("F#8"),
  G8 ("G8"),
  GS8 ("G#8"),
  A9 ("A9"),
  AS9 ("A#9"),
  B9 ("B9"),
  C9 ("C9"),
  CS9 ("C#9"),
  D9 ("D9"),
  DS9 ("D#9"),
  E9 ("E9"),
  F9 ("F9"),
  FS9 ("F#9"),
  G9 ("G9"),
  GS9 ("G#9"),
  A10 ("A10"),
  AS10 ("A#10"),
  B10 ("B10"),
  C10 ("C10"),
  CS10 ("C#10"),
  D10 ("D10"),
  DS10 ("D#10"),
  E10 ("E10"),
  F10 ("F10"),
  FS10 ("F#10"),
  G10 ("G10");

  private final String str;

  Pitch(String str) {
    this.str = str;
  }

  /**
   * @return will return this Pitch's name as a String
   */
  @Override
  public String toString() {
    return this.str;
  }

  /**
   * Returns a pitch from a given midi number
   *
   * @param i midi number
   * @return a pitch
   */
  public static Pitch fromInt(int i) {
    if (i < 0 || i >= values().length)
      throw new IllegalArgumentException("Invalid pitch number");
    return values()[i];
  }

  /**
   * Returns a pitch from a combination of octave and note
   *
   * @param octave a number from 0 - 10
   * @param note a number from 0 - 11 where 0 is A and 11 is G#
   *             min pitch is C0 and max is G10
   * @return a pitch
   */
  public static Pitch fromComb(int octave, int note) {
    try {
      return values()[12 * octave + note - 3];
    }
    catch (Exception e) {
      throw new IllegalArgumentException("Invalid combination of octave: "
              + octave + " and note: " + note);
    }
  }
}
