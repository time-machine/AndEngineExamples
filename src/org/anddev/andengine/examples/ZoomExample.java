package org.anddev.andengine.examples;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.SmoothCamera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.Layer;
import org.anddev.andengine.entity.Scene;
import org.anddev.andengine.entity.Scene.IOnSceneTouchListener;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.util.FPSCounter;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;

import android.view.MotionEvent;
import android.widget.Toast;

public class ZoomExample extends BaseExampleGameActivity {
  private static final int CAMERA_WIDTH = 720;
  private static final int CAMERA_HEIGHT = 480;

  private SmoothCamera mSmoothCamera;
  private Texture mTexture;
  private TextureRegion mFaceTextureRegion;

  @Override
  public Engine onLoadEngine() {
    Toast.makeText(this, "Touch and hold the scene and the camera will smoothly zoom in,\nRelease the scene to zoom out again",
        Toast.LENGTH_LONG).show();
    mSmoothCamera = new SmoothCamera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT, 10, 10,
        1.0f);
    return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE,
        new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), mSmoothCamera,
        false));
  }

  @Override
  public void onLoadResources() {
    mTexture = new Texture(64, 32);
    mFaceTextureRegion = TextureRegionFactory.createFromAsset(mTexture, this,
        "gfx/boxface.png", 0, 0);
    getEngine().getTextureManager().loadTexture(mTexture);
  }

  @Override
  public Scene onLoadScene() {
    getEngine().registerPreFrameHandler(new FPSCounter());

    final Scene scene = new Scene(1);
    scene.setBackgroundColor(0.09804f, 0.6274f, 0.8784f);

    final int x = (CAMERA_WIDTH - mFaceTextureRegion.getWidth()) / 2;
    final int y = (CAMERA_HEIGHT - mFaceTextureRegion.getHeight()) / 2;

    // create some faces and add them to the scene
    final Layer topLayer = scene.getTopLayer();
    topLayer.addEntity(new Sprite(x - 25, y - 25, mFaceTextureRegion));
    topLayer.addEntity(new Sprite(x + 25, y - 25, mFaceTextureRegion));
    topLayer.addEntity(new Sprite(x, y + 25, mFaceTextureRegion));

    scene.setOnSceneTouchListener(new IOnSceneTouchListener() {
      @Override
      public boolean onSceneTouchEvent(final Scene pScene,
          final MotionEvent pSceneMotionEvent) {
        switch (pSceneMotionEvent.getAction()) {
        case MotionEvent.ACTION_DOWN:
          mSmoothCamera.setZoomFactor(5.0f);
          break;
        case MotionEvent.ACTION_UP:
          mSmoothCamera.setZoomFactor(1.0f);
          break;
        }
        return true;
      }
    });

    return scene;
  }

  @Override
  public void onLoadComplete() {
  }
}
