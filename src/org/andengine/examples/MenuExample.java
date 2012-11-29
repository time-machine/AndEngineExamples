package org.andengine.examples;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.FPSCounter;
import org.anddev.andengine.entity.Scene;
import org.anddev.andengine.entity.SceneWithChild;
import org.anddev.andengine.entity.menu.IOnMenuItemClickerListener;
import org.anddev.andengine.entity.menu.MenuItem;
import org.anddev.andengine.entity.menu.MenuScene;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.entity.sprite.modifier.MoveModifier;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureRegion;
import org.anddev.andengine.opengl.texture.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.TiledTextureRegion;
import org.anddev.andengine.ui.activity.BaseGameActivity;
import org.anddev.andengine.util.Debug;

import android.view.KeyEvent;

public class MenuExample extends BaseGameActivity {
  private static final int CAMERA_WIDTH = 720;
  private static final int CAMERA_HEIGHT = 480;

  private static final int MENU_RESET = 0;
  private static final int MENU_QUIT = MENU_RESET + 1;

  private Camera mCamera;

  private SceneWithChild mMainScene;

  private Texture mTexture;
  private TiledTextureRegion mFaceTextureRegion;

  private MenuScene mMenuScene;

  private Texture mMenuTexture;
  private TextureRegion mMenuResetTextureRegion;
  private TextureRegion mMenuQuitTextureRegion;

  @Override
  public Engine onLoadEngine() {
    mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
    return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE,
        new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), mCamera));
  }

  @Override
  public void onLoadResources() {
    mMenuTexture = new Texture(256, 128);
    mMenuResetTextureRegion = TextureRegionFactory.createFromAsset(mMenuTexture,
        this, "gfx/menu_reset.png", 0, 0);
    mMenuQuitTextureRegion = TextureRegionFactory.createFromAsset(mMenuTexture,
        this, "gfx/menu_quit.png", 0, 64);

    mTexture = new Texture(128, 128);
    mFaceTextureRegion = TextureRegionFactory.createTiledFromAsset(mTexture,
        this, "gfx/boxface.png", 0, 36, 2, 1);

    getEngine().loadTextures(mMenuTexture, mTexture);
  }

  @Override
  public Scene onLoadScene() {
    getEngine().registerPreFrameHandler(new FPSCounter());

    mMenuScene = new MenuScene(new IOnMenuItemClickerListener() {
      @Override
      public void onMenuItemClicked(final MenuScene pMenuScene,
          final MenuItem pMenuItem) {
        switch (pMenuItem.getMenuID()) {
        case MENU_RESET:
          Debug.d("Reset");
          break;
        case MENU_QUIT:
          Debug.d("Quit");
          break;
        }
      }
    }, mCamera);

    mMenuScene.addMenuItem(new MenuItem(MENU_RESET, mMenuResetTextureRegion));
    mMenuScene.addMenuItem(new MenuItem(MENU_QUIT, mMenuQuitTextureRegion));
    mMenuScene.build();

    mMenuScene.setBackgroundEnabled(false);

    mMainScene = new SceneWithChild(1);
    mMainScene.setBackgroundColor(0.09804f, 0.6274f, 0.8784f);

    final AnimatedSprite face = new AnimatedSprite(0, 0, mFaceTextureRegion);
    face.animate(100);
    face.addSpriteModifier(new MoveModifier(30, 0,
        CAMERA_WIDTH - face.getWidth(), 0, CAMERA_HEIGHT - face.getHeight()));
    mMainScene.getTopLayer().addEntity(face);

    return mMainScene;
  }

  @Override
  public void onLoadComplete() {
  }

  @Override
  public boolean onKeyDown(final int pKeyCode, final KeyEvent pEvent) {
    if (pKeyCode == KeyEvent.KEYCODE_MENU &&
        pEvent.getAction() == KeyEvent.ACTION_DOWN) {
      if (mMainScene.hasChildScene()) {
        mMainScene.clearChildScene();
        mMenuScene.reset();
      }
      else {
        mMainScene.setChildSceneModal(mMenuScene, false, true);
      }
      return true;
    }
    else {
      return super.onKeyDown(pKeyCode, pEvent);
    }
  }
}
