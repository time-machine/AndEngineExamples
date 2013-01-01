package org.anddev.andengine.examples;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.SpriteBackground;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;

public class SpriteBackgroundExample extends BaseExample {
  private static final int CAMERA_WIDTH = 720;
  private static final int CAMERA_HEIGHT = 480;

  private Camera mCamera;

  private Texture mTexture;
  private TextureRegion mFaceTextureRegion;

  private Texture mBackgroundTexture;
  private TextureRegion mBackgroundGrassTextureRegion;

  @Override
  public Engine onLoadEngine() {
    mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
    return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE,
        new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), mCamera));
  }

  @Override
  public void onLoadResources() {
    TextureRegionFactory.setAssetBasePath("gfx/");

    mTexture = new Texture(32, 32, TextureOptions.BILINEAR);
    mFaceTextureRegion = TextureRegionFactory.createFromAsset(mTexture, this,
        "boxface.png", 0, 0);

    mBackgroundTexture = new Texture(32, 32, TextureOptions.REPEATING);
    mBackgroundGrassTextureRegion = TextureRegionFactory.createFromAsset(
        mBackgroundTexture, this, "background_grass.png", 0, 0);
    mBackgroundGrassTextureRegion.setWidth(CAMERA_HEIGHT);
    mBackgroundGrassTextureRegion.setHeight(CAMERA_HEIGHT);

    getEngine().getTextureManager().loadTextures(mTexture, mBackgroundTexture);
  }

  @Override
  public Scene onLoadScene() {
    getEngine().registerPreFrameHandler(new FPSLogger());

    final Scene scene = new Scene(1);
    scene.setBackground(new SpriteBackground(mCamera, new Sprite(0, 0,
        CAMERA_WIDTH, CAMERA_HEIGHT, mBackgroundGrassTextureRegion)));

    // calculate the coordinates for the face, so its centered on the camera
    final int centerX = (CAMERA_WIDTH - mFaceTextureRegion.getWidth()) / 2;
    final int centerY = (CAMERA_HEIGHT - mFaceTextureRegion.getHeight()) / 2;

    // create the face and add it to the scene
    final Sprite face = new Sprite(centerX, centerY, mFaceTextureRegion);
    scene.getTopLayer().addEntity(face);

    return scene;
  }

  @Override
  public void onLoadComplete() {
  }
}
