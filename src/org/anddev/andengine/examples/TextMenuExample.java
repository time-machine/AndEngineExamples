package org.anddev.andengine.examples;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.scene.menu.MenuScene;
import org.anddev.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.anddev.andengine.entity.scene.menu.item.ColoredTextMenuItem;
import org.anddev.andengine.entity.scene.menu.item.IMenuItem;
import org.anddev.andengine.entity.shape.modifier.MoveModifier;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.font.FontFactory;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;

import android.graphics.Color;
import android.view.KeyEvent;

public class TextMenuExample extends BaseExample
    implements IOnMenuItemClickListener {
  private static final int CAMERA_WIDTH = 720;
  private static final int CAMERA_HEIGHT = 480;

  protected static final int MENU_RESET = 0;
  protected static final int MENU_QUIT = MENU_RESET + 1;

  protected Camera mCamera;

  protected Scene mMainScene;

  private Texture mTexture;
  private TextureRegion mFaceTextureRegion;

  private Texture mFontTexture;
  private Font mFont;

  protected MenuScene mMenuScene;

  @Override
  public Engine onLoadEngine() {
    mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
    return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE,
        new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), mCamera));
  }

  @Override
  public void onLoadResources() {
    // load font/textures
    mFontTexture = new Texture(256, 256, TextureOptions.BILINEAR);

    FontFactory.setAssetBasePath("fonts");
    mFont = FontFactory.createFromAsset(mFontTexture, this, "Plok.ttf", 48,
        true, Color.WHITE);
    getEngine().getTextureManager().loadTexture(mFontTexture);
    getEngine().getFontManager().loadFont(mFont);

    // load sprite-textures
    mTexture = new Texture(64, 64, TextureOptions.BILINEAR);
    mFaceTextureRegion = TextureRegionFactory.createFromAsset(mTexture, this,
        "gfx/boxface_menu.png", 0, 0);
    getEngine().getTextureManager().loadTexture(mTexture);
  }

  @Override
  public Scene onLoadScene() {
    getEngine().registerUpdateHandler(new FPSLogger());

    mMenuScene = createMenuScene();

    // a simple scene with an animated face flying around
    mMainScene = new Scene(1);
    mMainScene.setBackground(new ColorBackground(0.09804f, 0.6274f, 0.8784f));

    final Sprite face = new Sprite(0, 0, mFaceTextureRegion);
    face.addShapeModifier(new MoveModifier(30, 0, CAMERA_WIDTH
        - face.getWidth(), 0, CAMERA_HEIGHT - face.getHeight()));
    mMainScene.getTopLayer().addEntity(face);

    return mMainScene;
  }

  @Override
  public void onLoadComplete() {
  }

  @Override
  public boolean onKeyDown(final int pKeyCode, final KeyEvent pEvent) {
    if (pKeyCode == KeyEvent.KEYCODE_MENU
        && pEvent.getAction() == KeyEvent.ACTION_DOWN) {
      if (mMainScene.hasChildScene()) {
        // remove the menu and reset it
        mMenuScene.back();
      } else {
        // attach the menu
        mMainScene.setChildScene(mMenuScene, false, true, true);
      }
      return true;
    }
    else {
      return super.onKeyDown(pKeyCode, pEvent);
    }
  }
  @Override
  public boolean onMenuItemClicked(final MenuScene pMenuScene,
      final IMenuItem pMenuItem, final float pMenuItemLocalX,
      final float pMenuItemLocalY) {
    switch (pMenuItem.getID()) {
    case MENU_RESET:
      // restart the animation
      mMainScene.reset();

      // remove the menu and reset it
      mMainScene.clearChildScene();
      mMenuScene.reset();
      return true;
    case MENU_QUIT:
      // end activity
      finish();
      return true;
    default:
      return false;
    }
  }

  protected MenuScene createMenuScene() {
    final MenuScene menuScene = new MenuScene(mCamera);

    menuScene.addMenuItem(new ColoredTextMenuItem(MENU_RESET, mFont, "RESET",
        1, 0, 0, 0, 0, 0));
    menuScene.addMenuItem(new ColoredTextMenuItem(MENU_QUIT, mFont, "QUIT",
        1, 0, 0, 0, 0, 0));
    menuScene.buildAnimations();

    menuScene.setBackgroundEnabled(false);
    menuScene.setOnMenuItemClickListener(this);

    return menuScene;
  }
}
