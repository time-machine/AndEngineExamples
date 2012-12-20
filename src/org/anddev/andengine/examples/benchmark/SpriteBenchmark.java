package org.anddev.andengine.examples.benchmark;

import javax.microedition.khronos.opengles.GL11;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.Layer;
import org.anddev.andengine.entity.Scene;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.vertex.RectangleVertexBuffer;

public class SpriteBenchmark extends BaseBenchmark {
  private static final int CAMERA_WIDTH = 720;
  private static final int CAMERA_HEIGHT = 480;

  private static final int SPRITE_COUNT = 1000;

  private Camera mCamera;
  private Texture mTexture;
  private TextureRegion mFaceTextureRegion;

  @Override
  protected float getBenchmarkDuration() {
    return 10;
  }

  @Override
  protected float getBenchmarkStartOffset() {
    return 2;
  }

  @Override
  public Engine onLoadEngine() {
    mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
    return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE,
        new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), mCamera, false));
  }

  @Override
  public void onLoadResources() {
    mTexture = new Texture(64, 32, TextureOptions.BILINEAR);
    mFaceTextureRegion = TextureRegionFactory.createFromAsset(mTexture, this,
        "gfx/boxface.png", 0, 0);
    getEngine().getTextureManager().loadTexture(mTexture);
  }

  @Override
  public Scene onLoadScene() {
    final Scene scene = new Scene(1);
    scene.setBackgroundColor(0.09804f, 0.6274f, 0.8784f);

    // as we are creating quite a lot of the same Sprites, we can let them share
    // a VertexBuffer to significantly increase performance
    final RectangleVertexBuffer sharedVertexBuffer =
        new RectangleVertexBuffer(GL11.GL_STATIC_DRAW);
    sharedVertexBuffer.onUpdate(0, 0, mFaceTextureRegion.getWidth(),
        mFaceTextureRegion.getHeight());

    final Layer topLayer = scene.getTopLayer();

    for (int i = 0; i < SPRITE_COUNT; i++) {
      final Sprite face = new Sprite(
          mRandom.nextFloat() * (CAMERA_WIDTH - 32),
          mRandom.nextFloat() * (CAMERA_HEIGHT - 32), mFaceTextureRegion,
          sharedVertexBuffer);
      topLayer.addEntity(face);
    }

    return scene;
  }
}
