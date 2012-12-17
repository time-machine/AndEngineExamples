package org.anddev.andengine.examples;

import java.util.Random;

import javax.microedition.khronos.opengles.GL11;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.Scene;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.opengl.vertex.RectangleVertexBuffer;

public class SpritesExample extends BaseExample {
  private static final long RANDOM_SEED = 1234567890;

  private static final int CAMERA_WIDTH = 720;
  private static final int CAMERA_HEIGHT = 480;

  private static final int SPRITE_COUNT = 30;

  private Camera mCamera;
  private Texture mTexture;
  private TiledTextureRegion mFaceTextureRegion;

  @Override
  public Scene onLoadScene() {
    getEngine().registerPreFrameHandler(new FPSLogger());

    final Scene scene = new Scene(1);
    scene.setBackgroundColor(0.09804f, 0.6274f, 0.8784f);

    final Random random = new Random(RANDOM_SEED);

    /* As we are creating quite a lot of the same Sprites, we can let them
       share a VertexBuffer to significantly increase performance */
    final RectangleVertexBuffer sharedVertexBuffer =
        new RectangleVertexBuffer(GL11.GL_DYNAMIC_DRAW);
    sharedVertexBuffer.onUpdate(0, 0, mFaceTextureRegion.getTileWidth(),
        mFaceTextureRegion.getTileHeight());

    for (int i = 0; i < SPRITE_COUNT; i++) {
      final AnimatedSprite face = new AnimatedSprite(
          random.nextFloat() * (CAMERA_WIDTH - 32),
          random.nextFloat() * (CAMERA_HEIGHT - 32), mFaceTextureRegion,
          sharedVertexBuffer);
      scene.getTopLayer().addEntity(face);
    }

    return scene;
  }

  @Override
  public void onLoadResources() {
    mTexture = new Texture(64, 32, TextureOptions.BILINEAR);
    mFaceTextureRegion = TextureRegionFactory.createTiledFromAsset(mTexture,
        this, "gfx/boxface_tiled.png", 0, 0, 2, 1);
    getEngine().getTextureManager().loadTexture(mTexture);
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
