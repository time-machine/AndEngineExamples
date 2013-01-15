package org.anddev.andengine.examples;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

public class AnimatedSpritesExample extends BaseExample {
  private static final int CAMERA_WIDTH = 480;
  private static final int CAMERA_HEIGHT = 320;

  private Camera mCamera;
  private Texture mTexture;

  private TiledTextureRegion mSnapdragonTextureRegion;
  private TiledTextureRegion mHelicopterTextureRegion;
  private TiledTextureRegion mBananaTextureRegion;
  private TiledTextureRegion mFaceTextureRegion;

  @Override
  public Scene onLoadScene() {
    getEngine().registerUpdateHandler(new FPSLogger());
    final Scene scene = new Scene(1);
    scene.setBackground(new ColorBackground(0.09804f, 0.6274f, 0.8784f));

    // quickly twinkling face
    final AnimatedSprite face = new AnimatedSprite(100, 50, mFaceTextureRegion);
    face.animate(100);
    scene.getLastChild().attachChild(face);

    // continuously flying helicopter
    final AnimatedSprite helicopter = new AnimatedSprite(320, 50,
        mHelicopterTextureRegion);
    helicopter.animate(new long[]{100, 100}, 1, 2, true);
    scene.getLastChild().attachChild(helicopter);

    // snapdragon
    final AnimatedSprite snapdragon = new AnimatedSprite(300, 200,
        mSnapdragonTextureRegion);
    snapdragon.animate(100);
    scene.getLastChild().attachChild(snapdragon);

    // funny banana
    final AnimatedSprite banana = new AnimatedSprite(100, 220,
        mBananaTextureRegion);
    banana.animate(100);
    scene.getLastChild().attachChild(banana);

    return scene;
  }

  @Override
  public void onLoadResources() {
    mTexture = new Texture(512, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
    TextureRegionFactory.setAssetBasePath("gfx/");
    mSnapdragonTextureRegion = TextureRegionFactory.createTiledFromAsset(
        mTexture, this, "snapdragon_tiled.png", 0, 0, 4, 3);
    mHelicopterTextureRegion = TextureRegionFactory.createTiledFromAsset(
        mTexture, this, "helicopter_tiled.png", 400, 0, 2, 2);
    mBananaTextureRegion = TextureRegionFactory.createTiledFromAsset(
        mTexture, this, "banana_tiled.png", 0, 180, 4, 2);
    mFaceTextureRegion = TextureRegionFactory.createTiledFromAsset(
        mTexture, this, "face_box_tiled.png", 132, 180, 2, 1);
    getEngine().getTextureManager().loadTexture(mTexture);
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
