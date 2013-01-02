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
import org.anddev.andengine.entity.scene.menu.item.IMenuItem;
import org.anddev.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.anddev.andengine.entity.shape.modifier.MoveModifier;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;

import android.view.KeyEvent;

public class MenuExample extends BaseExample implements IOnMenuItemClickListener {
  private static final int CAMERA_WIDTH = 720;
  private static final int CAMERA_HEIGHT = 480;

  protected static final int MENU_RESET = 0;
  protected static final int MENU_QUIT = MENU_RESET + 1;

  protected Camera mCamera;

  protected Scene mMainScene;

  private Texture mTexture;
  private TextureRegion mFaceTextureRegion;

  protected MenuScene mMenuScene;

  private Texture mMenuTexture;
  protected TextureRegion mMenuResetTextureRegion;
  protected TextureRegion mMenuQuitTextureRegion;

  @Override
  public Engine onLoadEngine() {
    mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
    return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE,
        new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), mCamera));
  }

  @Override
  public void onLoadResources() {
    mTexture = new Texture(64, 64, TextureOptions.BILINEAR);
    mFaceTextureRegion = TextureRegionFactory.createFromAsset(mTexture, this,
        "gfx/boxface_menu.png", 0, 0);
    getEngine().getTextureManager().loadTexture(mTexture);

    mMenuTexture = new Texture(256, 128, TextureOptions.BILINEAR);
    mMenuResetTextureRegion = TextureRegionFactory.createFromAsset(
        mMenuTexture, this, "gfx/menu_reset.png", 0, 0);
    mMenuQuitTextureRegion = TextureRegionFactory.createFromAsset(mMenuTexture,
        this, "gfx/menu_quit.png", 0, 50);
    getEngine().getTextureManager().loadTexture(mMenuTexture);
  }

  @Override
  public Scene onLoadScene() {
    getEngine().registerPreFrameHandler(new FPSLogger());
    createMenuScene();

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
      finish();
      return true;
    default:
      return false;
    }
  }

  protected void createMenuScene() {
    mMenuScene = new MenuScene(mCamera);

    mMenuScene.addMenuItem(new SpriteMenuItem(MENU_RESET, mMenuResetTextureRegion));
    mMenuScene.addMenuItem(new SpriteMenuItem(MENU_QUIT, mMenuQuitTextureRegion));
    mMenuScene.buildAnimations();

    mMenuScene.setBackgroundEnabled(false);
    mMenuScene.setOnMenuItemClickListener(this);
  }
}