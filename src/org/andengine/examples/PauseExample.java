package org.andengine.examples;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.CameraScene;
import org.anddev.andengine.entity.FPSCounter;
import org.anddev.andengine.entity.Scene;
import org.anddev.andengine.entity.SceneWithChild;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.sprite.modifier.MoveModifier;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureRegion;
import org.anddev.andengine.opengl.texture.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.TiledTextureRegion;
import org.anddev.andengine.ui.activity.BaseGameActivity;

import android.view.KeyEvent;


public class PauseExample extends BaseGameActivity {
  private static final int CAMERA_WIDTH = 720;
  private static final int CAMERA_HEIGHT= 480;

  private Camera mCamera;

  private Texture mTexture;
  private SceneWithChild mMainScene;
  private TiledTextureRegion mFaceTextureRegion;
  private TextureRegion mPauseTextureRegion;
  private CameraScene mPauseScene;

  @Override
  public Scene onLoadScene() {
    getEngine().registerPreFrameHandler(new FPSCounter());

    mPauseScene = new CameraScene(1, mCamera);

    // make the 'PAUSED' label centered on the camera
    final int x = (CAMERA_WIDTH - mPauseTextureRegion.getWidth()) / 2;
    final int y = (CAMERA_HEIGHT - mPauseTextureRegion.getHeight()) / 2;
    final Sprite pauseSprite = new Sprite(x, y, mPauseTextureRegion);
    mPauseScene.getTopLayer().addEntity(pauseSprite);
    mPauseScene.setBackgroundEnabled(false);

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
  public void onLoadResources() {
    mTexture = new Texture(128, 128);
    mPauseTextureRegion = TextureRegionFactory.createFromAsset(mTexture, this,
        "gfx/paused.png", 0, 0);
    mFaceTextureRegion = TextureRegionFactory.createTiledFromAsset(mTexture,
        this, "gfx/boxface.png", 0, 36, 2, 1);
    getEngine().loadTexture(mTexture);
  }

  @Override
  public void onLoadComplete() {
  }

  @Override
  public Engine onLoadEngine() {
    mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
    return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE,
        new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), mCamera));
  }

  @Override
  public boolean onKeyDown(final int keyCode, final KeyEvent event) {
    if (keyCode == KeyEvent.KEYCODE_MENU &&
        event.getAction() == KeyEvent.ACTION_DOWN) {
      if (getEngine().isRunning()) {
        mMainScene.setChildSceneModal(mPauseScene, false, true);
        getEngine().stop();
      }
      else {
        mMainScene.clearChildScene();
        getEngine().start();
      }
      return true;
    }
    return super.onKeyDown(keyCode, event);
  }

}
