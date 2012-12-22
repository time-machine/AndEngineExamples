package org.anddev.andengine.examples;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.Scene.IOnSceneTouchListener;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

import android.view.MotionEvent;
import android.widget.Toast;

public class UpdateTextureExample extends BaseExample {
  private static final int CAMERA_WIDTH = 480;
  private static final int CAMERA_HEIGHT = 320;

  private Camera mCamera;
  private Texture mTexture;
  private TiledTextureRegion mFaceTextureRegion;

  private boolean mToggleBox = true;

  @Override
  public Engine onLoadEngine() {
    Toast.makeText(this, "Touch the screen to update (redraw) an existing Texture " +
        "with every touch!", Toast.LENGTH_LONG).show();
    mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
    return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE,
        new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), mCamera));
  }

  @Override
  public void onLoadResources() {
    mTexture = new Texture(64, 32, TextureOptions.BILINEAR);
    mFaceTextureRegion = TextureRegionFactory.createTiledFromAsset(
        mTexture, this, "gfx/boxface_tiled.png", 0, 0, 2, 1);
    getEngine().getTextureManager().loadTexture(mTexture);
  }

  @Override
  public Scene onLoadScene() {
    getEngine().registerPreFrameHandler(new FPSLogger());

    final Scene scene = new Scene(1);
    scene.setBackgroundColor(0.09804f, 0.6274f, 0.8784f);

    // calculate the coordinates for the face, so it's centered on the camera
    final int x = (CAMERA_WIDTH - mFaceTextureRegion.getTileWidth()) / 2;
    final int y = (CAMERA_HEIGHT - mFaceTextureRegion.getTileHeight()) / 2;

    // create the face and add it to the scene
    final AnimatedSprite face = new AnimatedSprite(x, y, mFaceTextureRegion);
    face.animate(100);
    scene.getTopLayer().addEntity(face);

    scene.setOnSceneTouchListener(new IOnSceneTouchListener() {
      @Override
      public boolean onSceneTouchEvent(final Scene pScene,
          final MotionEvent pSceneMotionEvent) {
        if (pSceneMotionEvent.getAction() == MotionEvent.ACTION_DOWN) {
          toggle();
        }
        return true;
      }
    });

    return scene;
  }

  @Override
  public void onLoadComplete() {
  }

  private void toggle() {
    mTexture.clearTextureSources();
    mToggleBox = !mToggleBox;
    TextureRegionFactory.createTiledFromAsset(mTexture, this,
        mToggleBox ? "gfx/boxface_tiled.png" : "gfx/circleface_tiled.png",
        0, 0, 2, 1);
  }
}
