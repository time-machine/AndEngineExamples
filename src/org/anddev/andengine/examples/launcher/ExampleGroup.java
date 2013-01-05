package org.anddev.andengine.examples.launcher;

import org.anddev.andengine.examples.R;

public enum ExampleGroup {
  SIMPLE(R.string.examplegroup_simple, Example.LINE, Example.RECTANGLE,
      Example.SPRITE, Example.SPRITEREMOVE),
  MODIFIER_AND_ANIMATION(R.string.examplegroup_modifier_and_animation,
      Example.MOVINGBALL, Example.SHAPEMODIFIER, Example.SHAPEMODIFIERIRREGULAR,
      Example.PATHMODIFIER, Example.ANIMATEDSPRITES, Example.EASEFUNCTION),
  TOUCH(R.string.examplegroup_touch, Example.TOUCHDRAG, Example.MULTITOUCH,
      Example.ANALOGONSCREENCONTROL, Example.DIGITALONSCREENCONTROL,
      Example.ANALOGONSCREENCONTROLS, Example.COORDINATECONVERSION),
  PARTICLESYSTEM(R.string.examplegroup_particlesystems,
      Example.PARTICLESYSTEMSIMPLE, Example.PARTICLESYSTEMCOOL,
      Example.PARTICLESYSTEMNEXUS),
  MULTIPLAYER(R.string.examplegroup_multiplayer, Example.MULTIPLAYER),
  PHYSICS(R.string.examplegroup_physics, Example.COLLISIONDETECTION,
      Example.PHYSICS, Example.PHYSICSJUMP, Example.FIXEDSTEPPHYSICS,
      Example.PHYSICSREVOLUTEJOINT, Example.PHYSICSREMOVE),
  TEXT(R.string.examplegroup_text, Example.TEXT, Example.TICKERTEXT,
      Example.CHANGEABLETEXT, Example.CUSTOMFONT, Example.STROKEFONT),
  AUDIO(R.string.examplegroup_audio, Example.SOUND, Example.MUSIC,
      Example.MODPLAYER),
  ADVANCED(R.string.examplegroup_advanced, Example.SPLITSCREEN,
      Example.BOUNDCAMERA, Example.AUGMENTEDREALITY,
      Example.AUGMENTEDREALITYHORIZON),
  BACKGROUND(R.string.examplegroup_background,
      Example.REPEATINGSPRITEBACKGROUND, Example.AUTOPARALLAXBACKGROUND,
      Example.TMXTILEDMAP),
  OTHER(R.string.examplegroup_others, Example.PAUSE, Example.MENU,
      Example.SUBMENU, Example.TEXTMENU, Example.ZOOM, Example.IMAGEFORMATS,
      Example.TEXTUREOPTIONS, Example.LOADTEXTURE, Example.UPDATETEXTURE,
      Example.UNLOADTEXTURE),
  GAME(R.string.examplegroup_games, Example.GAME_SNAKE, Example.GAME_RACER),
  BENCHMARK(R.string.examplegroup_benchmarks, Example.BENCHMARK_SPRITE,
      Example.BENCHMARK_SHAPEMODIFIER, Example.BENCHMARK_ANIMATION,
      Example.BENCHMARK_TICKERTEXT, Example.BENCHMARK_PARTICLESYSTEM,
      Example.BENCHMARK_PHYSICS);

  public Example[] EXAMPLES;
  public int NAMERESID;

  private ExampleGroup(final int pNameResID, final Example ... pExamples) {
    NAMERESID = pNameResID;
    EXAMPLES = pExamples;
  }
}
