package org.anddev.andengine.examples;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.buffer.BufferObjectManager;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;

public class UnloadResourcesExample extends BaseExample {
  private static final int CAMERA_WIDTH = 720;
  private static final int CAMERA_HEIGHT= 480;

  private Camera mCamera;
  private Texture mTexture;
  private TextureRegion mClickToUnloadTextureRegion;

  @Override
  public Engine onLoadEngine() {
    mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
    return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE,
        new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), mCamera));
  }

  @Override
  public void onLoadResources() {
    mTexture = new Texture(128, 128, TextureOptions.BILINEAR);
    TextureRegionFactory.setAssetBasePath("gfx/");
    mClickToUnloadTextureRegion = TextureRegionFactory.createFromAsset(mTexture, this,
        "click_to_unload.png", 0, 0);
    getEngine().getTextureManager().loadTexture(mTexture);
  }

  @Override
  public Scene onLoadScene() {
    getEngine().registerUpdateHandler(new FPSLogger());
    final Scene scene = new Scene(1);
    scene.setBackground(new ColorBackground(0.09804f, 0.6274f, 0.8784f));

    final int x = (CAMERA_WIDTH - mClickToUnloadTextureRegion.getWidth()) / 2;
    final int y = (CAMERA_HEIGHT - mClickToUnloadTextureRegion.getHeight()) / 2;
    final Sprite clickToUnload = new Sprite(x, y, mClickToUnloadTextureRegion) {
      @Override
      public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,
          final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
        // completely remove all resources associated with this sprite
        BufferObjectManager.getActiveInstance().unloadBufferObject(
            getVertexBuffer());
        BufferObjectManager.getActiveInstance().unloadBufferObject(
            mClickToUnloadTextureRegion.getTextureBuffer());
        mEngine.getTextureManager().unloadTexture(mTexture);

        // and remove the sprite from the scene
        final Sprite thisRef = this;
        runOnUiThread(new Runnable() {
          @Override
          public void run() {
            scene.getTopLayer().removeEntity(thisRef);
          }
        });
        return true;
      }
    };

    scene.getTopLayer().addEntity(clickToUnload);
    scene.registerTouchArea(clickToUnload);

    return scene;
  }

  @Override
  public void onLoadComplete() {
  }
}
