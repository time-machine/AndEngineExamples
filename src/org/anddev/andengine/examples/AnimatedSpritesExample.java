package org.anddev.andengine.examples;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.FPSCounter;
import org.anddev.andengine.entity.Scene;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureManager;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.ui.activity.BaseGameActivity;

public class AnimatedSpritesExample extends BaseGameActivity {
  private static final int CAMERA_WIDTH = 720;
  private static final int CAMERA_HEIGHT = 480;

  private Camera mCamera;
  private Texture mTexture;
  private TiledTextureRegion mFaceTextureRegion;
  private TiledTextureRegion mHelicopterTextureRegion;
  private TiledTextureRegion mBananaTextureRegion;

  @Override
  public Scene onLoadScene() {
    getEngine().registerPreFrameHandler(new FPSCounter());
    final Scene scene = new Scene(1);
    scene.setBackgroundColor(0.09804f, 0.6274f, 0.8784f);

    // quickly twinkling face
    final AnimatedSprite face = new AnimatedSprite(150, 150, mFaceTextureRegion);
    face.animate(100);
    scene.getTopLayer().addEntity(face);

    // continuously flying helicopter
    final AnimatedSprite heli = new AnimatedSprite(550, 150,
        mHelicopterTextureRegion);
    heli.animate(new long[]{100, 100}, 1, 2, true);
    scene.getTopLayer().addEntity(heli);

    // continuously flying helicopter
    final AnimatedSprite heli2 = new AnimatedSprite(550, 300,
        mHelicopterTextureRegion.clone());
    heli2.animate(100);
    scene.getTopLayer().addEntity(heli2);

    // funny banana
    final AnimatedSprite banana = new AnimatedSprite(150, 300,
        mBananaTextureRegion);
    banana.animate(100);
    scene.getTopLayer().addEntity(banana);

    return scene;
  }

  @Override
  public void onLoadResources() {
    mTexture = new Texture(256, 128);
    mHelicopterTextureRegion = TextureRegionFactory.createTiledFromAsset(
        mTexture, this, "gfx/helicopter_tiled.png", 0, 0, 2, 2);
    mBananaTextureRegion = TextureRegionFactory.createTiledFromAsset(
        mTexture, this, "gfx/banana_tiled.png", 96, 0, 4, 2);
    mFaceTextureRegion = TextureRegionFactory.createTiledFromAsset(
        mTexture, this, "gfx/boxface_tiled.png", 96, 70, 2, 1);
    TextureManager.loadTexture(mTexture);
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
}
