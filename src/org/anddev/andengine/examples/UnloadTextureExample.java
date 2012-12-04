package org.anddev.andengine.examples;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.FPSCounter;
import org.anddev.andengine.entity.Scene;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.input.touch.IOnAreaTouchListener;
import org.anddev.andengine.input.touch.ITouchArea;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;

import android.view.MotionEvent;

public class UnloadTextureExample extends BaseExampleGameActivity {
  private static final int CAMERA_WIDTH = 720;
  private static final int CAMERA_HEIGHT= 480;

  private Camera mCamera;
  private Texture mTexture;
  private TextureRegion mTankTextureRegion;

  @Override
  public Engine onLoadEngine() {
    mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
    return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE,
        new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), mCamera, false));
  }

  @Override
  public void onLoadResources() {
    mTexture = new Texture(128, 128);
    TextureRegionFactory.setAssetBasePath("gfx/");
    mTankTextureRegion = TextureRegionFactory.createFromAsset(mTexture, this,
        "click_to_unload.png", 0, 0);
    getEngine().getTextureManager().loadTexture(mTexture);
  }

  @Override
  public Scene onLoadScene() {
    getEngine().registerPreFrameHandler(new FPSCounter());
    final Scene scene = new Scene(1);
    scene.setBackgroundColor(0.09804f, 0.6274f, 0.8784f);

    final int x = (CAMERA_WIDTH - mTankTextureRegion.getWidth()) / 2;
    final int y = (CAMERA_HEIGHT - mTankTextureRegion.getHeight()) / 2;
    final Sprite clickToUnload = new Sprite(x, y, mTankTextureRegion);
    clickToUnload.setColor(0, 1, 0);
    scene.getTopLayer().addEntity(clickToUnload);

    scene.registerTouchArea(clickToUnload);
    scene.setOnAreaTouchListener(new IOnAreaTouchListener() {
      @Override
      public boolean onAreaTouched(final ITouchArea pTouchArea,
          final MotionEvent pSceneMotionEvent) {
        getEngine().getTextureManager().unloadTexture(mTexture);
        return true;
      }
    });

    return scene;
  }

  @Override
  public void onLoadComplete() {
  }
}
