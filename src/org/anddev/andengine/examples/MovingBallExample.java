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
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

public class MovingBallExample extends BaseExampleGameActivity {
  private static final int CAMERA_WIDTH = 720;
  private static final int CAMERA_HEIGHT = 480;

  private static final float DEMO_VELOCITY = 100.0f;

  private Camera mCamera;
  private Texture mTexture;
  private TiledTextureRegion mFaceTextureRegion;

  @Override
  public Engine onLoadEngine() {
    mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
    return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE,
        new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), mCamera, false));
  }

  @Override
  public void onLoadResources() {
    mTexture = new Texture(64, 32);
    mFaceTextureRegion = TextureRegionFactory.createTiledFromAsset(mTexture,
        this, "gfx/circleface_tiled.png", 0, 0, 2, 1);
    getEngine().getTextureManager().loadTexture(mTexture);
  }

  @Override
  public Scene onLoadScene() {
    getEngine().registerPreFrameHandler(new FPSCounter());

    final Scene scene = new Scene(1);
    scene.setBackgroundColor(0.09804f, 0.6274f, 0.8784f);

    final int x = (CAMERA_WIDTH - mFaceTextureRegion.getWidth()) / 2;
    final int y = (CAMERA_HEIGHT - mFaceTextureRegion.getHeight()) / 2;
    final Ball ball = new Ball(x, y, mFaceTextureRegion);
    ball.setVelocity(DEMO_VELOCITY, DEMO_VELOCITY);

    scene.getTopLayer().addEntity(ball);

    return scene;
  }

  @Override
  public void onLoadComplete() {
  }

  private static class Ball extends AnimatedSprite {
    public Ball(final float pX, final float pY,
        final TiledTextureRegion pTiledTextureRegion) {
      super(pX, pY, pTiledTextureRegion);
    }

    @Override
    protected void onManagedUpdate(final float pSecondsElapsed) {
      if (mX < 0) {
        mVelocityX = DEMO_VELOCITY;
      }
      else if (mX + getWidth() > CAMERA_WIDTH) {
        mVelocityX = -DEMO_VELOCITY;
      }

      if (mY < 0) {
        mVelocityY = DEMO_VELOCITY;
      }
      else if (mY + getHeight() > CAMERA_HEIGHT) {
        mVelocityY = -DEMO_VELOCITY;
      }

      super.onManagedUpdate(pSecondsElapsed);
    }
  }
}
