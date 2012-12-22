package org.anddev.andengine.examples.game.snake;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
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

  @Override
  public Engine onLoadEngine() {
    mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
    return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE,
        new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), mCamera, false));
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

    getEngine().getTextureManager().loadTextures(mBackgroundTexture, mTexture);
  }

  @Override
  public Scene onLoadScene() {
    getEngine().registerPreFrameHandler(new FPSLogger());

    final Scene scene = new Scene(2);
    scene.getBottomLayer().addEntity(new Sprite(0, 0, mBackgroundTextureRegion));

    final Snake snake = new Snake(Direction.RIGHT, 0, CELLS_VERTICAL / 2,
        mHeadTextureRegion, mTailPartTextureRegion);
    scene.getTopLayer().addEntity(snake);

    scene.registerPreFrameHandler(new TimerHandler(1, new ITimerCallback() {
      @Override
      public void onTimePassed(final TimerHandler pTimerHandler) {
        pTimerHandler.reset();
        switch (MathUtils.random(0, 3)) {
        case 0:
          snake.setDirection(Direction.DOWN);
          break;
        case 1:
          snake.setDirection(Direction.RIGHT);
          break;
        case 2:
          snake.setDirection(Direction.LEFT);
          break;
        case 3:
          snake.setDirection(Direction.UP);
          break;
        }

        if (MathUtils.RANDOM.nextFloat() > 0.5f) {
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
