package org.anddev.andengine.examples;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.Scene;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.entity.util.FPSCounter;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

public class TextureOptionsExample extends BaseExampleGameActivity {
  private static final int CAMERA_WIDTH = 720;
  private static final int CAMERA_HEIGHT = 480;

  private Camera mCamera;
  private Texture mTexture;
  private Texture mTextureBilinear;
  private TiledTextureRegion mFaceTextureRegion;
  private TiledTextureRegion mFaceTextureRegionBilinear;

  @Override
  public Scene onLoadScene() {
    getEngine().registerPreFrameHandler(new FPSCounter());
    final Scene scene = new Scene(1);
    scene.setBackgroundColor(0.09804f, 0.6274f, 0.8784f);

    final int x = (CAMERA_WIDTH - mFaceTextureRegion.getWidth()) / 2;
    final int y = (CAMERA_HEIGHT - mFaceTextureRegion.getHeight()) / 2;

    final AnimatedSprite face = new AnimatedSprite(x - 120, y,
        mFaceTextureRegion);
    face.setScale(4);

    final AnimatedSprite faceBillinear = new AnimatedSprite(x + 120, y,
        mFaceTextureRegionBilinear);
    faceBillinear.setScale(4);

    scene.getTopLayer().addEntity(face);
    scene.getTopLayer().addEntity(faceBillinear);

    return scene;
  }

  @Override
  public void onLoadResources() {
    mTexture = new Texture(64, 32, TextureOptions.DEFAULT);
    mTextureBilinear = new Texture(64, 32, TextureOptions.BILINEAR);
    mFaceTextureRegion = TextureRegionFactory.createTiledFromAsset(mTexture,
        this, "gfx/boxface_tiled.png", 0, 0, 2, 1);
    mFaceTextureRegionBilinear = TextureRegionFactory.createTiledFromAsset(
        mTextureBilinear, this, "gfx/boxface_tiled.png", 0, 0, 2, 1);
    getEngine().getTextureManager().loadTextures(mTexture, mTextureBilinear);
  }

  @Override
  public void onLoadComplete() {
  }

  @Override
  public Engine onLoadEngine() {
    mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
    return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE,
        new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), mCamera, false));
  }
}
