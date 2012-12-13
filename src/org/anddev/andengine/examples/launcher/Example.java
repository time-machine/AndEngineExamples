package org.anddev.andengine.examples.launcher;

import org.anddev.andengine.examples.AnimatedSpritesExample;
import org.anddev.andengine.examples.AugmentedRealityExample;
import org.anddev.andengine.examples.AugmentedRealityHorizonExample;
import org.anddev.andengine.examples.CustomFontExample;
import org.anddev.andengine.examples.LineExample;
import org.anddev.andengine.examples.MenuExample;
import org.anddev.andengine.examples.MovingBallExample;
import org.anddev.andengine.examples.MultiplayerExample;
import org.anddev.andengine.examples.MusicExample;
import org.anddev.andengine.examples.ParticleSystemExample;
import org.anddev.andengine.examples.PathModifierExample;
import org.anddev.andengine.examples.PauseExample;
import org.anddev.andengine.examples.PhysicsExample;
import org.anddev.andengine.examples.PhysicsRemoveExample;
import org.anddev.andengine.examples.RectangleExample;
import org.anddev.andengine.examples.ShapeModifierExample;
import org.anddev.andengine.examples.SoundExample;
import org.anddev.andengine.examples.SplitScreenExample;
import org.anddev.andengine.examples.SpriteExample;
import org.anddev.andengine.examples.SpritesExample;
import org.anddev.andengine.examples.SubMenuExample;
import org.anddev.andengine.examples.TextExample;
import org.anddev.andengine.examples.TextureOptionsExample;
import org.anddev.andengine.examples.TickerTextExample;
import org.anddev.andengine.examples.TouchDragExample;
import org.anddev.andengine.examples.TouchDragManyExample;
import org.anddev.andengine.examples.UnloadTextureExample;

import android.app.Activity;

enum Example {
  ANIMATEDSPRITES(AnimatedSpritesExample.class, "Animated Sprites Example"),
  AR(AugmentedRealityExample.class, "AugmentedReality Example"),
  ARHORIZON(AugmentedRealityHorizonExample.class, "AugmentedReality Horizon Example"),
  CUSTOMFONT(CustomFontExample.class, "Custom Font Example"),
  LINE(LineExample.class, "Line Example"),
  MENU(MenuExample.class, "Menu Example"),
  MOVINGBALL(MovingBallExample.class, "Moving Ball Example"),
  MULTIPLAYER(MultiplayerExample.class, "Multiplayer Example"),
  MUSIC(MusicExample.class, "Music Example"),
  PAUSE(PauseExample.class, "Pause Example"),
  PATHMODIFIER(PathModifierExample.class, "PathModifier Example"),
  PARTICLESYSTEM(ParticleSystemExample.class, "ParticleSystem Example"),
  PHYSICS(PhysicsExample.class, "Physics Example"),
  PHYSICSREMOVE(PhysicsRemoveExample.class, "Physics Remove Example"),
  RECTANGLE(RectangleExample.class, "Rectangle Example"),
  SHAPEMODIFIER(ShapeModifierExample.class, "ShapeModifier Example"),
  SOUND(SoundExample.class, "Sound Example"),
  SPLITSCREEN(SplitScreenExample.class, "SplitScreen Example"),
  SPRITE(SpriteExample.class, "Sprite Example"),
  SPRITES(SpritesExample.class, "Sprites Example"),
  SUBMENU(SubMenuExample.class, "SubMenu Example"),
  TEXT(TextExample.class, "Text Example"),
  TEXTUREOPTIONS(TextureOptionsExample.class, "TextureOptions Example"),
  TICKERTEXT(TickerTextExample.class, "Ticker Text Example"),
  TOUCHDRAG(TouchDragExample.class, "TouchDrag Example"),
  TOUCHDRAGMANY(TouchDragManyExample.class, "TouchDrag Many Example"),
  UNLOADTEXTURE(UnloadTextureExample.class, "Unload Texture Example");

  public final Class<? extends Activity> CLASS;
  public final String NAME;

  private Example(final Class<? extends Activity> pExampleClass,
      final String pExampleName) {
    CLASS = pExampleClass;
    NAME = pExampleName;
  }

  @Override
  public String toString() {
    return NAME;
  }
}
