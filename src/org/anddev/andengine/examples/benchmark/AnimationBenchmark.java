package org.anddev.andengine.examples.benchmark;

import javax.microedition.khronos.opengles.GL11;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.Scene;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.opengl.vertex.RectangleVertexBuffer;

public class AnimationBenchmark extends BaseBenchmark {
  private static final int CAMERA_WIDTH = 720;
  private static final int CAMERA_HEIGHT = 480;

  private static final int SPRITE_COUNT = 150;

  private Camera mCamera;
  private Texture mTexture;

  private TiledTextureRegion mSnapdragonTextureRegion;
  private TiledTextureRegion mHelicopterTextureRegion;
  private TiledTextureRegion mBananaTextureRegion;
  private TiledTextureRegion mFaceTextureRegion;

  @Override
  public Engine onLoadEngine() {
    mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
    final EngineOptions engineOptions = new EngineOptions(true,
       ScreenOrientation.LANDSCAPE,
       new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), mCamera, false);
    engineOptions.getRenderHints().disableExtensionVertexBufferObjects();
    return new Engine(engineOptions);
  }

  @Override
  public void onLoadResources() {
    mTexture = new Texture(512, 256, TextureOptions.BILINEAR);
    TextureRegionFactory.setAssetBasePath("gfx/");
    mSnapdragonTextureRegion = TextureRegionFactory.createTiledFromAsset(
        mTexture, this, "snapdragon_tiled.png", 0, 0, 4, 3);
    mHelicopterTextureRegion = TextureRegionFactory.createTiledFromAsset(
        mTexture, this, "helicopter_tiled.png", 400, 0, 2, 2);
    mBananaTextureRegion = TextureRegionFactory.createTiledFromAsset(
        mTexture, this, "banana_tiled.png", 0, 180, 4, 2);
    mFaceTextureRegion = TextureRegionFactory.createTiledFromAsset(
        mTexture, this, "boxface_tiled.png", 132, 180, 2, 1);
    getEngine().getTextureManager().loadTexture(mTexture);
  }

  @Override
  public Scene onLoadScene() {
    final Scene scene = new Scene(1);
    scene.setBackgroundColor(0.09804f, 0.6274f, 0.8784f);

    // as we are creating quite a lot of the same Sprites, we can let them share
    // a VertexBuffer to significantly increase performance
    final RectangleVertexBuffer faceSharedVertexBuffer =
        new RectangleVertexBuffer(GL11.GL_DYNAMIC_DRAW);
    faceSharedVertexBuffer.onUpdate(0, 0, mFaceTextureRegion.getTileWidth(),
        mFaceTextureRegion.getTileHeight());

    final RectangleVertexBuffer helicopterSharedVertexBuffer =
        new RectangleVertexBuffer(GL11.GL_DYNAMIC_DRAW);
    helicopterSharedVertexBuffer.onUpdate(0, 0,
        mHelicopterTextureRegion.getTileWidth(),
        mHelicopterTextureRegion.getTileHeight());

    final RectangleVertexBuffer snapdragonSharedVertexBuffer =
        new RectangleVertexBuffer(GL11.GL_DYNAMIC_DRAW);
    snapdragonSharedVertexBuffer.onUpdate(0, 0,
        mSnapdragonTextureRegion.getTileWidth(),
        mSnapdragonTextureRegion.getTileHeight());

    final RectangleVertexBuffer bananaSharedVertexBuffer =
        new RectangleVertexBuffer(GL11.GL_DYNAMIC_DRAW);
    bananaSharedVertexBuffer.onUpdate(0, 0, mBananaTextureRegion.getTileWidth(),
        mBananaTextureRegion.getTileHeight());

    for (int i = 0; i < SPRITE_COUNT; i++) {
      // quickly twinkling face
      final AnimatedSprite face = new AnimatedSprite(
          mRandom.nextFloat() * (CAMERA_WIDTH - 32),
          mRandom.nextFloat() * (CAMERA_HEIGHT - 32),
          mFaceTextureRegion.clone(), faceSharedVertexBuffer);
      face.animate(50 + mRandom.nextInt(100));
      scene.getTopLayer().addEntity(face);

      // continuously flying helicopter
      final AnimatedSprite helicopter = new AnimatedSprite(
          mRandom.nextFloat() * (CAMERA_WIDTH - 48),
          mRandom.nextFloat() * (CAMERA_HEIGHT - 48),
          mHelicopterTextureRegion.clone(), helicopterSharedVertexBuffer);
      helicopter.animate(
          new long[] { 50 + mRandom.nextInt(100), 50 + mRandom.nextInt(100) },
          1, 2, true);
      scene.getTopLayer().addEntity(helicopter);

      // snapdragon
      final AnimatedSprite snapdragon = new AnimatedSprite(
          mRandom.nextFloat() * (CAMERA_WIDTH - 100),
          mRandom.nextFloat() * (CAMERA_HEIGHT - 60),
          mSnapdragonTextureRegion.clone(), snapdragonSharedVertexBuffer);
      snapdragon.animate(50 + mRandom.nextInt(100));
      scene.getTopLayer().addEntity(snapdragon);

      // funny banana
      final AnimatedSprite banana = new AnimatedSprite(
          mRandom.nextFloat() * (CAMERA_WIDTH - 32),
          mRandom.nextFloat() * (CAMERA_HEIGHT - 32),
          mBananaTextureRegion.clone(), bananaSharedVertexBuffer);
      banana.animate(50 + mRandom.nextInt(100));
      scene.getTopLayer().addEntity(banana);
    }

    return scene;
  }

  @Override
  protected float getBenchmarkDuration() {
    return 10;
  }

  @Override
  protected float getBenchmarkStartOffset() {
    return 2;
  }

  @Override
  protected float getBenchmarkID() {
    return ANIMATIONBENCHMARK_ID;
  }
}
