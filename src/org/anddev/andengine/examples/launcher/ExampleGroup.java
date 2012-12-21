package org.anddev.andengine.examples.launcher;

import org.anddev.andengine.examples.R;

public enum ExampleGroup {
  SIMPLE(R.string.examplegroup_simple),
  MODIFIER_AND_ANIMATION(R.string.examplegroup_modifier_and_animation),
  TOUCH(R.string.examplegroup_touch),
  PARTICLESYSTEMS(R.string.examplegroup_particlesystems),
  MULTIPLAYER(R.string.examplegroup_multiplayer),
  PHYSICS(R.string.examplegroup_physics),
  TEXT(R.string.examplegroup_text),
  AUDIO(R.string.examplegroup_audio),
  ADVANCED(R.string.examplegroup_advanced),
  OTHERS(R.string.examplegroup_others),
  GAMES(R.string.examplegroup_games),
  BENCHMARKS(R.string.examplegroup_benchmarks);

  public int NAMERESID;

  private ExampleGroup(final int pNameResID) {
    NAMERESID = pNameResID;
  }
}
