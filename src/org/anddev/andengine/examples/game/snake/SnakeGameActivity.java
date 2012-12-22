package org.anddev.andengine.examples.game.snake;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.camera.hud.controls.BaseOnScreenControl;
import org.anddev.andengine.engine.camera.hud.controls.BaseOnScreenControl.OnScreenControlListener;
import org.anddev.andengine.engine.camera.hud.controls.DigitalOnScreenControl;
import org.anddev.andengine.engine.handler.timer.ITimerCallback;
import org.anddev.andengine.engine.handler.timer.TimerHandler;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.examples.game.snake.adt.Direction;
import org.anddev.andengine.examples.game.snake.entity.Snake;
import org.anddev.andengine.examples.game.snake.util.constants.SnakeConstants;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.ui.activity.BaseGameActivity;
import org.anddev.andengine.util.MathUtils;

public class SnakeGameActivity extends BaseGameActivity implements
    SnakeConstants {
  private static final int CAMERA_WIDTH = CELLS_HORIZONTAL * CELL_WIDTH; // 640
  private static final int CAMERA_HEIGHT = CELLS_VERTICAL * CELL_HEIGHT; // 480

  private Camera mCamera;

  private Texture mTexture;
  private TextureRegion mTailPartTextureRegion;
  private TextureRegion mHeadTextureRegion;

  private Texture mBackgroundTexture;
  private TextureRegion mBackgroundTextureRegion;

  private Texture mOnScreenControlTexture;
  private TextureRegion mOnScreenControlBaseTextureRegion;
  private TextureRegion mOnScreenControlKnobTextureRegion;

  @Override
  public Engine onLoadEngine() {
    mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
    return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE,
        new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), mCamera));
  }

  @Override
  public void onLoadResources() {
    mTexture = new Texture(64, 64, TextureOptions.BILINEAR);
    mHeadTextureRegion = TextureRegionFactory.createFromAsset(mTexture, this,
        "gfx/snake_head.png", 0, 0);
    mTailPartTextureRegion = TextureRegionFactory.createFromAsset(mTexture,
        this, "gfx/snake_tailpart.png", 32, 0);

    mBackgroundTexture = new Texture(1024, 512, TextureOptions.DEFAULT);
    mBackgroundTextureRegion = TextureRegionFactory.createFromAsset(
        mBackgroundTexture, this, "gfx/background_forest.png", 0, 0);

    mOnScreenControlTexture = new Texture(256, 128, TextureOptions.BILINEAR);
    mOnScreenControlBaseTextureRegion = TextureRegionFactory.createFromAsset(
        mOnScreenControlTexture, this, "gfx/analog_onscreen_control_base.png",
        0, 0);
    mOnScreenControlKnobTextureRegion = TextureRegionFactory.createFromAsset(
        mOnScreenControlTexture, this, "gfx/analog_onscreen_control_knob.png",
        128, 0);
    getEngine().getTextureManager().loadTextures(mBackgroundTexture, mTexture,
        mOnScreenControlTexture);
  }

  @Override
  public Scene onLoadScene() {
    getEngine().registerPreFrameHandler(new FPSLogger());

    final Scene scene = new Scene(2);
    scene.getBottomLayer().addEntity(new Sprite(0, 0, mBackgroundTextureRegion));

    final Snake snake = new Snake(Direction.RIGHT, 0, CELLS_VERTICAL / 2,
        mHeadTextureRegion, mTailPartTextureRegion);
    snake.grow();
    scene.getTopLayer().addEntity(snake);

    final DigitalOnScreenControl digitalOnScreenControl = new DigitalOnScreenControl(
        0, CAMERA_HEIGHT - mOnScreenControlBaseTextureRegion.getHeight(),
        mCamera, mOnScreenControlBaseTextureRegion,
        mOnScreenControlKnobTextureRegion, 0.1f, new OnScreenControlListener() {
          @Override
          public void onControlChange(
              final BaseOnScreenControl pBaseOnScreenControl,
              final float pValueX, final float pValueY) {
            if (pValueX == 1) {
              snake.setDirection(Direction.RIGHT);
            }
            else if (pValueX == -1) {
              snake.setDirection(Direction.LEFT);
            }
            else if (pValueY == 1) {
              snake.setDirection(Direction.DOWN);
            }
            else if (pValueY == -1) {
              snake.setDirection(Direction.UP);
            }
          }
        });
    scene.setChildScene(digitalOnScreenControl, false, false);

    scene.registerPreFrameHandler(new TimerHandler(0.5f, new ITimerCallback() {
      @Override
      public void onTimePassed(final TimerHandler pTimerHandler) {
        pTimerHandler.reset();

        if (MathUtils.RANDOM.nextFloat() > 0.75f) {
          snake.grow();
        }

        snake.move();
      }
    }));

    return scene;
  }

  @Override
  public void onLoadComplete() {
  }
}
